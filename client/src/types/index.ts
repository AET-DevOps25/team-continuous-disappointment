export {};
declare global {
  type Message = {
    id: string;
    content: string;
    role: "user" | "assistant";
    timestamp: Date;
  };

  type Conversation = {
    id: string;
    title: string;
    messages: Message[];
    createdAt: Date;
  };

  type User = {
    id: number;
    username: string;
    name: string;
    email: string;
    token: string; //bearer token
  };
}
