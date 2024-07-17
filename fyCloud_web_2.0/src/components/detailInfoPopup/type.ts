import { ModalProps } from 'antd/lib/modal'

type BaseObjectType = { [key: string]: string }
export interface DetailInfoPopupPropsType extends ModalProps {
  data: BaseObjectType
  fieldNames: BaseObjectType
  backgroundColor?: string[]
  fontColor?: string[]
}

export type DetailInfoPropsType = {
  data?: BaseObjectType
  fieldNames?: BaseObjectType
  backgroundColor?: string[]
  fontColor?: string[]
}
