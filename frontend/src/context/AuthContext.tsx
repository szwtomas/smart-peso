import React, { createContext, useState } from "react";
import { api } from "../api";

export interface User {
  email: string;
  firstName: string;
  lastName: string;
  accessToken: string | undefined;
}

interface AuthContextProps {
  user: User | null;
  logIn: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  signUp: (
    email: string,
    password: string,
    firstName: string,
    lastName: string
  ) => Promise<void>;
}

const initialAuthContext: AuthContextProps = {
  user: null,
  logIn: async () => console.log("Login"),
  logout: async () => console.log("Logout"),
  signUp: async () => console.log("Signup"),
};

export const AuthContext = createContext<AuthContextProps>(initialAuthContext);

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = (props) => {
  const [user, setUser] = useState<User | null>(null);

  const logIn = async (email: string, password: string) => {
    const logInResponse = await api.logIn(email, password);
    setUser(logInResponse);
  };

  const logout = async () => {
    setUser(null);
  };

  const signUp = async (
    email: string,
    password: string,
    firstName: string,
    lastName: string
  ): Promise<void> => {
    const signUpResponse = await api.signUp({
      email,
      password,
      firstName,
      lastName,
    });

    setUser(signUpResponse);
  };

  return (
    <AuthContext.Provider value={{ user, logIn, logout, signUp }}>
      {props.children}
    </AuthContext.Provider>
  );
};
