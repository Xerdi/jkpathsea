package com.xerdi.jkpathsea;

public interface IKPathSea {

    String version();

    String varValue(String variable);

    boolean inNameOk(String name);

    boolean outNameOk(String name);

    String findFile(String name, FileFormatType format, boolean mustExist);

    default String findFile(String name, FileFormatType format) {
        return findFile(name, format, false);
    }

    default String findFile(String name) {
        return findFile(name, FileFormatType.tex);
    }

}
