import { useState, useCallback, useEffect } from "react";
import { useChats } from "./useChats";
import { useChatUpdate } from "./useChatUpdate";
import { useChatCreate } from "./useChatCreate";
import { useChatDelete } from "./useChatDelete";

export default function useChat() {
  const { chats } = useChats();
  const [activeConversationId, setActiveConversationId] = useState<
    string | null
  >(null);
  const [isTyping, setIsTyping] = useState(false);
  const { addMessageToChat } = useChatUpdate({
    onSuccess: () => setIsTyping(false),
    onError: () => setIsTyping(false),
  });
  const { createChat } = useChatCreate({});
  const { deleteChat } = useChatDelete({});

  const [activeConversation, setActiveConversation] = useState(
    chats.find((c) => c.id === activeConversationId) || null
  );

  const optimisticAddMessage = useCallback((content: string) => {
    const newMessage: Message = {
      id: crypto.randomUUID(),
      content,
      timestamp: new Date(),
      role: "USER",
    };
    setActiveConversation((prev) => {
      if (!prev) return null;
      return {
        ...prev,
        messages: [...prev.messages, newMessage],
      };
    });
  }, []);

  const addMessage = useCallback(
    (content: string) => {
      setIsTyping(true);
      optimisticAddMessage(content);
      addMessageToChat({
        message: content,
        conversationId: activeConversationId || "",
      });
    },
    [activeConversationId, addMessageToChat, optimisticAddMessage]
  );

  const createNewConversation = useCallback(
    async (title?: string) => await createChat(title ?? "New Conversation"),
    [createChat]
  );

  const deleteConversation = useCallback(
    (conversationId: string) => {
      deleteChat({ conversationId });
    },
    [deleteChat]
  );

  useEffect(() => {
    setActiveConversation(
      chats.find((c) => c.id === activeConversationId) || null
    );
  }, [setActiveConversation, chats, activeConversationId]);

  return {
    conversations: chats,
    activeConversation,
    activeConversationId,
    setActiveConversationId,
    addMessage,
    createNewConversation,
    deleteConversation,
    isTyping,
  };
}
