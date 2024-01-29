import { useCallback, useEffect } from 'react';
import { ComponentPropsWithoutRef } from 'react';
import { createPortal } from 'react-dom';
import { styled } from 'styled-components';
import { IoMdClose } from 'react-icons/io';

type Props = {
  isOpen: boolean;
  canBackdropClose?: boolean;
  canEscKeyClose?: boolean;
  hasCloseButton?: boolean;
  closeModal: () => void;
} & ComponentPropsWithoutRef<'dialog'>;

const Modal = ({
  isOpen = true,
  canBackdropClose = true,
  canEscKeyClose = true,
  hasCloseButton = true,
  closeModal,
  children,
  ...rest
}: Props) => {
  const onKeyDownEscape = useCallback(
    (event: KeyboardEvent) => {
      if (event.key !== 'Escape') return;
      closeModal();
    },
    [closeModal],
  );

  useEffect(() => {
    if (isOpen && canEscKeyClose) {
      window.addEventListener('keydown', onKeyDownEscape);
    }

    return () => {
      window.removeEventListener('keydown', onKeyDownEscape);
    };
  }, [isOpen, canEscKeyClose, onKeyDownEscape]);

  return createPortal(
    <>
      {isOpen && (
        <>
          <Backdrop onClick={canBackdropClose ? closeModal : undefined} />
          <Content {...rest}>
            {hasCloseButton && (
              <CloseButton
                type="button"
                onClick={closeModal}
                aria-label="모달 닫기"
              >
                <IoMdClose width={24} height={24} />
              </CloseButton>
            )}
            {children}
          </Content>
        </>
      )}
    </>,
    document.body,
  );
};

export default Modal;

const Backdrop = styled.div`
  position: fixed;
  inset: 0;
  background: var(--gray-shadow);
`;

const Content = styled.dialog`
  position: fixed;
  inset: 50% auto auto 50%;
  display: flex;
  justify-content: center;
  min-width: 20vw;
  max-height: 90vh;
  overflow: auto;
  padding: 2.5rem;
  border: none;
  border-radius: 8px;
  background-color: var(--white);
  transform: translate(-50%, -50%);
`;

const CloseButton = styled.button`
  position: absolute;
  inset: 2.5rem 2.5rem auto auto;
  border: none;
  background: none;

  &:hover {
    background-color: var(--gray-1);
  }
`;
