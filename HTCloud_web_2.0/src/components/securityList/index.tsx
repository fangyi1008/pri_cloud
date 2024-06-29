import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { SecurityGroup, SecurityRule } from '@/api/interface'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { ISecurityGroupListPropsType } from './type'
import { getSecurityGroupList, getSecurityRuleList } from '@/api/modules/network'
import { securityGroupListOperations } from '@/layouts/components/deviceManagement/securityGroup'
import { securityRuleListOperations } from '@/layouts/components/deviceManagement/securityRule'
import eventEmitter from '@/events/index'
import { BasicObj } from '../showInfoCard/type'
import { SelectGroupWrap } from './style'
import { useUpdateEffect } from 'ahooks'
export default function SecurityGroupList(props: ISecurityGroupListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const [selectedGroupRowKeys, setSelectedGroupRowKeys] = useState<Key[]>([])
  const [selectedRuleRowKeys, setSelectedRuleRowKeys] = useState<Key[]>([])
  const [clickGroup, setClickGroup] = useState<BasicObj>({})
  const listGroupRef: RefObject<IProTableListRef> = useRef(null)
  const listRuleRef: RefObject<IProTableListRef> = useRef(null)
  const groupTableOperation = [...securityGroupListOperations].splice(1)
  const ruleTableOperation = [...securityRuleListOperations].splice(1)
  const groupColumns: ProColumns<SecurityGroup.ISecurityGroupType>[] = [
    {
      title: '编号',
      dataIndex: 'securityGroupId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '安全组名称',
      dataIndex: 'securityGroupName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '描述',
      dataIndex: 'securityGroupRemark',
      ellipsis: true,
      search: false,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'date',
      ellipsis: true,
      search: false,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      width: 400,
      render: (text, record) => [
        groupTableOperation.map(item => (
          <a
            key={item.key}
            onClick={() => onGroupOpenOperation(item.type, [record.securityGroupId])}
          >
            {item.text}
          </a>
        )),
      ],
    },
  ]

  const ruleColumns: ProColumns<SecurityRule.ISecurityRuleType>[] = [
    {
      title: '编号',
      dataIndex: 'ruleId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '方向',
      dataIndex: 'inOutFlow',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '协议',
      dataIndex: 'agreeType',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '源CIDR',
      dataIndex: 'sourceMask',
      ellipsis: true,
    },
    {
      title: '源端口',
      dataIndex: 'sourcePort',
      ellipsis: true,
    },
    {
      title: '目的CIDR',
      dataIndex: 'destMask',
      ellipsis: true,
    },
    {
      title: '目的端口',
      dataIndex: 'destPort',
      ellipsis: true,
    },
    {
      title: '动作',
      dataIndex: 'action',
      ellipsis: true,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      width: 90,
      render: (text, record) => [
        ruleTableOperation.map(item => (
          <a key={item.key} onClick={() => onRuleOpenOperation(item.type, [record.ruleId])}>
            {item.text}
          </a>
        )),
      ],
    },
  ]

  const refresh = (ref: any, type: string) => {
    if (type === 'reload') {
      ref?.current?.getRef?.()?.reload?.()
    } else if (type === 'reset') {
      ref?.current?.getRef?.()?.reset?.()
    } else if (type === 'clearSelected') {
      ref?.current?.getRef?.()?.clearSelected?.()
    }
  }

  useEffect(() => {
    const securityListRefresh = async (params: {
      type: 'reload' | 'reset' | 'clearSelected'
      isGroup: boolean
      value?: any
      isDelete?: boolean
    }) => {
      const { type, isGroup, value, isDelete = false } = params
      if (isGroup) {
        if (Object.keys(clickGroup).length === 0) {
          refresh(listGroupRef, type)
          return
        }
        refresh(listGroupRef, type)
        if (isDelete && Array.isArray(value) && type === 'reload') {
          const { securityGroupName } = clickGroup
          const result = value?.find((item: string) => item === securityGroupName)
          result ? setClickGroup({}) : null
        } else if (
          Object.prototype.toString.call(value) === '[object Object]' &&
          type === 'reload'
        ) {
          const { securityGroupId: id } = value
          const { securityGroupId } = clickGroup
          value && id === securityGroupId && setClickGroup({ ...clickGroup, ...value })
        }
      } else {
        refresh(listRuleRef, type)
      }
    }
    // 注册事件
    eventEmitter.addListener('groupListLoad', securityListRefresh)
    return () => {
      eventEmitter.off('groupListLoad', securityListRefresh)
      // 移除注册的事件
    }
  }, [clickGroup])

  useUpdateEffect(() => {
    setSelectedRuleRowKeys([])
  }, [clickGroup])

  const getSecurityGroupListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getSecurityGroupList({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const getSecurityRuleListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { securityGroupId } = otherParam
    const { currPage, pageSize, totalCount, list } =
      (await getSecurityRuleList({ page: page + '', limit: limit + '', securityGroupId })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const onGroupOpenOperation = (type: string, value: Key[]) => {
    if (type === 'addSecurityGroup') {
      operateToDispath.addGroup(dispatch)
    } else if (type === 'editSecurityGroup') {
      operateToDispath.editGroup(dispatch, value[0])
    } else if (type === 'deleteSecurityGroup') {
      operateToDispath.deleteGroup(dispatch, value)
    }
  }

  const onRuleOpenOperation = (type: string, value: Key[]) => {
    if (type === 'addSecurityRule') {
      operateToDispath.addRule(dispatch, clickGroup)
    } else if (type === 'editSecurityRule') {
      operateToDispath.editRule(dispatch, value[0])
    } else if (type === 'deleteSecurityRule') {
      operateToDispath.deleteRule(dispatch, value)
    }
  }

  const addEditDeleteGroupOnClick = (type: string) => {
    onGroupOpenOperation(type, selectedGroupRowKeys)
  }
  const addEditDeleteRuleOnClick = (type: string) => {
    onRuleOpenOperation(type, selectedRuleRowKeys)
  }

  const securityGroupOperations = securityGroupListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn ? disabledFn(selectedGroupRowKeys) : false
    return {
      disabled,
      ...thoreData,
    }
  })

  const securityRuleOperations = securityRuleListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn ? disabledFn(selectedRuleRowKeys) : false
    return {
      disabled,
      ...thoreData,
    }
  })

  const securityGrouptoolBarRender = () => [
    <AddEditDelete key='key' data={securityGroupOperations} onClick={addEditDeleteGroupOnClick} />,
  ]

  const securityRuletoolBarRender = () => [
    <AddEditDelete key='key' data={securityRuleOperations} onClick={addEditDeleteRuleOnClick} />,
  ]

  const onGroupSelectedChange = (selectedRowKeys: Key[]) => {
    setSelectedGroupRowKeys(selectedRowKeys)
  }

  const onRuleSelectedChange = (selectedRowKeys: Key[]) => {
    setSelectedRuleRowKeys(selectedRowKeys)
  }
  const onRowClick = (record: any) => {
    setClickGroup(record)
  }
  const { securityGroupName } = clickGroup
  return (
    <>
      <CustomProTable<SecurityGroup.ISecurityGroupType>
        params={params}
        ref={listGroupRef}
        search={{
          labelWidth: 'auto',
        }}
        columns={groupColumns}
        request={getSecurityGroupListData}
        rowKey={'securityGroupId'}
        headerTitle='安全组'
        toolBarRender={securityGrouptoolBarRender}
        pagination={{
          pageSize: 10,
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
        selectedRowKeys={selectedGroupRowKeys}
        rowSelection={{
          alwaysShowAlert: true,
          onChange: onGroupSelectedChange,
        }}
        onRowClick={onRowClick}
      />
      {Object.keys(clickGroup).length !== 0 ? (
        <>
          <SelectGroupWrap>{`当前选中安全组为: ${securityGroupName}`}</SelectGroupWrap>
          <CustomProTable<SecurityRule.ISecurityRuleType>
            params={clickGroup}
            ref={listRuleRef}
            search={false}
            columns={ruleColumns}
            request={getSecurityRuleListData}
            rowKey={'ruleId'}
            headerTitle='安全规则'
            toolBarRender={securityRuletoolBarRender}
            pagination={{
              pageSize: 10,
            }}
            tableAlertRender={false}
            tableAlertOptionRender={false}
            selectedRowKeys={selectedRuleRowKeys}
            rowSelection={{
              alwaysShowAlert: true,
              onChange: onRuleSelectedChange,
            }}
          />
        </>
      ) : null}
    </>
  )
}
