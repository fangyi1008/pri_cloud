import styled from 'styled-components'

export const getCommonScrollStyle = (param: {
  background?: string
  trackColor?: string
  hoverColor?: string
  radius?: number
  width?: number
  isHoverShow?: boolean
}) => {
  const {
    background = '#d8d8d8',
    trackColor = '#EDEDED',
    hoverColor = '#d8d8d8',
    radius = 4,
    width = 6,
    isHoverShow = false,
  } = param
  return `
 &::-webkit-scrollbar {
    width: ${width}px;
    color: transparent;
  }

  &::-webkit-scrollbar-thumb {
    border-radius: ${radius}px;
    background: ${isHoverShow ? 'transparent' : background}
  }

  &::-webkit-scrollbar-track-piece {
    background: ${isHoverShow ? 'transparent' : trackColor}
  }

  &:hover::-webkit-scrollbar {width: ${width}px;}}

  &:hover::-webkit-scrollbar-thumb {
    background: ${background};
    transition: 0.2s;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: ${hoverColor};
    transition: 0.2s;
  }

  &:hover::-webkit-scrollbar-track {background: ${trackColor};}}

  &::-webkit-scrollbar-track {
    background: ${isHoverShow ? 'transparent' : trackColor};
  }`
}

export const H1Title = styled.h1`
  margin: 0;
  padding: 8px 0;
  font-size: 20px;
`
