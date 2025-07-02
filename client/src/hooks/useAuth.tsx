import { useAuth as useOAuth } from "react-oidc-context";
import { useNavigate } from "react-router-dom";
import { useUserInfo } from "./useUserInfo";

export const useAuth = () => {
  const auth = useOAuth();
  const navigate = useNavigate();
  const { user } = useUserInfo();
  if (auth.isLoading) {
    return { user, isLoading: auth.isLoading, signout: auth.signoutRedirect };
  }

  if (!auth.user || auth.user.expired) {
    navigate("/login");
  }

  return { user, isLoading: auth.isLoading, signout: auth.signoutRedirect };
};
