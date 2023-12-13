import { useContext, useEffect, useState } from "react";
import { Navbar } from "./navbar/Navbar";
import Sidebar from "./sidebar/Sidebar";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { MobileNavbar } from "./navbar/MobileNavbar";

export interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const navigate = useNavigate();
  const { user, logout } = useContext(AuthContext);
  const [width, setWidth] = useState(window.innerWidth);
  useEffect(() => {
    if (!user) {
      navigate("/login");
    }
  }, [user, navigate]);

  useEffect(() => {
    const handleResizeWindow = () => setWidth(window.innerWidth);
    window.addEventListener("resize", handleResizeWindow);
    return () => window.removeEventListener("resize", handleResizeWindow);
  }, []);

  const isLittle: boolean = width <= 900;

  if (isLittle) {
    return (
      <div>
        <div className="w-full">
          <MobileNavbar logout={logout} />
        </div>
        <div className="flex-grow p-6 md:overflow-y-auto md:p-12 bg-[#f9faff]">
          {children}
        </div>
      </div>
    );
  } else {
    return (
      <div>
        <div className="w-full">
          <Navbar />
        </div>
        <div className="flex flex-row">
          <Sidebar logout={logout} />
          <div className="flex-grow p-6 md:overflow-y-auto md:p-12 bg-[#f9faff]">
            {children}
          </div>
        </div>
      </div>
    );
  }
}
