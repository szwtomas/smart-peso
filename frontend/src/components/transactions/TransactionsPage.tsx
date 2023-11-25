import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { TransactionContainer } from "./transactionTable/TransactionsContainer";

export function TransactionPage() {
  const { user } = useContext(AuthContext);
  console.log(user);

  return (
    <div>
      <TransactionContainer />
    </div>
  );
}
