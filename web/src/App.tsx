import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "@/components/HomePage";
import { LoginForm } from "@/components/auth/LoginForm";
import SuperadminDashboard from "@/components/dashboard/superadmin/SuperadminDashboard";
import AdminDashboard from "@/components/dashboard/admin/AdminDashboard";
import StudentDashboard from "@/components/dashboard/student/StudentDashboard";
import ProtectedRoute from "@/components/auth/ProtectedRoute";
import { useEffect } from "react";
import useAuthStore from "@/lib/useAuthStore";
import Logout from "@/components/auth/Logout";

function App() {
  const setUser = useAuthStore((state) => state.setUser);

  // On app load, try to fetch the current user from the backend.
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch("/api/v1/auth/me", {
          credentials: "include",
        });
        if (res.ok) {
          const data = await res.json();
          setUser(data);
        }
      } catch (err) {
        console.error("Failed to fetch user", err);
      }
    };
    fetchUser();
  }, [setUser]);

  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginForm />} />

        {/* Protected Routes */}
        <Route element={<ProtectedRoute allowedRoles={["SUPER_ADMIN"]} />}>
          <Route
            path="/dashboard/superadmin"
            element={<SuperadminDashboard />}
          />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
          <Route path="/dashboard/admin" element={<AdminDashboard />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["STUDENT"]} />}>
          <Route path="/dashboard/student" element={<StudentDashboard />} />
        </Route>
        <Route path="/logout" element={<Logout />} />
      </Routes>
    </Router>
  );
}

export default App;
