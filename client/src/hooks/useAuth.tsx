import { useState } from "react";
import { useAuth as useOAuth } from "react-oidc-context";

export const useAuth = () => {
  const auth = useOAuth();
  const [user, setUser] = useState<User | null>(null);

  if (auth.isLoading) {
    return { user, isLoading: auth.isLoading, signout: auth.signoutRedirect };
  }
  if (!auth.user || auth.user.expired) {
    auth.signinRedirect();
  }

  if (auth.user?.access_token && !user) {
    fetch("https://gitlab.lrz.de/api/v4/user", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${auth.user.access_token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Error: ${response.status} ${response.statusText}`);
        }
        return response.json();
      })
      .then((userData) => {
        setUser({ ...userData, token: auth.user?.access_token });
      })
      .catch((error) => {
        console.error("Failed to fetch user data:", error);
      });
  }

  return { user, isLoading: auth.isLoading, signout: auth.signoutRedirect };
};
