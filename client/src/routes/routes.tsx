import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import { PrivateRoute } from "./PrivateRoute";
import ChatPage from "../pages/ChatPage";
export const router = createBrowserRouter([
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
