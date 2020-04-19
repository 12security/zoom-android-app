package com.zipow.videobox.ptapp;

public interface ParseUrlActionError {
    public static final int ParseUrlActionError_NotSameDomain = 1;
    public static final int ParseUrlActionError_Parameter = 4;
    public static final int ParseUrlActionError_Success = 0;
    public static final int ParseUrlActionError_System = 5;
    public static final int ParseUrlActionError_UnknownResource = 3;
    public static final int ParseUrlActionError_UnknownSchema = 2;
}
