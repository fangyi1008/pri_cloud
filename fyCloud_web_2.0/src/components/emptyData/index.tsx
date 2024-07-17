import { InboxOutlined } from '@ant-design/icons'
import { EmptyWrap, Text } from './style'

function EmptyData() {
  return (
    <EmptyWrap>
      <InboxOutlined style={{ fontSize: 30 }} />
      <Text> No Data</Text>
    </EmptyWrap>
  )
}

export default EmptyData
