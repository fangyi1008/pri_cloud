import { Table } from 'antd'
import { TaskDeskWrap, IconWrap, TableWrap, DeskHeader, HeaderTitle } from './style'
import { CloseOutlined } from '@ant-design/icons'
import { TaskDeskPropsType } from './type'

function TaskDesk(props: TaskDeskPropsType) {
  const { data = [], columns = [], tableScroll, visible = false, onClick, title } = props

  return (
    <TaskDeskWrap visible={visible}>
      <DeskHeader>
        <HeaderTitle>{title} </HeaderTitle>
        <IconWrap onClick={onClick}>
          <CloseOutlined />
        </IconWrap>
      </DeskHeader>
      <TableWrap>
        <Table columns={columns} dataSource={data} bordered scroll={tableScroll} size={'small'} />
      </TableWrap>
    </TaskDeskWrap>
  )
}

export default TaskDesk
