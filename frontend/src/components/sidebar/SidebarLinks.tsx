import {
  Cog8ToothIcon,
  HomeIcon,
  BanknotesIcon,
} from "@heroicons/react/24/outline";

const links = [
  { name: "Dashboard", href: "/dashboard", icon: HomeIcon },
  {
    name: "Transactions",
    href: "/dashboard/transactions",
    icon: BanknotesIcon,
  },
  {
    name: "Configuration",
    href: "/dashboard/configuration",
    icon: Cog8ToothIcon,
  },
];

export function SidebarLinks() {
  return (
    <>
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <a
            key={link.name}
            href={link.href}
            className={
              "flex flex-row h-[48px] items-center justify-around gap-2 rounded-md p-1 text-xl font-medium hover:bg-sky-100 hover:text-primary md:flex-none md:justify-start md:p-2 md:px-3"
            }
          >
            <LinkIcon className="w-6" />
            <span className="hidden md:block">{link.name}</span>
          </a>
        );
      })}
    </>
  );
}
