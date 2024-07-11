package com.xerdi.jkpathsea;

public class KPathSea implements IKPathSea, AutoCloseable {

    private final IKPathSea kpse;

    public KPathSea() {
        if (KPathSeaNative.hasNativeSupport()) {
            kpse = new KPathSeaNative();
        } else {
            kpse = new KPathSeaProcess();
        }
    }

    public KPathSea(String program) {
        if (KPathSeaNative.hasNativeSupport()) {
            kpse = new KPathSeaNative(program);
        } else {
            kpse = new KPathSeaProcess(program);
        }
    }

    @Override
    public String version() {
        return kpse.version();
    }

    @Override
    public String varValue(String variable) {
        return kpse.varValue(variable);
    }

    @Override
    public String findFile(String filename, FileFormatType format, boolean mustExist) {
        return kpse.findFile(filename, format, mustExist);
    }

    @Override
    public void close() {
        if (kpse instanceof AutoCloseable) {
            try {
                ((AutoCloseable) kpse).close();
            } catch (Exception e) {
                System.err.println("Failed to close kpse");
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

    public static boolean usesNativeImplementation() {
        return KPathSeaNative.hasNativeSupport();
    }

    public static void main(String[] args) {
        try (KPathSea kpse = new KPathSea()) {
            System.out.printf("%s %s%n", kpse.version(), usesNativeImplementation() ? "native impl" : "cmdline impl");
            System.out.println("TEXMFHOME: " + kpse.varValue("TEXMFHOME"));
            System.out.println("STY file: " + kpse.findFile("babel.sty"));
            System.out.println("Lua file: " + kpse.findFile("gitinfo-lua.lua", FileFormatType.lua));
        }
    }
}
