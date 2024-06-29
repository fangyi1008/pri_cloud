import { Fragment, useState } from 'react'
import MultipleForm from '../'
import { BasicObj, ChangeParamType, FormDataType } from '../type'
import { InboxOutlined } from '@ant-design/icons'
import { message, Table } from 'antd'
import win from '../../selectBoxGroup/demo/icon_os_windows.svg'
import linux from '../../selectBoxGroup/demo/icon_os_linux.svg'

const defaultClassifyData = [
  { value: 'htCloud', label: '宏途' },
  { value: 'testOne', label: '测试一' },
]
const defaultOptionsData = [
  { label: '上课上班', value: 'Apple' },
  { label: 'Pear', value: 'Pear' },
  { label: 'Orange', value: 'Orange', disabled: true },
]
const options = [
  { label: '苹果', value: 'Apple' },
  { label: '梨', value: 'Pear' },
  { label: '橘子', value: 'Orange' },
]
const UploadProps = {
  name: 'file',
  multiple: true,
  action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
  onChange(info: any) {
    const { status } = info.file
    if (status !== 'uploading') {
      console.log(info.file, info.fileList)
    }
    if (status === 'done') {
      message.success(`${info.file.name} file uploaded successfully.`)
    } else if (status === 'error') {
      message.error(`${info.file.name} file upload failed.`)
    }
  },
  onDrop(e: BasicObj) {
    console.log('Dropped files', e.dataTransfer.files)
  },
}
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'id',
    dataIndex: 'clusterId',
    key: 'clusterId',
  },
  {
    title: '分布式资源调度',
    dataIndex: 'drsSwitch',
    key: 'drsSwitch',
  },

  {
    title: '高可用',
    dataIndex: 'haSwitch',
    key: 'haSwitch',
  },

  {
    title: '数据中心',
    dataIndex: 'dataCenterId',
    key: 'dataCenterId',
  },

  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
]

export default function Demo() {
  const formData: FormDataType[] = [
    {
      key: 'name',
      title: '名称',
      type: 'input',
      placeholder: '请输入名称',
      isRequired: true,
    },
    {
      key: 'os',
      title: '操作系统',
      type: 'selectBoxGroup',
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
    {
      key: 'os2',
      title: '操作系统',
      type: 'selectBoxGroup',
      data: [
        {
          label: 'win',
          value: 'win',
        },
        {
          label: 'linux',
          value: 'linux',
        },
      ],
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
    },
    {
      key: 'ps',
      title: '注释',
      type: 'input',
      placeholder: '请选择注释',
    },
    {
      key: 'options',
      title: '单选',
      type: 'radio',
      data: defaultOptionsData,
    },
    {
      key: 'checkbox',
      title: '',
      type: 'checkbox',
      data: options,
    },
    {
      key: 'textarea',
      title: '描述',
      type: 'textarea',
    },
    {
      key: 'upload',
      title: '上传',
      type: 'upload',
      uploadProps: UploadProps,
      render: () => (
        <>
          <p className='ant-upload-drag-icon'>
            <InboxOutlined />
          </p>
          <p
            style={{
              margin: '0 30px 4px',
              color: 'rgba(0, 0, 0, 0.85)',
              fontSize: '16px',
            }}
          >
            点击或者拖拽文件到这个区域来上传
          </p>
          <p className='ant-upload-hint'>支持单个或批量上传</p>
        </>
      ),
    },
    {
      key: 'table',
      title: '',
      type: 'table',
      component: <Table columns={columns} />,
    },
  ]
  const value = {
    name: '',
    desc: '',
    category: [],
    key: '',
    ps: '',
    options: [],
    checkbox: [],
  }
  const [valueObj, setValueObj]: any = useState(value)
  const onChange = (item: ChangeParamType) => {
    const { key, newValue } = item
    setValueObj({ ...valueObj, [key]: newValue })
  }

  return (
    <Fragment>
      <MultipleForm data={formData} onChange={onChange} value={valueObj} />
    </Fragment>
  )
}
