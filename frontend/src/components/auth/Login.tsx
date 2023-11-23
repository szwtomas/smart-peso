import { Button, Card, CardBody, CardHeader, Input } from "@nextui-org/react";

export function Login() {
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
          />
          <Input
            type="password"
            label="Password"
            placeholder="Enter your password"
            className="my-2"
          />
          <Button color="primary" radius="sm" className="mt-2">
            <b>Log In</b>
          </Button>
        </CardBody>
      </Card>
    </div>
  );
}
