import { Fragment, useState } from 'react'
import FormPopup from '../'
import { BasicObj } from '../type'
import { FormDataType } from '../../multipleForm/type'
import { ButtonConfirmSpan } from '../../multipleForm/style'

const defaultClassifyData = [
  { value: 'htCloud', text: '宏途' },
  { value: 'testOne', text: '测试一' },
]

export default function Demo() {
  const [visible, setVisible] = useState(false)

  const [loading, setLoading] = useState(false)
  const handleBtnClick = () => {
    setVisible(true)
  }
  const handleConfirm = async (obj: BasicObj) => {
    setLoading(true)
    await new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve({})
      }, 2000)
    })
    setLoading(false)
    setVisible(false)
  }
  const handleCancelClick = () => {
    setVisible(false)
  }

  const formData: FormDataType[] = [
    {
      key: 'title',
      title: '名称',
      type: 'input',
      placeholder: '请输入名称',
    },
    {
      key: 'desc',
      title: '描述',
      type: 'input',
      placeholder: '请输入描述',
    },
    {
      key: 'category',
      title: '存储类型',
      type: 'select',
      data: defaultClassifyData,
      placeholder: '请选择分类',
      mode: 'multiple',
    },
    {
      key: 'key',
      title: '机型',
      type: 'text',
      value: 'value',
      placeholder: '请选择分类',
      // multiple: true,
    },
    {
      key: 'ps',
      title: '注释',
      type: 'input',
      placeholder: '请选择注释',
      // multiple: true,
    },
  ]
  const onChange = (value: BasicObj) => {
    console.log(value)
  }
  return (
    <Fragment>
      <ButtonConfirmSpan onClick={handleBtnClick} width={'100%'}>
        打开表单弹窗
      </ButtonConfirmSpan>
      <FormPopup
        onOk={handleConfirm}
        onCancel={handleCancelClick}
        data={formData}
        loading={loading}
        title={() => {
          return '新建数据中心'
        }}
        visible={visible}
        onChange={onChange}
        formValue={{}}
      />
    </Fragment>
  )
}
