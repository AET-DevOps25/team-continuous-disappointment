import { useState, useCallback } from "react";
import { useChats } from "./useChats";
import { useChatUpdate } from "./useChatUpdate";
import { useChatCreate } from "./useChatCreate";
import { useChatDelete } from "./useChatDelete";

export default function useChat() {
  const { chats } = useChats();
  const [activeConversationId, setActiveConversationId] = useState<
    string | null
  >(null);
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [isTyping, setIsTyping] = useState(false);
  const { addMessageToChat } = useChatUpdate({});
  const { createChat } = useChatCreate({});
  const { deleteChat } = useChatDelete({});

  const activeConversation =
    chats.find((c) => c.id === activeConversationId) || null;

  const addMessage = useCallback(
    (content: string) => {
      addMessageToChat({
        message: content,
        conversationId: activeConversationId || "",
      });
    },
    [activeConversationId, addMessageToChat]
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
