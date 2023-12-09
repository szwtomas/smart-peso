import {
  Avatar,
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Select,
  SelectItem,
  Textarea,
} from "@nextui-org/react";
import { Transaction } from "../../../context/TransactionContext";
import { useState } from "react";

export interface EditTransactionModalProps {
  isOpen: boolean;
  onOpenChange: () => void;
  transaction: Transaction;
  onSaveEdit: (transaction: Transaction) => void;
}

export function EditTransactionModal(props: EditTransactionModalProps) {
  const transaction = props.transaction;
  const [name, setName] = useState<string>(transaction.name);
  const [date, setDate] = useState<string>(transaction.date);
  const [type, setType] = useState<string>(transaction.type);
  const [currency, setCurrency] = useState<string>(transaction.currency);
  const [value, setValue] = useState<number>(transaction.value);
  const [category, setCategory] = useState<string>(transaction.category);
  const [description, setDescription] = useState<string>(
    transaction.description
  );

  function shouldDisableSaveButton(): boolean {
    return (
      name === transaction.name &&
      date === transaction.date &&
      type === transaction.type &&
      currency === transaction.currency &&
      value === transaction.value &&
      category === transaction.category &&
      description === transaction.description
    );
  }

  return (
    <Modal closeButton isOpen={props.isOpen} onOpenChange={props.onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              <p>Editar Transacción</p>
            </ModalHeader>
            <ModalBody>
              <div>
                <div className="mt-1 mb-3">
                  <p className="text-md text-default-500">
                    Nombre de la transacción
                  </p>
                  <Input
                    isRequired
                    type="text"
                    variant="flat"
                    size="md"
                    placeholder="Ej: Compras en el supermercado"
                    radius="sm"
                    className="my-1"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    label="Nombre"
                  />
                </div>
                <div className="my-2 mb-3">
                  <p className="text-md text-default-500">
                    Tipo de transacción
                  </p>
                  <Select
                    isRequired
                    disallowEmptySelection
                    variant="flat"
                    placeholder="Ingreso"
                    defaultSelectedKeys={["income"]}
                    size="md"
                    radius="sm"
                    className="my-1"
                    label="Tipo transacción"
                    value={type}
                    selectionMode="single"
                    onChange={(e) => {
                      setType(e.target.value);
                    }}
                  >
                    <SelectItem key="income" value="income">
                      Ingreso
                    </SelectItem>
                    <SelectItem key="expense" value="expense">
                      Gasto
                    </SelectItem>
                  </Select>
                </div>

                <div className="my-2 mb-3">
                  <p className="text-md text-default-500">
                    Moneda de la transacción
                  </p>
                  <Select
                    isRequired
                    disallowEmptySelection
                    variant="flat"
                    placeholder="Pesos"
                    size="md"
                    radius="sm"
                    className="my-1"
                    label="Moneda"
                    defaultSelectedKeys={["ARS"]}
                    onChange={(e) => {
                      setCurrency(e.target.value);
                    }}
                    startContent={
                      currency === "ARS" ? (
                        <Avatar
                          alt="Argentina"
                          className="w-4 h-4"
                          src="https://flagcdn.com/ar.svg"
                        />
                      ) : (
                        <Avatar
                          alt="USA"
                          className="w-4 h-4"
                          src="https://flagcdn.com/us.svg"
                        />
                      )
                    }
                  >
                    <SelectItem
                      key="ARS"
                      value="ARS"
                      startContent={
                        <Avatar
                          alt="Argentina"
                          className="w-6 h-6"
                          src="https://flagcdn.com/ar.svg"
                        />
                      }
                    >
                      Pesos
                    </SelectItem>
                    <SelectItem
                      key="USD"
                      value="USD"
                      startContent={
                        <Avatar
                          alt="USA"
                          className="w-6 h-6"
                          src="https://flagcdn.com/us.svg"
                        />
                      }
                    >
                      Dólares
                    </SelectItem>
                  </Select>
                </div>
                <div className="my-2 mb-3">
                  <p className="text-md text-default-500">
                    Valor de la transacción
                  </p>
                  <Input
                    type="number"
                    label="Valor"
                    size="md"
                    radius="sm"
                    placeholder="0"
                    startContent={
                      <div className="pointer-events-none flex items-center">
                        <span className="text-default-400 text-small">$</span>
                      </div>
                    }
                    value={value !== 0 ? value.toString() : ""}
                    onChange={(e) => setValue(e.target.valueAsNumber)}
                    className="my-1"
                  />
                </div>
                <div className="mt-2">
                  <p className="text-md text-default-500">Fecha</p>
                  <Input
                    type="date"
                    variant="flat"
                    size="md"
                    radius="sm"
                    className="my-1"
                    placeholder=""
                    label=""
                    value={date}
                    onValueChange={(newDate) => {
                      setDate(newDate);
                    }}
                  />
                </div>
                <div className="mt-2">
                  <p className="text-md text-default-500">Categoría</p>
                  <Input
                    size="md"
                    radius="sm"
                    value={category}
                    onChange={(e) => {
                      setCategory(e.target.value);
                    }}
                    className="my-1"
                    type="text"
                    placeholder="Ej: Servicios"
                    label="Categoría"
                  />
                </div>
                <div className="mt-2">
                  <p className="text-md text-default-500">
                    Descripción de la transacción
                  </p>
                  <Textarea
                    type="text"
                    variant="flat"
                    placeholder="Ej: Compras que hice en el supermercado de la esquina cuando volví del trabajo"
                    className="my-2"
                    radius="sm"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                  />
                </div>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button
                radius="sm"
                color="danger"
                variant="light"
                onClick={() => onClose()}
              >
                Cerrar
              </Button>
              <Button
                isDisabled={shouldDisableSaveButton()}
                variant="shadow"
                color="primary"
                onClick={() => {
                  onClose();
                  props.onSaveEdit({
                    transactionId: transaction.transactionId,
                    name,
                    date,
                    type: type as "income" | "expense",
                    currency: currency as "ARS" | "USD",
                    value,
                    category,
                    description,
                  });
                }}
                radius="sm"
              >
                Editar
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
