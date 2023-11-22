import {
  Navbar as NextUINavbar,
  NavbarContent,
  NavbarBrand,
} from "@nextui-org/react";
import { LoginNavbarButton } from "./LoginNavbarButton";

export function Navbar() {
  return (
    <NextUINavbar
      maxWidth="full"
      position="sticky"
      isBlurred={false}
      isBordered
      className="bg-primary shadow-xl"
    >
      <NavbarContent className="basis-1/5 sm:basis-full justify-start">
        <NavbarBrand className="gap-3 max-w-fit">
          <a className="flex justify-start items-center gap-1" href={"/"}>
            <p className="text-white fixed left-0 top-0 flex pl-5 text-3xl lg:static font-inter font-black">
              SMART PESO
            </p>
          </a>
        </NavbarBrand>
      </NavbarContent>
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <LoginNavbarButton />
      </NavbarContent>
    </NextUINavbar>
  );
}
