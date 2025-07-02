import { useQuery } from "@tanstack/react-query";
import { useAuth as useOAuth } from "react-oidc-context";
import { getUserInfo } from "../services/userService";

export const useUserInfo = () => {
  const auth = useOAuth();

  const { isLoading, isError, data, error } = useQuery({
    queryKey: ["userInfo", auth.user?.access_token],
    queryFn: () => {
      if (!auth.user?.access_token) {
        throw new Error("No access token available");
      }
      return getUserInfo(auth.user.access_token);
    },
    enabled: !!auth.user?.access_token,
  });

  return {
    isLoading,
    isError,
    user: data ?? null,
    error,
  };
};
