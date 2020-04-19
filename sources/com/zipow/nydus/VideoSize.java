package com.zipow.nydus;

public class VideoSize {
    public int height;
    public int width;

    public VideoSize() {
    }

    public VideoSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof VideoSize)) {
            return false;
        }
        VideoSize videoSize = (VideoSize) obj;
        if (videoSize.width == this.width && videoSize.height == this.height) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (this.width << 16) | this.height;
    }

    public boolean similarTo(Object obj) {
        boolean z = false;
        if (!(obj instanceof VideoSize)) {
            return false;
        }
        VideoSize videoSize = (VideoSize) obj;
        if (videoSize.width * this.height == this.width * videoSize.height) {
            z = true;
        }
        return z;
    }
}
