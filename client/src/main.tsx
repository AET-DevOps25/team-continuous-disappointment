import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { AuthProvider } from "react-oidc-context";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import { router } from "./routes/routes";
import "./index.css";

const queryClient = new QueryClient();

(async () => {
  const gitlabConfig = await fetch("/config.json")
    .then((response) => response.json())
    .then((config) => config as { redirectUri: string; clientId: string });

  const oidcConfig = {
    authority: "https://gitlab.lrz.de",
    response_type: "code",
    scope: "read_user",
    onSigninCallback: () => {
      const url = new URL(window.location.href);
      url.searchParams.delete("code");
      url.searchParams.delete("state");
      window.history.replaceState(
        {},
        document.title,
        url.pathname + url.search
      );
    },
    redirect_uri: gitlabConfig.redirectUri,
    client_id: gitlabConfig.clientId,
  };

  createRoot(document.getElementById("root")!).render(
    <StrictMode>
      <AuthProvider {...oidcConfig}>
        <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
        </QueryClientProvider>
      </AuthProvider>
    </StrictMode>
  );
})();
