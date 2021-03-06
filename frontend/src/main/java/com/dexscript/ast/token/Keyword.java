package com.dexscript.ast.token;

import com.dexscript.ast.core.Text;

public interface Keyword {

    static boolean $(Text src, int i, char b0) {
        if (i + 1 > src.end) {
            return false;
        }
        return src.bytes[i] == b0;
    }

    static boolean $(Text src, int i, char b0, char b1) {
        if (i + 2 > src.end) {
            return false;
        }
        return src.bytes[i] == b0 && src.bytes[i + 1] == b1;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2) {
        if (i + 3 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3) {
        if (i + 4 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3, char b4) {
        if (i + 5 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5) {
        if (i + 6 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6) {
        if (i + 7 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6, char b7) {
        if (i + 8 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6
                && src.bytes[i + 7] == b7;
    }

    static boolean $(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6, char b7, char b8) {
        if (i + 9 > src.end) {
            return false;
        }
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6
                && src.bytes[i + 7] == b7
                && src.bytes[i + 8] == b8;
    }
}
