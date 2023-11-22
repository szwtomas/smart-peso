import { SidebarLinks } from "./SidebarLinks";

export default function Sidebar() {
  return (
    <aside className="h-screen border-grey-500 border-r-2">
      <div className="h-full flex flex-col bg-white shadow-sm">
        <div className="p-4 pb-2 flex flex-col justify-start gap-2">
          <SidebarLinks />
        </div>
      </div>
    </aside>
  );
}
