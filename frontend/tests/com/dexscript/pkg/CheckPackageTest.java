package com.dexscript.pkg;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;

import static com.dexscript.pkg.FS.$p;

public class CheckPackageTest {

    @Before
    public void setup() {
        FS.fs = Jimfs.newFileSystem(Configuration.unix());
    }

    @Test
    public void no_error() throws Exception {
        Files.createDirectory($p("/pkg1"));
        Files.write($p("/pkg1/__spi__.ds"), "".getBytes());
        Assert.assertTrue(CheckPackage.$("/pkg1"));
    }

    @Test
    public void package_dir_not_found() throws IOException {
        Assert.assertFalse(CheckPackage.$("/pkg1"));
    }

    @Test
    public void spi_file_not_found() throws IOException {
        Files.createDirectory($p("/pkg1"));
        Assert.assertFalse(CheckPackage.$("/pkg1"));
    }

    @Test
    public void syntax_error() throws IOException {
        Files.createDirectory($p("/pkg1"));
        Files.write($p("/pkg1/__spi__.ds"), "".getBytes());
        Files.write($p("/pkg1/1.ds"), ("" +
                "f Hello() {\n" +
                "   World()\n" +
                "}").getBytes());
        Assert.assertFalse(CheckPackage.$("/pkg1"));
    }
}
