package com.dropbox.core;

import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.StringUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class DbxSdkVersion {
    private static final String ResourceName = "/sdk-version.txt";
    public static final String Version = loadVersion();

    private static final class LoadException extends Exception {
        private static final long serialVersionUID = 0;

        public LoadException(String str) {
            super(str);
        }
    }

    private static String loadLineFromResource() throws LoadException {
        InputStream resourceAsStream;
        try {
            resourceAsStream = DbxSdkVersion.class.getResourceAsStream(ResourceName);
            if (resourceAsStream != null) {
                BufferedReader bufferedReader = new BufferedReader(IOUtil.utf8Reader(resourceAsStream));
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String readLine2 = bufferedReader.readLine();
                    if (readLine2 == null) {
                        IOUtil.closeInput(resourceAsStream);
                        return readLine;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Found more than one line.  Second line: ");
                    sb.append(StringUtil.m33jq(readLine2));
                    throw new LoadException(sb.toString());
                }
                throw new LoadException("No lines.");
            }
            throw new LoadException("Not found.");
        } catch (IOException e) {
            throw new LoadException(e.getMessage());
        } catch (Throwable th) {
            IOUtil.closeInput(resourceAsStream);
            throw th;
        }
    }

    private static String loadVersion() {
        try {
            String loadLineFromResource = loadLineFromResource();
            if (Pattern.compile("[0-9]+(?:\\.[0-9]+)*(?:-[-_A-Za-z0-9]+)?").matcher(loadLineFromResource).matches()) {
                return loadLineFromResource;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Text doesn't follow expected pattern: ");
            sb.append(StringUtil.m33jq(loadLineFromResource));
            throw new LoadException(sb.toString());
        } catch (LoadException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error loading version from resource \"sdk-version.txt\": ");
            sb2.append(e.getMessage());
            throw new RuntimeException(sb2.toString());
        }
    }
}
