import { useState } from 'react'
import AntdFormPopup from '../index'
import win from '../../selectBoxGroup/demo/icon_os_windows.svg'
import linux from '../../selectBoxGroup/demo/icon_os_linux.svg'
import { FormDataType } from '@/components/multipleFormItem/type'
import { Button } from 'antd'

const formInitialValues = {
  name: '',
  describe: '',
  os: 'win',
  version: 'htCloud',
}

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const oneFormData: FormDataType[] = [
    {
      id: 'name',
      content: '显示名称',
      type: 'input',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        placeholder: '请选择输入显示名称',
      },
    },
    {
      id: 'name1',
      content: '显示名称',
      type: 'input',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        placeholder: '请选择输入显示名称',
      },
    },
    {
      id: 'name2',
      content: '显示名称',
      type: 'input',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        placeholder: '请选择输入显示名称',
      },
    },
    {
      id: 'name3',
      content: '显示名称',
      type: 'input',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        placeholder: '请选择输入显示名称',
      },
    },
    {
      id: 'describe',
      content: '描述',
      type: 'input',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        placeholder: '请选择输入显示名称',
      },
    },
    {
      id: 'os',
      content: '操作系统',
      type: 'selectBoxGroup',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        data: [
          {
            label: 'win',
            value: 'win',
            src: win,
          },
          {
            label: 'linux',
            value: 'linux',
            src: linux,
          },
        ],
      },
    },
    {
      id: 'version',
      content: '版本',
      type: 'select',
      formItemProps: {
        required: true,
        rules: [
          {
            required: true,
          },
        ],
      },
      componentProps: {
        options: [
          { value: 'htCloud', label: '宏途' },
          { value: 'testOne', label: '测试一' },
        ],
        placeholder: '请选择版本',
      },
    },
  ]

  const onCancel = () => {
    setVisible(false)
  }
  const onFinish = () => {}
  const onFinishFailed = () => {}
  const onValuesChange = () => {}

  return (
    <>
      <Button
        onClick={() => {
          setVisible(true)
        }}
      >
        打开
      </Button>
      <AntdFormPopup
        popupTitle='测试表单'
        width='700px'
        height='300px'
        formInitialValues={formInitialValues}
        visible={visible}
        formData={oneFormData}
        onFinishFailed={onFinishFailed}
        onFinish={onFinish}
        onCancel={onCancel}
        onValuesChange={onValuesChange}
      />
    </>
  )
}
