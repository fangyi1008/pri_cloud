import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Table } from 'antd'
import { BasicObj } from './type'
import { TableWrap } from './style'

export default function Index(props: any) {
  const {
    tableData = [],
    tableColumn = [],
    tableProps = {},
    onTableChange,
    onSelectOnchange,
    firstColRender,
    onRedirectTo,
    selectedRowKeys,
    onRow,
    isShowCheckbox = false,
    scroll = { x: 800 },
  } = props
  const [newColumns, setNewColumns] = useState(tableColumn)
  const rowKeyRef = useRef('-1')

  const rowSelection = {
    selectedRowKeys,
    onChange: (selectedRowKeys: any, selectedRows: any) => {
      onSelectOnchange && onSelectOnchange(selectedRowKeys, selectedRows)
    },
  }

  const data = useMemo(() => tableData, [tableData])

  const onHrefClick = (record: any) => () => {
    onRedirectTo && onRedirectTo(record)
  }
  const renderColumns = () => {
    const columnsCopy = [...newColumns]
    if (firstColRender) {
      if (typeof firstColRender === 'boolean') {
        columnsCopy[0].render = (text: string, record: BasicObj) => (
          <div
            style={{ cursor: 'pointer', color: 'rgb(0, 136, 204)' }}
            onClick={onHrefClick(record)}
          >
            {text}
          </div>
        )
        setNewColumns([...columnsCopy])
      } else if (typeof firstColRender === 'function') {
        firstColRender && firstColRender()
      }
    }
    setNewColumns([...tableColumn])
  }
  useEffect(() => {
    renderColumns()
  }, [])

  const selection = isShowCheckbox
    ? {
        type: 'checkbox',
        ...rowSelection,
      }
    : null
  const setRowClassName = (record: { key: string }) =>
    record.key === rowKeyRef.current ? 'clickRowStyle' : ''

  return (
    <TableWrap>
      <Table
        onRow={record => ({
          onClick: () => {
            rowKeyRef.current = record.key || ''
            onRow && onRow(record)
          },
        })}
        rowSelection={selection}
        {...tableProps}
        dataSource={data}
        columns={newColumns}
        size={'small'}
        scroll={scroll}
        onChange={onTableChange}
        rowClassName={setRowClassName}
      />
    </TableWrap>
  )
}
