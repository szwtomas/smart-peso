import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { TransactionContainer } from "./transactionTable/TransactionsContainer";
import { Layout } from "../Layout";

export function TransactionPage() {
  const { user } = useContext(AuthContext);
  console.log(user);

  return (
    <Layout>
      <TransactionContainer />
    </Layout>
  );
}
