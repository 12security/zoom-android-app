package com.zipow.videobox.util.safe;

import androidx.annotation.NonNull;
import com.zipow.nydus.VideoCapCapability;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.view.bookmark.BookmarkItem;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.HashMap;

public class SafeObjectInputStream extends ObjectInputStream {
    public SafeObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    protected SafeObjectInputStream() throws IOException, SecurityException {
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveClass(@NonNull ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        String name = objectStreamClass.getName();
        if (name.equals(ArrayList.class.getName()) || name.equals(HashMap.class.getName()) || name.equals(Number.class.getName()) || name.equals(Integer.class.getName()) || name.equals(BookmarkItem.class.getName()) || name.equals(VideoCapCapability.class.getName()) || name.equals(GroupAction.class.getName()) || name.equals(String[].class.getName()) || name.equals(VideoCapCapability[].class.getName())) {
            return super.resolveClass(objectStreamClass);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported trust Class: ");
        sb.append(objectStreamClass.getName());
        sb.append("Please use trust class");
        throw new ClassNotFoundException(sb.toString());
    }
}
