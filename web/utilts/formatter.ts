/**
 * Formats the number with digits
 *
 * @param num The number that should be formatted
 * @return the formatted number string
 */
export function formatNumber(num: number): string {
    const spl = `${num}`.split('.');
    const digits = spl[0].length;
    let construct = [];
    for (let i=1; i<=digits; i++) {
        construct.push(spl[0].charAt(digits-i));
        if (i % 3 === 0 && (digits-i) !== 0) {
            construct.push('.');
        }
    }
    if (spl.length === 2) {
        return construct.reverse().join('') + ',' + spl[1];
    }
    return construct.reverse().join('');
}