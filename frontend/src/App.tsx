import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { Providers } from "./Providers";
import { Layout } from "./components/Layout";
import { Login } from "./components/auth/Login";
import { SignUp } from "./components/auth/SignUp";
import { Logout } from "./components/auth/LogOut";

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <Layout>
        <div>Hello world!</div>
      </Layout>
    ),
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/signup",
    element: <SignUp />,
  },
  {
    path: "/logout",
    element: <Logout />,
  },
  {
    path: "/dashboard",
    element: (
      <Layout>
        <div>Dashboard</div>
      </Layout>
    ),
  },
  {
    path: "/transactions",
    element: (
      <Layout>
        <div>Transactions</div>
      </Layout>
    ),
  },
  {
    path: "/configuration",
    element: (
      <Layout>
        <div>Configuration</div>
      </Layout>
    ),
  },
]);

const App = () => {
  return (
    <>
      <Providers>
        <RouterProvider router={router} />
      </Providers>
    </>
  );
};

export default App;
