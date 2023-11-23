import {
  Cog8ToothIcon,
  HomeIcon,
  BanknotesIcon,
} from "@heroicons/react/24/outline";
import { useLocation } from "react-router-dom";
import { clsx } from "clsx";

const links = [
  { name: "Dashboard", href: "/dashboard", icon: HomeIcon },
  {
    name: "Transactions",
    href: "/transactions",
    icon: BanknotesIcon,
  },
  {
    name: "Configuration",
    href: "/configuration",
    icon: Cog8ToothIcon,
  },
];

export function SidebarLinks() {
  const { pathname } = useLocation();
  return (
    <>
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <a
            key={link.name}
            href={link.href}
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
          </a>
        );
      })}
    </>
  );
}
