/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexscript.parser;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DexLanguage extends Language {
  public static final Language INSTANCE = new DexLanguage();

  private DexLanguage() {
    super("ds", "text/ds", "text/x-ds", "application/x-ds");
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return DexConstants.Dex;
  }

  @Nullable
  @Override
  public LanguageFileType getAssociatedFileType() {
    return DexFileType.INSTANCE;
  }

  @Override
  public boolean isCaseSensitive() {
    return true;
  }
}