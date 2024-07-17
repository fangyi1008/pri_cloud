import { Button, Result } from 'antd'
import { useNavigate } from 'react-router-dom'
import { HOME_URL } from '@/config/config'
import './index.less'

const NotNetwork = () => {
  const navigate = useNavigate()
  const goHome = () => {
    navigate(HOME_URL)
  }
  return (
    <Result
      status='500'
      title='500'
      subTitle='对不起，服务发生错误'
      extra={
        <Button type='primary' onClick={goHome}>
          返回首页
        </Button>
      }
    />
  )
}

export default NotNetwork
