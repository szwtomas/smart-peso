import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Input,
  Link as NextUILink,
} from "@nextui-org/react";
import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import { toast } from "react-toastify";

export function Login() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  async function handleLogIn(email: string, password: string) {
    try {
      await login(email, password);
      navigate("/dashboard");
    } catch (error) {
      toast.error("Wrong email or password!");
      console.log(error);
    }
  }

  return (
    <div className="w-full flex justify-center">
      <Card className="w-[600px] m-10 p-5">
        <CardHeader title="Login">
          <h2 className="mx-2 text-3xl">
            <b>Iniciá sesión en Smart Peso</b>
          </h2>
        </CardHeader>
        <CardBody>
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
            label="Password"
            placeholder="Ingresá tu contraseña"
            className="my-2"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <NextUILink
            underline="always"
            className="text-primary mx-2 my-1"
            onClick={() => {
              navigate("/forgot-password");
            }}
          >
            <p>Olvidaste la contraseña?</p>
          </NextUILink>
          <Button
            color="primary"
            radius="sm"
            className="mt-2"
            onClick={() => handleLogIn(email, password)}
          >
            <b>Iniciar Sesión</b>
          </Button>
          <div>
            <span className="text-center">No tenés una cuenta?</span>
            <NextUILink
              underline="none"
              className="mx-2 mt-3"
              color="primary"
              onClick={() => {
                navigate("/signup");
              }}
            >
              <p>Creá una acá! </p>
            </NextUILink>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
