import LoginForm from './components/LoginForm'
import loginLeft from '@/assets/images/login_left.png'
import logo from '@/assets/images/banner.png'
import './index.less'

const Login = () => {
  return (
    <div className='login-container'>
      <div className='login-box'>
        <div className='login-left'>
          <img src={loginLeft} alt='login' />
        </div>
        <div className='login-form'>
          <div className='login-logo'>
            <img className='login-icon' src={logo} alt='logo' />
          </div>
          <LoginForm />
        </div>
      </div>
    </div>
  )
}

export default Login
