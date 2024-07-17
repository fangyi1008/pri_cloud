import styled from 'styled-components'

import audio from '../images/audio-icon.png'
import image from '../images/image-icon.png'
import text from '../images/text-icon.png'
import video from '../images/video-icon.png'
import zip from '../images/zip.png'

export const FileList = styled.div`
  position: relative;
`

export const FilePanel = styled.div`
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 6px 6px 0 0;
  box-shadow: 0 0 10px rgb(0 0 0 / 20%);
`

export const FilePanelHead = styled.div`
  display: flex;
  height: 40px;
  line-height: 40px;
  padding: 0 15px;
  border-bottom: 1px solid #ddd;
`

export const FilePanelTitel = styled.div`
  font-size: 16px;
`

export const FilePanelOperate = styled.div`
  font-size: 12px;
  flex: 1;
  display: inline-flex;
  justify-content: end;
`

export const OperateItem = styled.span`
  cursor: pointer;
  user-select: none;
  font-size: 18px;
  margin-right: 8px;
`

export const FilePanelBody = styled.ul<{ collapse: boolean }>`
  position: relative;
  height: ${props => (props.collapse ? '0' : '240px')};
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #fff;
  transition: height 0.2s;
  margin: 0;
`

export const ListEmpty = styled.div<{ collapse: boolean }>`
  height: 240px;
  height: ${props => (props.collapse ? '0' : '240px')};
  display: flex;
  align-items: center;
  justify-content: center;
  transition: height 0.2s;
  font-size: 16px;
  overflow-x: hidden;
`

export const FilePanelBodyItem = styled.li`
  background-color: #fff;
`

export const UploaderFileWrap = styled.div.attrs<{ status: string }>(
  (props: { status: string }) => ({
    'data-status': props.status,
  }),
)<{ status: string }>`
  position: relative;
  height: 49px;
  line-height: 49px;
  overflow: hidden;
  border-bottom: 1px solid #cdcdcd;
  &.uploader-file[data-status='waiting'] .uploader-file-pause,
  &.uploader-file[data-status='uploading'] .uploader-file-pause {
    display: block;
  }
  &.uploader-file[data-status='paused'] .uploader-file-resume {
    display: block;
  }
  &.uploader-file[data-status='error'] .uploader-file-retry {
    display: block;
  }
  &.uploader-file[data-status='success'] .uploader-file-remove {
    display: none;
  }
  &.uploader-file[data-status='error'] .uploader-file-progress {
    background: #ffe0e0;
  }
`
export const UploaderFileProgress = styled.div<{ status: string }>`
  position: absolute;
  width: 100%;
  height: 100%;
  background: #e2eeff;
  ${props => (props.status === 'uploading' ? 'transition: all 0.4s linear' : '')};
  -webkit-transform: translateX(-100%);
  transform: translateX(-100%);
`
export const UploaderFileInfo = styled.div`
  position: relative;
  z-index: 1;
  height: 100%;
  overflow: hidden;
  display: flex;
`
export const UploaderFileName = styled.div`
  width: 45%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-indent: 14px;
  & > .uploader-file-icon {
    width: 24px;
    height: 24px;
    display: inline-block;
    vertical-align: top;
    margin-top: 13px;
    margin-right: 8px;
  }
  & > .uploader-file-icon::before {
    content: '';
    display: block;
    height: 100%;
    font-size: 24px;
    line-height: 1;
    text-indent: 0;
  }
  & > .uploader-file-icon[data-icon='image'] {
    background: url(${image});
  }
  & > .uploader-file-icon[data-icon='audio'] {
    background: url(${audio});
    background-size: contain;
  }
  & > .uploader-file-icon[data-icon='video'] {
    background: url(${video});
  }
  & > .uploader-file-icon[data-icon='document'] {
    background: url(${text});
  }
  & > .uploader-file-icon[data-icon='unknown'] {
    background: url(${zip}) no-repeat center;
    background-size: contain;
  }
`
export const UploaderFileSize = styled.div`
  width: 13%;
  text-indent: 10px;
`
export const UploaderFileMeta = styled.div`
  width: 8%;
`
export const UploaderFileStatus = styled.div`
  width: 24%;
  text-indent: 10px;
  position: relative;
  height: 100%;
  .custom-status {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 1;
    margin: 0;
    padding: 0;
  }
`
export const UploaderFileActions = styled.div`
  width: 10%;
  & > span {
    display: none;
    float: left;
    width: 16px;
    height: 16px;
    margin-top: 16px;
    margin-right: 8px;
    cursor: pointer;
    background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAABkCAYAAAD0ZHJ6AAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAAAJcEhZcwAACxMAAAsTAQCanBgAAARkSURBVGje7ZnfS1NRHMAH4ptPkvQSuAdBkCxD8FUQJMEULUgzy1KyyPVQ4JMiiP4Bvg6EwUQQfMmwhwRDshwaKUjDVCgoSdDNHkzTJZ6+Z37Purve8+PeTb2TM/ggu+ew89l33x8H9BBCPG7GowXTJej3+wnDvEm0JuLC04+EYWftVAUv+fiCvDUdQR1BHUEdQR3BTIygvixoQS14XgTtthLVdpNWwXRLqvQ724LplFRtyrYF0yVpFLQrKRVMh6RZ0I6kkmCqklaCqpKZH0FX56Crq9jVfdDVk0RfFrSgFsxkQVmLcdKCVrKySCrryhPEyYShhzOcrFtG0EoilfHHk1CRU5rF6ZjNZhlVOW6RnMSVyyilKies4pO41diVy8wIujoHXV3FGdMHXTtJKLFYTLhZtq4vC1rwXApCZTIqgR6g1PBMCO9DL3bMMSqBHqDU8EyISDAHiGKvWwcCQG2KgjlAFCDAOhAAap0K5gKLphk8mqJgLrCIgoxRJ4J5wKpJ7gAoMkn5EBXBPGDVJHcAFJmkfIhQcAql1oBpTvTol9gG9pm4RHAKpdaAaU706JfYBvaZuJVgPQrt4sFlnOh5MC/p3lmJYD0K7eLBZZzoeTAv6d5ZnuAYHjpgEOnk5F0ufhG6v1ggOIaHDhhEOjl5l4tfhO4vthLcwAMrFNvLJO5vEwhu4IEViu1lEve3WQmyoihQFBzG/V0CQVYUBYqCw7i/SxTBcpsRbFeIYLnNCLZbCY5b5KAnxRwct8hBj9McZFVMW0ihRNBuFdMWUigRlFaxuQ9WWYjRMTiIe5z0wSoLMToGB3GPsA9aTZIJoB+nRgBnM1tzOkkmgH6cGgGczWzNpzqLx3n/aULJJgezeNw07oxQySbVywKjBOgFRnDs+VEsx8FlgVEC9AIjOPb8KJYjvSzoG7UW1IJaUAtqQS14toLNM5fN5APdwBJA8G83Pk/aK/rgzVvXzeQD3cASQPBvNz5P2ssTzAaGUIrHEO6zI5gNDKEUjyHcxxWkh4Ylcowwk1QQpIeGJXKMMJO0EgwqyjGCioJBJvDrxRMSuVOTJEXfbz1/bHwWtBL0yoQehK6RucgE+bGzanzulQh6E3IgQV+xpc8kcrfuSO7eTfJ3ZYmQw0Oy9azVKOk1C/bJ5D5F38YPeLfx0rjWJxHsS0SqsSYuxySjj5qO5Oj7xQWy2VBtFOwzCy6ryH3YfE3uh64Y1xckgstJPydEjkkeHv07Iy4Xaao15+KCWTBx6M/db+T9xivSErqaJDdzXI6yLRE8Vgg0coex/SPJvT0SbWu0KpZtbgSpCH3NRt7I5OxHkObc6heU+/M/J5vrpBFM5GBLqCQux14COXs5CNXK5OjPGm1tSMrJSOMNYQ4mVTGV/L6zTL7+DovkbFUxbSW0Wo05l8hJWsU+cRWfSh+Mt5Lb1ck/J1TvVsdDaR/MiEni+llsdZuZp62EViu+96bpNjNPWwmtVnzvFd5m9IVVC54x/wA7gNvqFG9vXQAAAABJRU5ErkJggg==')
      no-repeat 0 0;
  }
  & > span:hover {
    background-position-x: -21px;
  }
  & > .uploader-file-pause {
    background-position-y: 0;
  }
  & > .uploader-file-resume {
    background-position-y: -17px;
  }
  & > .uploader-file-retry {
    background-position-y: -53px;
  }
  & > .uploader-file-remove {
    display: block;
    background-position-y: -34px;
  }
`
