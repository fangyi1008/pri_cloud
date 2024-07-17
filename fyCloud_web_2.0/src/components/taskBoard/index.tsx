import CloseOutlined from '@ant-design/icons/lib/icons/CloseOutlined'
import { Table, Drawer, Space, Button } from 'antd'
import { TaskDeskPropsType } from './type'

function TaskDesk(props: TaskDeskPropsType) {
  const { columns, data, visible, onClose } = props
  return (
    <Drawer
      title='任务列表'
      placement='bottom'
      open={visible}
      height={280}
      bodyStyle={{ padding: 10 }}
      onClose={onClose}
      closable={false}
      extra={
        <Space>
          <Button onClick={onClose} icon={<CloseOutlined />} size={'small'} />
        </Space>
      }
    >
      <Table scroll={{ y: 154 }} sticky columns={columns} dataSource={data} pagination={false} />
    </Drawer>
  )
}

export default TaskDesk
