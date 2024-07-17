import { PoolFileOperationType, PoolListDataType, SelectPoolFilePropsType } from './type'
import React from 'react'
import {
  ModalLeftWrap,
  ModalRightWrap,
  OperationButton,
  PoolInfoContentWrap,
  PoolInfoImgWrap,
  PoolInfoWrap,
  PoolOperationButton,
  PoolWrap,
  SearchPopupWrap,
} from './style'
import storage from './imgs/storage.svg'
import { Table, Tooltip, Empty } from 'antd'
import { isNotNullArray } from '@common/utils'

function SelectPoolFile(props: SelectPoolFilePropsType) {
  const {
    fieldNames = {
      title: 'title',
      valueField: 'id',
      subtitle: 'subtitle',
      describe: 'describe',
    },
    value,
    operations = [],
    poolListData = [],
    tableOtherProps = {},
    tablePagination,
    tableColumns,
    tableData,
    tableSelectedKeys = [],
    tableSelectionType = 'checkbox',
    tableRowKey = 'id',
    isShowPool = true,
    onOperationClick,
    onPoolSelect,
    onPoolFilesSelect,
    onTableChange,
  } = props

  const operationClickHandler =
    (operation: PoolFileOperationType, poolData: PoolListDataType) => (event: React.UIEvent) => {
      event.stopPropagation()
      event.preventDefault()
      onOperationClick && onOperationClick(operation, poolData)
    }

  const poolSelectHandler = (item: PoolListDataType) => () => {
    onPoolSelect && onPoolSelect(item)
  }

  const onPoolFilesSelectHandler = (selectedRowKeys: React.Key[], selectedRows: any[]) => {
    onPoolFilesSelect && onPoolFilesSelect(selectedRowKeys, selectedRows)
  }

  const rowSelection = {
    ...tableOtherProps.rowSelection,
    onChange: (selectedRowKeys: React.Key[], selectedRows: any[]) => {
      onPoolFilesSelectHandler(selectedRowKeys, selectedRows)
    },
  }

  return (
    <SearchPopupWrap>
      {isShowPool && (
        <>
          {poolListData.length > 0 ? (
            <ModalLeftWrap>
              {poolListData.map(poolItem => {
                const {
                  valueField = 'id',
                  title = 'title',
                  subtitle = 'subtitle',
                  describe = 'describe',
                } = fieldNames
                const poolKeyValue = poolItem[valueField]
                const titleValue = poolItem[title]
                const subtitleValue = poolItem[subtitle]
                const describeValue = poolItem[describe]
                const isSelect = value === poolKeyValue
                return (
                  <PoolWrap
                    isSelect={isSelect}
                    onClick={poolSelectHandler(poolItem)}
                    key={poolKeyValue}
                  >
                    <PoolInfoWrap>
                      <PoolInfoImgWrap>
                        <img src={storage} />
                      </PoolInfoImgWrap>
                      <PoolInfoContentWrap>
                        <p title={titleValue}>{titleValue}</p>
                        <p title={subtitleValue}>{subtitleValue}</p>
                        <p title={describeValue}>{describeValue}</p>
                      </PoolInfoContentWrap>
                    </PoolInfoWrap>
                    {isNotNullArray(operations) && (
                      <PoolOperationButton>
                        {operations.map(item => {
                          const { type, src, icon, title } = item
                          return (
                            <Tooltip title={title} key={title}>
                              <OperationButton onClick={operationClickHandler(item, poolItem)}>
                                {type === 'img' ? <img src={src} /> : icon}
                              </OperationButton>
                            </Tooltip>
                          )
                        })}
                      </PoolOperationButton>
                    )}
                  </PoolWrap>
                )
              })}
            </ModalLeftWrap>
          ) : (
            <ModalLeftWrap>
              <Empty />
            </ModalLeftWrap>
          )}
        </>
      )}
      <ModalRightWrap>
        <Table
          {...tableOtherProps}
          rowKey={tableRowKey}
          onRow={record => {
            return {
              onClick: event => {
                const { [tableRowKey]: key } = record
                tableSelectionType === 'radio' &&
                  onPoolFilesSelectHandler &&
                  onPoolFilesSelectHandler([key], [record])
              },
            }
          }}
          rowSelection={{
            type: tableSelectionType,
            selectedRowKeys: tableSelectedKeys,
            ...rowSelection,
          }}
          columns={tableColumns}
          dataSource={tableData}
          pagination={tablePagination}
          onChange={onTableChange}
        />
      </ModalRightWrap>
    </SearchPopupWrap>
  )
}

export default SelectPoolFile
