import React from 'react';
import { Bot } from 'lucide-react';

const TypingIndicator: React.FC = () => {
  return (
    <div className="py-6 px-4 md:px-8 bg-gray-50 dark:bg-gray-900">
      <div className="max-w-3xl mx-auto flex items-start space-x-4">
        <div className="flex-shrink-0 h-8 w-8 rounded-full flex items-center justify-center bg-green-100 text-green-600 dark:bg-green-900 dark:text-green-300">
          <Bot className="h-5 w-5" />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center mb-1">
            <p className="text-sm font-semibold text-gray-900 dark:text-white">
              AI Assistant
            </p>
          </div>
          <div className="flex space-x-1.5">
            <div className="w-2 h-2 rounded-full bg-gray-400 dark:bg-gray-500 animate-bounce" style={{ animationDelay: '0.1s' }}></div>
            <div className="w-2 h-2 rounded-full bg-gray-400 dark:bg-gray-500 animate-bounce" style={{ animationDelay: '0.2s' }}></div>
            <div className="w-2 h-2 rounded-full bg-gray-400 dark:bg-gray-500 animate-bounce" style={{ animationDelay: '0.3s' }}></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TypingIndicator;