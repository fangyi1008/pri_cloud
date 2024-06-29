import React, { useRef } from 'react'
import MultipleFormItem from '../index'
import { Button, Form } from 'antd'
import { FormInstance } from 'antd/es/form'
import { FormDataType } from '../type'
import win from '../../selectBoxGroup/demo/icon_os_windows.svg'
import linux from '../../selectBoxGroup/demo/icon_os_linux.svg'

const formData: FormDataType[] = [
  {
    id: 'name1',
    content: '一个文本框',
    type: 'input',
    formItemProps: {
      required: true,
      rules: [
        {
          message: '请输入mane',
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入名称',
    },
  },
  {
    id: 'text',
    content: '展示文本',
    type: 'text',
    componentProps: {
      defaultValue: '123123',
    },
  },
  {
    id: 'name2',
    content: 'textarea',
    type: 'textarea',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请输入名称',
    },
  },
  {
    id: 'switch',
    content: '一个switch',
    type: 'switch',
    formItemProps: {
      required: true,
      valuePropName: 'checked',
      rules: [
        {
          required: true,
        },
      ],
    },
  },
  {
    id: 'Rate',
    content: 'Rate',
    type: 'rate',
  },
  {
    id: 'cascader',
    content: 'cascader',
    type: 'cascader',
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
        {
          value: 'zhejiang',
          label: 'Zhejiang',
          children: [
            {
              value: 'hangzhou',
              label: 'Hangzhou',
            },
          ],
        },
      ],
    },
  },
  {
    id: 'searchInput',
    content: '搜索框',
    type: 'searchInput',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      onDelete: () => {
        console.log('onSearch')
      },
      onSearch: () => {
        console.log('onSearch')
      },
      allowDelete: true,
      addonBefore: <div>before</div>,
      addonAfter: <div>after</div>,
      prefix: <div>prefix</div>,
      suffix: <div>prefix</div>,
    },
  },
  {
    id: 'unitNumber',
    content: '单位输入框',
    type: 'unitNumber',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      onDelete: () => {
        console.log('onDelete ==> unitNumber')
      },
      unitOptions: [
        { a: '个', b: '个' },
        { a: '核', b: '核' },
      ],
      min: 2,
      max: 10,
      step: 2,
      fieldNames: { label: 'a', value: 'b' },
      allowDelete: true,
    },
  },
  {
    id: 'group-from',
    content: 'group-from',
    type: 'groupFrom',
    groupItems: [
      {
        id: 'name',
        content: '名称',
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
              label: 'win1',
              value: 'win1',
              src: win,
            },
            {
              label: 'linux1',
              value: 'linux1',
              src: linux,
            },
          ],
        },
      },
      {
        id: 'os',
        content: '操作系统',
        type: 'input',
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: { placeholder: '请输入名称' },
      },
      {
        id: 'category',
        content: '存储类型',
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
          placeholder: '请选择分类',
          mode: 'multiple',
        },
      },
      {
        id: 'radio',
        content: '单选',
        type: 'radio',
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
            { label: '上课上班', value: 'Apple' },
            { label: 'Pear', value: 'Pear' },
            { label: 'Orange', value: 'Orange', disabled: true },
          ],
        },
      },
      {
        id: 'checkbox',
        content: 'checkbox',
        type: 'checkbox',
        componentProps: {
          options: [
            { label: '苹果', value: 'Apple' },
            { label: '梨', value: 'Pear' },
            { label: '橘子', value: 'Orange' },
          ],
        },
      },
    ],
  },
]

export default function Demo() {
  const formRef: React.RefObject<FormInstance> = useRef(null)
  const validateMessages = {
    required: '${label}是必填字段',
  }
  const onFinish = async () => {
    try {
      const values = await formRef.current?.validateFields()
      console.log('values==>', values)
    } catch (ex) {
      console.log('value error==>', ex)
    }
  }
  const onValuesChange = (changedValues: any, allValues: any) => {
    console.log('onValuesChange==>', changedValues, allValues)
  }

  return (
    <>
      <Form
        validateMessages={validateMessages}
        ref={formRef}
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 18 }}
        onValuesChange={onValuesChange}
      >
        <MultipleFormItem formItems={formData} labelField={'content'} labelNameField={'id'} />
        <Form.Item wrapperCol={{ span: 24, offset: 16 }}>
          <Button type='primary' onClick={onFinish}>
            Check
          </Button>
        </Form.Item>
      </Form>
    </>
  )
}
