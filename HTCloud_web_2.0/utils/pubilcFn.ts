const isArray = (val: any) =>
  typeof val === 'object' && Object.prototype.toString.call(val) === '[object Array]'
export const existArray = (data: any) => data && data.length > 0 && isArray(data)
export const matchValue = (key: string, target: string) => key && key.indexOf(target) !== -1
