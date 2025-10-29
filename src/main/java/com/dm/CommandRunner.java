package com.dm;

import com.dm.model.Pair;

import java.io.File;
import java.util.List;

public interface CommandRunner {

    /**
     *
     * @param commandArray - a command to be executed in a String array
     * @param directory    - a directory where the command will be executed. If null the command will be executed
     *                     in a current directory
     * @return - Pair<Integer, List<String>>
     */
    Pair<Integer, List<String>> run(String[] commandArray, File directory);
}
