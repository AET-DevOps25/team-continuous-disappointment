from typing import List
from langchain_core.messages import BaseMessage, HumanMessage, AIMessage
from langchain_core.language_models.chat_models import BaseChatModel
from langchain_core.outputs import ChatResult, ChatGeneration
from pydantic import Field

from genai.service.openwebui_service import generate_response


class ChatModel(BaseChatModel):
    model_name: str = Field(default="llama3.3:latest")

    def _generate(self, messages: List[BaseMessage], stop=None, **kwargs) -> ChatResult:
        prompt = "\n".join([msg.content for msg in messages if isinstance(msg, HumanMessage)])
        response_text = generate_response(self.model_name, prompt)

        return ChatResult(
            generations=[ChatGeneration(message=AIMessage(content=response_text))]
        )

    @property
    def _llm_type(self) -> str:
        return "recipai-custom-model"

# For Testing purposes 
# if __name__ == "__main__":
#     llm = ChatModel(model_name="llama3.3:latest")

#     message = HumanMessage(content="What is langchain, explain very briefly?")

#     response = llm.invoke([message])

#     print("LLM response:\n", response.content)
