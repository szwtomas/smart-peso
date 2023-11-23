import { Button, Card, CardBody, CardHeader, Input } from "@nextui-org/react";
import { useState } from "react";

async function handleSignUp(
  firstName: string,
  lastName: string,
  email: string,
  password: string
) {
  console.log(
    `Signing up ${firstName} ${lastName} with email ${email} and password ${password}`
  );
}

export function SignUp() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");

  return (
    <div className="w-full flex justify-center">
      <Card className="w-[600px] m-10 p-5">
        <CardHeader title="Login">
          <h2 className="mx-2 text-3xl">
            <b>Create an user to use Smart Peso</b>
          </h2>
        </CardHeader>
        <CardBody>
          <div className="flex w-full flex-wrap md:flex-nowrap gap-4 mb-3">
            <Input
              type="email"
              label="First Name"
              placeholder="Enter your first name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
            <Input
              type="email"
              label="Last Name"
              placeholder="Enter your last name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </div>
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
          <Button
            color="primary"
            radius="sm"
            className="mt-2"
            onClick={() => handleSignUp(firstName, lastName, email, password)}
          >
            <b>Log In</b>
          </Button>
        </CardBody>
      </Card>
    </div>
  );
}
