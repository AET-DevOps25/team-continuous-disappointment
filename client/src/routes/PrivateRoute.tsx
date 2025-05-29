import { useAuth } from "../hooks/useAuth";

interface PrivateRouteProps {
  component: React.ReactNode;
}
export const PrivateRoute = ({ component }: PrivateRouteProps) => {
  const { isLoading } = useAuth();
  if (isLoading) {
    return <div>Loading...</div>;
  }
  return <>{component}</>;
};
