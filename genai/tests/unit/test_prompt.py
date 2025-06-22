from service.rag_service import process_raw_messages, prepare_prompt
from langchain_core.messages import HumanMessage, AIMessage


def test_process_raw_messages_creates_correct_types():
    raw = [
        {"role": "user", "content": "Hi"},
        {"role": "assistant", "content": "Hello!"}
    ]
    messages = process_raw_messages(raw)
    assert isinstance(messages[0], HumanMessage)
    assert isinstance(messages[1], AIMessage)
    assert messages[0].content == "Hi"
    assert messages[1].content == "Hello!"


def test_process_raw_messages_ignores_unknown_roles():
    raw = [
        {"role": "user", "content": "Hi"},
        {"role": "system", "content": "Should be ignored"}
    ]
    messages = process_raw_messages(raw)
    assert len(messages) == 1
    assert isinstance(messages[0], HumanMessage)


def test_prepare_prompt_structure():
    system_prompt = "You are a helpful assistant. Context: {context}"
    query = "What's a good recipe with eggs?"
    docs = "Here are some egg-based recipes."
    messages = [AIMessage(content="Hi there!")]

    prompt = prepare_prompt(system_prompt, query, docs, messages)

    assert hasattr(prompt, "to_messages")
    final_messages = prompt.to_messages()
    assert isinstance(final_messages[-1], HumanMessage)
    assert "What's a good recipe with eggs?" in final_messages[-1].content


def test_prepare_prompt_includes_docs_context():
    system_prompt = "Use this: {context}"
    query = "Tell me something"
    docs = "Documented info"
    messages = []

    prompt = prepare_prompt(system_prompt, query, docs, messages)
    rendered = prompt.to_string()
    assert "Documented info" in rendered
    assert "Tell me something" in rendered
