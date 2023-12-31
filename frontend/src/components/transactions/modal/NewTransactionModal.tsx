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
import { useState } from "react";
import { CreateTransactionFormData } from "../../../services/TransactionService";

export interface NewTransactionModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  onCreateTransaction: (transaction: CreateTransactionFormData) => void;
}

function dateToString(date: Date): string {
  return date.toISOString().split("T")[0];
}

export function NewTransactionModal(props: NewTransactionModalProps) {
  const [transactionName, setTransactionName] = useState<string>("");
  const [transactionType, setTransactionType] = useState<string>("income");
  const [category, setCategory] = useState<string>("");
  const [currency, setCurrency] = useState<string>("ARS");
  const [description, setDescription] = useState<string>("");
  const [transactionValue, setTransactionValue] = useState<number>(0);
  const [date, setDate] = useState<string>(dateToString(new Date()));

  function resetFields() {
    setTransactionName("");
    setTransactionType("income");
    setCategory("");
    setCurrency("ARS");
    setDescription("");
    setTransactionValue(0);
    setDate(dateToString(new Date()));
  }

  return (
    <Modal
      closeButton
      isOpen={props.isOpen}
      onOpenChange={props.onOpenChange}
      className="p-4"
      size="4xl"
    >
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              <h2 className="text-2xl text-primary">CREAR NUEVA TRANSACCIÓN</h2>
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
                    value={transactionName}
                    onChange={(e) => setTransactionName(e.target.value)}
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
                    value={transactionType}
                    selectionMode="single"
                    onChange={(e) => {
                      setTransactionType(e.target.value);
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
                    value={
                      transactionValue !== 0 ? transactionValue.toString() : ""
                    }
                    onChange={(e) =>
                      setTransactionValue(e.target.valueAsNumber)
                    }
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
                color="danger"
                variant="light"
                onPress={onClose}
                radius="sm"
              >
                Cerrar
              </Button>
              <Button
                variant="shadow"
                color="primary"
                onClick={() => {
                  props.onCreateTransaction({
                    transactionName,
                    transactionType,
                    currency,
                    description,
                    transactionValue,
                    category,
                    date,
                  });
                  onClose();
                  resetFields();
                }}
                radius="sm"
              >
                Crear Transacción
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
