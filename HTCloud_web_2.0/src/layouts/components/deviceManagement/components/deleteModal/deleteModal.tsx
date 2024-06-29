import { Modal } from 'antd'
import { ExclamationCircleOutlined } from '@ant-design/icons'
import { DeleteDataCenterWrap } from './style'
import { DeleteModalProps } from './type'

export function DeleteModal(props: DeleteModalProps) {
  const { visible, title, content, handleOk, handleCancel } = props

  return (
    <Modal title={title} open={visible} onOk={handleOk} onCancel={handleCancel}>
      <DeleteDataCenterWrap>
        <ExclamationCircleOutlined
          style={{ fontSize: '38px', color: 'red', marginRight: '20px' }}
        />
        {content}
      </DeleteDataCenterWrap>
    </Modal>
  )
}
