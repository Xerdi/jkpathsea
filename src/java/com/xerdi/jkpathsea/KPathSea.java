package com.xerdi.jkpathsea;

public class KPathSea {

    // Native handle to the KPathSea instance
    private long instance;

    // Native methods
    private native long init();
    public native String version();
    private native void set_program_name(long handle, String invocationName, String programName);
    private native String find_file(long handle, String filename, int format, boolean mustExist);
    private native void destroy(long handle);

    public KPathSea() {
        this("lualatex");
    }

    public KPathSea(String program) {
        instance = init();
        set_program_name(instance, "java", program);
    }

    public String findFile(String filename, FileFormatType format, boolean mustExist) {
        return find_file(instance, filename, format.ordinal(), mustExist);
    }

    public String findFile(String filename, FileFormatType format) {
        return findFile(filename, format, false);
    }

    public String findFile(String filename) {
        return findFile(filename, FileFormatType.kpse_tex_format);
    }

    // Clean up native resources
    public void cleanup() {
        destroy(instance);
    }

    // Finalizer
    @Override
    protected void finalize() throws Throwable {
        try {
            cleanup();
        } finally {
            super.finalize();
        }
    }

    static {
        System.loadLibrary("jkpathsea");
    }

    public static void main(String[] args) {
        KPathSea kpse = new KPathSea();
        System.out.println(kpse.version());
        String result = kpse.findFile("lua-placeholders.sty");
        System.out.println("File found at: " + result);
    }

}
