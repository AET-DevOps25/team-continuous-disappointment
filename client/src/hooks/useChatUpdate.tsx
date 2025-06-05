import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useUser } from "../contexts/userContext";
import { addMessageToConversation } from "../services/chatService";

interface UseChatUpdateProps {
  onSuccess?: () => void;
  onError?: (error: Error) => void;
}

export const useChatUpdate = ({ onSuccess, onError }: UseChatUpdateProps) => {
  const { user } = useUser();
  const queryClient = useQueryClient();
  const { mutate, isError, error } = useMutation({
    mutationKey: ["chatUpdate", user?.id],
    mutationFn: ({
      message,
      conversationId,
    }: {
      message: string;
      conversationId: string;
    }) => addMessageToConversation(user!.token, conversationId, message),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chats"] });
      if (onSuccess) {
        onSuccess();
      }
    },
    onError: (error: Error) => {
      console.error("Error updating chat:", error);
      if (onError) {
        onError(error);
      }
    },
  });

  return { addMessageToChat: mutate, isError, error };
};
