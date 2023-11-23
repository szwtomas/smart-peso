import React, { createContext, useEffect, useState } from "react";
import {
  User,
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signOut,
  createUserWithEmailAndPassword,
} from "firebase/auth";
import { app } from "../firebase/firebase";

interface AuthContextProps {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  isLoggedIn: () => boolean;
  signUp: (email: string, password: string, username: string) => Promise<void>;
}

const initialAuthContext: AuthContextProps = {
  user: null,
  login: async () => console.log("Login"),
  logout: async () => console.log("Logout"),
  isLoggedIn: () => false,
  signUp: async () => console.log("Signup"),
};

export const AuthContext = createContext<AuthContextProps>(initialAuthContext);

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = (props) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const auth = getAuth(app);
    const unsubscribe = onAuthStateChanged(auth, (user: User | null) => {
      console.log("Setting user: ");
      console.log(user);
      setUser(user);
    });

    return () => {
      unsubscribe();
    };
  }, []);

  const login = async (email: string, password: string) => {
    const auth = getAuth(app);
    await signInWithEmailAndPassword(auth, email, password);
    setUser(auth.currentUser);
  };

  const logout = async () => {
    try {
      const auth = getAuth(app);
      await signOut(auth);
      setUser(null);
    } catch (err) {
      console.error(`Error logging out: ${err}`);
    }
  };

  const isLoggedIn = (): boolean => {
    return user !== null;
  };

  const signUp = async (
    email: string,
    password: string,
    username: string
  ): Promise<void> => {
    console.log("Signin up in user: " + username);
    const auth = getAuth(app);
    const response = await createUserWithEmailAndPassword(
      auth,
      email,
      password
    );
    console.log(response);
    // ... Calls backend to create user
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isLoggedIn, signUp }}>
      {props.children}
    </AuthContext.Provider>
  );
};
