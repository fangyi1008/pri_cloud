export function createTableData(
  baseObject: { [key: string]: string },
  count: number,
  ignore: string[],
  key = 'id',
  indexStart = 1,
) {
  const array: { [key: string]: string }[] = []
  for (let i = indexStart; i < indexStart + count; i++) {
    const obj: { [key: string]: any } = { [key]: i }
    Object.keys(baseObject).forEach(key => {
      if (!ignore.includes(key)) {
        obj[key] = baseObject[key] + i
      }
    })
    array.push({ ...baseObject, ...obj })
  }
  return array
}

export function isNotEmptyArray(data: any) {
  if (!Array.isArray(data)) {
    return false
  }
  return data.length > 0
}
export const getWidthOrHeight = (value?: number | string) => {
  return value ? value + `px` : '100%'
}
export const isValueEqualType = (data: any, type: string) => {
  return Object.prototype.toString.call(data) === `[object ${type}]`
}

export const isNotNullArray = (data: any[]): boolean => {
  if (Array.isArray(data)) {
    return data.length > 0
  }
  return false
}

export const fileAcceptConfig = {
  image: ['.png', '.jpg', '.jpeg', '.gif', '.bmp'],
  video: ['.mp4', '.rmvb', '.mkv', '.wmv', '.flv'],
  document: ['.doc', '.docx', '.xls', '.xlsx', '.ppt', '.pptx', '.pdf', '.txt', '.tif', '.tiff'],
  file: ['.iso'],
  getAllFileType() {
    return [...this.image, ...this.video, ...this.document, ...this.file]
  },
}
