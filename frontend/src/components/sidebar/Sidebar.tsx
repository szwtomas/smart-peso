import { SidebarLinks } from "./SidebarLinks";

export interface SidebarProps {
  logout: () => void;
}

export default function Sidebar(props: SidebarProps) {
  return (
    <aside className={"h-screen border-grey-500 border-r-2"}>
      <div className="h-full flex flex-col bg-white shadow-sm">
        <div className="p-5 pb-2 flex flex-col justify-start gap-2">
          <SidebarLinks logout={props.logout} />
        </div>
      </div>
    </aside>
  );
}
