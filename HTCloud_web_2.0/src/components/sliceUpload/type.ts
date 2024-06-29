import React from 'react'

export interface ISliceUploadPropsType {
  autoStart: boolean
  fileCheckFn?: (file: any) => Promise<boolean>
  fileComplete?: (file: any) => void
}

export interface IUnsupportPropsType {
  unsupportText?: string
}

export interface IBtnPropsType {
  directory?: boolean
  single?: boolean
  attrs?: Record<string, any>
  children?: React.ReactNode
}

export interface IFileItemPropsType {
  file: Record<string, any>
}
export interface IFileListPanlPropsType {
  onClose?: () => void
  fileAdded?: (file: any) => void
  fileComplete?: (file: any) => void
}

export interface IOptions extends IUploaderOptions {
  singleFile?: boolean
  accept?: string
}

export interface IAssignBrowseFnParams extends IUploaderOptions {
  options: IOptions
  queryParam: Record<string, any>
}

export interface IUploaderOptions {
  target?: string
  chunkSize?: number
  fileParameterName?: string
  headers?: Record<string, string>
  withCredentials?: boolean
  testChunks?: boolean
}

export interface ICreateAssignBrowseParam {
  singleFile?: boolean
  attributes?: Record<string, any>
}

export type ISliceUploadRef = {
  getUploader: () => any
  openSelectFileWindow: (params: IAssignBrowseFnParams) => void
}
