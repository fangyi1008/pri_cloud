import React, { Ref, useEffect, useRef, useState, useImperativeHandle } from 'react'
import { Button, Input } from 'antd'
import { CloseOutlined, SearchOutlined } from '@ant-design/icons'
import { SearchInputPropsType, SearchInputValueType, SearchInputRefType } from './type'
import { buttonIconFontSize } from '../../common/commonAariable'
import SelectPoolsPopup from '../selectPoolsPopup'
import { TablePaginationConfig, TableProps } from 'antd/lib/table'

const { Group } = Input

function SearchInput(props: SearchInputPropsType, ref: Ref<SearchInputRefType>) {
  const {
    allowDelete = true,
    allowSearch = true,
    columns = [],
    selectedRowKeys: inputSelectedRowKeys,
    popupTitle = '标题',
    valueField = '',
    sourceData: inputSourceData,
    tableRowKey = 'id',
    resultIsObject = false,
    isShowPool = false,
    fieldNames,
    operations = [],
    onDelete,
    onSearch,
    onChange,
    onPoolSelect,
    onOperationClick,
    onCheck,
    onTableChange,
    ...inputOtherProps
  } = props

  useImperativeHandle(ref, () => {
    return {
      openSearchPopup: ({
        selectedRowKeys = [],
        sourceData = [],
        poolListData,
        selectedPoolKey,
        pageInfo,
      } = {}) => {
        selectedRowKeys && setSelectedRowKeys(selectedRowKeys)
        sourceData && setSourceData(sourceData)
        pageInfo && setPageInfo(pageInfo)
        if (isShowPool) {
          poolListData && setPoolListData(poolListData)
          selectedPoolKey && setSelectedPoolKey(selectedPoolKey)
        }
        setVisible(true)
      },
    }
  })

  const { value: inputValue } = inputOtherProps
  const [visible, setVisible] = useState<boolean>(false)
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>(inputSelectedRowKeys || [])
  const [sourceData, setSourceData] = useState<{ [key: string]: string }[]>(inputSourceData ?? [])

  const [selectedPoolKey, setSelectedPoolKey] = useState<string | number>('')
  const [poolListData, setPoolListData] = useState<{ [key: string]: string }[]>(
    inputSourceData ?? [],
  )
  const [pageInfo, setPageInfo] = useState<TablePaginationConfig>({
    current: 1,
    pageSize: 10,
  })

  useEffect(() => {
    setSelectedRowKeys(inputSelectedRowKeys || [])
  }, [inputSelectedRowKeys])

  useEffect(() => {
    setSourceData(inputSourceData || [])
  }, [inputSourceData])

  const selectedNetWorkRef = useRef()

  const onSearchHandler = () => {
    onSearch && onSearch()
  }

  const onPoolFilesSelect = (rowKeys: React.Key[], data: any[]) => {
    selectedNetWorkRef.current = data[0]
    setSelectedRowKeys(rowKeys)
  }

  const onTableChangeHandler: TableProps<any>['onChange'] = (
    pagination,
    filters,
    sorter,
    extra,
  ) => {
    const { current, pageSize } = pagination
    if (onTableChange) {
      onTableChange(pagination, filters, sorter, extra)
    } else {
      setPageInfo({
        pageSize: pageSize || 1,
        current: current || 10,
      })
    }
  }

  const onDeleteHandler = () => {
    if (!inputValue) {
      return
    }
    onChange && onChange('')
    onDelete && onDelete()
  }

  const onOk = async () => {
    const result = resultIsObject
      ? selectedNetWorkRef.current
      : selectedNetWorkRef?.current?.[valueField]
    if (onCheck) {
      const checkFlag = await onCheck(result)
      if (!checkFlag) {
        return
      }
    }
    setVisible(false)
    onChange && onChange(result)
    selectedNetWorkRef.current = undefined
  }

  const pagination: TablePaginationConfig = {
    ...pageInfo,
    size: 'small',
    showSizeChanger: true,
  }

  const getValue = (data: SearchInputValueType) => {
    if (typeof data === 'string') {
      return inputValue
    } else {
      return data?.[valueField]
    }
  }

  const inputWidth = `calc(100% - ${allowDelete ? '64px' : '32px'})`
  return (
    <Group compact>
      <Input
        {...inputOtherProps}
        style={{ width: inputWidth }}
        onChange={onChange}
        value={getValue(inputValue || {})}
      />
      {allowSearch && (
        <Button onClick={onSearchHandler} icon={<SearchOutlined style={buttonIconFontSize} />} />
      )}
      {allowDelete && (
        <Button onClick={onDeleteHandler} icon={<CloseOutlined style={buttonIconFontSize} />} />
      )}
      <SelectPoolsPopup
        title={popupTitle}
        visible={visible}
        width={'60%'}
        centered
        destroyOnClose={true}
        fieldNames={fieldNames}
        isShowPool={isShowPool}
        tableData={sourceData}
        tableColumns={columns}
        tableSelectedKeys={selectedRowKeys}
        tablePagination={pagination}
        tableSelectionType={'radio'}
        tableRowKey={tableRowKey}
        value={selectedPoolKey}
        onPoolSelect={onPoolSelect}
        operations={operations}
        poolListData={poolListData}
        onOperationClick={onOperationClick}
        onTableChange={onTableChangeHandler}
        onPoolFilesSelect={onPoolFilesSelect}
        okText={'确定'}
        cancelText={'取消'}
        onOk={onOk}
        onCancel={() => setVisible(false)}
      />
    </Group>
  )
}

export default React.forwardRef(SearchInput)
