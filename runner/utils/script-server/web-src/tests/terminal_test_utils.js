export const escapePrefix = '\u001B[';

export function format(...styles) {
    if (!Array.isArray(styles)) {
        styles = [styles];
    }

    return escapePrefix + styles.join(';') + 'm';
}

export function moveCursorUp(lines) {
    return escapePrefix + lines + 'A';
}

export function moveCursorDown(lines) {
    return escapePrefix + lines + 'B';
}

export function moveCursorLeft(positions) {
    return escapePrefix + positions + 'D';
}

export function moveCursorRight(positions) {
    return escapePrefix + positions + 'C';
}

export function moveToPosition(line, column, command = 'H') {
    return escapePrefix + line + ';' + column + command;
}

export function savePosition() {
    return escapePrefix + 's';
}

export function restorePosition() {
    return escapePrefix + 'u';
}

export function clearLineToRight() {
    return clearLine('');
}

export function clearLineToLeft() {
    return clearLine('1');
}

export function clearFullLine() {
    return clearLine('2');
}

export function clearLine(arg) {
    return escapePrefix + arg + 'K';
}

export function clearScreen() {
    return escapePrefix + '2J';
}

export function clearScreenUp() {
    return escapePrefix + '1J';
}

export function clearScreenDown() {
    return escapePrefix + 'J';
}
