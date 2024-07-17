export type BoxItemType = {
  src?: string
  value: string
  label: string
  describe?: string
}

export type DirectionType = 'column' | 'row'

export type CheckBoxButtonWrapType = {
  width?: number
  height?: number
  space?: number
  isChecked?: boolean
  direction?: DirectionType
}

export interface CommonType {
  onChange?: (key: string, item: BoxItemType) => void
  width?: number
  height?: number
  space?: number
  direction?: DirectionType
}

export interface SelectBoxGroupPropsType extends CommonType {
  data: BoxItemType[]
  value: string
}

export interface SelectBoxPropsType extends CommonType {
  data: BoxItemType
  isChecked?: boolean
}
