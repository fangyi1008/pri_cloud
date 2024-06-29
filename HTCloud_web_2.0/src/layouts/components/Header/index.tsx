import { Layout } from 'antd'
import AvatarIcon from './components/AvatarIcon'
import CollapseIcon from './components/CollapseIcon'
import BreadcrumbNav from './components/BreadcrumbNav'
import AssemblySize from './components/AssemblySize'
import Language from './components/Language'
import Theme from './components/Theme'
import Fullscreen from './components/Fullscreen'
import { useAppSelector } from '@redux/store'
import './index.less'

const LayoutHeader = () => {
  const { Header } = Layout
  const userName = useAppSelector(state => state.global.userInfo?.username)
  return (
    <Header>
      <div className='header-lf'>
        <CollapseIcon />
        <BreadcrumbNav />
      </div>
      <div className='header-ri'>
        <Fullscreen />
        <span className='username'>{userName}</span>
        <AvatarIcon />
      </div>
    </Header>
  )
}

export default LayoutHeader
