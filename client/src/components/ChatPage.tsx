import { useState } from 'react';

type Message = {
  text: string;
  sender: 'user' | 'ai';
};

const ChatPage = () => {
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState<Message[]>([]);

  const handleSend = () => {
    if (input.trim()) {
      setMessages([...messages, { text: input, sender: 'user' }]);
      setInput('');
      // Simulate AI response
      setTimeout(() => {
        setMessages((prevMessages) => [...prevMessages, { text: 'AI-generated recipe based on your input: [Sample Recipe Details Here]', sender: 'ai' }]);
      }, 1000);
    }
  };

  return (
    <div className="chat-page">
      <div className="chat-window">
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.sender}`}>{msg.text}</div>
        ))}
      </div>
      <div className="chat-input-area">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Enter ingredients and preferences..."
        />
        <button onClick={handleSend}>Generate Recipe</button>
      </div>
    </div>
  );
};

export default ChatPage;