import CardForm from '../cardForm'
import { useState } from 'react'
import { BasicObj } from '../../formPopop/type'
import { ButtonConfirmSpan } from '../../multipleForm/style'
import { RedoOutlined, SmallDashOutlined } from '@ant-design/icons'
import { ButtonListType } from '../type'
const cardButtonList = [
  {
    key: 'refresh',
    text: '刷新',
    icon: <RedoOutlined />,
  },
  {
    key: 'detail',
    text: '详情',
    icon: <SmallDashOutlined />,
  },
]
const cardButtonList1 = [
  {
    key: 'refresh',
    icon: <RedoOutlined />,
  },
  {
    key: 'detail',
    text: '详情',
    icon: <SmallDashOutlined />,
  },
  {
    key: 'update',
    text: '修改',
    icon: <RedoOutlined />,
  },
]
const data: {
  usedSpace: string
  description: string
  cardButtonList: ButtonListType
  title: string
  key: string
}[] = [
  {
    key: '11',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList,
  },
  {
    key: '111',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList1,
  },
  {
    key: '12',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList1,
  },
  {
    key: '13',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList1,
  },
  {
    key: '14',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList1,
  },
  {
    key: '15',
    title: '本地存储池',
    description: 'hahahah',
    usedSpace: '还有11GB可用',
    cardButtonList: cardButtonList1,
  },
]

const buttonList = [
  {
    key: 'add',
    type: 'add',
    text: '新增存储卷',
  },
  {
    key: 'upload',
    type: 'upload',
    text: '上传存储卷',
  },
  {
    key: 'confirm',
    type: 'confirm',
    text: '确定',
  },
  {
    key: 'close',
    type: 'close',
    text: '关闭',
  },
]
const onClick = (type: string) => {
  console.log(type)
}

const onCardButtonClick = (key: string, id: string) => {
  console.log(key, id)
}

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const handleBtnClick = () => {
    setVisible(true)
  }
  const handleConfirm = async (obj: BasicObj) => {
    setVisible(false)
  }
  const handleCancelClick = () => {
    setVisible(false)
  }
  const onSelect = () => {
    console.log(111)
  }
  return (
    <>
      <ButtonConfirmSpan onClick={handleBtnClick} width={'100%'}>
        打开表单弹窗
      </ButtonConfirmSpan>
      <CardForm
        onSelect={onSelect}
        buttonType={'default'}
        buttonList={buttonList}
        onClick={onClick}
        onCardButtonClick={onCardButtonClick}
        data={data}
        visible={visible}
        onOk={handleConfirm}
        onCancel={handleCancelClick}
        title={() => {
          return '新建存储卷'
        }}
        onTableChange={() => {}}
        tableProps={{}}
      />
    </>
  )
}
