import { useState, useEffect } from "react";
import { Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { AuthProvider } from "react-oidc-context";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import useChat from "./hooks/useChat";
import { classNames } from "./utils/helpers";
import ChatPage from "./pages/ChatPage";
import { oidcConfig } from "./oidcConfig";
import "./index.css";

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const { conversations, createNewConversation, deleteConversation } =
    useChat();
  const navigate = useNavigate();

  // Handle closing sidebar on mobile when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      const sidebar = document.getElementById("sidebar");
      const sidebarButton = document.getElementById("sidebar-button");

      if (
        isSidebarOpen &&
        sidebar &&
        sidebarButton &&
        !sidebar.contains(event.target as Node) &&
        !sidebarButton.contains(event.target as Node)
      ) {
        setIsSidebarOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isSidebarOpen]);

  const handleNewConversation = () => {
    const newConversation = createNewConversation();
    navigate(`/c/${newConversation.id}`);
  };

  const handleDeleteConversation = (id: string) => {
    deleteConversation(id);
    if (conversations.length > 1) {
      const remainingConversations = conversations.filter((c) => c.id !== id);
      navigate(`/c/${remainingConversations[0].id}`);
    } else {
      handleNewConversation();
    }
  };

  return (
    <AuthProvider {...oidcConfig}>
      <div className="flex h-screen bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100">
        <div id="sidebar">
          <Sidebar
            conversations={conversations}
            createNewConversation={handleNewConversation}
            deleteConversation={handleDeleteConversation}
            isOpen={isSidebarOpen}
          />
        </div>

        <div
          className={classNames(
            "flex flex-col flex-1 h-full transition-all duration-300 ease-in-out",
            isSidebarOpen ? "md:ml-72" : ""
          )}
        >
          <Header
            isSidebarOpen={isSidebarOpen}
            toggleSidebar={() => setIsSidebarOpen(!isSidebarOpen)}
          />

          <Routes>
            <Route path="/c/:conversationId" element={<ChatPage />} />
            <Route
              path="/"
              element={<Navigate to={`/c/${conversations[0].id}`} replace />}
            />
          </Routes>
        </div>
      </div>
    </AuthProvider>
  );
}

export default App;
