import { TitleWrapper, FooterWrapper, CustomFooterWrapper } from './style'
import { FormPopupPropsType } from './type'
import FormContent from '../multipleForm/index'
import { Modal, Button } from 'antd'

export default function FormPopup(props: FormPopupPropsType) {
  const {
    data,
    title,
    onCancel,
    customFooter,
    visible,
    onOk,
    onChange,
    formValue,
    loading = false,
  } = props

  return (
    <Modal
      destroyOnClose
      footer={false}
      visible={visible}
      onCancel={onCancel}
      onOk={onOk}
      width={'50%'}
    >
      <TitleWrapper>{title ? title() : null}</TitleWrapper>
      <FormContent data={data} onChange={onChange} value={formValue} />
      {customFooter ? (
        <CustomFooterWrapper> {customFooter(formValue)}</CustomFooterWrapper>
      ) : (
        <FooterWrapper>
          <Button onClick={onCancel} loading={loading}>
            取消
          </Button>
          <Button type={'primary'} onClick={onOk} loading={loading}>
            确定
          </Button>
        </FooterWrapper>
      )}
    </Modal>
  )
}
