export {};
declare global {
  type Message = {
    id: string;
    content: string;
    timestamp: Date;
    role: "user" | "assistant";
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
