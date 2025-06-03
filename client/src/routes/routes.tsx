import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import { PrivateRoute } from "./PrivateRoute";
import ChatPage from "../pages/ChatPage";
import LoginPage from "../pages/LoginPage";
export const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <PrivateRoute component={<ChatPage />} />,
      },

      {
        path: "/c/:conversationId",
        element: <PrivateRoute component={<ChatPage />} />,
      },

      { path: "*", element: <div>404</div> },
    ],
  },
]);
