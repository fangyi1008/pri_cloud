import { TitleWrapper, FooterWrapper } from '../formPopop/style'
import AddEditDelete from '../addEditDelete'
import { Modal } from 'antd'
import Card from './card'
import { cardForm } from './style'
import { BasicObj } from '../multipleForm/type'

export default function Basic(props: BasicObj) {
  const {
    onRow,
    data,
    title,
    onCancel,
    visible,
    onOk,
    onSelect,
    storageData,
    buttonList,
    buttonType,
    onClick,
    onCardButtonClick,
    onTableChange,
    tableProps,
  } = props

  return (
    <Modal
      footer={false}
      visible={visible}
      onCancel={onCancel}
      onOk={onOk}
      width={'70%'}
      style={cardForm}
    >
      <TitleWrapper>{title ? title() : null}</TitleWrapper>
      <Card
        onRow={onRow}
        data={data}
        onSelect={onSelect}
        storageData={storageData}
        onCardButtonClick={onCardButtonClick}
        onTableChange={onTableChange}
        tableProps={tableProps}
      />
      <FooterWrapper>
        <AddEditDelete data={buttonList} onClick={onClick} buttonType={buttonType} />
      </FooterWrapper>
    </Modal>
  )
}
