import { useState, useEffect } from "react";
import { useNavigate, Outlet } from "react-router-dom";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import useChat from "../hooks/useChat";
import { classNames } from "../utils/helpers";

function Layout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
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

  const handleNewConversation = async () => {
    const newConversation = await createNewConversation();
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
    <div className="flex h-screen bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <div id="sidebar" className="relative h-full">
        <Sidebar
          conversations={conversations}
          createNewConversation={handleNewConversation}
          deleteConversation={handleDeleteConversation}
          isOpen={isSidebarOpen}
        />
        {isSidebarOpen && (
          <button
            className="absolute bottom-4 right-4 p-2 rounded-full bg-gray-200 dark:bg-gray-700 hover:bg-gray-300 dark:hover:bg-gray-600 shadow-md transition-colors"
            onClick={() => setIsSidebarOpen(false)}
            aria-label="Collapse sidebar"
          >
            <span className="material-icons">chevron_left</span>
          </button>
        )}
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
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;
