import { StrictMode } from "react";
import { AuthProvider } from "react-oidc-context";
import { createRoot } from "react-dom/client";
import { oidcConfig } from "./oidcConfig";
import "./index.css";

import App from "./App.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <App />
    </AuthProvider>
  </StrictMode>
);
