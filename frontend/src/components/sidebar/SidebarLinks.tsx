import {
  Cog8ToothIcon,
  HomeIcon,
  BanknotesIcon,
  ArrowLeftOnRectangleIcon,
} from "@heroicons/react/24/outline";
import { useLocation } from "react-router-dom";
import { clsx } from "clsx";
import { Link } from "react-router-dom";

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

export function SidebarLinks() {
  const { pathname } = useLocation();
  return (
    <>
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <Link
            key={link.name}
            to={link.href}
            className={clsx(
              "flex flex-row h-[48px] items-center justify-around gap-2 rounded-md p-1 text-xl font-medium hover:bg-sky-100 hover:text-primary md:flex-none md:justify-start md:p-2 md:px-3",
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
