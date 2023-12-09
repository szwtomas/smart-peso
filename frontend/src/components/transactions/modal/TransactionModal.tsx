import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
} from "@nextui-org/react";
import { Transaction } from "../../../context/TransactionContext";

export interface TransactionModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  transaction: Transaction;
}

export function TransactionModal(props: TransactionModalProps) {
  const { transaction } = props;

  return (
    <Modal closeButton isOpen={props.isOpen} onOpenChange={props.onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1">
              <h2 className="text-2xl text-primary">TRANSACCIÓN</h2>
            </ModalHeader>
            <ModalBody>
              <div>
                <Input
                  isReadOnly
                  type="text"
                  label="Name"
                  variant="bordered"
                  defaultValue={transaction.name}
                  className="max-w-xs"
                />
              </div>
              <div>
                <Input type="date" />
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
                isDisabled={true}
                variant="shadow"
                color="primary"
                onClick={() => {
                  console.log("Clicked primary");
                }}
                radius="sm"
              >
                Guardar
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
