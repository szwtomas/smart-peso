import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Input,
  Link,
} from "@nextui-org/react";
import { useContext, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

export function SignUp() {
  const { signUp } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");

  async function handleSignUp(
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ) {
    try {
      await signUp(email, password, firstName, lastName);
      navigate("/dashboard");
    } catch (err) {
      console.log(err);
      toast.error("Error creating user");
    }
  }

  return (
    <div className="w-full flex justify-center">
      <Card className="w-[600px] m-10 p-5">
        <CardHeader title="Login">
          <h2 className="mx-2 text-3xl">
            <b>Creá un usuario de Smart Peso</b>
          </h2>
        </CardHeader>
        <CardBody>
          <div className="flex w-full flex-wrap md:flex-nowrap gap-4 mb-3">
            <Input
              type="name"
              label="Nombre"
              placeholder="Ingresá tu nombre"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
            <Input
              type="apellido"
              label="Apellido"
              placeholder="Ingresá tu apellido"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </div>
          <Input
            type="email"
            label="Email"
            placeholder="Ingresá tu dirección de email"
            className="mt-1 mb-3"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Input
            type="password"
            label="Contraseña"
            placeholder="Elegí una contraseña"
            className="my-2"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            color="primary"
            radius="sm"
            className="mt-2"
            onClick={() => handleSignUp(firstName, lastName, email, password)}
          >
            <b>Crear Cuenta</b>
          </Button>
          <div>
            <span className="text-center">Ya tenés una cuenta?</span>
            <Link
              underline="none"
              className="mx-2 mt-3"
              color="primary"
              onClick={() => {
                navigate("/login");
              }}
            >
              <p>Iniciá Sesión acá!</p>
            </Link>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
