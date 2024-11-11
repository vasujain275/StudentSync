import { User, Shield } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Toggle } from "@/components/ui/toggle";

interface LoginFormProps {
  userType: "student" | "admin";
  onUserTypeChange: (type: "student" | "admin") => void;
}

export const LoginForm = ({ userType, onUserTypeChange }: LoginFormProps) => {
  return (
    <Card>
      <CardHeader>
        <div className="flex justify-center space-x-4 mb-4">
          <Toggle
            pressed={userType === "student"}
            onPressedChange={() => onUserTypeChange("student")}
            className="flex items-center gap-2 data-[state=on]:bg-blue-600"
          >
            <User className="h-4 w-4" />
            Student
          </Toggle>
          <Toggle
            pressed={userType === "admin"}
            onPressedChange={() => onUserTypeChange("admin")}
            className="flex items-center gap-2 data-[state=on]:bg-blue-600"
          >
            <Shield className="h-4 w-4" />
            Admin
          </Toggle>
        </div>
        <CardTitle className="text-center">
          {userType === "student" ? "Student Login" : "Admin Login"}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <form className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input id="email" type="email" placeholder="Enter your email" />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <Input
              id="password"
              type="password"
              placeholder="Enter your password"
            />
          </div>
          <Button className="w-full bg-blue-600 hover:bg-blue-700">
            Login
          </Button>
          {userType === "student" && (
            <p className="text-sm text-center text-gray-500">
              <a href="#" className="text-blue-600 hover:underline">
                Forgot password?
              </a>
            </p>
          )}
        </form>
      </CardContent>
    </Card>
  );
};
