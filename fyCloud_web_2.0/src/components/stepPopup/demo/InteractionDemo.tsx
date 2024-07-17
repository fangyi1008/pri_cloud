import { Fragment, useEffect, useState, useRef } from 'react'
import StepPopup from '../index'
import SearchInput from '@components/searchInput'
import { Form, Button } from 'antd'
import { createTableData } from '../../../common/utils'
import { SearchInputRefType } from '../../searchInput/type'
const { Item } = Form

function SelectNetwork(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const searchRef = useRef<SearchInputRefType>()

  const newWork = Form.useWatch('netWork', form)
  useEffect(() => {
    if (!newWork) return
    const { ip, mtuSize } = newWork
    form.setFieldsValue({ macAddress: ip, MTU: mtuSize })
    refreshResultData(form.getFieldsValue())
  }, [newWork])

  const onSearch = () => {
    setTimeout(() => {
      const tableData = createTableData(
        {
          vmSwitchName: '网络名称',
          mtuSize: '150',
          ip: '192.168.1.291',
          gateway: '192.168.1.1',
          netMask: '255.255.255.0',
        },
        20,
        [],
        'value',
      )
      const value = form.getFieldValue('netWork')?.vmSwitchName
      const { value: key } = tableData.find(item => item.vmSwitchName === value) ?? {}
      searchRef?.current?.openSearchPopup({
        selectedRowKeys: [key],
        sourceData: tableData,
      })
    }, 500)
  }

  return (
    <>
      <Item {...formItemProps}>
        <SearchInput ref={searchRef} {...otherComponentProps} resultIsObject onSearch={onSearch} />
      </Item>
    </>
  )
}

function SelectSecurityGroup(props: any) {
  const { formItemProps, componentProps } = props
  const form = Form.useFormInstance()
  const searchRef = useRef<SearchInputRefType>()

  const onSearch = () => {
    setTimeout(() => {
      const value = form.getFieldValue('securityGroup')
      const tableData = createTableData(
        {
          securityGroupName: '安全组',
          securityGroupMark: '一个很安全的',
        },
        20,
        [],
      )
      const { id: key } = tableData.find(item => item.securityGroupName === value) ?? {}
      searchRef?.current?.openSearchPopup({
        selectedRowKeys: [key],
        sourceData: tableData,
      })
    }, 500)
  }

  return (
    <>
      <Item {...formItemProps}>
        <SearchInput ref={searchRef} {...componentProps} onSearch={onSearch} />
      </Item>
    </>
  )
}

export default function Demo() {
  const [visible, setVisible] = useState(false)
  const [current, setCurrent] = useState(0)
  const [formObject, setFormObject] = useState({})
  const steps1 = [
    {
      key: 'First',
      title: 'First',
      data: [
        {
          id: 'netWork-group',
          content: 'netWork-group',
          type: 'groupFrom',
          groupItems: [
            {
              id: 'netWork',
              content: '网络',
              type: 'custom',
              component: SelectNetwork,
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
                popupTitle: '选择虚拟交换机',
                tableRowKey: 'value',
                valueField: 'vmSwitchName',
                readOnly: true,
                columns: [
                  {
                    title: '名称',
                    dataIndex: 'vmSwitchName',
                    key: 'vmSwitchName',
                  },
                  {
                    title: 'MTU',
                    dataIndex: 'mtuSize',
                    key: 'mtuSize',
                  },
                  {
                    title: 'IP地址',
                    dataIndex: 'ip',
                    key: 'ip',
                  },
                  {
                    title: '网关',
                    dataIndex: 'gateway',
                    key: 'gateway',
                  },
                  {
                    title: '子网掩码',
                    dataIndex: 'netMask',
                    key: 'netMask',
                  },
                ],
              },
            },

            {
              id: 'macAddress',
              content: 'Mac地址11',
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
                step: 1,
              },
            },
            {
              id: 'securityGroup',
              content: '安全组',
              type: 'custom',
              component: SelectSecurityGroup,
              componentProps: {
                allowDelete: true,
                popupTitle: '选择安全组',
                valueField: 'securityGroupName',
                readOnly: true,
                columns: [
                  {
                    title: '名称',
                    dataIndex: 'securityGroupName',
                    key: 'securityGroupName',
                  },
                  {
                    title: '备注',
                    dataIndex: 'securityGroupMark',
                    key: 'securityGroupMark',
                  },
                ],
              },
            },
          ],
        },
        {
          id: 'type',
          content: '迁移类型',
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
                label: '更改主机',
                value: '更改主机',
                describe: '更改主机',
              },
              {
                label: '更改数据存储',
                value: '更改数据存储',
                describe: '更改数据存储',
              },
              {
                label: '更改主机和数据存储',
                value: '更改主机和数据存储',
                describe: '更改主机和数据存储',
              },
            ],
            width: 160,
            direction: 'column',
          },
        },
      ],
    },
  ]

  return (
    <Fragment>
      <Button
        onClick={() => {
          setFormObject({
            netWork: {
              value: 1,
              vmSwitchName: '网络名称1',
              mtuSize: '222',
              ip: '250.250.250.250',
              gateway: '250.250.250.250',
              netMask: '255.255.255.0',
            },
            securityGroup: {
              key: 1,
              securityGroupName: '安全组1',
              securityGroupMark: '一个很安全的',
            },
          })
        }}
      >
        修改formValue
      </Button>
      <Button
        type={'primary'}
        onClick={() => {
          setVisible(true)
        }}
      >
        打开表单
      </Button>
      <StepPopup
        steps={steps1}
        current={current}
        layoutType={'vertical'}
        popupTitle={'新建数据中心'}
        visible={visible}
        formInitialValues={formObject}
        onCancel={() => {
          setVisible(false)
        }}
        onFinish={(data: any) => {
          console.log('onFinish===>', data)
          setVisible(false)
        }}
        onFinishFailed={(data: any) => {
          console.log(data)
        }}
        isShowResult={true}
      />
    </Fragment>
  )
}
