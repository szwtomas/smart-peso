import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
} from "@nextui-org/react";
import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";

export function Navbar() {
  const { user } = useContext(AuthContext);

  return (
    <NextUINavbar
      isBordered
      maxWidth="full"
      position="sticky"
      isBlurred={false}
      className="bg-primary shadow-xl p-1 lg:p-2 min-w-full"
    >
      <NavbarContent className="basis-full">
        <NavbarBrand className="gap-3 max-w-fit">
          <a className="flex justify-start items-center gap-1" href={"/"}>
            <p className="text-white left-0 top-0 flex pl-5 text-3xl static font-inter font-black">
              SMART PESO
            </p>
          </a>
        </NavbarBrand>
      </NavbarContent>
      <NavbarContent className="basis-full" justify="end">
        <p className="text-white text-lg">
          {user ? `${user.firstName} ${user.lastName}` : ""}
        </p>
      </NavbarContent>
    </NextUINavbar>
  );
}
