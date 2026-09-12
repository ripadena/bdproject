package ru.misha.millionaire.core;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AppPathsTest {

    @Test
    void baseDirContainsPomXml() {
        Path base = AppPaths.getAppBaseDir().toAbsolutePath().normalize();
        assertTrue(Files.exists(base.resolve("pom.xml")));
    }

    @Test
    void dataDirIsUnderBaseDir() {
        Path base = AppPaths.getAppBaseDir().toAbsolutePath().normalize();
        Path data = AppPaths.getDataDir().toAbsolutePath().normalize();
        assertEquals(base.resolve("data").normalize(), data);
        assertTrue(Files.exists(data));
        assertFalse(data.toString().contains(".."));
    }
}
