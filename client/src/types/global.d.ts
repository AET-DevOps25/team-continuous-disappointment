import { DIETARY_PREFERENCES } from "./enums";

export {};
declare global {
  type Message = {
    id: string;
    content: string;
    timestamp: Date;
    role: "USER" | "ASSISTANT";
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
    dietaryPreferences: DIETARY_PREFERENCES[];
    token: string; //bearer token
  };

  type DietaryPreference =
    (typeof DIETARY_PREFERENCES)[keyof typeof DIETARY_PREFERENCES];
}
