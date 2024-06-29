import {
  getSelectStorageData,
  storagePoolTargetByIp,
  unFormatDev,
} from '@/api/modules/storagePools'
import { BasicObj } from '@/components/multipleForm/type'
import { Form, Input, Radio, Select } from 'antd'
import { RefObject, useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router'
import baseComponents from '@/components/multipleFormItem/commonAariable'
import { DeleteDataCenterWrap } from '../deleteModal/style'
import { ExclamationCircleOutlined } from '@ant-design/icons'
import { UnitNumberValueType } from '@/components/unitNumberInput/type'
import UnitNumberInput from '@/components/unitNumberInput'
import SearchInput from '@/components/searchInput'
import { SearchOutlined, ReloadOutlined } from '@common/antdIcons'
import { SearchInputRefType, SearchInputValueType } from '@/components/searchInput/type'
import { PoolFileOperationType, PoolListDataType } from '@/components/selectPoolsPopup/type'
import { selectBasicVol } from '@/api/modules/storagePools'
import DetailInfoPopup from '@/components/detailInfoPopup'
import { BulbTwoTone } from '@common/antdIcons'
import { WarnWrap } from './style'
import { DefaultOptionType } from 'antd/lib/select'
import eventEmitter from '@/events/index'

export function SetStoragePoolPath(props: any) {
  const { formItemProps, componentProps } = props
  const [elementType, setTElementType] = useState<{ type: string; componentProps: BasicObj }>({
    type: 'input',
    componentProps: {},
  })
  const { Item } = Form
  const { id: hostId } = useParams()
  const form = Form.useFormInstance()
  const poolType = form.getFieldValue('poolType')
  const storagePoolName = form.getFieldValue('storagePoolName')
  const getData = async (poolType: string) => {
    form.setFieldValue('storagePoolPath', undefined)
    if (poolType === 'lvm') {
      const id = hostId ? hostId : form.getFieldValue('hostId')
      const result = await unFormatDev(id)
      const { msg, code, unFormatList } = result || {}
      if (msg === 'success' && code === 0) {
        const options = unFormatList ? unFormatList.map(item => ({ value: item, label: item })) : []
        form.setFieldValue('storagePoolPath', options?.[0]?.label)
        setTElementType({ type: 'select', componentProps: { options, disabled: false } })
      }
    } else if (poolType === 'dir') {
      form.setFieldValue('storagePoolPath', '/htcloud/storagePool/' + storagePoolName)
      setTElementType({ type: 'input', componentProps: {} })
    } else {
      form.setFieldValue('storagePoolPath', '/dev/disk/by-path')
      setTElementType({ type: 'input', componentProps: {} })
    }
  }
  useEffect(() => {
    getData(poolType)
  }, [poolType])
  const Element = baseComponents[elementType.type] || null
  return (
    <Item {...formItemProps}>
      <Element {...componentProps} {...elementType.componentProps} />
    </Item>
  )
}

export function SetFormat(props: any) {
  const { formItemProps, componentProps } = props
  const { Item } = Form
  const form = Form.useFormInstance()
  const [show, setShow] = useState<boolean>(false)
  const isFormdata = form.getFieldValue('isFormdata')
  useEffect(() => {
    setShow(isFormdata?.length === 1)
  }, [isFormdata])
  return show ? (
    <Item {...formItemProps}>
      <Radio.Group {...componentProps} />
    </Item>
  ) : null
}

export function SetIp(props: any) {
  const { formItemProps, componentProps } = props
  const { Item } = Form
  const form = Form.useFormInstance()
  const blur = () => {
    const ip = form.getFieldValue('storageIp')
    eventEmitter.emit('getOptions', ip)
  }
  return (
    <Item {...formItemProps}>
      <Input onBlur={blur} {...componentProps} />
    </Item>
  )
}

export const SetIqnList = (props: any) => {
  const { formItemProps, componentProps } = props
  const { Item } = Form
  const form = Form.useFormInstance()
  const [options, setOptions] = useState<{ value: string; label: string }[]>([])
  const { id: hostId } = useParams()
  const getData = async (ip: string) => {
    const id = hostId ? hostId : form.getFieldValue('hostId')
    const result = await storagePoolTargetByIp({ ip, hostId: id })
    const { msg, code, discoveryResult, iqnFormatList } = result || {}
    if (msg === 'success' && code === 0) {
      const options = iqnFormatList
        ? iqnFormatList.map((item: string) => ({
            label: item.slice(0, -1),
            value: item.slice(0, -1),
          }))
        : []
      setOptions(options)
      form.setFieldValue('iqn', discoveryResult.slice(0, -1))
    }
  }

  form.setFieldValue('iqnFormat', options?.[0]?.value)
  useEffect(() => {
    // 注册事件
    eventEmitter.addListener('getOptions', getData)
    return () => {
      eventEmitter.off('getOptions', getData)
      // 移除注册的事件
    }
  }, [])

  return (
    <Item {...formItemProps}>
      <Select options={options} placeholder={'请选择磁盘'} />
    </Item>
  )
}

export const SetIqnFormat = (props: any) => {
  const { formItemProps, componentProps } = props
  const { Item } = Form
  const { data } = componentProps
  let list: DefaultOptionType[] = []
  const optionList = async () => {
    const { iqnFormatList } = data ? await storagePoolTargetByIp(data) : { iqnFormatList: [] }
    const optionsData =
      iqnFormatList.length !== 0
        ? iqnFormatList.map((item: string) => ({
            label: item.slice(0, -1),
            value: item.slice(0, -1),
          }))
        : []
    list = [...optionsData]
  }
  useEffect(() => {
    optionList()
  }, [])

  return (
    <Item {...formItemProps}>
      <Select options={list} />
    </Item>
  )
}

export function SetAlert() {
  return (
    <DeleteDataCenterWrap>
      <ExclamationCircleOutlined style={{ fontSize: '38px', color: 'red', marginRight: '20px' }} />
      {'您确定要格式化吗'}
    </DeleteDataCenterWrap>
  )
}

export function SetDelete() {
  return (
    <DeleteDataCenterWrap>
      <ExclamationCircleOutlined style={{ fontSize: '38px', color: 'red', marginRight: '20px' }} />
      {'您确定要删除该存储卷吗'}
    </DeleteDataCenterWrap>
  )
}

export function SetWarn() {
  return (
    <WarnWrap>
      <BulbTwoTone style={{ marginRight: '10px' }} />
      {'输入ip获取iqn和磁盘数据'}
    </WarnWrap>
  )
}

export function SetCapacity(props: any) {
  const { formItemProps } = props
  const { Item } = Form
  const [value, setValue] = useState<UnitNumberValueType>({
    unitValue: 'G',
  })
  const option = [
    { label: 'GB', value: 'G' },
    { label: 'TB', value: 'T' },
    { label: 'MB', value: 'M' },
  ]
  const onChange = (target: UnitNumberValueType) => {
    const { inputValue, unitValue = '' } = target
    setValue({ inputValue, unitValue })
  }
  return (
    <Item {...formItemProps}>
      <UnitNumberInput value={value} allowDelete={false} unitOptions={option} onChange={onChange} />
    </Item>
  )
}

export const SetBasicVolumePath = (props: any) => {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const { Item } = Form
  const form = Form.useFormInstance()
  const createFormat = Form.useWatch('createFormat')
  const [show, setShow] = useState<boolean>(true)
  const [detailData, setDetailData] = useState<{ visible: boolean; data: BasicObj }>({
    visible: false,
    data: {},
  })
  const { id: hostId } = useParams()
  const onDelete = () => {
    setValue('')
    form.setFieldValue('basicVolumePath', undefined)
    form.setFieldValue('basicVolumeId', undefined)
  }

  useEffect(() => {
    createFormat === 'qcow2' ? setShow(true) : setShow(false)
  }, [createFormat])

  const [value, setValue] = useState<any>(false)
  const selectPoolIdRef = useRef<string>('')
  const searchRef: RefObject<SearchInputRefType> = useRef(null)
  const operations: PoolFileOperationType[] = [
    { type: 'icon', icon: <ReloadOutlined />, title: '刷新' },
    { type: 'icon', icon: <SearchOutlined />, title: '详情' },
  ]

  const onSearch = async () => {
    selectPoolIdRef.current = ''
    await getStorageData({
      page: '1',
      limit: '10',
    })
  }

  const onChange = (value?: SearchInputValueType) => {
    if (typeof value === 'object') {
      if (Reflect.has(value, 'storageId') && Reflect.has(value, 'storagePath')) {
        form.setFieldValue('basicVolumePath', value.storagePath)
        form.setFieldValue('basicVolumeId', value.storageId)
      } else {
        form.setFieldValue('basicVolumePath', '')
        form.setFieldValue('basicVolumeId', '')
      }
    }
    value && setValue(value)
  }

  const onPoolSelect = async (data: any) => {
    const { storagePoolId } = data
    selectPoolIdRef.current = storagePoolId
    const {
      currPage = 1,
      pageSize = 10,
      totalCount = 0,
      list,
      storagePoolList,
    } = (await getSelectStorageData({
      hostId,
      storagePoolId,
      status: 1,
      page: '1',
      limit: '10',
    })) ?? {}
    searchRef?.current?.openSearchPopup({
      selectedRowKeys: [],
      sourceData: list,
      selectedPoolKey: storagePoolId,
      poolListData: storagePoolList,
      pageInfo: {
        current: currPage,
        total: totalCount,
        pageSize,
      },
    })
  }

  const onOperationClick = (item: PoolFileOperationType, data: PoolListDataType) => {
    const { title } = item
    if (title === '详情') {
      setDetailData({ visible: true, data })
    } else {
      getStorageData({ page: '1', limit: '10' })
    }
  }

  const getStorageData = async (pageInfo: any) => {
    const selectedStoragePoolId = selectPoolIdRef.current
    const param = selectPoolIdRef.current
      ? {
          hostId,
          storagePoolId: selectedStoragePoolId,
          status: 1,
          ...pageInfo,
        }
      : {
          hostId,
          status: 1,
          ...pageInfo,
        }
    const {
      list = [],
      totalCount = 0,
      pageSize = 10,
      currPage = 1,
      storagePoolList,
    } = (await getSelectStorageData(param)) ?? {}
    const { storagePoolId } = storagePoolList[0] || { storagePoolId: '' }
    searchRef?.current?.openSearchPopup({
      selectedRowKeys: [],
      sourceData: list,
      selectedPoolKey: storagePoolId,
      poolListData: storagePoolList,
      pageInfo: {
        current: currPage,
        total: totalCount,
        pageSize,
      },
    })
  }

  const onTableChange = async (pageInfo: any) => {
    const { current, pageSize } = pageInfo
    await getStorageData({ page: `${current}`, limit: `${pageSize}` })
  }
  const onCheck = async (data: any): Promise<boolean> => {
    const { storageId } = data
    return selectBasicVol(storageId)
  }

  const fieldNames = {
    storagePoolName: '名称',
    poolShowName: '显示名称',
    poolType: '类型',
    storagePoolPath: '目标路基',
    capacity: '总容量',
    usedSpace: '已分配容量',
    freeSpace: '实际可用容量',
    status: '状态',
  }
  const { visible, data } = detailData
  return show ? (
    <Item {...formItemProps}>
      <SearchInput
        {...otherComponentProps}
        ref={searchRef}
        onSearch={onSearch}
        onPoolSelect={onPoolSelect}
        onTableChange={onTableChange}
        onOperationClick={onOperationClick}
        onChange={onChange}
        operations={operations}
        onDelete={onDelete}
        value={value}
        onCheck={onCheck}
      />
      <DetailInfoPopup
        onCancel={() => {
          setDetailData({ visible: false, data: [] })
        }}
        bodyStyle={{ height: '290px', padding: '0' }}
        visible={visible}
        title={'详情数据'}
        data={data}
        fieldNames={fieldNames}
      />
    </Item>
  ) : null
}

export function SetBasicVolumeId(props: any) {
  const { formItemProps, componentProps } = props
  const { Item } = Form
  const createFormat = Form.useWatch('createFormat')
  const [show, setShow] = useState<boolean>(true)
  useEffect(() => {
    createFormat === 'qcow2' ? setShow(true) : setShow(false)
  }, [createFormat])

  return show ? (
    <Item {...formItemProps}>
      <Input {...componentProps} />
    </Item>
  ) : null
}
