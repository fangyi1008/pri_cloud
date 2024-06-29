import React from 'react'
import { ModalProps } from 'antd/es/modal'
import { ColumnsType, TablePaginationConfig, TableProps } from 'antd/lib/table'

export type FieldNameType = {
  title?: string
  subtitle?: string
  describe?: string
  valueField?: string
}

export type PoolFileOperationType = {
  type: 'img' | 'icon'
  icon?: React.ReactNode
  src?: string
  title: string
}

export type PoolListDataType = {
  [key: string]: any
}

export interface SearchPopupPropsType extends ModalProps, SelectPoolFilePropsType {}

export interface SelectPoolFilePropsType {
  isShowPool?: boolean
  value?: string | number
  fieldNames?: FieldNameType
  operations?: PoolFileOperationType[]
  poolListData?: PoolListDataType[]
  tableData?: { [key: string]: any }[]
  tableColumns: ColumnsType<any>
  tableSelectedKeys?: React.Key[]
  tablePagination?: TablePaginationConfig
  tableOtherProps?: TableProps<any>
  tableSelectionType?: 'checkbox' | 'radio'
  tableRowKey?: string
  onTableChange?: TableProps<any>['onChange']
  onPoolSelect?: (item: PoolListDataType) => void
  onPoolFilesSelect?: (selectedRowKeys: React.Key[], selectedRows: any[]) => void
  onOperationClick?: (item: PoolFileOperationType, poolData: PoolListDataType) => void
}
