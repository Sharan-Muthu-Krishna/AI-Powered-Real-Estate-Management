from langchain_huggingface import HuggingFaceEmbeddings
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_google_genai import ChatGoogleGenerativeAI
from dotenv import load_dotenv

# --- ChromaDB was also evaluated during development ---
# from langchain_community.vectorstores import Chroma
# CHROMA_DB_PATH = r".\chroma_db"
# CHROMA_COLLECTION_NAME = "real_estate_docs"

load_dotenv()

VECTOR_DB_PATH = r".\faiss_index"
CHUNK_SIZE = 350
CHUNK_OVERLAP = 50

print("started loading config")

embeddings = HuggingFaceEmbeddings(
    model_name="sentence-transformers/all-MiniLM-L6-v2"
)
print("loaded embeddings in config")

# Use stable text splitter
splitter = RecursiveCharacterTextSplitter(
    chunk_size=CHUNK_SIZE,
    chunk_overlap=CHUNK_OVERLAP
)
print("loaded splitter in config")

LLM = ChatGoogleGenerativeAI(
    model="gemma-3n-e2b-it",
    temperature=0.2,
)
print("loaded LLM in config")
