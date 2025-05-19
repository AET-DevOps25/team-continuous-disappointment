import { useAuth } from "../hooks/useAuth";

interface PrivateRouteProps {
  children: React.ReactNode;
}

export const PrivateRoute = ({ children }: PrivateRouteProps) => {
  const { isLoading } = useAuth();
  if (isLoading) {
    return <div>Loading...</div>;
  }
  return <>{children}</>;
};
