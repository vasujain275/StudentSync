import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuthStore from "@/lib/useAuthStore";

export const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const setUser = useAuthStore((state) => state.setUser);
  const navigate = useNavigate();

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();
    try {
      // Call the login endpoint (the backend sets HTTP-only cookies)
      const response = await fetch("/api/v1/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
        credentials: "include", // ensure cookies are sent/received
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
      }

      // After a successful login, fetch user details from /auth/me
      const meResponse = await fetch("/api/v1/auth/me", {
        credentials: "include",
      });

      if (!meResponse.ok) {
        throw new Error("Failed to fetch user info");
      }
      const userData = await meResponse.json();
      setUser(userData);

      // Redirect based on the user's role (customize as needed)
      if (userData.userRole === "SUPER_ADMIN")
        navigate("/dashboard/superadmin");
      else if (userData.userRole === "ADMIN") navigate("/dashboard/admin");
      else if (userData.userRole === "STUDENT") navigate("/dashboard/student");
      else navigate("/");
    } catch (err) {
      console.error(`Error Occurred - ${err}`);
    }
  };

  return (
    <Card className="w-full max-w-sm mx-auto">
      <CardHeader>
        <CardTitle className="text-lg font-semibold text-center">
          Login
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4" onSubmit={handleLogin}>
          <div>
            <Label htmlFor="username">Username</Label>
            <Input
              id="username"
              type="text"
              placeholder="Enter your username"
              className="mt-1"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div>
            <Label htmlFor="password">Password</Label>
            <Input
              id="password"
              type="password"
              placeholder="Enter your password"
              className="mt-1"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <Button type="submit" className="w-full">
            Login
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};
