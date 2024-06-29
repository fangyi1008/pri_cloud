import { useState } from 'react'
import { Button, Form, Input, message } from 'antd'
import { useNavigate } from 'react-router-dom'
import { Login } from '@/api/interface'
import { loginApi } from '@/api/modules/login'
import { getUserinfoApi } from '@/api/modules/user'
import { HOME_URL } from '@/config/config'
import { UserOutlined, LockOutlined, CloseCircleOutlined, RedoOutlined } from '@ant-design/icons'
import { setToken, setUserInfo } from '@redux/modules/global/global'
import { setTabsList } from '@redux/modules/tabs/tabs'
import { useAppDispatch } from '@redux/store'
import { transformUuid } from '@/utils/util'

const LoginForm = () => {
  const navigate = useNavigate()
  const [form] = Form.useForm()
  const [loading, setLoading] = useState<boolean>(false)
  const dispatch = useAppDispatch()
  const [uuid, setUuid] = useState(transformUuid())
  // 登录
  const onFinish = async (loginForm: Login.ReqLoginForm) => {
    try {
      setLoading(true)
      const result = await loginApi({ ...loginForm, uuid })
      dispatch(setToken(result?.token || ''))
      dispatch(setTabsList([]))
      const userInfo = await getUserinfoApi()
      dispatch(setUserInfo(userInfo.user))
      message.success('登录成功！')
      navigate(HOME_URL)
    } catch (ex) {
      setUuid(transformUuid())
    } finally {
      setLoading(false)
    }
  }

  const refreshCode = () => {
    setUuid(transformUuid())
  }

  const resetForm = () => {
    form.resetFields()
  }

  return (
    <Form
      form={form}
      name='basic'
      labelCol={{ span: 5 }}
      initialValues={{ remember: true }}
      onFinish={onFinish}
      size='large'
      autoComplete='off'
    >
      <Form.Item name='username' rules={[{ required: true, message: '请输入用户名' }]}>
        <Input placeholder='用户名' prefix={<UserOutlined />} />
      </Form.Item>
      <Form.Item name='password' rules={[{ required: true, message: '请输入密码' }]}>
        <Input.Password autoComplete='new-password' placeholder='密码' prefix={<LockOutlined />} />
      </Form.Item>
      <Form.Item>
        <Form.Item name='captcha' rules={[{ required: true, message: '请输入验证码' }]} noStyle>
          <Input placeholder='验证码' style={{ width: '218px', marginRight: '20px' }} />
        </Form.Item>
        <img className='login-code' src={'/htcloud/captcha.jpg?uuid=' + uuid} />
        <RedoOutlined onClick={refreshCode} />
      </Form.Item>
      <Form.Item className='login-btn'>
        <Button onClick={resetForm} icon={<CloseCircleOutlined />}>
          重置
        </Button>
        <Button type='primary' htmlType='submit' loading={loading} icon={<UserOutlined />}>
          登录
        </Button>
      </Form.Item>
    </Form>
  )
}
export default LoginForm
