import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAuthStore from "@/lib/useAuthStore";

const Logout = () => {
  const logout = useAuthStore((state) => state.logout);
  const navigate = useNavigate();

  useEffect(() => {
    const performLogout = async () => {
      try {
        const response = await fetch("/api/v1/auth/logout", {
          method: "POST",
          credentials: "include", // ensure cookies are sent/received
        });

        if (response.ok) {
          // Clear client-side auth state
          logout();
          // Redirect to login page
          navigate("/", { replace: true });
        } else {
          console.error("Logout failed:", response.statusText);
        }
      } catch (error) {
        console.error("Logout error:", error);
      }
    };

    performLogout();
  }, [logout, navigate]);

  return <div>Logging out...</div>;
};

export default Logout;
