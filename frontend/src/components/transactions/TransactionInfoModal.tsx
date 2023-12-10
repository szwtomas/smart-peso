import { Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/react";
import { Transaction } from "../../services/TransactionService";

export interface TransactionInfoModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  transaction: Transaction;
}

export function TransactionInfoModal(props: TransactionInfoModalProps) {
  const { transaction, isOpen, onOpenChange } = props;
  return (
    <Modal
      closeButton
      isOpen={isOpen}
      onOpenChange={onOpenChange}
      className="p-4"
    >
      <ModalContent>
        {() => {
          return (
            <>
              <ModalHeader>
                <h2 className="text-2xl text-primary">TRANSACCIÓN</h2>
              </ModalHeader>
              <ModalBody>
                <div>
                  <div className="mt-1 mb-3">
                    <span className="text-md text-default-500">Nombre: </span>
                    <span className="text-md">{transaction.name}</span>
                  </div>
                </div>
              </ModalBody>
            </>
          );
        }}
      </ModalContent>
    </Modal>
  );
}
