export type TaskDeskPropsType = {
  data: DataType[]
  columns: DataType[]
  tableScroll?: { x: number; y: number }
  visible?: boolean
  onClick?: () => void
  title?: string
}

export type DataType = { [key: string]: any }
