"""Retriever module for the RAG pipeline.

Supports FAISS (active) and ChromaDB (evaluated alternative, see commented code below).

Raises:
    ValueError: If the VECTOR_DB_PATH environment variable is not set.
"""

from typing import List
import time
from langchain_community.vectorstores import FAISS
# from langchain_community.vectorstores import Chroma  
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_core.documents import Document

from rag_pipeline.config import VECTOR_DB_PATH, embeddings


def load_vector_store() -> FAISS:
    """Loads the FAISS vector store from the specified path.

    Raises:
        ValueError: If the VECTOR_DB_PATH environment variable is not set.

    Returns:
        FAISS: The FAISS vector store loaded from the specified path.
    """

    if not VECTOR_DB_PATH:
        raise ValueError("VECTOR_DB_PATH environment variable is not set.")
    return FAISS.load_local(
        VECTOR_DB_PATH, embeddings, allow_dangerous_deserialization=True
    )


def retrieve_documents(query: str, top_k: int = 5) -> List[Document]:
    """Retrieves documents relevant to the query from the vector store.

    Args:
        query (str): The query string to search for.
        top_k (int, optional): The number of top documents to consider for retrieval. Defaults to 5.

    Returns:
        List[Docuemnt]: A list of documents retrieved from the vector store.
    """
    store = load_vector_store()
    start = time.perf_counter()
    result= store.similarity_search(query, k=top_k)
    print(f"🔍 Document retrieval time: {time.perf_counter() - start:.2f}s")
    return result


# def load_chroma_store() -> Chroma:
#     from rag_pipeline.config import CHROMA_DB_PATH, CHROMA_COLLECTION_NAME
#     return Chroma(
#         collection_name=CHROMA_COLLECTION_NAME,
#         persist_directory=CHROMA_DB_PATH,
#         embedding_function=embeddings,
#     )
#
# def retrieve_documents_chroma(query: str, top_k: int = 5,
#                                filter_metadata: dict = None) -> List[Document]:
#     """ChromaDB retriever with optional metadata filtering.
#
#     Example usage with metadata filter (not possible with FAISS):
#         retrieve_documents_chroma("property address", filter_metadata={"source_type": "legal_document"})
#     """
#     store = load_chroma_store()
#     if filter_metadata:
#         # ChromaDB's built-in where filter — FAISS cannot do this
#         result = store.similarity_search(query, k=top_k, filter=filter_metadata)
#     else:
#         result = store.similarity_search(query, k=top_k)
#     return result


