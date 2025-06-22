from rag.llm.chat_model import ChatModel
from langchain_core.messages import HumanMessage, AIMessage
from unittest.mock import patch

def test_llm_type_property():
    model = ChatModel()
    assert model._llm_type == "recipai-custom-model"

def test_get_system_prompt_contains_context():
    model = ChatModel()
    prompt = model.get_system_prompt()
    assert isinstance(prompt, str)
    assert "{context}" in prompt

@patch("rag.llm.chat_model.generate_response")
def test_generate_calls_openwebui_and_returns_response(mock_generate):
    mock_generate.return_value = "This is a mock response"
    model = ChatModel(model_name="mock-model")

    messages = [
        HumanMessage(content="What can I cook with potatoes?"),
        AIMessage(content="You can make mashed potatoes."),
        HumanMessage(content="Anything more creative?")
    ]

    result = model._generate(messages)
    assert result.generations[0].message.content == "This is a mock response"
    mock_generate.assert_called_once()
    called_model_name, called_prompt = mock_generate.call_args[0]
    assert called_model_name == "mock-model"
    assert "User: What can I cook with potatoes?" in called_prompt
    assert "Assistant: You can make mashed potatoes." in called_prompt