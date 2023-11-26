import { useContext } from "react";
import { SidebarLinks } from "./SidebarLinks";
import { AuthContext } from "../../context/AuthContext";

export default function Sidebar() {
  const { logout } = useContext(AuthContext);
  return (
    <aside className="h-screen border-grey-500 border-r-2">
      <div className="h-full flex flex-col bg-white shadow-sm">
        <div className="p-5 pb-2 flex flex-col justify-start gap-2">
          <SidebarLinks logout={logout} />
        </div>
      </div>
    </aside>
  );
}
