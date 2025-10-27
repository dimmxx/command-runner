package com.dm.impl;

import java.io.File;

public class ProcessBuilderFactory {

    public ProcessBuilder construct(File directory, String[] commandArray,
                                    boolean workDir, boolean redirectErrorStream) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commandArray);
        if (workDir && !directory.isFile()) {
            processBuilder.directory(directory);
        }
        processBuilder.redirectErrorStream(redirectErrorStream);
        return processBuilder;
    }
}
