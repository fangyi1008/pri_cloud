import { Vm } from '@/api/interface'
import { BasicCardDataType } from '@/components/showInfoCard/type'
import { nowTime } from '@/utils/util'
import ShowInfoCard from '@components/showInfoCard'
import { VmAbstractWrap } from './style'

export interface IVmAbstractProps {
  vmInfo: Vm.IVmType
}

export const VmAbstract = (props: IVmAbstractProps) => {
  const { vmInfo } = props
  const formData = (vmInfo: Vm.IVmType): BasicCardDataType[] => {
    const { vmName, vmMark, state, createTime, vmOs } = vmInfo
    return [
      {
        key: 'vmName',
        label: '名称',
        type: 'text',
        value: vmName || '-',
      },
      {
        key: 'vmMark',
        label: '备注',
        type: 'text',
        value: vmMark || '-',
      },
      {
        key: 'state',
        label: '转态',
        type: 'text',
        value: state || '-',
      },
      {
        key: '系统类型',
        label: '系统类型',
        type: 'text',
        value: vmOs || '-',
      },
      {
        key: 'createTime',
        label: '创建时间',
        type: 'text',
        value: nowTime(createTime) || '-',
      },
    ]
  }
  return (
    <VmAbstractWrap>
      <ShowInfoCard
        basicData={formData(vmInfo)}
        cardTitle={'基本信息'}
        type={'basic'}
        width={300}
      />
    </VmAbstractWrap>
  )
}

export default VmAbstract
