import React, { useState, useRef, useEffect } from 'react'
import MultipleFormItem from '../index'
import { Button, Form, Select } from 'antd'
import { FormInstance } from 'antd/es/form'
import { FormDataType } from '../type'
const { Item } = Form

function CustomDemo(props: any) {
  const { formItemProps, componentProps } = props
  const [options, setOptions] = useState<{ value: string }[]>([])
  const form = Form.useFormInstance()
  const category = Form.useWatch('category', form)
  const getData = async (category: string) => {
    const promise = new Promise(() => {
      setTimeout(() => {
        // 模拟调用了一个接口
        const data =
          category === 'testOne'
            ? [{ value: 'testOne-1' }, { value: 'testOne-2' }, { value: 'testOne-3' }]
            : [{ value: 'gold' }, { value: 'lime' }]
        const defaultValue = data[0]?.value ?? ''
        setOptions(data)
        form.setFieldsValue({ custom: defaultValue })
      }, 1000)
    })
  }
  useEffect(() => {
    getData(category)
  }, [category])
  return (
    <Item {...formItemProps}>
      <Select mode='multiple' options={options} />
    </Item>
  )
}

const formData: FormDataType[] = [
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
    },
  },
  {
    id: 'custom',
    content: '自定义组件',
    type: 'custom',
    component: CustomDemo,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
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

  const onSetData = () => {
    formRef.current?.setFieldsValue({ category: 'testOne' })
  }

  return (
    <>
      <Button type='primary' onClick={onSetData}>
        赋值
      </Button>
      <Form
        validateMessages={validateMessages}
        initialValues={{ category: 'htCloud' }}
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
