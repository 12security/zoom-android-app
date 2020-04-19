package com.zipow.videobox.photopicker;

import com.zipow.videobox.photopicker.entity.Photo;

public interface Selectable {
    void clearSelection();

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

    void toggleSelection(Photo photo);
}
