import { Tooltip, useDisclosure } from "@nextui-org/react";
import { Transaction } from "../../../context/TransactionContext";
import { yyyyMMddToddMMyyyy } from "../../../utils/utils";
import { DeleteIcon, EditIcon, EyeIcon } from "../../Icons";
import { EditTransactionModal } from "../modal/EditTransactionModal";

export interface TransactionTableCellProps {
  transaction: Transaction;
  columnKey: React.Key;
  onSaveEditTransaction: (transaction: Transaction) => void;
}

export function TransactionTableCell(props: TransactionTableCellProps) {
  const { transaction, columnKey } = props;
  const editTrasactionDisclosure = useDisclosure();
  const isOpenEdit = editTrasactionDisclosure.isOpen;
  const onOpenEdit = editTrasactionDisclosure.onOpen;
  const onOpenChangeEdit = editTrasactionDisclosure.onOpenChange;
  const cellValue = transaction[columnKey as keyof Transaction];
  switch (columnKey) {
    case "id":
      return <p>{cellValue as string}</p>;
    case "name":
      return <p>{cellValue as string}</p>;
    case "type":
      return (
        <p
          className={
            transaction.type === "income" ? "text-green-600" : "text-red-600"
          }
        >
          {cellValue === "income" ? "Ingreso" : "Gasto"}
        </p>
      );
    case "date":
      return <p>{yyyyMMddToddMMyyyy(cellValue as string)}</p>;
    case "currency":
      return <p>{cellValue as string}</p>;
    case "value":
      return (
        <p
          className={
            transaction.type === "income" ? "text-green-600" : "text-red-600"
          }
        >
          ${cellValue.toString()}
        </p>
      );
    case "category":
      return <p>{cellValue as string}</p>;
    case "actions":
      return (
        <div className="relative flex items-center gap-7 justify-end">
          <Tooltip content="Ver más">
            <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
              <EyeIcon />
            </span>
          </Tooltip>
          <Tooltip content="Editar" onClick={onOpenEdit}>
            <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
              <EditIcon onClick={onOpenEdit} />
            </span>
          </Tooltip>
          <EditTransactionModal
            isOpen={isOpenEdit}
            onOpenChange={onOpenChangeEdit}
            transaction={transaction}
            onSaveEdit={props.onSaveEditTransaction}
          />
          <Tooltip color="danger" content="Eliminar">
            <span className="text-lg text-danger cursor-pointer active:opacity-50">
              <DeleteIcon />
            </span>
          </Tooltip>
        </div>
      );
    default:
      return <p className="text-red-600">Error</p>;
  }
}
