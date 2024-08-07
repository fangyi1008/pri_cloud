import '@/styles/theme/theme-default.less'
import { ThemeConfigProp } from '@/redux/interface'

/**
 * @description 全局主题设置
 * */
const useTheme = (themeConfig: ThemeConfigProp) => {
  const { weakOrGray, isDark } = themeConfig
  const initTheme = () => {
    // 灰色和弱色切换
    const body = document.documentElement as HTMLElement
    if (!weakOrGray) body.setAttribute('style', '')
    if (weakOrGray === 'weak') body.setAttribute('style', 'filter: invert(80%)')
    if (weakOrGray === 'gray') body.setAttribute('style', 'filter: grayscale(1)')
  }
  initTheme()

  return {
    initTheme,
  }
}

export default useTheme
