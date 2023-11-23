import { useContext, useEffect } from "react";
import { Navbar } from "./navbar/Navbar";
import Sidebar from "./sidebar/Sidebar";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  useEffect(() => {
    if (!user) {
      navigate("/login");
    }
  }, [user, navigate]);

  return (
    <div>
      <div className="w-full">
        <Navbar />
      </div>
      <div className="flex flex-row">
        <Sidebar />
        <div className="flex-grow p-6 md:overflow-y-auto md:p-12 bg-[#f9faff]">
          {children}
        </div>
      </div>
    </div>
  );
}
