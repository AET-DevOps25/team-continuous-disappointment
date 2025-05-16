export {};

declare global {
  type User = {
    id: number;
    username: string;
    name: string;
    email: string;
    token: string; //bearer token
  };
}
