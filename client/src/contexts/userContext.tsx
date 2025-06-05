import { createContext, ReactNode, useContext } from "react";
import { useAuth } from "../hooks/useAuth";

interface UserContextType {
  user: User | null;
  isLoading: boolean;
}

const UserContext = createContext<UserContextType | null>(null);

export const UserContextProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const { user, isLoading } = useAuth();
  return (
    <UserContext.Provider value={{ user, isLoading }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};
export default UserContext;
