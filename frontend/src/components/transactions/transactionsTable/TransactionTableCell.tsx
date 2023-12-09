import {
  Button,
  Popover,
  PopoverContent,
  PopoverTrigger,
  Tooltip,
  useDisclosure,
} from "@nextui-org/react";
import { Transaction } from "../../../context/TransactionContext";
import { yyyyMMddToddMMyyyy } from "../../../utils/utils";
import { DeleteIcon, EditIcon, EyeIcon } from "../../Icons";
import { EditTransactionModal } from "../modal/EditTransactionModal";

export interface TransactionTableCellProps {
  transaction: Transaction;
  columnKey: React.Key;
  onSaveEditTransaction: (transaction: Transaction) => void;
  onDeleteTransaction: (transaction: Transaction) => void;
}

export function TransactionTableCell(props: TransactionTableCellProps) {
  const { transaction, columnKey } = props;
  const editTrasactionDisclosure = useDisclosure();
  const isOpenEdit = editTrasactionDisclosure.isOpen;
  const onOpenEdit = editTrasactionDisclosure.onOpen;
  const onOpenChangeEdit = editTrasactionDisclosure.onOpenChange;
  const cellValue = transaction[columnKey as keyof Transaction];

  const content = (
    <PopoverContent>
      <div className="px-1 py-2">
        <div className="text-small font-bold">Eliminar transacción</div>
        <div className="text-tiny">¿Estás seguro/a?</div>
        <Button
          color="danger"
          variant="solid"
          size="sm"
          className="mt-2 flex items-center justify-center w-full font-bold"
          startContent={<DeleteIcon />}
          onClick={() => {
            props.onDeleteTransaction(transaction);
          }}
        >
          Eliminar
        </Button>
      </div>
    </PopoverContent>
  );

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
          <Tooltip
            content={
              transaction.description === ""
                ? "No hay descripción"
                : transaction.description
            }
          >
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
          <Popover placement="bottom" color="default">
            <PopoverTrigger>
              <span className="text-lg text-danger cursor-pointer active:opacity-50">
                <DeleteIcon />
              </span>
            </PopoverTrigger>

            {content}
          </Popover>
        </div>
      );
    default:
      return <p className="text-red-600">Error</p>;
  }
}
