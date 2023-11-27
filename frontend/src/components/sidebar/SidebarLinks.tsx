import {
  Cog8ToothIcon,
  HomeIcon,
  BanknotesIcon,
  ArrowLeftOnRectangleIcon,
} from "@heroicons/react/24/outline";
import { useLocation, useNavigate } from "react-router-dom";
import { clsx } from "clsx";
import { Link } from "react-router-dom";

export interface SidebarLinksProps {
  logout: () => void;
}

const links = [
  { name: "Panel Principal", href: "/dashboard", icon: HomeIcon },
  {
    name: "Transacciones",
    href: "/transactions",
    icon: BanknotesIcon,
  },
  {
    name: "Configuración",
    href: "/configuration",
    icon: Cog8ToothIcon,
  },
  {
    name: "Cerrar Sesión",
    href: "/logout",
    icon: ArrowLeftOnRectangleIcon,
  },
];

export function SidebarLinks(props: SidebarLinksProps) {
  const { pathname } = useLocation();
  const navigate = useNavigate();
  return (
    <>
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <Link
            key={link.name}
            to={link.href !== "/logout" ? link.href : "#"}
            onClick={async () => {
              if (link.href === "/logout") {
                await props.logout();
                navigate("/login");
              }
            }}
            className={clsx(
              `flex flex-row h-[48px] items-center justify-around gap-2 rounded-md p-1 text-xl font-${
                pathname === link.href ? "medium" : "italic"
              } hover:bg-sky-100 hover:text-primary md:flex-none md:justify-start md:p-2 md:px-3`,
              {
                "bg-sky-100 text-blue-primary": pathname === link.href,
              }
            )}
          >
            <LinkIcon
              className={
                "w-6 " + (pathname === link.href ? "text-primary" : "")
              }
            />
            <span
              className={
                "hidden md:block " +
                (pathname === link.href ? "text-primary" : "")
              }
            >
              {link.name}
            </span>
          </Link>
        );
      })}
    </>
  );
}
