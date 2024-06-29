import { Fragment, useState } from 'react'
import FormStepPopup from '../index'
import { FormDataType } from '../../multipleForm/type'
import { ButtonConfirmSpan } from '../../multipleForm/style'

const defaultClassifyData = [
  { value: 'htCloud', label: '宏途' },
  { value: 'testOne', label: '测试一' },
]

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const [horizontalVisible, setHorizontalVisible] = useState(false)

  const [current, setCurrent] = useState(0)

  const formData: FormDataType[] = [
    {
      key: 'name',
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
      key: 'machine',
      title: '机型',
      type: 'text',
      value: 'value',
      placeholder: '请选择分类',
    },
    {
      key: 'ps',
      title: '注释',
      type: 'input',
      placeholder: '请选择注释',
    },
  ]

  const onPrev = () => {
    setCurrent(current - 1)
  }
  const onNext = () => {
    setCurrent(current + 1)
  }

  const steps = [
    {
      key: 'First',
      title: 'First',
      data: formData,
      render: () => <></>,
    },
    {
      key: 'Second',
      title: 'Second',
      data: formData,
    },
    {
      key: 'Last',
      title: 'Last',
      data: formData,
    },
  ]

  return (
    <Fragment>
      <ButtonConfirmSpan
        onClick={() => {
          setVisible(true)
        }}
        width={'100%'}
      >
        打开横向布局弹窗
      </ButtonConfirmSpan>
      <FormStepPopup
        steps={steps}
        onPrev={onPrev}
        onNext={onNext}
        current={current}
        layoutType={'vertical'}
        title={'新建数据中心'}
        visible={visible}
        onCancel={() => {
          setVisible(false)
        }}
        onFinish={() => {
          setVisible(false)
        }}
        onChange={() => {}}
      />
      <br />
      <ButtonConfirmSpan
        onClick={() => {
          setHorizontalVisible(true)
        }}
        width={'100%'}
      >
        打开竖向布局弹窗
      </ButtonConfirmSpan>

      <FormStepPopup
        steps={steps}
        onPrev={onPrev}
        onNext={onNext}
        current={current}
        layoutType={'horizontal'}
        title={'新建数据中心'}
        visible={horizontalVisible}
        onCancel={() => {
          setHorizontalVisible(false)
        }}
        onChange={() => {}}
      />
    </Fragment>
  )
}
