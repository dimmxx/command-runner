package com.dm.impl;

import com.dm.model.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandRunnerImplTest {

    private final String[] COMMAND = List.of("some", "command").toArray(String[]::new);
    private final String ONE_LINE_OUTPUT = "one line command execution";
    private final String MULTI_LINE_OUTPUT = "multiple\nline\ncommand\nexecution\n";
    private final String IO_EXCEPTION_MESSAGE = "throwing IO Exception";
    private final String RUNTIME_EXCEPTION_MESSAGE = "throwing Runtime Exception";
    @InjectMocks
    CommandRunnerImpl testInstance;
    @Mock
    private File file;
    @Mock
    private Process process;
    @Mock
    private ProcessBuilderFactory processBuilderFactory;
    @Mock
    private ProcessBuilder processBuilder;

    @BeforeEach
    void setUp() throws IOException {
        when(processBuilderFactory.construct(any(File.class), any(String[].class), any(Boolean.class), any(Boolean.class))).thenReturn(processBuilder);
        when(processBuilder.start()).thenReturn(process);
    }

    @Test
    void run_whenOneLineOutput() throws InterruptedException {
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream(ONE_LINE_OUTPUT.getBytes(StandardCharsets.UTF_8)));
        when(process.waitFor()).thenReturn(0);

        Pair<Integer, List<String>> actual = testInstance.run(file, COMMAND, true);

        assertEquals(0, actual.getKey());
        assertEquals(1, actual.getValue().size());
        assertEquals("one line command execution", actual.getValue().get(0));
        assertFalse(actual.getMetaData().isEmpty());
    }

    @Test
    void run_whenMultipleLinesOutput() throws InterruptedException {
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream(MULTI_LINE_OUTPUT.getBytes(StandardCharsets.UTF_8)));
        when(process.waitFor()).thenReturn(0);

        Pair<Integer, List<String>> actual = testInstance.run(file, COMMAND, true);

        assertEquals(0, actual.getKey());
        assertEquals(4, actual.getValue().size());
        assertEquals(List.of("multiple", "line", "command", "execution"), actual.getValue());
        assertFalse(actual.getMetaData().isEmpty());
    }

    @Test
    void run_whenThrowIOException() throws InterruptedException {
        when(process.getInputStream()).thenAnswer(i -> {
            throw new IOException(IO_EXCEPTION_MESSAGE);
        });
        Pair<Integer, List<String>> actual = testInstance.run(file, COMMAND, true);
        assertNotEquals(0, actual.getKey());
        assertEquals(0, actual.getValue().size());
        assertFalse(actual.getMetaData().isEmpty());
        assertTrue(actual.getMetaData().contains(IO_EXCEPTION_MESSAGE));
    }

    @Test
    void run_whenThrowRuntimeException() throws InterruptedException {
        when(process.getInputStream()).thenAnswer(i -> {
            throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE);
        });
        Pair<Integer, List<String>> actual = testInstance.run(file, COMMAND, true);
        assertNotEquals(0, actual.getKey());
        assertEquals(0, actual.getValue().size());
        assertFalse(actual.getMetaData().isEmpty());
        assertTrue(actual.getMetaData().contains(RUNTIME_EXCEPTION_MESSAGE));
    }

}