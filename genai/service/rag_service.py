from typing import List, Dict

from langchain_qdrant import QdrantVectorStore
from langchain_core.messages import BaseMessage, HumanMessage, AIMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder


def retrieve_similar_docs(vector_store: QdrantVectorStore, user_query: str):
    """Retrieve similar documents based on the user query"""
    retriever = vector_store.as_retriever(search_kwargs={"k": 5})
    retrieved_docs = retriever.invoke(user_query)
    docs_content = "\n\n".join(doc.page_content for doc in retrieved_docs)
    return docs_content


def prepare_prompt(system_prompt: str,
                   user_query: str,
                   docs_content: str,
                   messages: List[BaseMessage]):
    """Prepare the prompt with prompt templates to give to LLM"""
    prompt_template = ChatPromptTemplate([
        "system", system_prompt,
        MessagesPlaceholder("msgs")
    ])

    full_messages = messages + [HumanMessage(content=user_query)]

    prompt = prompt_template.invoke({
        "context": docs_content,
        "msgs": full_messages
    })

    return prompt


def process_raw_messages(raw_messages: List[Dict]) -> List[BaseMessage]:
    """Turns raw messages into BaseMessages, so they can be passed into LLM"""
    processed_messages = []
    for msg in raw_messages:
        role = msg.get("role")
        content = msg.get("content")

        if role.upper() == "USER":
            processed_messages.append(HumanMessage(content=content))

        elif role.upper() == "ASSISTANT":
            processed_messages.append(AIMessage(content=content))

    return processed_messages
