import { InputProps } from 'antd/es/input'
import { ColumnsType } from 'antd/es/table'
import React from 'react'
import {
  FieldNameType,
  PoolFileOperationType,
  PoolListDataType,
} from '@components/selectPoolsPopup/type'
import { TableProps } from 'antd/lib/table'
import { TablePaginationConfig } from 'antd/lib/table/interface'

export type SearchInputValueType = string | Record<string, any>

export type SearchInputRefType = {
  openSearchPopup: (init?: {
    selectedRowKeys?: React.Key[]
    sourceData?: Record<string, any>[]
    poolListData?: Record<string, any>[]
    selectedPoolKey?: string | number
    pageInfo?: TablePaginationConfig
  }) => void
}

export interface SearchInputPropsType extends Omit<InputProps, 'value' | 'onChange'> {
  value?: SearchInputValueType
  allowDelete?: boolean
  allowSearch?: boolean
  columns?: ColumnsType
  selectedRowKeys?: React.Key[]
  sourceData?: Record<string, any>[]
  popupTitle?: string
  valueField?: string
  tableRowKey?: string
  resultIsObject?: boolean
  fieldNames?: FieldNameType
  isShowPool?: boolean
  operations?: PoolFileOperationType[]
  onCheck?: (data: any) => boolean
  onTableChange?: TableProps<any>['onChange']
  onChange?: (value?: SearchInputValueType) => void
  onPoolSelect?: (item: PoolListDataType) => void
  onOperationClick?: (item: PoolFileOperationType, poolData: PoolListDataType) => void
  onDelete?: () => void
  onSearch?: () => void
}
