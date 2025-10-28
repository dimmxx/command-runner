package com.dm.impl;

import com.dm.CommandRunner;
import com.dm.model.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyList;

public class CommandRunnerImpl implements CommandRunner {

    private static final Logger LOGGER = LogManager.getLogger(CommandRunnerImpl.class);
    private static final String ERROR_LOG_TEMPLATE = "CMD ERROR: [%s], directory [%s], command [%s]";
    private static final String LOG_TEMPLATE = "CMD EXECUTED: status [%d], outputSize [%d], time [%d ms], directory [%s], command [%s]";
    private final ProcessBuilderFactory processBuilderFactory;

    public CommandRunnerImpl(ProcessBuilderFactory processBuilderFactory) {
        this.processBuilderFactory = processBuilderFactory;
    }

    @Override
    public Pair<Integer, List<String>> run(File directory, String[] commandArray, boolean workDir) {
        long start = currentTimeMillis();
        String errorLogMessage;
        ProcessBuilder processBuilder = processBuilderFactory.construct(directory, commandArray, workDir, true);
        try {
            Process process = processBuilder.start();
            try (InputStream stdStream = process.getInputStream()) {
                List<String> stdOutput = new BufferedReader(new InputStreamReader(stdStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.toList());
                int status = process.waitFor();

                String logMessage = format(LOG_TEMPLATE, status, stdOutput.size(), (currentTimeMillis() - start),
                    directory.getAbsoluteFile(), Arrays.toString(commandArray));

                LOGGER.info(logMessage);
                return Pair.of(status, stdOutput, logMessage);
            }
        } catch (InterruptedException e) {
            errorLogMessage = this.logError(e, directory, commandArray);
            LOGGER.error(errorLogMessage);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            errorLogMessage = this.logError(e, directory, commandArray);
            LOGGER.error(errorLogMessage);
        }
        return Pair.of(1, emptyList(), errorLogMessage);
    }

    private String logError(Exception exception, File directory, String[] commandArray) {
        return format(ERROR_LOG_TEMPLATE, exception.getMessage(), directory.getAbsoluteFile(), Arrays.toString(commandArray));
    }
}
