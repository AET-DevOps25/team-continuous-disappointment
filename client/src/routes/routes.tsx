import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import ChatPage from "../pages/ChatPage";
import LoginPage from "../pages/LoginPage";
import PreferencesPage from "../pages/PreferencesPage";
import { UserContextProvider } from "../contexts/userContext";

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/",
    element: (
      <UserContextProvider>
        <Layout />
      </UserContextProvider>
    ),
    children: [
      {
        path: "",
        element: <ChatPage />,
      },
      {
        path: "c/:conversationId",
        element: <ChatPage />,
      },
      {
        path: "preferences",
        element: <PreferencesPage />,
      },
      { path: "*", element: <div>404</div> },
    ],
  },
]);
