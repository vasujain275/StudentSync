import { useState } from "react";
import { Navbar } from "@/components/layout/Navbar";
import { NoticeCard } from "@/components/notices/NoticeCard";
import { LoginForm } from "@/components/auth/LoginForm";

const HomePage = () => {
  const [userType, setUserType] = useState<"student" | "admin">("student");

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          <NoticeCard />
          <LoginForm userType={userType} onUserTypeChange={setUserType} />
        </div>
      </main>
    </div>
  );
};

export default HomePage;
