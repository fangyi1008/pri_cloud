import { Fragment, useState } from 'react'
import StepPopup from '../index'
import { ButtonConfirmSpan } from '../../multipleForm/style'
import { FormDataType } from '../../multipleFormItem/type'
import win from '../../selectBoxGroup/demo/icon_os_windows.svg'
import linux from '../../selectBoxGroup/demo/icon_os_linux.svg'

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const [horizontalVisible, setHorizontalVisible] = useState(false)
  const [current, setCurrent] = useState(0)
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

  const twoFormData: FormDataType[] = [
    {
      id: 'cpu-group',
      content: 'cpu-group',
      type: 'groupFrom',
      groupItems: [
        {
          id: 'cpu',
          content: 'cpu',
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
            allowDelete: false,
            unitOptions: [{ a: '个', b: '个' }],
            min: 1,
            max: 4,
            step: 1,
            fieldNames: { label: 'a', value: 'b' },
          },
        },
        {
          id: 'unitNumber',
          content: 'cpu核数',
          type: 'unitNumber',
          formItemProps: {
            required: true,
            rules: [
              {
                required: true,
              },
              {
                validator: (_: any, value) => {
                  if (!value) {
                    return Promise.reject()
                  }
                  const { inputValue, unitValue, UnitObject } = value
                  if (!inputValue || !unitValue) {
                    return Promise.reject()
                  }
                  return Promise.resolve()
                },
              },
            ],
          },
          componentProps: {
            unitOptions: [{ a: '核', b: '核' }],
            min: 1,
            max: 4,
            step: 1,
            fieldNames: { label: 'a', value: 'b' },
            allowDelete: false,
          },
        },
      ],
    },
    {
      id: 'Memory',
      content: '内存',
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
        allowDelete: false,
        unitOptions: [
          { a: 'GB', b: 'GB' },
          { a: 'MB', b: 'MB' },
        ],
        min: 1,
        max: 60,
        step: 1,
        fieldNames: { label: 'a', value: 'b' },
      },
    },
    {
      id: 'netWork-group',
      content: 'netWork-group',
      type: 'groupFrom',
      groupItems: [
        {
          id: 'netWork',
          content: '网络',
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
            onSearch: () => {
              console.log('onSearch')
            },
            allowDelete: false,
          },
        },
        {
          id: 'searchInput',
          content: '安全组',
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
            onSearch: () => {
              console.log('onSearch')
            },
            allowDelete: false,
          },
        },
        {
          id: 'netWorkType',
          content: '网络类型',
          type: 'select',
          componentProps: {
            options: [
              { value: '高速网卡', label: '高速网卡' },
              { value: '普通网卡', label: '普通网卡' },
            ],
            placeholder: '请选择网卡类型',
          },
        },
        {
          id: 'macAddress',
          content: 'Mac地址',
          type: 'input',
          formItemProps: {
            rules: [
              {
                pattern: /^([0-9a-fA-F]{2})(([/\s:-][0-9a-fA-F]{2}){5})$/,
                message: '请输入正确的MAC地址！',
              },
            ],
          },
          componentProps: {
            placeholder: '请输入Mac地址',
          },
        },
        {
          id: 'MTU',
          content: 'MTU',
          type: 'inputNumber',
          formItemProps: {
            required: true,
            rules: [
              {
                required: true,
              },
            ],
          },
          componentProps: {
            style: { width: '100%' },
            placeholder: '请输入MTU',
            min: 1,
            max: 4,
            step: 1,
          },
        },
      ],
    },
    {
      id: 'hardDisk-group',
      content: 'hardDisk-group',
      type: 'groupFrom',
      groupItems: [
        {
          id: 'hardDisk',
          content: '硬盘',
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
            allowDelete: false,
            unitOptions: [
              { a: 'GB', b: 'GB' },
              { a: 'TB', b: 'TB' },
              { a: 'MB', b: 'MB' },
            ],
            min: 1,
            max: 1000,
            step: 1,
            fieldNames: { label: 'a', value: 'b' },
          },
        },
      ],
    },
    {
      id: 'CD-group',
      content: 'CD-group',
      type: 'groupFrom',
      groupItems: [
        {
          id: 'hardDisk',
          content: '光驱',
          type: 'unitNumber',
          componentProps: {
            allowDelete: false,
            unitOptions: [
              { a: 'GB', b: 'GB' },
              { a: 'TB', b: 'TB' },
              { a: 'MB', b: 'MB' },
            ],
            min: 1,
            max: 1000,
            step: 1,
            fieldNames: { label: 'a', value: 'b' },
          },
        },
      ],
    },
  ]

  const steps = [
    {
      key: '1',
      title: '基本信息',
      data: oneFormData,
    },
    {
      key: '2',
      title: '硬件信息',
      data: twoFormData,
    },
    {
      key: '3',
      title: '硬件信息',
      data: [
        {
          id: 'Memory1',
          content: '内存',
          type: 'unitNumber',
          formItemProps: {
            required: true,
            rules: [
              {
                required: true,
              },
            ],
          },
        },
      ],
    },
  ]

  const formInitialValues = {
    name: '',
    describe: '',
    os: 'win',
    version: 'htCloud',
    cpu: { inputValue: 1, unitValue: '个' },
    unitNumber: { inputValue: 2, unitValue: '核' },
  }

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
      <StepPopup
        onValuesChange={(a: any, b: any) => {
          console.log(a, b)
        }}
        onStepChange={(current: number) => {
          setCurrent(current)
        }}
        steps={steps}
        current={current}
        layoutType={'vertical'}
        popupTitle={'新建数据中心'}
        resultTitle={'新建虚拟机'}
        visible={visible}
        isShowResult={false}
        formInitialValues={formInitialValues}
        resultData={formInitialValues}
        onCancel={() => {
          setVisible(false)
        }}
        onFinish={(data: any) => {
          console.log(data)
          setVisible(false)
        }}
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

      <StepPopup
        onStepChange={(current: number) => {
          setCurrent(current)
        }}
        steps={steps}
        current={current}
        layoutType={'horizontal'}
        popupTitle={'新建数据中心'}
        resultTitle={'新建虚拟机'}
        visible={horizontalVisible}
        formInitialValues={formInitialValues}
        resultData={formInitialValues}
        isShowResult={true}
        onCancel={() => {
          setHorizontalVisible(false)
        }}
        onFinish={(data: any) => {
          console.log(data)
          setHorizontalVisible(false)
        }}
      />
    </Fragment>
  )
}
