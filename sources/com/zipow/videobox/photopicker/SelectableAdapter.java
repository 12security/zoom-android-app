package com.zipow.videobox.photopicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.photopicker.entity.Photo;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.OsUtil;

public abstract class SelectableAdapter<VH extends ViewHolder> extends Adapter<VH> implements Selectable {
    private static final String TAG = "SelectableAdapter";
    public int currentDirectoryIndex = 0;
    protected List<PhotoDirectory> photoDirectories = new ArrayList();
    protected List<String> selectedPhotos = new ArrayList();

    public boolean isSelected(@NonNull Photo photo) {
        return getSelectedPhotos().contains(OsUtil.isAtLeastQ() ? photo.getUri().toString() : photo.getPath());
    }

    public void toggleSelection(@NonNull Photo photo) {
        String uri = OsUtil.isAtLeastQ() ? photo.getUri().toString() : photo.getPath();
        if (this.selectedPhotos.contains(uri)) {
            this.selectedPhotos.remove(uri);
        } else {
            this.selectedPhotos.add(uri);
        }
    }

    public void clearSelection() {
        this.selectedPhotos.clear();
    }

    public int getSelectedItemCount() {
        return this.selectedPhotos.size();
    }

    public void setCurrentDirectoryIndex(int i) {
        this.currentDirectoryIndex = i;
    }

    @NonNull
    public List<Photo> getCurrentPhotos() {
        return ((PhotoDirectory) this.photoDirectories.get(this.currentDirectoryIndex)).getPhotos();
    }

    @NonNull
    public List<String> getCurrentPhotoPaths() {
        ArrayList arrayList = new ArrayList(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            arrayList.add(OsUtil.isAtLeastQ() ? photo.getUri().toString() : photo.getPath());
        }
        return arrayList;
    }

    public List<String> getSelectedPhotos() {
        return this.selectedPhotos;
    }
}
