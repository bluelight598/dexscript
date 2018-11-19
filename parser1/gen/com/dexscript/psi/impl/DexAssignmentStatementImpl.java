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

public class DexAssignmentStatementImpl extends DexStatementImpl implements DexAssignmentStatement {

  public DexAssignmentStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitAssignmentStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DexExpression.class);
  }

  @Override
  @NotNull
  public DexLeftHandExprList getLeftHandExprList() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, DexLeftHandExprList.class));
  }

  @Override
  @NotNull
  public DexAssignOp getAssignOp() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, DexAssignOp.class));
  }

}