from langchain_core.prompts import PromptTemplate
from langchain_core.runnables import RunnableLambda
from rag_pipeline.config import LLM
from rag_pipeline.retriever import retrieve_documents
from concurrent.futures import ThreadPoolExecutor, as_completed
import time

TEMPLATE = """You are an expert assistant in analyzing and interpreting all types of documents.

Here is the relevant document text:
{context}

User Question:
{question}

Instructions:
Answer clearly and concisely in natural language.
Base your response strictly on the provided text.
Limit your answer to one line.
Return ONLY the plain text answer.
"""

def run_query_pipeline(question: str) -> str:
    start = time.perf_counter()
    documents = retrieve_documents(question)
    print(f"🔍 Retrieval time: {time.perf_counter() - start:.2f}s")

    context = "\n".join(f"[{i+1}] {doc.page_content}" for i, doc in enumerate(documents))

    prompt = PromptTemplate.from_template(TEMPLATE)
    llm = LLM

    prepare_inputs = RunnableLambda(lambda _: {"context": context, "question": question})
    chain = prepare_inputs | prompt | llm

    start = time.perf_counter()
    response = chain.invoke({})
    print(f"🤖 LLM generation time: {time.perf_counter() - start:.2f}s")

    # ✅ Always convert AIMessage → string
    if hasattr(response, "content"):
        return response.content.strip()

    return str(response).strip()


def run_batch_query_pipeline(questions: list[str]) -> list[str]:
    results = [""] * len(questions)
    futures = {}

    # ✅ Run sequentially to avoid Google GenAI throttling
    with ThreadPoolExecutor(max_workers=1) as executor:
        for idx, question in enumerate(questions):
            futures[executor.submit(run_query_pipeline, question)] = idx

        for future in as_completed(futures):
            idx = futures[future]
            try:
                results[idx] = future.result()
            except Exception as e:
                results[idx] = f"[Error] {str(e)}"
                print(f"❌ Actual Error in question {idx+1}: {e}")

    return results
