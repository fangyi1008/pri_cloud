import { Space } from 'antd'
import AddEditDelete from '../index'
import { MailOutlined, AppstoreOutlined, SettingOutlined } from '@ant-design/icons'
const menuData = [
  {
    label: 'Navigation One',
    key: 'mail',
    icon: <MailOutlined />,
  },
  {
    label: 'Navigation Two',
    key: 'app',
    icon: <AppstoreOutlined />,
    disabled: true,
  },
  {
    label: 'Navigation Three - Submenu',
    key: 'SubMenu',
    icon: <SettingOutlined />,
    children: [
      {
        type: 'group',
        label: 'Item 1',
        children: [
          {
            label: 'Option 1',
            key: 'setting:1',
          },
          {
            label: 'Option 2',
            key: 'setting:2',
          },
        ],
      },
      {
        type: 'group',
        label: 'Item 2',
        children: [
          {
            label: 'Option 3',
            key: 'setting:3',
          },
          {
            label: 'Option 4',
            key: 'setting:4',
          },
        ],
      },
    ],
  },
  {
    label: (
      <a href='https://ant.design' target='_blank' rel='noopener noreferrer'>
        Navigation Four - Link
      </a>
    ),
    key: 'alipay',
  },
]
const data = [
  {
    key: 'key1',
    type: 'add',
    text: '新增',
  },
  {
    key: 'key2',
    type: 'edit',
    text: '编辑',
  },
  {
    key: 'key3',
    type: 'delete',
    text: '删除',
  },
  {
    key: 'key4',
    type: 'run',
    text: '运行',
    isSelect: true,
    menuData: [],
    disabled: true,
  },
  {
    key: 'key5',
    type: 'suspend',
    text: '暂停',
  },
  {
    key: 'key6',
    type: 'close',
    text: '关闭',
    isSelect: true,
    menuData: [],
  },
  {
    key: 'key7',
    type: 'restart',
    text: '重启',
  },
  {
    key: 'key8',
    type: 'console',
    text: '控制台',
    isSelect: true,
    menuData: [],
  },
  {
    key: 'key9',
    type: 'transfer',
    text: '迁移',
  },
  {
    key: 'key10',
    type: 'operate',
    text: '更多操作',
    isSelect: true,
    menuData,
  },
  {
    key: 'update',
    text: '上传',
  },
]
const onClick = (type: string) => {
  console.log(type)
}
export default function Demo() {
  return (
    <Space direction='vertical'>
      <AddEditDelete data={data} onClick={onClick} />
    </Space>
  )
}
