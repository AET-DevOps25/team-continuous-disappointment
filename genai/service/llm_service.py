# from config import Config
# from rag.llm.cloud_chat_model import CloudLLM
from rag.llm.chat_model import ChatModel

from langchain_core.prompt_values import PromptValue


# Set chat model for local llm models
# Make calls to local models in openwebui hosted by the university
llm = ChatModel(model_name="llama3.3:latest")

# Alternatively, we can switch to a chat model based on cloud models as well
# If you want to use other cloud models, please adjust model_name,
# model_provider, and api key
# accordingly

# Examples:
# llm_cloud_anthropic = CloudLLM(
#     model_name="claude-3-sonnet-20240229",
#     model_provider="anthropic",
#     api_key=Config.api_key_anthropic,
# )
# llm_cloud_openai = CloudLLM(
#     model_name="gpt-4-1106-preview",
#     model_provider="openai",
#     api_key=Config.api_key_openai,
# )
#
# llm_cloud_mistral = CloudLLM(
#     model_name="mistral-medium",
#     model_provider="mistral",
#     api_key=Config.api_key_mistral,
# )

# If no parameters are provided, the default cloud model will be openai.
# If a cloud model is wanted, please remove the comment
# for package import "CloudLLM"

# Example:
# llm = CloudLLM()  # same as llm_cloud_openai


def get_system_prompt() -> str:
    """
    Returns the system prompt for the LLM.
    This function provides the initial context and instructions for the LLM.
    """
    return llm.get_system_prompt()


def generate_response(prompt: PromptValue) -> str:
    response = llm.invoke(prompt)
    return response.content
