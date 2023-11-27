import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Radio,
  RadioGroup,
  Textarea,
} from "@nextui-org/react";
import { useState } from "react";

export interface NewTransactionModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
}

export function NewTransactionModal(props: NewTransactionModalProps) {
  const [transactionName, setTransactionName] = useState<string>("");
  const [selected, setSelected] = useState<string>("income");
  const [description, setDescription] = useState<string>("");
  return (
    <Modal
      closeButton
      aria-aria-labelledby="create-transaction"
      isOpen={props.isOpen}
      onOpenChange={props.onOpenChange}
      className="p-4"
    >
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              <h2 className="text-2xl text-primary">
                Agregar nueva transacción
              </h2>
            </ModalHeader>
            <ModalBody>
              <div>
                <Input
                  type="text"
                  variant="faded"
                  size="lg"
                  label={
                    <span className="text-md text-default-500">
                      Nombre de la transacción
                    </span>
                  }
                  labelPlacement="outside"
                  placeholder="Ej: Compras en el supermercado"
                  className="my-4"
                  radius="sm"
                  value={transactionName}
                  onChange={(e) => setTransactionName(e.target.value)}
                />
                <RadioGroup
                  label="Seleccioná el tipo de transacción"
                  orientation="horizontal"
                  value={selected}
                  onValueChange={setSelected}
                >
                  <Radio value="income">Ingreso</Radio>
                  <Radio value="expense">Gasto</Radio>
                </RadioGroup>
                <Textarea
                  type="text"
                  variant="faded"
                  size="lg"
                  label={
                    <span className="text-md text-default-500">
                      Descripción de la transacción
                    </span>
                  }
                  labelPlacement="outside"
                  placeholder="Ej: Compras que hice en el supermercado de la esquina cuando volví del trabajo"
                  className="my-4"
                  radius="sm"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
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
              <Button color="primary" onPress={onClose} radius="sm">
                Crear
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
