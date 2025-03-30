import { Navigate, Outlet } from "react-router-dom";
import useAuthStore from "@/lib/useAuthStore";

interface ProtectedRouteProps {
  allowedRoles?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
  const user = useAuthStore((state) => state.user);

  // If there's no authenticated user, redirect to login.
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // If allowedRoles are specified and userRole does not match, redirect.
  if (allowedRoles && !allowedRoles.includes(user.userRole)) {
    return <Navigate to="/login" replace />;
  }

  // Otherwise, render the requested route.
  return <Outlet />;
};

export default ProtectedRoute;
