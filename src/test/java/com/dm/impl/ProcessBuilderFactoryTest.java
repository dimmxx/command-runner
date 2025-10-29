package com.dm.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessBuilderFactoryTest {

    @TempDir
    File tempDir;

    @Test
    void testConstructSetsCommand() {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(true);
        String[] command = {"echo", "hello"};
        ProcessBuilder pb = factory.construct(command, tempDir);

        assertEquals(Arrays.asList(command), pb.command());
    }

    @Test
    void testConstructSetsWorkingDirectoryWhenValid() {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(false);
        String[] command = {"ls"};
        ProcessBuilder pb = factory.construct(command, tempDir);

        assertEquals(tempDir, pb.directory());
    }

    @Test
    void testConstructDoesNotSetWorkingDirectoryWhenNull() {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(false);
        String[] command = {"ls"};
        ProcessBuilder pb = factory.construct(command, null);

        assertNull(pb.directory());
    }

    @Test
    void testConstructDoesNotSetWorkingDirectoryWhenFile() throws IOException {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(false);
        String[] command = {"ls"};
        File tempFile = File.createTempFile("test", ".txt", tempDir);

        ProcessBuilder pb = factory.construct(command, tempFile);

        assertNull(pb.directory());
    }

    @Test
    void testRedirectErrorStreamTrue() {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(true);
        String[] command = {"ls"};
        ProcessBuilder pb = factory.construct(command, tempDir);

        assertTrue(pb.redirectErrorStream());
    }

    @Test
    void testRedirectErrorStreamFalse() {
        ProcessBuilderFactory factory = new ProcessBuilderFactory(false);
        String[] command = {"ls"};
        ProcessBuilder pb = factory.construct(command, tempDir);

        assertFalse(pb.redirectErrorStream());
    }
}