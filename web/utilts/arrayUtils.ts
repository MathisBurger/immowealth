export function isUniqueArray(array: any[]) {
    for (let i=0; i<array.length; i++) {
        if (array.indexOf(array[i]) !== array.lastIndexOf(array[i])) {
            return false;
        }
    }
    return true;
}