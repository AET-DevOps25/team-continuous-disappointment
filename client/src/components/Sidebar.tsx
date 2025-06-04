import React from "react";
import { Plus, MessageSquare, Trash2 } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import { formatTimestamp, classNames } from "../utils/helpers";

interface SidebarProps {
  conversations: Conversation[];
  createNewConversation: () => void;
  deleteConversation: (id: string) => void;
  isOpen: boolean;
}

const Sidebar: React.FC<SidebarProps> = ({
  conversations,
  createNewConversation,
  deleteConversation,
  isOpen,
}) => {
  const { conversationId } = useParams();

  return (
    <aside
      className={classNames(
        "fixed inset-y-0 left-0 z-20 flex flex-col w-72 bg-gray-50 dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 transition-transform transition-opacity duration-300 ease-in-out",
        isOpen
          ? "translate-x-0 opacity-100 pointer-events-auto"
          : "-translate-x-full opacity-0 pointer-events-none"
      )}
    >
      <div className="flex-shrink-0 h-14 flex items-center px-4 border-b border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-800">
        <button
          onClick={createNewConversation}
          className="flex items-center justify-center w-full px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
        >
          <Plus className="w-4 h-4 mr-2" />
          New Chat
        </button>
      </div>

      <div className="flex flex-col flex-1 overflow-hidden">
        <div className="flex-1 overflow-y-auto p-2 space-y-2">
          {conversations.map((conversation) => (
            <Link
              key={conversation.id}
              to={`/c/${conversation.id}`}
              className={classNames(
                "flex items-center justify-between p-3 rounded-md cursor-pointer group",
                conversationId === conversation.id
                  ? "bg-gray-200 dark:bg-gray-700"
                  : "hover:bg-gray-100 dark:hover:bg-gray-700"
              )}
            >
              <div className="flex items-center min-w-0">
                <MessageSquare className="w-5 h-5 text-gray-500 dark:text-gray-400 mr-3 flex-shrink-0" />
                <div className="min-w-0">
                  <p className="text-sm font-medium text-gray-900 dark:text-white truncate">
                    {conversation.title}
                  </p>
                  <p className="text-xs text-gray-500 dark:text-gray-400">
                    {formatTimestamp(conversation.createdAt)}
                  </p>
                </div>
              </div>
              <button
                onClick={(e) => {
                  e.preventDefault();
                  deleteConversation(conversation.id);
                }}
                className="p-1 text-gray-400 rounded-md hover:text-gray-500 dark:hover:text-gray-300 focus:outline-none opacity-0 group-hover:opacity-100 transition-opacity"
                aria-label="Delete conversation"
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </Link>
          ))}
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
