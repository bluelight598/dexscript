package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

public final class DexNegativeExpr extends DexUnaryOperator {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 100;

    public DexNegativeExpr(Text src) {
        super(src);
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b)) {
                continue;
            }
            if (b == '-') {
                right = DexExpr.parse(new Text(src.bytes, i + 1, src.end), RIGHT_RANK);
                return;
            }
            // not -
            return;
        }
    }

    public static DexNegativeExpr $(String src) {
        return new DexNegativeExpr(new Text(src));
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }
}
