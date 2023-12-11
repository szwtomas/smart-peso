import React, { createContext, useEffect, useState } from "react";
import { authService } from "../services";

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

  useEffect(() => {
    const defaultLogin = async () => {
      const defaultEmail = import.meta.env.VITE_DEFAULT_EMAIL as string;
      if (defaultEmail) {
        logIn(defaultEmail, import.meta.env.VITE_DEFAULT_PASSWORD as string);
      }
    };

    defaultLogin();
  }, []);

  const logIn = async (email: string, password: string) => {
    const authResponse = await authService.logIn(email, password);
    setUser({
      email: authResponse.email,
      firstName: authResponse.firstName,
      lastName: authResponse.lastName,
      accessToken: authResponse.accessToken,
    });
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
    const authResponse = await authService.signUp({
      email,
      password,
      firstName,
      lastName,
    });

    setUser({
      email: authResponse.email,
      firstName: authResponse.firstName,
      lastName: authResponse.lastName,
      accessToken: authResponse.accessToken,
    });
  };

  return (
    <AuthContext.Provider value={{ user, logIn, logout, signUp }}>
      {props.children}
    </AuthContext.Provider>
  );
};
