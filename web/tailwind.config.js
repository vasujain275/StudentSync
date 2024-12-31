/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ["class"],
  content: ["./index.html", "./src/**/*.{ts,tsx,js,jsx}"],
  theme: {
    extend: {
      colors: {
        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",
        card: {
          DEFAULT: "hsl(var(--card))",
          foreground: "hsl(var(--card-foreground))",
        },
        popover: {
          DEFAULT: "hsl(var(--popover))",
          foreground: "hsl(var(--popover-foreground))",
        },
        // Custom color names
        primary: {
          DEFAULT: "#0ea5e9", // Light blue
          light: "#38bdf8", // Lighter blue
          dark: "#0284c7", // Dark blue
          foreground: "#ffffff", // White text for contrast
        },
        secondary: {
          DEFAULT: "#0369a1", // Secondary blue
          foreground: "#ffffff", // White text for contrast
        },
        muted: {
          DEFAULT: "#7dd3fc", // Muted light blue
          foreground: "#4b5563", // Muted dark gray text
        },
        accent: {
          DEFAULT: "#f0f9ff", // Very light blue for accents
          foreground: "#0369a1", // Secondary color for accent text
        },
        destructive: {
          DEFAULT: "#ef4444", // Red for destructive actions
          foreground: "#ffffff", // White text for contrast
        },
        border: "#d1d5db", // Light gray border
        input: "#e5e7eb", // Light input background
        ring: "#0284c7", // Blue ring
        chart: {
          1: "#0ea5e9", // Primary blue
          2: "#0284c7", // Darker blue
          3: "#7dd3fc", // Lighter blue
          4: "#f0f9ff", // Very light blue
          5: "#0369a1", // Secondary blue
        },
      },
      fontFamily: {
        sans: ["Inter", "sans-serif"],
        serif: ["Merriweather", "serif"],
        mono: ["Menlo", "monospace"],
      },
      borderRadius: {
        lg: "var(--radius)",
        md: "calc(var(--radius) - 2px)",
        sm: "calc(var(--radius) - 4px)",
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
};
