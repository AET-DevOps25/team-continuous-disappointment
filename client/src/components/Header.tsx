import React from "react";
import { Menu, X, Sun, Moon, Utensils } from "lucide-react";
import { Link } from "react-router-dom";
import useTheme from "../hooks/useTheme";
import { classNames } from "../utils/helpers";

interface HeaderProps {
  isSidebarOpen: boolean;
  toggleSidebar: () => void;
}

const Header: React.FC<HeaderProps> = ({ isSidebarOpen, toggleSidebar }) => {
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="sticky top-0 z-10 w-full border-b border-gray-200 dark:border-gray-700 bg-white/80 dark:bg-gray-900/80 backdrop-blur">
      <div className="flex items-center justify-between h-14 px-4">
        <div className="flex items-center">
          <button
            onClick={toggleSidebar}
            className="p-2 rounded-md text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 focus:outline-none"
            aria-label={isSidebarOpen ? "Close sidebar" : "Open sidebar"}
          >
            {isSidebarOpen ? (
              <X className="h-5 w-5" />
            ) : (
              <Menu className="h-5 w-5" />
            )}
          </button>
          <h1
            className={classNames(
              "text-lg font-semibold text-gray-800 dark:text-white ml-3",
              isSidebarOpen ? "hidden md:block" : "block"
            )}
          >
            RecipAI
          </h1>
        </div>
        <div className="flex items-center space-x-2">
          <Link
            to="/preferences"
            className="p-2 rounded-md text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 focus:outline-none transition-colors"
            aria-label="Dietary Preferences"
          >
            <Utensils className="h-5 w-5" />
          </Link>
          <button
            onClick={toggleTheme}
            className="p-2 rounded-md text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 focus:outline-none transition-colors"
            aria-label={
              theme === "dark" ? "Switch to light mode" : "Switch to dark mode"
            }
          >
            {theme === "dark" ? (
              <Sun className="h-5 w-5" />
            ) : (
              <Moon className="h-5 w-5" />
            )}
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
