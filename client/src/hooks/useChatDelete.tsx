import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useUser } from "../contexts/userContext";
import { deleteConversation } from "../services/chatService";

interface UseChatDelete {
  onSuccess?: () => void;
  onError?: (error: Error) => void;
}

export const useChatDelete = ({ onSuccess, onError }: UseChatDelete) => {
  const { user } = useUser();
  const queryClient = useQueryClient();
  const { mutate, isError, error } = useMutation({
    mutationKey: ["chatDelete", user?.id],
    mutationFn: ({ conversationId }: { conversationId: string }) =>
      deleteConversation(user!.token, conversationId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chats"] });
      if (onSuccess) {
        onSuccess();
      }
    },
    onError: (error: Error) => {
      console.error("Error deleting chat:", error);
      if (onError) {
        onError(error);
      }
    },
  });

  return { deleteChat: mutate, isError, error };
};
