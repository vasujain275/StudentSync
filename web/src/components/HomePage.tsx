import { Navbar } from "@/components/common/Navbar";
import { NoticeCard } from "@/components/notices/NoticeCard";
import { LoginForm } from "@/components/auth/LoginForm";
import { ThemeProvider } from "@/components/theme/theme-provider";

const HomePage = () => {
  return (
    <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
      <div className="min-h-screen bg-background text-foreground">
        <Navbar />
        <main className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div className="flex justify-center">
              <LoginForm />
            </div>
            <div className="flex justify-center">
              <NoticeCard />
            </div>
          </div>
        </main>
      </div>
    </ThemeProvider>
  );
};

export default HomePage;
