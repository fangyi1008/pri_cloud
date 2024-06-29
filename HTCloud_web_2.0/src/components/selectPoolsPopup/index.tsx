import { Modal } from 'antd'
import { SearchPopupPropsType } from './type'
import SelectPools from './SelectPools'

function SearchPopup(props: SearchPopupPropsType) {
  const {
    title = '标题',
    visible = false,
    width = '60%',
    centered = true,
    isShowPool = false,
    value,
    operations,
    poolListData,
    tableData,
    tableColumns,
    tableSelectedKeys,
    tablePagination,
    tableOtherProps,
    tableSelectionType,
    tableRowKey = 'id',
    fieldNames,
    onTableChange,
    onPoolSelect,
    onOperationClick,
    onPoolFilesSelect,
    ...modelOtherProps
  } = props

  return (
    <>
      <Modal
        title={title}
        visible={visible}
        width={width}
        centered={centered}
        bodyStyle={{ padding: '0 20px' }}
        {...modelOtherProps}
      >
        <SelectPools
          isShowPool={isShowPool}
          fieldNames={fieldNames}
          value={value}
          operations={operations}
          poolListData={poolListData}
          onPoolSelect={onPoolSelect}
          onOperationClick={onOperationClick}
          tableSelectedKeys={tableSelectedKeys}
          tableColumns={tableColumns}
          tableData={tableData}
          tablePagination={tablePagination}
          tableSelectionType={tableSelectionType}
          tableRowKey={tableRowKey}
          tableOtherProps={{
            size: 'small',
            scroll: { y: 298 },
            ...tableOtherProps,
          }}
          onTableChange={onTableChange}
          onPoolFilesSelect={onPoolFilesSelect}
        />
      </Modal>
    </>
  )
}

export default SearchPopup
