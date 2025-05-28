import React from 'react';
import { Message } from '../types';
import { formatTimestamp } from '../utils/helpers';
import { User, Bot } from 'lucide-react';

interface MessageItemProps {
  message: Message;
}

const MessageItem: React.FC<MessageItemProps> = ({ message }) => {
  const isUser = message.role === 'user';

  return (
    <div
      className={`py-6 px-4 md:px-8 ${
        isUser ? 'bg-white dark:bg-gray-800' : 'bg-gray-50 dark:bg-gray-900'
      } animate-fadeIn`}
    >
      <div className="max-w-3xl mx-auto flex items-start space-x-4">
        <div className={`flex-shrink-0 h-8 w-8 rounded-full flex items-center justify-center ${
          isUser ? 'bg-blue-100 text-blue-600 dark:bg-blue-900 dark:text-blue-300' : 'bg-green-100 text-green-600 dark:bg-green-900 dark:text-green-300'
        }`}>
          {isUser ? <User className="h-5 w-5" /> : <Bot className="h-5 w-5" />}
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center mb-1">
            <p className="text-sm font-semibold text-gray-900 dark:text-white">
              {isUser ? 'You' : 'AI Assistant'}
            </p>
            <span className="ml-2 text-xs text-gray-500 dark:text-gray-400">
              {formatTimestamp(message.timestamp)}
            </span>
          </div>
          <div className="prose dark:prose-invert prose-sm sm:prose-base max-w-none">
            {message.content.split('\n').map((paragraph, i) => (
              <p key={i} className="mb-2 text-gray-800 dark:text-gray-200">
                {paragraph}
              </p>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default MessageItem;