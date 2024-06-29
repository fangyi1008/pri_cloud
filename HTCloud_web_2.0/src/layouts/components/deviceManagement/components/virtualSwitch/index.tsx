import { Form, Select, Checkbox, Radio } from 'antd'
import { useEffect, useState } from 'react'
import { useAppSelector } from '@/redux/store'
import { getVmSwitchPhysicalNetworkList } from '@/api/modules/physicalNetworkService'
const { Item } = Form
const { Group } = Checkbox
const { Group: RadioGroup } = Radio

export function VSSelectHost(props: any) {
  const { formItemProps, componentProps } = props
  const hostInfo = useAppSelector(state => state.hostInfo.info)
  const form = Form.useFormInstance()

  useEffect(() => {
    form.setFieldValue('hostId', hostInfo.hostId)
    componentProps.refreshResultData &&
      componentProps.refreshResultData({ hostId: hostInfo.hostName })
  }, [])
  return (
    <Item {...formItemProps}>
      <Select options={[{ value: hostInfo.hostId, label: hostInfo.hostName }]} disabled={true} />
    </Item>
  )
}

export function LinkModeAndLoadMode(props: any) {
  const [isShow, setIsShow] = useState(false)
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherProps } = componentProps
  const form = Form.useFormInstance()
  const networkType = Form.useWatch('netMachine', form)
  useEffect(() => {
    setIsShow(networkType && networkType.length > 1)
  }, [networkType])
  return isShow ? (
    <Item {...formItemProps}>
      <RadioGroup {...otherProps} />
    </Item>
  ) : null
}

export function SelectNetMachine(props: any) {
  const { formItemProps } = props
  const [options, setOptions] = useState<{ value: string; label: string }[]>([])
  const hostInfo = useAppSelector(state => state.hostInfo.info)

  const getNetMachine = async () => {
    const list = await getVmSwitchPhysicalNetworkList(hostInfo.hostId)
    const optionsList = list.map(item => ({ value: item, label: item }))
    setOptions(optionsList)
  }
  useEffect(() => {
    getNetMachine()
  }, [])
  return (
    <Item {...formItemProps}>
      {options.length > 0 ? <Group options={options} /> : <div>无可用端口</div>}
    </Item>
  )
}
