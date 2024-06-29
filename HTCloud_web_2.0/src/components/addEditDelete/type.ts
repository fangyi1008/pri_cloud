import { ItemType } from 'antd/es/menu/hooks/useItems'

export type AddEditDeletePropsType = {
  data: DataType[]
  onClick?: (type: string, key: string) => void
  onMenuClick?: (key: string) => void
  buttonType?: 'primary' | 'default'
}
export type DataType = {
  key: string
  type?: string
  text: string
  isSelect?: boolean
  menuData?: ItemType[]
  disabled?: boolean
  alias?: string
}
