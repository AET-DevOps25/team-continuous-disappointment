import { useState, useCallback } from "react";
import { generateId } from "../utils/helpers";

export default function useChat() {
  const [conversations, setConversations] = useState<Conversation[]>([
    {
      id: generateId(),
      title: "New conversation",
      messages: [],
      createdAt: new Date(),
    },
  ]);
  const [activeConversationId, setActiveConversationId] = useState<string>(
    conversations[0].id
  );
  const [isTyping, setIsTyping] = useState(false);

  const activeConversation =
    conversations.find((c) => c.id === activeConversationId) ||
    conversations[0];

  const addMessage = useCallback(
    (content: string, role: "user" | "assistant") => {
      const newMessage: Message = {
        id: generateId(),
        content,
        role,
        timestamp: new Date(),
      };

      setConversations((prevConversations) =>
        prevConversations.map((conversation) =>
          conversation.id === activeConversationId
            ? {
                ...conversation,
                messages: [...conversation.messages, newMessage],
                title:
                  conversation.messages.length === 0 && role === "user"
                    ? content.substring(0, 30) +
                      (content.length > 30 ? "..." : "")
                    : conversation.title,
              }
            : conversation
        )
      );

      if (role === "user") {
        // Simulate bot typing
        setIsTyping(true);
        const typingTimeout = setTimeout(() => {
          const responses = [
            "I'm an AI assistant here to help you with your questions.",
            "That's an interesting question. Let me think about it.",
            "I can provide information on a wide range of topics.",
            "I'm designed to be helpful, harmless, and honest in my responses.",
            "I'm still learning, but I'll do my best to assist you.",
          ];
          const randomResponse =
            responses[Math.floor(Math.random() * responses.length)];
          addMessage(randomResponse, "assistant");
          setIsTyping(false);
        }, 1500);

        return () => clearTimeout(typingTimeout);
      }
    },
    [activeConversationId]
  );

  const createNewConversation = useCallback(() => {
    const newConversation: Conversation = {
      id: generateId(),
      title: "New conversation",
      messages: [],
      createdAt: new Date(),
    };

    setConversations((prev) => [...prev, newConversation]);
    setActiveConversationId(newConversation.id);
    return newConversation;
  }, []);

  const deleteConversation = useCallback(
    (id: string) => {
      setConversations((prev) =>
        prev.filter((conversation) => conversation.id !== id)
      );

      if (activeConversationId === id && conversations.length > 1) {
        // Set the active conversation to the most recent one that's not the deleted one
        const remainingConversations = conversations.filter((c) => c.id !== id);
        setActiveConversationId(remainingConversations[0].id);
      }
    },
    [activeConversationId, conversations]
  );

  return {
    conversations,
    activeConversation,
    activeConversationId,
    setActiveConversationId,
    addMessage,
    createNewConversation,
    deleteConversation,
    isTyping,
  };
}
