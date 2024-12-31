import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const LoginForm = () => {
  return (
    <Card className="w-full max-w-sm mx-auto p-6">
      <CardHeader>
        <CardTitle className="text-lg font-semibold text-center">
          Login
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4">
          <div>
            <Label htmlFor="username" className="text-sm font-medium">
              Username
            </Label>
            <Input
              id="username"
              type="text"
              placeholder="Enter your username"
              className="mt-1"
            />
          </div>
          <div>
            <Label htmlFor="password" className="text-sm font-medium">
              Password
            </Label>
            <Input
              id="password"
              type="password"
              placeholder="Enter your password"
              className="mt-1"
            />
          </div>
          <Button className="w-full bg-blue-600 hover:bg-blue-700 text-base">
            Login
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};
