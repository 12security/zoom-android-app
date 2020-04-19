package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class FileContent extends AbstractInputStreamContent {
    private final File file;

    public boolean retrySupported() {
        return true;
    }

    public FileContent(String str, File file2) {
        super(str);
        this.file = (File) Preconditions.checkNotNull(file2);
    }

    public long getLength() {
        return this.file.length();
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public File getFile() {
        return this.file;
    }

    public FileContent setType(String str) {
        return (FileContent) super.setType(str);
    }

    public FileContent setCloseInputStream(boolean z) {
        return (FileContent) super.setCloseInputStream(z);
    }
}
