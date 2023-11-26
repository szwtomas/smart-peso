import { useContext } from "react";
import { AuthContext } from "./context/AuthContext";
import { Navigate, Route, Routes } from "react-router-dom";
import { DashboardPage } from "./components/Dashboard/DashboardPage";
import { Login } from "./components/auth/Login";
import { SignUp } from "./components/auth/SignUp";
import { ForgotPassword } from "./components/auth/ForgotPassword";
import { TransactionPage } from "./components/transactions/TransactionsPage";
import { Layout } from "./components/Layout";

export function AppRoutes() {
  const { user } = useContext(AuthContext);
  const isAuthenticated = user !== null && user.email !== undefined;

  return (
    <Routes>
      <Route
        path="/dashboard"
        element={isAuthenticated ? <DashboardPage /> : <Navigate to="/login" />}
      />
      <Route
        path="/transactions"
        element={isAuthenticated ? <TransactionPage /> : <Login />}
      />
      <Route
        path="/configuration"
        element={
          isAuthenticated ? (
            <Layout>
              <div>Configuration</div>
            </Layout>
          ) : (
            <Login />
          )
        }
      />
      <Route
        path="/login"
        element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />}
      />
      <Route
        path="/signup"
        element={isAuthenticated ? <Navigate to="/dashboard" /> : <SignUp />}
      />
      <Route
        path="/forgot"
        element={
          isAuthenticated ? <Navigate to="/dashboard" /> : <ForgotPassword />
        }
      />
    </Routes>
  );
}
