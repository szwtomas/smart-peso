import { RouterProvider, createBrowserRouter } from "react-router-dom";
import { Providers } from "./Providers";
import { Layout } from "./components/Layout";
import { Login } from "./components/auth/Login";

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
