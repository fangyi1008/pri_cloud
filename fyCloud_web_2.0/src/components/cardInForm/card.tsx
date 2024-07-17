import { Card, Table } from 'antd'
import { Action, Container, Operation, CardPart, ContentStyle, InfoStyle, card } from './style'
import { nanoid } from 'nanoid'
import { RedoOutlined, SmallDashOutlined } from '@ant-design/icons'
import { buttonType, ButtonListType, cardFormType, dataType, itemType } from './type'
const columns = [
  {
    title: '文件名',
    dataIndex: 'storageVolumeName',
    key: 'storageVolumeName',
  },
  {
    title: '类型',
    dataIndex: 'filesystem',
    key: 'filesystem',
  },
  {
    title: '大小',
    dataIndex: 'fileSize',
    key: 'fileSize',
  },
  {
    title: '使用者',
    dataIndex: 'createUserId',
    key: 'createUserId',
  },
]
const getId = () => nanoid()

const CardAndTable = (props: cardFormType) => {
  const { data, storageData, onSelect, onRow, onCardButtonClick, onTableChange, tableProps } = props

  const handleClick = (key: string, id: string) => () => {
    onCardButtonClick && onCardButtonClick(key, id)
  }
  const getActionIcon = (key: string, Icon: JSX.Element, id: string, text?: string) => {
    return (
      <Action key={getId()} onClick={handleClick(key, id)}>
        {Icon}
        {text && text}
      </Action>
    )
  }

  const actionButton = (cardButtonList: ButtonListType, id: string) => [
    <Operation key={id}>
      {cardButtonList ? (
        cardButtonList.map((item: buttonType) => {
          const { key, icon, text } = item
          return getActionIcon(key, icon, id, text)
        })
      ) : (
        <>
          {getActionIcon('refresh', <RedoOutlined />, id)}
          {getActionIcon('detail', <SmallDashOutlined />, id)}
        </>
      )}
    </Operation>,
  ]

  const CardData = (data: dataType) =>
    data.map((item: itemType) => (
      <Card
        key={item.key}
        bordered={true}
        hoverable
        style={card}
        actions={actionButton(item.cardButtonList, item.key)}
        onClick={() => {
          onSelect && onSelect(item.key)
        }}
      >
        <ContentStyle key={getId()}>
          <InfoStyle key={getId()}>{item.storagePoolName}</InfoStyle>
          <InfoStyle key={getId()}>本地存储</InfoStyle>
          <InfoStyle key={getId()}>{item.usedSpace}</InfoStyle>
        </ContentStyle>
      </Card>
    ))

  return (
    <Container>
      <CardPart>{CardData(data)}</CardPart>
      <Table
        columns={columns}
        dataSource={storageData}
        onRow={onRow}
        onChange={onTableChange}
        {...tableProps}
      />
    </Container>
  )
}

export default CardAndTable
