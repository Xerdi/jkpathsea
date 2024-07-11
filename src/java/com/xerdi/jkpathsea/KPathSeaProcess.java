package com.xerdi.jkpathsea;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KPathSeaProcess implements IKPathSea {

    private final static String KPSEWHICH = "kpsewhich";
    private final static String[] EMPTY = new String[]{null};

    private final String program;

    public KPathSeaProcess() {
        this.program = null;
    }

    public KPathSeaProcess(String program) {
        this.program = program;
    }

    @Override
    public String version() {
        return cmdline(kpseArgs("-version"))[0];
    }

    @Override
    public String varValue(String variable) {
        return cmdline(kpseArgs(String.format("-var-value=%s", variable)))[0];
    }

    @Override
    public String findFile(String filename, FileFormatType format, boolean mustExist) {
        String[] args;
        if (mustExist)
            args = kpseArgs(String.format("-format=%s", format.name()), "-must-exist", filename);
        else
            args = kpseArgs(String.format("-format=%s", format.name()), filename);
        return cmdline(args)[0];
    }

    private String[] kpseArgs(String ...args) {
        List<String> argList = new ArrayList<>();
        argList.add(KPSEWHICH);
        if (program != null) {
            argList.add(String.format("-progname=%s", program));
        }
        argList.addAll(List.of(args));
        return argList.toArray(String[]::new);
    }

    private String[] cmdline(String... arg) {
        // TODO: get rid of runtime errors and return EMPTY instead
        int exitCode;
        Process process;
        try {
            process = new ProcessBuilder(arg).start();
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String[] result = EMPTY;

        if (exitCode == 0) {
            InputStream rawOutput = process.getInputStream();
            if (rawOutput == null) {
                throw new RuntimeException("Failed to read output of command kpsewhich");
            }
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(rawOutput));
                result = reader.lines().toArray(String[]::new);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException(String.format("kpsewhich return with exit code %d", exitCode));
        }

        return result;
    }

    public static void main(String[] args) {
        KPathSeaProcess kpse = new KPathSeaProcess();
        System.out.println(kpse.version());
        System.out.println("TEXMFHOME: " + kpse.varValue("TEXMFHOME"));
        System.out.println("STY file: " + kpse.findFile("babel.sty"));
        System.out.println("Lua file: " + kpse.findFile("gitinfo-lua.lua", FileFormatType.lua));
    }
}
