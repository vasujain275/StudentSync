import { HelpCircle, School } from "lucide-react";
import { Button } from "@/components/ui/button";
import { ModeToggle } from "@/components/theme/mode-toggle";

export const Navbar = () => {
  return (
    <nav className="bg-background shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <div className="flex items-center">
            <School className="h-8 w-8 text-primary" />
            <span className="ml-2 text-xl font-bold text-foreground">
              StudentSync
            </span>
          </div>
          <div className="flex items-center gap-4">
            <Button
              variant="outline"
              size="sm"
              className="flex items-center gap-2"
            >
              <HelpCircle className="h-4 w-4" />
              Help
            </Button>
            <ModeToggle />
          </div>
        </div>
      </div>
    </nav>
  );
};
