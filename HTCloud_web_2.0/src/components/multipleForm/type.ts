export type BasicObj = { [key: string]: any }

export type SelectDataType = {
  value: any
  label?: string
  [key: string]: any
}

export interface FormDataType extends BasicObj {
  key: string
  title: any
  type:
    | 'select'
    | 'input'
    | 'text'
    | 'radio'
    | 'checkbox'
    | 'textarea'
    | 'upload'
    | 'table'
    | 'custom'
    | 'selectBoxGroup'
  data?: SelectDataType[]
  value?: string | string[]
  placeholder?: string
  component?: JSX.Element
}
export type ChangeParamType = {
  key: string
  newValue: string | string[]
  newItem?: SelectDataType | SelectDataType[]
  newLabel?: string | string[]
}
export interface MultipleFormPropsType {
  data: FormDataType[]
  onChange: (valueObj: {
    newValue: string | string[]
    newItem?: SelectDataType | SelectDataType[]
    newLabel?: string | string[]
    key: string
  }) => void
  value?: BasicObj
}
