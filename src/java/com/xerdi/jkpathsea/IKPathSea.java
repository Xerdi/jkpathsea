package com.xerdi.jkpathsea;

public interface IKPathSea {

    String version();

    String varValue(String variable);

    String findFile(String filename, FileFormatType format, boolean mustExist);

    default String findFile(String filename, FileFormatType format) {
        return findFile(filename, format, false);
    }

    default String findFile(String filename) {
        return findFile(filename, FileFormatType.tex);
    }

}
