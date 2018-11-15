// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexFunctionType extends DexType, DexSignatureOwner {

  @Nullable
  DexSignature getSignature();

  @NotNull
  PsiElement getFunction();

}