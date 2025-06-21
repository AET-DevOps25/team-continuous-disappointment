import os

from langchain.chat_models import init_chat_model
from langchain_core.prompt_values import PromptValue
from langchain_core.messages import BaseMessage

from config import Config


class CloudLLM:
    """A concrete implementation of a cloud-based LLM. Uses openai as the default LLM provider."""

    def __init__(
            self,
            model_name: str = "gpt-4-1106-preview",
            model_provider: str = "openai",
            api_key: str = Config.api_key_openai
    ):
        provider = model_provider.lower()
        if provider == "openai":
            os.environ["OPENAI_API_KEY"] = (
                    api_key
                    or
                    os.getenv("API_OPENAI", "")
            )
        elif provider == "anthropic":
            os.environ["ANTHROPIC_API_KEY"] = (
                    api_key
                    or
                    os.getenv("API_ANTHROPIC", "")
            )
        elif provider == "mistral":
            os.environ["MISTRAL_API_KEY"] = (
                    api_key
                    or
                    os.getenv("API_MISTRAL", "")
            )
        elif provider == "huggingface":
            os.environ["HUGGINGFACEHUB_API_TOKEN"] = (
                    api_key
                    or
                    os.getenv("API_HUGGINGFACEHUB", "")
            )
        else:
            raise ValueError(f"Unsupported LLM provider: {provider}")

        self.model = init_chat_model(
            model=model_name,
            model_provider=provider
        )

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
    def invoke(self, prompt: PromptValue) -> BaseMessage:
        """Invoke the LLM with the given prompt"""
        return self.model.invoke(prompt)

