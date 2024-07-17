import { FormDataType } from '../multipleForm/type'

export type BasicObj = { [key: string]: any }

export interface FormPopupPropsType {
  data: FormDataType[]
  title?: () => JSX.Element | string
  visible?: boolean
  customFooter?: (valueObj?: BasicObj) => JSX.Element | string
  onChange: (valueObj: BasicObj) => void
  onOk: (valueObj: BasicObj) => void
  onCancel?: () => void
  formValue: BasicObj
  loading?: boolean
}
