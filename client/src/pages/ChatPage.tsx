import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import MessageList from "../components/MessageList";
import ChatInput from "../components/ChatInput";
import useChat from "../hooks/useChat";

function ChatPage() {
  const { conversationId } = useParams();
  const navigate = useNavigate();
  const {
    conversations,
    activeConversation,
    setActiveConversationId,
    addMessage,
    isTyping,
  } = useChat();

  useEffect(() => {
    if (conversationId) {
      const conversation = conversations.find((c) => c.id === conversationId);
      if (conversation) {
        setActiveConversationId(conversationId);
      } else {
        // If conversation doesn't exist, redirect to the first conversation
        navigate(`/c/${conversations[0].id}`);
      }
    }
  }, [conversationId, conversations, navigate, setActiveConversationId]);

  const handleSendMessage = (content: string) => {
    addMessage(content, "user");
  };

  if (!activeConversation) {
    return null;
  }

  return (
    <>
      <main className="flex flex-col flex-1 overflow-hidden">
        <MessageList
          messages={activeConversation.messages}
          isTyping={isTyping}
        />
        <ChatInput onSendMessage={handleSendMessage} disabled={isTyping} />
      </main>
    </>
  );
}

export default ChatPage;
