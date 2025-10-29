package com.dm.impl;

import java.io.File;

public class ProcessBuilderFactory {

    private final boolean redirectErrorStream;

    public ProcessBuilderFactory(boolean redirectErrorStream) {
        this.redirectErrorStream = redirectErrorStream;
    }

    public ProcessBuilder construct(String[] commandArray, File workDir) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandArray);
        if (workDir != null && !workDir.isFile()) {
            processBuilder.directory(workDir);
        }
        processBuilder.redirectErrorStream(redirectErrorStream);
        return processBuilder;
    }
}
