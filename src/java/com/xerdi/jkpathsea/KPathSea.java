package com.xerdi.jkpathsea;

public class KPathSea {

    // Native handle to the KPathSea instance
    private long nativeHandle;

    // Native methods
    public native void nativeInit();
    public native void setProgramName(String invocationName, String programName);
    public native String findFile(String filename);
    public native void nativeCleanup();

    // Java constructor
    public KPathSea() {
        nativeInit();
    }

    // Clean up native resources
    @Override
    protected void finalize() throws Throwable {
        nativeCleanup();
        super.finalize();
    }

    static {
        System.loadLibrary("jkpathsea");
    }

    public static void main(String[] args) {
        KPathSea kpse = new KPathSea();
        kpse.setProgramName("lualatex", "lualatex");
        String result = kpse.findFile("lua-placeholders.sty");
        System.out.println("File found at: " + result);
    }

}
