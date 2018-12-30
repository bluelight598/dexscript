package com.dexscript.pkg;

import com.dexscript.ast.DexPackage;
import com.dexscript.shim.OutShim;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class DexPackages {

    public static FileSystem fs = FileSystems.getDefault();

    public static Path $p(String first, String... more) {
        return fs.getPath(first, more);
    }
}