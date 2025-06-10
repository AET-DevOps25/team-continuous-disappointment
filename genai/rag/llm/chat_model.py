from typing import List
from langchain_core.messages import BaseMessage, HumanMessage, AIMessage
from langchain_core.language_models.chat_models import BaseChatModel
from langchain_core.outputs import ChatResult, ChatGeneration
from pydantic import Field

from genai.service.openwebui_service import generate_response


class ChatModel(BaseChatModel):
    model_name: str = Field(default="llama3.3:latest")

    def _generate(self, messages: List[BaseMessage],
                  stop=None,
                  **kwargs) -> ChatResult:
        prompt = "\n".join([
            f"User: {m.content}" if isinstance(m, HumanMessage)
            else f"Assistant: {m.content}" if isinstance(m, AIMessage)
            else ""
            for m in messages
            ])
        response_text = generate_response(self.model_name, prompt)

        return ChatResult(
            generations=[
                ChatGeneration(message=AIMessage(content=response_text))
                ]
        )

    @property
    def _llm_type(self) -> str:
        return "recipai-custom-model"

    def get_system_prompt(self) -> str:
        """System prompt for the LLM"""
        return """
            You are an intelligent assistant that helps users discover
            and generate recipes based on the ingredients they provide.

            Use the contextual information provided below to tailor
            your responses.

            If relevant recipes or suggestions are found in the context,
            prioritize those. If no relevant context is available,
            use your own knowledge to help the user.

            Context:
            {context}

            Be clear, creative, and helpful. If the user also asks
            follow-up questions (e.g., dietary adjustments, name references,
            meal timing), answer them precisely based on the
            context and query.
            """
