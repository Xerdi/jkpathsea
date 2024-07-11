package com.xerdi.jkpathsea;

public class KPathSeaNative implements IKPathSea, AutoCloseable {

    private static boolean NATIVE_SUPPORT = true;
    private final long instance;

    private native long init();
    public native String version();
    private native void set_program_name(long handle, String invocationName, String programName);
    private native String var_value(long handle, String variable);
    private native String find_file(long handle, String filename, int format, boolean mustExist);
    private native void destroy(long handle);

    public KPathSeaNative() {
        this("pdflatex");
    }

    public KPathSeaNative(String program) {
        if (!NATIVE_SUPPORT)
            throw new UnsupportedOperationException("Could not find native library jkpathsea");
        instance = init();
        set_program_name(instance, "java", program);
    }

    public String varValue(String variable) {
        return var_value(instance, variable);
    }

    public String findFile(String filename, FileFormatType format, boolean mustExist) {
        return find_file(instance, filename, format.ordinal(), mustExist);
    }

    public String findFile(String filename, FileFormatType format) {
        return findFile(filename, format, false);
    }

    public String findFile(String filename) {
        return findFile(filename, FileFormatType.tex);
    }

    @Override
    public void close() {
        destroy(instance);
    }

    static {
        try {
            System.loadLibrary("jkpathsea");
        } catch (UnsatisfiedLinkError err) {
            System.err.println("No native support for KPathSea found");
            NATIVE_SUPPORT = false;
        }
    }

    public static boolean hasNativeSupport() {
        return NATIVE_SUPPORT;
    }

    public static void main(String[] args) {
        try (KPathSeaNative kpse = new KPathSeaNative()) {
            System.out.println(kpse.version());
            System.out.println("TEXMFHOME: " + kpse.varValue("TEXMFHOME"));
            System.out.println("STY file: " + kpse.findFile("babel.sty"));
            System.out.println("Lua file: " + kpse.findFile("gitinfo-lua.lua", FileFormatType.lua));
        }
    }

}
