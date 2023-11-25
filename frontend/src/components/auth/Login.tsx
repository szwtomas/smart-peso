import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Checkbox,
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
  const [rememberMe, setRememberMe] = useState<boolean>(false);
  const navigate = useNavigate();
  const { login, user } = useContext(AuthContext);
  if (user) {
    navigate("/dashboard");
  }

  async function handleLogIn(
    email: string,
    password: string,
    rememberMe: boolean
  ) {
    try {
      await login(email, password, rememberMe);
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
          <Checkbox
            isSelected={rememberMe}
            className="my-2 mx-1 p-0"
            onValueChange={() => {
              setRememberMe(!rememberMe);
            }}
          >
            Recordar Inicio de Sesión
          </Checkbox>
          <Button
            color="primary"
            radius="sm"
            className="mt-2"
            onClick={() => handleLogIn(email, password, rememberMe)}
          >
            <b>Iniciar Sesión</b>
          </Button>
          <div>
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
              <span>O quizás</span>
              <NextUILink
                underline="none"
                className="text-primary mx-2 my-1"
                onClick={() => {
                  navigate("/forgot-password");
                }}
              >
                <p>olvidaste la contraseña?</p>
              </NextUILink>
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
