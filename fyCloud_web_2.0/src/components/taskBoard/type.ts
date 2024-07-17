export type TaskDeskPropsType = {
  data: DataType[]
  columns: DataType[]
  visible?: boolean
  onClose?: () => void
}

export type DataType = { [key: string]: any }
