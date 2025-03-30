import { create } from "zustand";

export interface User {
  username: string;
  email: string;
  userRole: string;
  firstName?: string;
  lastName?: string;
  avatar?: string;
}

interface AuthState {
  user: User | null;
  setUser: (user: User) => void;
  logout: () => void;
}

const useAuthStore = create<AuthState>((set) => ({
  user: null,
  setUser: (user) => set({ user }),
  logout: () => set({ user: null }),
}));

export default useAuthStore;
