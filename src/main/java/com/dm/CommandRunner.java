package com.dm;

import com.dm.model.Pair;

import java.io.File;
import java.util.List;

public interface CommandRunner {

    Pair<Integer, List<String>> run(File directory, String[] commandArray, boolean workDir);
}
