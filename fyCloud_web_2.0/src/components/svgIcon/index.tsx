import cluster from '@src/assets/images/cluster.svg'
import dataCenter from '@src/assets/images/data_center.svg'
import hostClose from '@src/assets/images/host_close.svg'
import hostPause from '@src/assets/images/host_pause.svg'
import hostRuning from '@src/assets/images/host_runing.svg'
import hostUnkown from '@src/assets/images/host_unkown.svg'
import hostMaintenance from '@src/assets/images/host_maintenance.svg'
import vmClose from '@src/assets/images/vm_close.svg'
import vmPause from '@src/assets/images/vm_pause.svg'
import vmRunning from '@src/assets/images/vm_running.svg'
import vmUnkown from '@src/assets/images/vm_unkown.svg'
import { SvgWrap } from './style'

interface SvgProps {
  name: string // 图标的名称 ==> 必传
  color?: string //图标的颜色 ==> 非必传
  prefix?: string // 图标的前缀 ==> 非必传（默认为"icon"）
  iconStyle?: { [key: string]: any } // 图标的样式 ==> 非必传
}

export default function SvgIcon(props: SvgProps) {
  const { name, prefix = 'icon', iconStyle = { width: '100px', height: '100px' } } = props
  const symbolId = `#${prefix}-${name}`
  return (
    <svg aria-hidden='true' style={iconStyle}>
      <use href={symbolId} />
    </svg>
  )
}

export const ClusterSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={cluster}></img>
      </SvgWrap>
    </>
  )
}

export const DataCenterSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={dataCenter}></img>
      </SvgWrap>
    </>
  )
}

export const HostCloseSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={hostClose}></img>
      </SvgWrap>
    </>
  )
}

export const HostPauseSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={hostPause}></img>
      </SvgWrap>
    </>
  )
}

export const HostRuningSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={hostRuning}></img>
      </SvgWrap>
    </>
  )
}

export const HostUnkownSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={hostUnkown}></img>
      </SvgWrap>
    </>
  )
}

export const HostMaintenanceSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={hostMaintenance}></img>
      </SvgWrap>
    </>
  )
}

export const VmCloseSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={vmClose}></img>
      </SvgWrap>
    </>
  )
}

export const VmPauseSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={vmPause}></img>
      </SvgWrap>
    </>
  )
}

export const VmRunningSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={vmRunning}></img>
      </SvgWrap>
    </>
  )
}

export const VmUnkownSvg = () => {
  return (
    <>
      <SvgWrap className='ant-menu-item-icon'>
        <img src={vmUnkown}></img>
      </SvgWrap>
    </>
  )
}
