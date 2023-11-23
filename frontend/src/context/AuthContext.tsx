import React, { createContext, useEffect, useState } from "react";
import {
  getAuth,
  User as FirebaseUser,
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signOut,
  createUserWithEmailAndPassword,
  updateProfile,
} from "firebase/auth";
import { app } from "../firebase/firebase";

export interface User {
  email: string;
  uid: string;
  name: string;
}

interface AuthContextProps {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  isLoggedIn: () => boolean;
  signUp: (
    email: string,
    password: string,
    firstName: string,
    lastName: string
  ) => Promise<void>;
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

  function userFromFirebaseUser(firebaseUser: FirebaseUser | null): User {
    return {
      email: firebaseUser?.email as string,
      uid: firebaseUser?.uid as string,
      name: firebaseUser?.displayName as string,
    };
  }

  useEffect(() => {
    const auth = getAuth(app);
    const unsubscribe = onAuthStateChanged(
      auth,
      (user: FirebaseUser | null) => {
        console.log("Setting user: ");
        console.log(user);
        setUser(userFromFirebaseUser(user));
      }
    );

    return () => {
      unsubscribe();
    };
  }, []);

  const login = async (email: string, password: string) => {
    const auth = getAuth(app);
    const userCredentials = await signInWithEmailAndPassword(
      auth,
      email,
      password
    );
    const loggedUser: User = userFromFirebaseUser(userCredentials.user);
    setUser(loggedUser);
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
    firstName: string,
    lastName: string
  ): Promise<void> => {
    const auth = getAuth(app);
    const userSignUpResponse = await createUserWithEmailAndPassword(
      auth,
      email,
      password
    );

    await updateProfile(userSignUpResponse.user, {
      displayName: `${firstName} ${lastName}`,
    });

    setUser(userFromFirebaseUser(userSignUpResponse.user));
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isLoggedIn, signUp }}>
      {props.children}
    </AuthContext.Provider>
  );
};
