export interface DeleteModalProps {
  visible: boolean
  title: string
  content: string
  handleOk: () => void
  handleCancel: () => void
}
