import { useRef, useImperativeHandle, forwardRef, Ref } from 'react'
import type { ActionType } from '@ant-design/pro-components'
import { ProTable } from '@ant-design/pro-components'
import { IProTableListPropsType, IProTableListRef } from './type'

function ProTableList<T extends Record<string, any>>(
  props: IProTableListPropsType<T>,
  ref: Ref<IProTableListRef>,
) {
  const {
    columns,
    request,
    headerTitle = '表格',
    rowKey = 'id',
    search = {
      labelWidth: 'auto',
    },
    params = {},
    toolBarRender,
    toolbar,
    pagination = false,
    rowSelection = {
      alwaysShowAlert: true,
    },
    options = {
      setting: {
        listsHeight: 200,
      },
    },
    dateFormatter = 'string',
    tableAlertOptionRender,
    tableAlertRender,
    selectedRowKeys,
    onRowClick,
  } = props

  useImperativeHandle(ref, () => {
    return {
      getRef: () => {
        return actionRef.current
      },
    }
  })
  const actionRef = useRef<ActionType>(null)
  const onRow = (record: any) => {
    return {
      onClick: () => {
        onRowClick && onRowClick(record)
      },
    }
  }
  return (
    <ProTable<T>
      columns={columns}
      actionRef={actionRef}
      params={params}
      request={async (params = {}, sort, filter) => {
        return await request(params, sort, filter)
      }}
      rowKey={rowKey}
      search={search}
      options={options}
      pagination={pagination}
      dateFormatter={dateFormatter}
      headerTitle={headerTitle}
      toolbar={toolbar}
      rowSelection={selectedRowKeys ? { ...rowSelection, selectedRowKeys } : rowSelection}
      tableAlertOptionRender={tableAlertOptionRender}
      tableAlertRender={tableAlertRender}
      toolBarRender={toolBarRender}
      onRow={onRow}
    />
  )
}

const HostListRef = forwardRef(ProTableList) as <T extends object = any>(
  props: React.PropsWithChildren<IProTableListPropsType<T>> & {
    ref?: React.RefObject<IProTableListRef>
  },
) => React.ReactElement

export default HostListRef
