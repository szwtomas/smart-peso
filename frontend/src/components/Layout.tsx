import { Navbar } from "./navbar/Navbar";
import Sidebar from "./sidebar/Sidebar";

export interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
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
