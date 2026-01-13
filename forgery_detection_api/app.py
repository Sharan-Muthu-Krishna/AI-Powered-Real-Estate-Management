from flask import Flask, request, jsonify
from flask_cors import CORS
from PIL import Image, ImageChops, ImageEnhance
import numpy as np
import os
import tempfile
import cv2
import tensorflow as tf
from werkzeug.utils import secure_filename
from flasgger import Swagger

app = Flask(__name__)
CORS(app)
Swagger(app)

# Load the TensorFlow model at startup for forgery detection
MODEL_PATH = 'production_model.keras'
try:
    forgery_model = tf.keras.models.load_model(MODEL_PATH)
    print("Forgery detection model loaded successfully.")
except Exception as e:
    print(f"Error loading forgery detection model: {e}")
    forgery_model = None


# -------------------------
# ELA Processing
# -------------------------
def perform_ela(image_path, output_path, quality=90):
    try:
        original_image = Image.open(image_path)
        temp_image_path = 'temp_image.jpg'
        original_image.save(temp_image_path, 'JPEG', quality=quality)

        compressed_image = Image.open(temp_image_path)

        if original_image.size != compressed_image.size:
            compressed_image = compressed_image.resize(original_image.size)

        if original_image.mode != compressed_image.mode:
            compressed_image = compressed_image.convert(original_image.mode)

        ela_image = ImageChops.difference(original_image, compressed_image)

        if ela_image.mode != 'RGB':
            ela_image = ela_image.convert('RGB')

        extrema = ela_image.getextrema()
        max_diff = max([max(channel_extrema) for channel_extrema in extrema])
        scale = 255.0 / max_diff if max_diff != 0 else 1.0
        ela_image = ImageEnhance.Brightness(ela_image).enhance(scale)

        ela_array = np.array(ela_image)
        ela_gray = np.mean(ela_array, axis=2)

        ela_gray_image = Image.fromarray(ela_gray.astype(np.uint8))
        ela_gray_image.save(output_path)

        os.remove(temp_image_path)
        return output_path
    except Exception as e:
        print(f"Error in perform_ela: {e}")
        return None


# -------------------------
# Forgery Model Inference
# -------------------------
def perform_forgery_inference(ela_image_path):
    try:
        img = cv2.imread(ela_image_path)
        resized = tf.image.resize(img, (256, 256))
        input_data = np.expand_dims(resized / 255.0, axis=0)

        yhat = forgery_model.predict(input_data)
        confidence_score = float(yhat[0][0])
        return confidence_score
    except Exception as e:
        print(f"Error in forgery inference: {e}")
        return None


# -------------------------
# API Endpoint
# -------------------------
@app.route('/detect-forgery', methods=['POST'])
def process_image():
    """
    Detect Image Forgery
    ---
    tags:
      - Forgery Detection
    consumes:
      - multipart/form-data
    parameters:
      - name: image
        in: formData
        type: file
        required: true
    responses:
      200:
        description: Success
    """

    if forgery_model is None:
        return jsonify({'error': 'Model not loaded.'}), 500

    if 'image' not in request.files:
        return jsonify({'error': 'No image part in the request.'}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No selected file.'}), 400

    try:
        filename = secure_filename(file.filename)

        with tempfile.NamedTemporaryFile(delete=False, suffix=os.path.splitext(filename)[1]) as temp_file:
            file_path = temp_file.name
            file.save(file_path)

        ela_output_path = tempfile.NamedTemporaryFile(delete=False, suffix=".jpg").name

        saved_ela_image_path = perform_ela(file_path, ela_output_path)
        if saved_ela_image_path is None:
            os.remove(file_path)
            return jsonify({'error': 'ELA processing failed.'}), 500

        confidence_score = perform_forgery_inference(saved_ela_image_path)
        if confidence_score is None:
            os.remove(file_path)
            os.remove(saved_ela_image_path)
            return jsonify({'error': 'Inference failed.'}), 500

        # 0 → forged, 1 → authentic
        forgery_probability = round(1 - confidence_score, 4)

        response = {
            "forgery_detected": forgery_probability > 0.5,
            "forgery_probability": forgery_probability,
            "authentic_probability": round(confidence_score, 4)
        }

        os.remove(file_path)
        os.remove(saved_ela_image_path)

        return jsonify(response), 200

    except Exception as e:
        print(f"Error processing image: {e}")
        return jsonify({'error': 'Internal server error.'}), 500


@app.route('/')
def index():
    return """
    <h1>Image Forgery Detection API</h1>
    <p>POST an image to <code>/detect-forgery</code></p>
    <pre>
    curl -X POST -F "image=@test.jpg" http://localhost:5000/detect-forgery
    </pre>
    """


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
