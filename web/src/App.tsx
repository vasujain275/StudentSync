import HomePage from "@/components/HomePage";
import { ThemeProvider } from "@/components/theme/theme-provider";

function App() {
  return (
    <>
      <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
        <HomePage />
      </ThemeProvider>
    </>
  );
}

export default App;
