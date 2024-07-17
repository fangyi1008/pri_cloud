import { Space } from 'antd'
import Search from '../index'

const onSearch = (value: string) => console.log(value)

export default function Demo() {
  return (
    <Space direction='vertical'>
      <Search placeholder='input search text' onSearch={onSearch} addonBefore={'data-search'} />
      <Search placeholder='input search text' onSearch={onSearch} />
    </Space>
  )
}
