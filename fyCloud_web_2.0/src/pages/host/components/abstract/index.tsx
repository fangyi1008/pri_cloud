import { BasicCardDataType } from '@/components/showInfoCard/type'
import { nowTime } from '@/utils/util'
import ShowInfoCard from '@components/showInfoCard'
import { IHostTabsPropsType } from '../tabs'
import { VmAbstractWrap } from './style'

export const HostAbstract = (props: IHostTabsPropsType) => {
  const { nodeInfo } = props
  const formData = (nodeInfo: any): BasicCardDataType[] => {
    const { cores, cpus, memory, mhz, model, nodes, sockets, threads } = nodeInfo || {}
    return [
      {
        key: 'cores',
        label: 'socket核数',
        type: 'text',
        value: cores || '-',
      },
      {
        key: 'cpus',
        label: 'CPU数目',
        type: 'text',
        value: cpus || '-',
      },
      {
        key: 'memory',
        label: '内存',
        type: 'text',
        value: memory || '-',
      },
      {
        key: 'mhz',
        label: 'CPU频率',
        type: 'text',
        value: mhz || '-',
      },
      {
        key: 'model',
        label: 'CPU型号',
        type: 'text',
        value: model || '-',
      },
      {
        key: 'nodes',
        label: 'NUMA节点数目',
        type: 'text',
        value: nodes || '-',
      },
      {
        key: 'sockets',
        label: 'CPU socket数目',
        type: 'text',
        value: sockets || '-',
      },
      {
        key: 'threads',
        label: '核线程数目',
        type: 'text',
        value: threads || '-',
      },
    ]
  }
  return (
    <VmAbstractWrap>
      <ShowInfoCard
        basicData={formData(nodeInfo)}
        cardTitle={'基本信息'}
        type={'basic'}
        width={300}
      />
    </VmAbstractWrap>
  )
}

export default HostAbstract
