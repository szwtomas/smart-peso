import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Input,
  Link as NextUILink,
} from "@nextui-org/react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

async function handleLogIn(email: string, password: string) {
  console.log(`Logging in email ${email} with password ${password}`);
}

export function Login() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const navigate = useNavigate();

  return (
    <div className="w-full flex justify-center">
      <Card className="w-[600px] m-10 p-5">
        <CardHeader title="Login">
          <h2 className="mx-2 text-3xl">
            <b>Log In to Smart Peso</b>
          </h2>
        </CardHeader>
        <CardBody>
          <Input
            type="email"
            label="Email"
            placeholder="Enter your email"
            className="mt-1 mb-3"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Input
            type="password"
            label="Password"
            placeholder="Enter your password"
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
            Forgot Password?
          </NextUILink>
          <Button
            color="primary"
            radius="sm"
            className="mt-2"
            onClick={() => handleLogIn(email, password)}
          >
            <b>Log In</b>
          </Button>
        </CardBody>
      </Card>
    </div>
  );
}
