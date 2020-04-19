package com.zipow.cmmlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.w3c.dom.Document;

public class CmmHelper {
    @NonNull
    private static native Object buildXMLImpl(Object obj);

    @NonNull
    private static native Object getClassFromStrImpl(String str);

    @NonNull
    private static native Object getFileBy1ParamImpl(String str);

    @NonNull
    private static native Object getFileBy2ParamImpl(File file, String str);

    @NonNull
    private static native String getMD5AlgorithmStringImpl();

    private static native void loadUrlImpl(Object obj, String str, Map<String, String> map);

    @NonNull
    private static native Object newInPutFromFileImpl(String str);

    @NonNull
    private static native Object newOutPutFromFileImpl(String str);

    @NonNull
    private static native Object openInputStreamByContextImpl(Context context, Object obj);

    private static native void sendBroadcastImpl(Context context, Intent intent, String str);

    @Nullable
    public static File getFileBy1Param(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Object fileBy1ParamImpl = getFileBy1ParamImpl(str);
        if (fileBy1ParamImpl instanceof File) {
            return (File) fileBy1ParamImpl;
        }
        return null;
    }

    @Nullable
    public static File getFileBy2Param(@Nullable File file, String str) {
        if (TextUtils.isEmpty(str) || file == null) {
            return null;
        }
        Object fileBy2ParamImpl = getFileBy2ParamImpl(file, str);
        if (fileBy2ParamImpl instanceof File) {
            return (File) fileBy2ParamImpl;
        }
        return null;
    }

    @NonNull
    public static String getMD5AlgorithmString() {
        return getMD5AlgorithmStringImpl();
    }

    @Nullable
    public static Document buildXML(@Nullable InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        Object buildXMLImpl = buildXMLImpl(inputStream);
        if (buildXMLImpl instanceof Document) {
            return (Document) buildXMLImpl;
        }
        return null;
    }

    @Nullable
    public static OutputStream newOutPutFromFile(String str) throws FileNotFoundException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Object newOutPutFromFileImpl = newOutPutFromFileImpl(str);
        if (newOutPutFromFileImpl instanceof OutputStream) {
            return (OutputStream) newOutPutFromFileImpl;
        }
        return null;
    }

    public static void loadUrl(WebView webView, String str) {
        loadUrl(webView, str, null);
    }

    public static void loadUrl(@Nullable WebView webView, String str, Map<String, String> map) {
        if (!TextUtils.isEmpty(str) && webView != null) {
            loadUrlImpl(webView, str, map);
        }
    }

    @Nullable
    public static Class<?> getClassFromStr(String str) throws ClassNotFoundException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Object classFromStrImpl = getClassFromStrImpl(str);
        if (classFromStrImpl instanceof Class) {
            return (Class) classFromStrImpl;
        }
        return null;
    }

    @Nullable
    public static InputStream newInPutFromFile(String str) throws FileNotFoundException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Object newInPutFromFileImpl = newInPutFromFileImpl(str);
        if (newInPutFromFileImpl instanceof InputStream) {
            return (InputStream) newInPutFromFileImpl;
        }
        return null;
    }

    public static void sendBroadcast(@Nullable Context context, @Nullable Intent intent) {
        if (context != null && intent != null) {
            sendBroadcastImpl(context, intent, null);
        }
    }

    public static void sendBroadcast(@Nullable Context context, @Nullable Intent intent, String str) {
        if (context != null && intent != null) {
            sendBroadcastImpl(context, intent, str);
        }
    }

    @Nullable
    public static InputStream openInputStreamByContext(@Nullable Context context, @Nullable Uri uri) throws FileNotFoundException {
        if (uri == null || context == null) {
            return null;
        }
        Object openInputStreamByContextImpl = openInputStreamByContextImpl(context, uri);
        if (openInputStreamByContextImpl instanceof InputStream) {
            return (InputStream) openInputStreamByContextImpl;
        }
        return null;
    }
}
