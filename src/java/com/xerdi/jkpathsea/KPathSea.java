package com.xerdi.jkpathsea;

public class KPathSea implements AutoCloseable {

    private final long instance;

    private native long init();
    public native String version();
    private native void set_program_name(long handle, String invocationName, String programName);
    private native String var_value(long handle, String variable);
    private native String find_file(long handle, String filename, int format, boolean mustExist);
    private native void destroy(long handle);

    public KPathSea() {
        this("pdflatex");
    }

    public KPathSea(String program) {
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
        return findFile(filename, FileFormatType.tex_format);
    }

    @Override
    public void close() {
        destroy(instance);
    }

    static {
        System.loadLibrary("jkpathsea");
    }

    public static void main(String[] args) {
        try (KPathSea kpse = new KPathSea()) {
            System.out.println(kpse.version());
            System.out.println("TEXMFHOME: " + kpse.varValue("TEXMFHOME"));
            System.out.println("STY file: " + kpse.findFile("babel.sty"));
            System.out.println("Lua file: " + kpse.findFile("gitinfo-lua.lua", FileFormatType.lua_format));
        }
    }

}
