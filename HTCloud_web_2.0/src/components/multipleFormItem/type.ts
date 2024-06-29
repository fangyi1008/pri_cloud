import { FormItemProps } from 'antd/es/form'
import React from 'react'

type ComponentType = React.FC<{
  formItemProps?: FormItemProps
  componentProps?: { [key: string]: any }
}>

export interface FormDataType {
  key?: string
  type: string
  title?: string
  component?: ComponentType
  formItemProps?: FormItemProps
  componentProps?: { [key: string]: any }
  groupItems?: FormDataType[]
  [key: string]: any
}

export interface FormGroupPropsType {
  formItems: FormDataType[]
  labelField?: string
  labelNameField?: string
  labelAlign?: 'left' | 'right'
  refreshResultData?: (data: { [key: string]: any }) => void
}

export interface FormNormalItemRowPropsType {
  type: string
  labelAlign?: 'left' | 'right'
  component?: ComponentType
  formItemProps?: FormItemProps
  componentProps?: { [key: string]: any }
}

export interface FormGroupItemRowPropsType {
  data?: FormDataType
  labelField: string
  labelNameField: string
  labelAlign?: 'left' | 'right'
  refreshResultData?: (data: { [key: string]: any }) => void
}
