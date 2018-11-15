// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.psi.*;

public class DexStringLiteralImpl extends DexExpressionImpl implements DexStringLiteral {

  public DexStringLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitStringLiteral(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getRawString() {
    return findChildByType(RAW_STRING);
  }

  @Override
  @Nullable
  public PsiElement getSstring() {
    return findChildByType(SSTRING);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

  public boolean isValidHost() {
    return DexPsiImplUtil.isValidHost(this);
  }

  @NotNull
  public DexStringLiteralImpl updateText(@NotNull String text) {
    return DexPsiImplUtil.updateText(this, text);
  }

  @NotNull
  public DexStringLiteralEscaper createLiteralTextEscaper() {
    return DexPsiImplUtil.createLiteralTextEscaper(this);
  }

  @NotNull
  public String getDecodedText() {
    return DexPsiImplUtil.getDecodedText(this);
  }

}