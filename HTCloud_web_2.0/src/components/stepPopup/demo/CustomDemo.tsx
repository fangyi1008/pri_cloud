import { Fragment, useEffect, useState } from 'react'
import StepPopup from '../index'
import { Form, Select, Button } from 'antd'
import win from '../../selectBoxGroup/demo/icon_os_windows.svg'
import linux from '../../selectBoxGroup/demo/icon_os_linux.svg'
const { Item } = Form

const windowVersion = [
  {
    value: '0',
    label: 'Microsoft Windows Server 2016(64位)',
  },
  {
    value: '1',
    label: 'Microsoft Windows Server 2012 R2(64位)',
  },
  {
    value: '2',
    label: 'Microsoft Windows Server 2012(64位)',
  },
  {
    value: '3',
    label: 'Microsoft Windows Server 2008 R2(64位)',
  },
  {
    value: '4',
    label: 'Microsoft Windows Server 2008(64位)',
  },
  {
    value: '5',
    label: 'Microsoft Windows Server 2008(32位)',
  },
  {
    value: '6',
    label: 'Microsoft Windows Server 2003 R2(64位)',
  },
]

const linuxVersion = [
  {
    value: '0',
    label: 'Red Hat Enterprise Linux 7(64位)',
  },
  {
    value: '1',
    label: 'Red Hat Enterprise Linux 7(32位)',
  },
  {
    value: '2',
    label: 'Red Hat Enterprise Linux 6(64位)',
  },
  {
    value: '3',
    label: 'Red Hat Enterprise Linux 6(32位)',
  },
  {
    value: '4',
    label: 'Red Hat Enterprise Linux 5.10(64位)',
  },
  {
    value: '5',
    label: 'Red Hat Enterprise Linux 5.10(32位)',
  },
]

function CustomDemo(props: any) {
  const { formItemProps, componentProps } = props
  const [options, setOptions] = useState<{ value: string }[]>([])
  const form = Form.useFormInstance()
  const os = Form.useWatch('os', form)
  const getData = async (category: string) => {
    const promise = new Promise(() => {
      setTimeout(() => {
        const data = category === 'win' ? windowVersion : linuxVersion
        const defaultValue = data[0]?.value ?? ''
        setOptions(data)
        form.setFieldsValue({ version: defaultValue })
      }, 1000)
    })
  }
  useEffect(() => {
    getData(os)
  }, [os])
  return (
    <Item {...formItemProps}>
      <Select options={options} />
    </Item>
  )
}

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const [current, setCurrent] = useState(0)

  const steps1 = [
    {
      key: 'First',
      title: 'First',
      data: [
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
          componentProps: {
            options: [
              { value: 'htCloud', label: '宏途' },
              { value: 'testOne', label: '测试一' },
            ],
            placeholder: '请选择版本',
          },
        },
      ],
    },
  ]

  return (
    <Fragment>
      <Button
        type={'primary'}
        onClick={() => {
          setVisible(true)
        }}
      >
        表单联动
      </Button>
      <StepPopup
        steps={steps1}
        current={current}
        layoutType={'vertical'}
        popupTitle={'新建数据中心'}
        visible={visible}
        onCancel={() => {
          setVisible(false)
        }}
        onFinish={() => {
          setVisible(false)
        }}
        isShowResult={true}
      />
    </Fragment>
  )
}
