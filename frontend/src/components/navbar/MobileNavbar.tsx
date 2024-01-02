import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
} from "@nextui-org/react";
import {
  Cog8ToothIcon,
  HomeIcon,
  BanknotesIcon,
  ArrowLeftOnRectangleIcon,
  CurrencyDollarIcon,
} from "@heroicons/react/24/outline";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { clsx } from "clsx";

export interface MobileNavbarProps {
  logout: () => void;
}

const links = [
  { name: "Mis Finanzas", href: "/dashboard", icon: HomeIcon },
  { name: "Cotizaciones Dólar", href: "/prices", icon: CurrencyDollarIcon },
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

export function MobileNavbar(props: MobileNavbarProps) {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  return (
    <Navbar isBordered className="bg-primary">
      <NavbarContent className="sm:hidden" justify="start">
        <NavbarMenuToggle className="text-white text-3xl" />
      </NavbarContent>

      <NavbarContent className="sm:hidden p-3" justify="start">
        <NavbarBrand>
          <p className="font-bold text-white text-2xl mr-[100px]">SMART PESO</p>
        </NavbarBrand>
      </NavbarContent>

      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarBrand>
          <p className="font-bold text-white text-xl">SMART PESO</p>
        </NavbarBrand>
        <div className="flex flex-row items-center justify-between gap-6 ml-10">
          <NavbarItem>
            <Link className="text-white" to="#">
              Panel Principal
            </Link>
          </NavbarItem>
          <NavbarItem isActive>
            <Link to="#" aria-current="page" className="text-white">
              Transacciones
            </Link>
          </NavbarItem>
          <NavbarItem>
            <Link className="text-white" to="#">
              Configuración
            </Link>
          </NavbarItem>
        </div>

        <NavbarItem className="items-center">
          <Link className="flex text-white ml-[60%] items-center" to="/logout">
            <p className="text-semibold">Cerrar Sesión</p>
          </Link>
        </NavbarItem>
      </NavbarContent>
      <NavbarMenu className="flex flex-col gap-6  items-center mt-3">
        {links.map((link) => {
          const LinkIcon = link.icon;
          return (
            <NavbarMenuItem key={link.name}>
              <Link
                key={link.name}
                style={{
                  transition: "background-color 0.35s ease",
                }}
                to={link.href !== "/logout" ? link.href : "#"}
                onClick={async () => {
                  if (link.href === "/logout") {
                    await props.logout();
                    navigate("/login");
                  }
                }}
                className={clsx(
                  `flex flex-row h-[48px] items-center justify-around gap-2 rounded-md p-1 text-xl font-${
                    pathname === link.href ? "semibold" : "normal"
                  } hover:bg-sky-100 hover:text-primary md:flex-none md:justify-start md:p-2 md:px-3`,
                  {
                    "text-blue-primary": pathname === link.href,
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
                    "block " + (pathname === link.href ? "text-primary" : "")
                  }
                >
                  {link.name}
                </span>
              </Link>
            </NavbarMenuItem>
          );
        })}
      </NavbarMenu>
    </Navbar>
  );
}
