import { ButtonSpan, Wrap, IconWrap, ButtonWrap } from './style'
import { ButtonAddSpan } from '../formPopop/style'
import {
  CaretRightOutlined,
  DownOutlined,
  PauseOutlined,
  BorderOutlined,
  RedoOutlined,
  DesktopOutlined,
  PlusOutlined,
} from '@ant-design/icons'
import { Dropdown, Space, Menu, Spin } from 'antd'
import { AddEditDeletePropsType } from './type'
import EmptyData from '../emptyData/index'
import { ItemType } from 'antd/es/menu/hooks/useItems'
import { isNotEmptyArray } from '@common/utils'
import { useAppSelector } from '@/redux/store'

const getPreIcon = (type: string) => {
  switch (type) {
    case 'add':
      return <PlusOutlined style={{ fontSize: '16px' }} />
    case 'run':
      return <CaretRightOutlined style={{ fontSize: '16px' }} />
    case 'suspend':
      return <PauseOutlined style={{ fontSize: '16px' }} />
    case 'close':
      return <BorderOutlined style={{ fontSize: '14px', background: 'gray', marginRight: 2 }} />
    case 'restart':
      return <RedoOutlined style={{ fontSize: '16px' }} />
    case 'console':
      return <DesktopOutlined style={{ fontSize: '16px', paddingRight: 2 }} />
    default:
      return null
  }
}

const SetLoading = (props: any) => {
  const { type, text, buttonKey, handleClick, alias } = props
  const state = useAppSelector(store => alias && store[alias as keyof typeof store])
  const { isloading, percent } = state && state[buttonKey as keyof typeof state]
  return (
    <ButtonSpan onClick={isloading ? () => {} : handleClick(type, buttonKey)}>
      {isloading ? (
        <>
          <Spin size='small' spinning={isloading} style={{ marginRight: '5px' }}></Spin>
          {percent + '%'}
        </>
      ) : null}
      {text}
    </ButtonSpan>
  )
}

function AddEditDelete(props: AddEditDeletePropsType) {
  const { data = [], onClick, onMenuClick, buttonType = 'primary' } = props
  const handleClick = (type: string, key: string) => () => {
    onClick && onClick(type, key)
  }
  const handleMenuClick = (event: { key: string }) => {
    const { key } = event
    onMenuClick && onMenuClick(key)
  }
  const getDropdownData = (data: ItemType[]) => {
    return isNotEmptyArray(data) ? (
      <Menu items={data} onClick={handleMenuClick} />
    ) : (
      <Menu>
        <EmptyData />
      </Menu>
    )
  }
  const getMenu = (data: ItemType[]) => {
    return (
      <Dropdown overlay={getDropdownData(data)}>
        <IconWrap>
          <Space>
            <DownOutlined style={{ fontSize: '10px' }} />
          </Space>
        </IconWrap>
      </Dropdown>
    )
  }
  return (
    <Wrap>
      {data.map((item, index) => {
        const { key, type = '', text = '', isSelect, menuData = [], disabled = false, alias } = item
        return (
          <ButtonWrap key={`${key}_${index}`} disabled={disabled}>
            {type === 'download' ? (
              <SetLoading {...{ type, text, buttonKey: key, handleClick, alias }}></SetLoading>
            ) : (
              <ButtonSpan onClick={handleClick(type, key)}>
                {getPreIcon(type)}
                {text}
              </ButtonSpan>
            )}
            {/* {buttonType === 'primary' ? (
              <ButtonSpan onClick={handleClick(type, key)} type={type}>
                {getPreIcon(type)}
                {text}
              </ButtonSpan>
            ) : (
              <ButtonAddSpan onClick={handleClick(type, key)}>{text}</ButtonAddSpan>
            )} */}
            {isSelect ? getMenu(menuData) : null}
          </ButtonWrap>
        )
      })}
    </Wrap>
  )
}

export default AddEditDelete
