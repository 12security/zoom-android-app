package com.google.api.client.googleapis.media;

import java.io.IOException;

public interface MediaHttpDownloaderProgressListener {
    void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException;
}
