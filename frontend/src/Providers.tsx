import { NextUIProvider } from "@nextui-org/react";
import { AuthProvider } from "./context/AuthContext";

interface ProvidersProps {
  children: React.ReactNode;
}

export const Providers = ({ children }: ProvidersProps) => {
  return (
    <NextUIProvider>
      <AuthProvider>{children}</AuthProvider>
    </NextUIProvider>
  );
};
