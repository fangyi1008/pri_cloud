import { useEffect, useRef, useState } from 'react'
import { Form, Select, Tree } from 'antd'
import { TreeProps, DataNode } from 'antd/lib/tree'
import { SearchInputRefType } from '@components/searchInput/type'
import SearchInput from '@components/searchInput'
import UnitNumberInput from '@components/unitNumberInput'
import { PoolFileOperationType, PoolListDataType } from '@components/selectPoolsPopup/type'
import { SearchOutlined, CaretDownOutlined } from '@ant-design/icons'
import { useAppSelector } from '@/redux/store'
import {
  getHostMemory,
  getSecurityGroupsData,
  getSelectStorageInfo,
  GetSelectStorageParam,
  getStoragePoolList,
  getVirtualSwitchData,
  getVMRandomMac,
  moveVmList,
  selectBasicVol,
  selectCdIso,
} from '@/api/modules/virtualMachineService'
import cluster from '@/assets/icons/cluster.svg'
import dataCenter from '@/assets/icons/data_center.svg'
import hostClose from '@/assets/icons/host_close.svg'
import hostOpen from '@/assets/icons/host_open.svg'

const { Item } = Form
const windowVersion = [
  {
    value: 'Microsoft Windows Server 2016(64位)',
    label: 'Microsoft Windows Server 2016(64位)',
  },
  {
    value: 'Microsoft Windows Server 2012 R2(64位)',
    label: 'Microsoft Windows Server 2012 R2(64位)',
  },
  {
    value: 'Microsoft Windows Server 2012(64位)',
    label: 'Microsoft Windows Server 2012(64位)',
  },
  {
    value: 'Microsoft Windows Server 2008 R2(64位)',
    label: 'Microsoft Windows Server 2008 R2(64位)',
  },
  {
    value: 'Microsoft Windows Server 2008(64位)',
    label: 'Microsoft Windows Server 2008(64位)',
  },
  {
    value: 'Microsoft Windows Server 2008(32位)',
    label: 'Microsoft Windows Server 2008(32位)',
  },
  {
    value: 'Microsoft Windows Server 2003 R2(64位)',
    label: 'Microsoft Windows Server 2003 R2(64位)',
  },
]
const linuxVersion = [
  {
    value: 'Red Hat Enterprise Linux 7(64位)',
    label: 'Red Hat Enterprise Linux 7(64位)',
  },
  {
    value: 'Red Hat Enterprise Linux 7(32位)',
    label: 'Red Hat Enterprise Linux 7(32位)',
  },
  {
    value: 'Red Hat Enterprise Linux 6(64位)',
    label: 'Red Hat Enterprise Linux 6(64位)',
  },
  {
    value: 'Red Hat Enterprise Linux 6(32位)',
    label: 'Red Hat Enterprise Linux 6(32位)',
  },
  {
    value: 'Red Hat Enterprise Linux 5.10(64位)',
    label: 'Red Hat Enterprise Linux 5.10(64位)',
  },
  {
    value: 'Red Hat Enterprise Linux 5.10(32位)',
    label: 'Red Hat Enterprise Linux 5.10(32位)',
  },
]

export function createTableData(
  baseObject: { [key: string]: string },
  count: number,
  ignore: string[],
  key = 'id',
  indexStart = 1,
) {
  const array: { [key: string]: string }[] = []
  for (let i = indexStart; i < indexStart + count; i++) {
    const obj: { [key: string]: any } = { [key]: i }
    Object.keys(baseObject).forEach(key => {
      if (!ignore.includes(key)) {
        obj[key] = baseObject[key] + i
      }
    })
    array.push({ ...baseObject, ...obj })
  }
  return array
}

export function OsVersion(props: any) {
  const { formItemProps, componentProps } = props
  const [options, setOptions] = useState<{ value: string }[]>([])
  const form = Form.useFormInstance()
  const os = Form.useWatch('os', form)

  useEffect(() => {
    const data = os === 'win' ? windowVersion : linuxVersion
    const defaultValue = data[0]?.value ?? ''
    setOptions(data)
    form.setFieldsValue({ version: defaultValue })
  }, [os])
  return (
    <Item {...formItemProps}>
      <Select options={options} />
    </Item>
  )
}

export function SelectNetwork(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const searchRef = useRef<SearchInputRefType>()
  const { name } = formItemProps
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)
  const newWork = Form.useWatch('vmSwitch', form)

  useEffect(() => {
    if (!newWork) return
    const { gateway, ip, mtuSize, netMask } = newWork
    form.setFieldValue('MTU', mtuSize)
    refreshResultData({ MTU: mtuSize })
  }, [newWork])

  const getVirtualSwitch = async (pageInfo: any) => {
    const { page, limit } = pageInfo
    const data = await getVirtualSwitchData({ page, limit, hostId: hostInfo.hostId })
    const { list = [], totalCount = 0, pageSize = 10, currPage = 1 } = data ?? {}
    const value = form.getFieldValue('vmSwitch')?.vmSwitchName
    const { vmSwitchId: key } = list.find((item: any) => item.vmSwitchName === value) ?? {}
    searchRef?.current?.openSearchPopup({
      selectedRowKeys: [key],
      sourceData: list,
      pageInfo: {
        current: currPage,
        total: totalCount,
        pageSize,
      },
    })
  }

  const onSearch = async () => {
    await getVirtualSwitch({ page: '1', limit: '10' })
  }

  const onTableChange = async (pageInfo: any) => {
    const { current, pageSize } = pageInfo
    await getVirtualSwitch({ page: `${current}`, limit: `${pageSize}` })
  }

  return (
    <>
      <Item {...formItemProps}>
        <SearchInput
          ref={searchRef}
          {...otherComponentProps}
          resultIsObject
          onSearch={onSearch}
          onTableChange={onTableChange}
        />
      </Item>
    </>
  )
}

export function VMMac(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const { name } = formItemProps

  const onSearch = async () => {
    const result = await getVMRandomMac()
    console.log()
    const { mac = '' } = result || {}
    form.setFieldsValue({ vmNetworkMac: mac })
    refreshResultData({ vmNetworkMac: mac })
  }

  return (
    <>
      <Item {...formItemProps}>
        <SearchInput {...otherComponentProps} onSearch={onSearch} />
      </Item>
    </>
  )
}

export function SelectSecurityGroup(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const searchRef = useRef<SearchInputRefType>()

  const getSecurityGroupData = async (pageInfo: any) => {
    const securityGroup = await getSecurityGroupsData(pageInfo)
    const { list = [], totalCount = 0, pageSize = 10, currPage = 1 } = securityGroup ?? {}
    const { securityGroupId: key } = form.getFieldValue('securityGroup') || {}
    searchRef?.current?.openSearchPopup({
      selectedRowKeys: [key],
      sourceData: list,
      pageInfo: {
        current: currPage,
        total: totalCount,
        pageSize,
      },
    })
  }

  const onSearch = async () => {
    await getSecurityGroupData({ page: '1', limit: '10' })
  }

  const onTableChange = async (pageInfo: any) => {
    const { current, pageSize } = pageInfo
    await getSecurityGroupData({ page: `${current}`, limit: `${pageSize}` })
  }
  return (
    <>
      <Item {...formItemProps}>
        <SearchInput
          ref={searchRef}
          {...otherComponentProps}
          onSearch={onSearch}
          onTableChange={onTableChange}
        />
      </Item>
    </>
  )
}

export function SelectMem(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)
  const [maxMemory, setMaxMemory] = useState<number>(10)
  const form = Form.useFormInstance()
  const vmMemSize = Form.useWatch('vmMemSize', form)
  const memoryUnitRef = useRef<string>('')

  const computeUnitMax = async (type: string) => {
    const memory = await getHostMemory(hostInfo.hostId || '')
    const memorySize = Math.floor(type === 'GB' ? memory / 2 / 1024 / 1024 : memory / 2 / 1024)
    setMaxMemory(memorySize)
  }
  useEffect(() => {
    if (!vmMemSize) return
    const { unitValue } = vmMemSize
    if (memoryUnitRef.current !== unitValue) {
      memoryUnitRef.current = unitValue
      computeUnitMax(unitValue || 'GB')
    }
  }, [vmMemSize])

  return (
    <>
      <Item {...formItemProps}>
        <UnitNumberInput
          {...otherComponentProps}
          disabled={hostInfo.vmState === '运行'}
          max={maxMemory}
        />
      </Item>
    </>
  )
}

export function SelecctCpu(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)
  return (
    <>
      <Item {...formItemProps}>
        <UnitNumberInput {...otherComponentProps} disabled={hostInfo.vmState === '运行'} />
      </Item>
    </>
  )
}

export function UserHardDisk(
  init: boolean,
  dependencies: string | number,
  eqFn: (value: string | number) => boolean,
  OperationFn?: () => void,
) {
  const [isShow, setIsShow] = useState<boolean>(init)
  useEffect(() => {
    if (!dependencies) return
    OperationFn && OperationFn()
    if (eqFn(dependencies)) {
      setIsShow(true)
      return
    }
    setIsShow(false)
  }, [dependencies])
  return { isShow }
}

function PoolInfoPopup(props: any) {}

export function HardDiskFile(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const hardDiskType = Form.useWatch('diskCreateType', form)
  const { isShow } = UserHardDisk(false, hardDiskType, value => [2, 3].includes(value as number))
  const poolSearchRef = useRef<SearchInputRefType>(null)
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)
  const operations: PoolFileOperationType[] = [
    // { type: 'icon', icon: <SearchOutlined />, title: '详情' },
  ]
  const selectPoolIdRef = useRef<string>('')
  const onPoolSearch = async () => {
    selectPoolIdRef.current = ''
    await getPoolFileData({ page: '1', limit: '10' })
  }

  const onPoolSelect = async (data: any) => {
    const { storagePoolId } = data
    selectPoolIdRef.current = storagePoolId
    const { hostId } = hostInfo
    const fileInfo = form.getFieldValue('basicVolume')
    const { storageId = '' } = fileInfo ?? {}
    const { page, storagePoolList = [] } =
      (await getSelectStorageInfo({
        poolType: 'dir',
        hostId,
        storagePoolId,
        status: 1,
        page: '1',
        limit: '10',
      })) ?? {}
    const { list, totalCount = 0, pageSize = 10, currPage = 1 } = page || {}
    poolSearchRef?.current?.openSearchPopup({
      selectedRowKeys: storageId ? [storageId] : [],
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

  const getPoolFileData = async (pageInfo: any) => {
    const { hostId } = hostInfo
    const fileInfo = form.getFieldValue('basicVolume')
    const { storagePoolId = '', storageId = '' } = fileInfo || {}
    const selectedStoragePoolId = selectPoolIdRef.current || storagePoolId
    const param: GetSelectStorageParam =
      fileInfo || selectPoolIdRef.current
        ? {
            poolType: 'dir',
            hostId,
            status: 1,
            storagePoolId: selectedStoragePoolId,
            ...pageInfo,
          }
        : {
            poolType: 'dir',
            hostId,
            status: 1,
            ...pageInfo,
          }
    const { page, storagePoolList = [] } = (await getSelectStorageInfo(param)) ?? {}
    const { storagePoolId: key } = storagePoolList[0]
    const { list = [], totalCount = 0, pageSize = 10, currPage = 1 } = page || {}
    poolSearchRef?.current?.openSearchPopup({
      selectedRowKeys: storageId ? [storageId] : [],
      sourceData: list,
      selectedPoolKey: selectedStoragePoolId || key,
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
    await getPoolFileData({ page: `${current}`, limit: `${pageSize}` })
  }

  const onOperationClick = (item: PoolFileOperationType, data: PoolListDataType) => {}

  const onCheck = async (data: any): Promise<boolean> => {
    const { storageId } = data
    return selectBasicVol(storageId)
  }

  return (
    <>
      {isShow && (
        <Item {...formItemProps}>
          <SearchInput
            {...otherComponentProps}
            ref={poolSearchRef}
            operations={operations}
            onSearch={onPoolSearch}
            onPoolSelect={onPoolSelect}
            onOperationClick={onOperationClick}
            onTableChange={onTableChange}
            onCheck={onCheck}
          />
        </Item>
      )}
    </>
  )
}

export function HardDiskPool(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, isShow: isShowInput = false, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const hardDiskType = Form.useWatch('diskCreateType', form)
  const { isShow } = UserHardDisk(isShowInput, hardDiskType, value => value === 1)
  const fileSearchRef = useRef<SearchInputRefType>(null)
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)

  const getStoragePoolData = async (pageInfo: any) => {
    const { page, limit } = pageInfo
    const result = await getStoragePoolList({
      page,
      limit,
      hostId: hostInfo.hostId,
      status: 1,
    })
    const { list = [], totalCount, currPage, pageSize } = result ?? {}
    const { storagePoolId = '' } = form.getFieldValue('storagePool') ?? {}
    fileSearchRef?.current?.openSearchPopup({
      sourceData: list,
      selectedRowKeys: [storagePoolId],
      pageInfo: {
        current: currPage,
        total: totalCount,
        pageSize,
      },
    })
  }

  const onFileSearch = async () => {
    await getStoragePoolData({ page: '1', limit: '10' })
  }

  const onTableChange = async (pageInfo: any) => {
    const { current, pageSize } = pageInfo
    await getStoragePoolData({ page: `${current}`, limit: `${pageSize}` })
  }

  return (
    <>
      {isShow && (
        <Item {...formItemProps}>
          <SearchInput
            {...otherComponentProps}
            ref={fileSearchRef}
            onSearch={onFileSearch}
            onTableChange={onTableChange}
          />
        </Item>
      )}
    </>
  )
}

export function SelectCD(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()

  const cdSearchRef = useRef<SearchInputRefType>(null)
  const hostInfo = useAppSelector(state => state.vmOperate.edit.initialValues)
  const selectPoolIdRef = useRef<string>('')

  const onPoolSearch = async () => {
    selectPoolIdRef.current = ''
    await getCdFileData({ page: '1', limit: '10' })
  }

  const onPoolSelect = async (data: any) => {
    const { storagePoolId } = data
    selectPoolIdRef.current = storagePoolId
    const { hostId } = hostInfo
    const fileInfo = form.getFieldValue('driveCD')
    const { storageId = '' } = fileInfo ?? {}
    const { page, storagePoolList = [] } =
      (await getSelectStorageInfo({
        hostId,
        storagePoolId,
        status: 1,
        page: '1',
        limit: '10',
      })) ?? {}
    const { list, totalCount = 0, pageSize = 10, currPage = 1 } = page || {}
    cdSearchRef?.current?.openSearchPopup({
      selectedRowKeys: storageId ? [storageId] : [],
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

  const getCdFileData = async (pageInfo: any) => {
    const { hostId } = hostInfo
    const fileInfo = form.getFieldValue('driveCD')
    const { storagePoolId = '', storageId = '' } = fileInfo || {}
    const selectedStoragePoolId = selectPoolIdRef.current || storagePoolId
    const param: GetSelectStorageParam =
      fileInfo || selectPoolIdRef.current
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
    const { page, storagePoolList = [] } = (await getSelectStorageInfo(param)) ?? {}
    const { storagePoolId: key } = storagePoolList[0] || {}
    const { list = [], totalCount = 0, pageSize = 10, currPage = 1 } = page || {}
    cdSearchRef?.current?.openSearchPopup({
      selectedRowKeys: storageId ? [storageId] : [],
      sourceData: list,
      selectedPoolKey: selectedStoragePoolId || key,
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
    await getCdFileData({ page: `${current}`, limit: `${pageSize}` })
  }

  const onCheck = async (data: any): Promise<boolean> => {
    const { storageId } = data
    return selectCdIso(storageId)
  }

  return (
    <>
      <Item {...formItemProps}>
        <SearchInput
          {...otherComponentProps}
          ref={cdSearchRef}
          onSearch={onPoolSearch}
          onPoolSelect={onPoolSelect}
          onTableChange={onTableChange}
          onCheck={onCheck}
        />
      </Item>
    </>
  )
}

export function CDDriver(props: any) {
  const { formItemProps, componentProps } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()
  const linkType = Form.useWatch('linkType', form)
  const { isShow } = UserHardDisk(
    false,
    linkType,
    value => value === '镜像文件',
    () => {
      const aa = linkType === '镜像文件' ? { driveCD: '' } : { driveCD: '/dev/cdrom' }
      form.setFieldsValue(aa)
      refreshResultData({ driveCD: '/dev/cdrom' })
    },
  )
  const cdSearchRef = useRef<SearchInputRefType>(null)

  const operations: PoolFileOperationType[] = [
    { type: 'icon', icon: <SearchOutlined />, title: '删除' },
    { type: 'icon', icon: <SearchOutlined />, title: '添加' },
  ]

  const onPoolSearch = () => {
    const tableData = createTableData(
      {
        poolShowName: '数据池',
        poolType: '本地数据池',
        storagePoolPath: '/dim/data/data',
        size: '200GM',
        status: '活动状态',
      },
      20,
      ['poolType', 'storagePoolPath', 'size', 'status'],
      'id',
    )
    const { id, poolShowName } = tableData[0]

    const sourceData = createTableData(
      {
        fileName: `${poolShowName}-数据池`,
        fileSize: '200GM',
        type: '文件',
      },
      20,
      ['fileSize', 'type'],
      'id',
    )
    const { id: key } = tableData[0]

    cdSearchRef?.current?.openSearchPopup({
      sourceData,
      selectedRowKeys: [key],
      poolListData: tableData,
      selectedPoolKey: id,
    })
  }

  const onPoolSelect = (data: any) => {
    const { poolName, id: key } = data
    const tableData = createTableData(
      {
        vmSwitchName: `${poolName}-网络名称`,
        mtuSize: '150',
        ip: '192.168.1.291',
        gateway: '192.168.1.1',
        netMask: '255.255.255.0',
      },
      20,
      [],
      'value',
    )
    cdSearchRef?.current?.openSearchPopup({
      selectedRowKeys: [],
      sourceData: tableData,
      selectedPoolKey: key,
    })
  }

  const onOperationClick = (item: PoolFileOperationType, data: PoolListDataType) => {
    console.log('item', data)
  }

  return (
    <>
      <Item {...formItemProps}>
        {isShow ? (
          <SearchInput
            {...otherComponentProps}
            ref={cdSearchRef}
            operations={operations}
            onSearch={onPoolSearch}
            onPoolSelect={onPoolSelect}
            onOperationClick={onOperationClick}
          />
        ) : (
          <SearchInput allowSearch={false} readOnly />
        )}
      </Item>
    </>
  )
}

export function FormTree(props: any) {
  const { value = {}, onChange, valueField = 'key', ...otherProps } = props
  const onSelect: TreeProps['onSelect'] = (selectedKeys, info: any) => {
    const {
      node: { key, state, title, type },
    } = info
    onChange && onChange({ key, state, title, type })
  }
  const { [valueField]: checkedKey } = value || {}
  return <Tree {...otherProps} checkedKeys={[checkedKey]} onSelect={onSelect} />
}

export function SelectHost(props: any) {
  const { formItemProps, componentProps, checkedKeys } = props
  const { refreshResultData, ...otherComponentProps } = componentProps
  const form = Form.useFormInstance()

  const [hostData, setHostData] = useState<DataNode[]>([])

  useEffect(() => {
    const getMoveVmList = async () => {
      const result = await moveVmList()
      setHostData(result)
    }
    getMoveVmList()
  }, [])

  const getIcon = (props: any) => {
    const {
      data: { type, state },
    } = props
    if (type === 'dataCenter') {
      return <img src={dataCenter} />
    }
    if (type === 'cluster') {
      return <img src={cluster} />
    }
    if (type === 'host' && state === '1') {
      return <img src={hostOpen} />
    }
    if (type === 'host' && state !== '1') {
      return <img src={hostClose} />
    }
  }

  return (
    <>
      <Item {...formItemProps}>
        <FormTree
          switcherIcon={<CaretDownOutlined style={{ fontSize: '14px' }} />}
          treeData={hostData}
          showIcon
          icon={getIcon}
        />
      </Item>
    </>
  )
}
