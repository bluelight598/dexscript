// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexOrExpr extends DexBinaryExpr {

  @NotNull
  PsiElement getCondOr();

}
