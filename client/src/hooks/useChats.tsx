import { useQuery } from "@tanstack/react-query";
import { getConversationsOfUser } from "../services/chatService";
import { useUser } from "../contexts/userContext";

export const useChats = () => {
  const { user } = useUser();
  const { isLoading, isError, data, error } = useQuery({
    queryKey: ["chats"],
    queryFn: () => getConversationsOfUser(user!.token),
    enabled: !!user,
  });

  return { isLoading, isError, chats: data ?? [], error };
};
