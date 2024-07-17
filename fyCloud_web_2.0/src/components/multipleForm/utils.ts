export const initialToUpperCase = (word: string) => {
  return word.slice(0, 1).toUpperCase() + word.slice(1)
}
export const getValidateKey = (validatePrefix: string, key: string) => {
  return `${validatePrefix}${initialToUpperCase(key)}`
}
