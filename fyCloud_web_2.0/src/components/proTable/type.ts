import { TableProps } from 'antd'
import type { ActionType, ProColumns, ProTableProps } from '@ant-design/pro-components'

export interface IProTableListPropsType<T = any, P = any> {
  columns?: ProColumns<T>[]
  request: (
    params: Record<string, any>,
    sort: Record<string, any>,
    filter: Record<string, any>,
  ) => Promise<{
    data: T[]
    page: number
    success: boolean
    total: number
  }>
  params?: P
  rowKey?: string
  headerTitle?: string
  search?: ProTableProps<T, P>['search']
  toolBarRender?: ProTableProps<T, P>['toolBarRender']
  pagination?: TableProps<T>['pagination']
  rowSelection?: ProTableProps<T, P>['rowSelection']
  options?: ProTableProps<T, P>['options']
  dateFormatter?: ProTableProps<T, P>['dateFormatter']
  toolbar?: ProTableProps<T, P>['toolbar']
  tableAlertOptionRender?: ProTableProps<T, P>['tableAlertOptionRender']
  tableAlertRender?: ProTableProps<T, P>['tableAlertRender']
  selectedRowKeys?: React.Key[]
  onRowClick?: (record: any, index?: any) => void
}

export type IProTableListRef = {
  getRef?: () => ActionType | null
}
