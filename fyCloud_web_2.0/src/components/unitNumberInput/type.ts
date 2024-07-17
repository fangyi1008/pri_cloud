import { SelectDataType } from '../multipleForm/type'

export interface UnitNumberValueType {
  inputValue?: number
  unitValue?: string
  UnitObject?: SelectDataType
}

export type UnitNumberInput = {
  inputValue?: number
  unitValue?: string
}

export interface UnitNumberInputPropsType {
  fieldNames?: any
  value?: UnitNumberInput
  unitOptions?: SelectDataType[]
  allowDelete?: boolean
  disabled?: boolean
  max?: number
  min?: number
  step?: number
  onDelete?: () => void
  onChange?: (value: UnitNumberValueType) => void
}
