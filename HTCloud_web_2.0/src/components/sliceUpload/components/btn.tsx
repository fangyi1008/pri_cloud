import React, { useContext, useRef, useEffect } from 'react'
import { UploaderContext } from '../uploaderContext'
import { IBtnPropsType } from '../type'

export default function Btn(props: IBtnPropsType) {
  const { uploaderRef, support } = useContext(UploaderContext)
  const inputWrapRef: React.RefObject<HTMLLabelElement> = useRef(null)
  const { directory = false, single = false, attrs = {}, children } = props
  useEffect(() => {
    const uploader = uploaderRef?.current
    if (uploader) {
      uploader.uploader.assignBrowse(inputWrapRef.current, directory, single, attrs)
    }
  })
  return (
    <>
      {support ? (
        <label className='uploader-btn' ref={inputWrapRef}>
          {children}
        </label>
      ) : null}
    </>
  )
}
