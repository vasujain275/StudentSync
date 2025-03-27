import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { FormEvent, useState } from "react";

export const LoginForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e: FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch("/api/v1/auth/login", {
        method: "post",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      console.log(response);
      if (!response.ok) {
        throw new Error(`Error: ${response.statusText}`);
      }
    } catch (err) {
      console.log(`Error Occured - ${err}`);
    }
  };

  return (
    <Card className="w-full max-w-sm mx-auto p-6 bg-card">
      <CardHeader>
        <CardTitle className="text-lg font-semibold text-center text-primary">
          Login
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4" onSubmit={(e) => handleLogin(e)}>
          <div>
            <Label
              htmlFor="username"
              className="text-sm font-medium text-muted-foreground"
            >
              Username
            </Label>
            <Input
              id="username"
              type="text"
              placeholder="Enter your username"
              className="mt-1"
              value={username}
              onChange={(e) => {
                setUsername(e.target.value);
              }}
            />
          </div>
          <div>
            <Label
              htmlFor="password"
              className="text-sm font-medium text-muted-foreground"
            >
              Password
            </Label>
            <Input
              id="password"
              type="password"
              placeholder="Enter your password"
              className="mt-1"
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
              }}
            />
          </div>
          <Button className="w-full bg-primary hover:bg-primary-dark text-base text-primary-foreground">
            Login
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};
