import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useUser } from "../contexts/userContext";
import { createConversation } from "../services/chatService";

interface UseChatCreateProps {
  onSuccess?: () => void;
  onError?: (error: Error) => void;
}
export const useChatCreate = ({ onSuccess, onError }: UseChatCreateProps) => {
  const { user } = useUser();
  const queryClient = useQueryClient();

  const { mutateAsync, isError, error } = useMutation({
    mutationKey: ["chatCreate", user?.id],
    mutationFn: (title: string) => createConversation(user!.token, title),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chats"] });
      if (onSuccess) {
        onSuccess();
      }
    },
    onError: (error: Error) => {
      console.error("Error creating chat:", error);
      if (onError) {
        onError(error);
      }
    },
  });

  return { createChat: mutateAsync, isError, error };
};
