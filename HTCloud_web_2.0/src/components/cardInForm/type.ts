import { TablePaginationConfig } from 'antd/lib/table/interface'
import { BasicObj } from '../multipleForm/type'

export type buttonType = { text?: string; key: string; icon: JSX.Element }
export type ButtonListType = buttonType[]
export type itemType = {
  storagePoolName: string
  usedSpace: string
  description: string
  cardButtonList: ButtonListType
  title: string
  key: string
}
export type dataType = itemType[]
export type cardFormType = {
  data: dataType
  storageData: BasicObj[]
  onSelect: (id: string) => void
  onRow: (record: BasicObj) => { [key: string]: () => void }
  onCardButtonClick: (key: string, id: string) => void
  onTableChange: (params: TablePaginationConfig) => void
  tableProps: {
    pagination: {
      current: number
      pageSize: number
      total: number
    }
  }
}
