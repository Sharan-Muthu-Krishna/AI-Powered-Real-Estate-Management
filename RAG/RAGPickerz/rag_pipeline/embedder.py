"""Builds a vector store from the provided documents.

Supports both FAISS (active) and ChromaDB (evaluated, see commented implementation below).
FAISS was selected for its superior search speed and lower memory footprint.
See config.py for detailed comparison.

Returns:
    FAISS: The FAISS vector store containing the document embeddings.
"""

import os
import time
import hashlib
from typing import List
from langchain_core.documents import Document

from langchain_community.vectorstores import FAISS
# from langchain_community.vectorstores import Chroma  
from rag_pipeline.config import VECTOR_DB_PATH, embeddings, splitter

def compute_hash(text: str) -> str:
    """Computes SHA256 hash of the given text."""
    return hashlib.sha256(text.encode("utf-8")).hexdigest()

def build_vector_store(documents: List[Document]) -> FAISS:
    """Builds or updates the vector store from the provided documents, skipping duplicates."""
    vector_store = None
    existing_hashes = set()

    if os.path.exists(os.path.join(VECTOR_DB_PATH, "index.faiss")):
        print("✅ Vector store already exists. Loading from disk.")
        vector_store = FAISS.load_local(VECTOR_DB_PATH, embeddings, allow_dangerous_deserialization=True)
        all_docs = vector_store.similarity_search("warmup", k=10000)
        existing_hashes = {doc.metadata.get("hash") for doc in all_docs if "hash" in doc.metadata}

    start_split = time.perf_counter()
    chunks = splitter.create_documents([doc.page_content for doc in documents])
    print(f"🧩 Split into {len(chunks)} chunks in {time.perf_counter() - start_split:.2f}s")

    start_chunk = time.perf_counter()
    new_chunks = []
    for chunk in chunks:
        h = compute_hash(chunk.page_content)
        if h not in existing_hashes:
            chunk.metadata["hash"] = h
            new_chunks.append(chunk)
        
    print(f"🔍 Found {len(new_chunks)} new chunks to embed in {time.perf_counter() - start_chunk:.2f}s")

    if not new_chunks:
        return vector_store

    start_embed = time.perf_counter()
    if vector_store:
        vector_store.add_documents(new_chunks)
    else:
        vector_store = FAISS.from_documents(new_chunks, embeddings)
    print(f"📦 Embedded & indexed {len(new_chunks)} new chunks in {time.perf_counter() - start_embed:.2f}s")

    start_save = time.perf_counter()
    vector_store.save_local(os.path.join(VECTOR_DB_PATH))
    print(f"💾 Saved updated vector store in {time.perf_counter() - start_save:.2f}s")

    return vector_store



# def build_chroma_vector_store(documents: List[Document]) -> Chroma:
#     """Builds or loads a ChromaDB vector store from the provided documents.
#
#     ChromaDB handles deduplication via document IDs internally,
#     so manual SHA-256 hashing is not needed (unlike FAISS).
#     """
#     from rag_pipeline.config import CHROMA_DB_PATH, CHROMA_COLLECTION_NAME
#
#     chunks = splitter.create_documents([doc.page_content for doc in documents])
#
#     # ChromaDB supports unique IDs per document — duplicates are skipped automatically
#     chunk_ids = [compute_hash(chunk.page_content) for chunk in chunks]
#
#     # Add metadata for filtering (ChromaDB's built-in advantage over FAISS)
#     for chunk in chunks:
#         chunk.metadata["source_type"] = "legal_document"
#         chunk.metadata["indexed_at"] = time.strftime("%Y-%m-%d %H:%M:%S")
#
#     vector_store = Chroma.from_documents(
#         documents=chunks,
#         embedding=embeddings,
#         collection_name=CHROMA_COLLECTION_NAME,
#         persist_directory=CHROMA_DB_PATH,
#         ids=chunk_ids,  # automatic dedup via unique IDs
#     )


