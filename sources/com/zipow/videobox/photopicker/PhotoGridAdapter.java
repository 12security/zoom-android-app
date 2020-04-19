package com.zipow.videobox.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.photopicker.entity.Photo;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableOnSubscribe;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.schedulers.Schedulers;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhotoGridAdapter extends SelectableAdapter<PhotoViewHolder> {
    private static final int COL_NUMBER_DEFAULT = 3;
    private static final long GIF_LIMIT_SIZE = 8388608;
    public static final int ITEM_TYPE_CAMERA = 100;
    public static final int ITEM_TYPE_PHOTO = 101;
    /* access modifiers changed from: private */
    public boolean checkStateEnable;
    private int columnNumber;
    private boolean hasCamera;
    private int imageSize;
    /* access modifiers changed from: private */
    @Nullable
    public CompositeDisposable mCompositeDisposable;
    private RequestManager mGlide;
    /* access modifiers changed from: private */
    public int mMaxSelectedCount;
    /* access modifiers changed from: private */
    @Nullable
    public OnItemCheckStateChangedListener mOnItemCheckStateChangedListener;
    /* access modifiers changed from: private */
    @Nullable
    public OnClickListener onCameraClickListener;
    /* access modifiers changed from: private */
    @Nullable
    public OnPhotoClickListener onPhotoClickListener;
    /* access modifiers changed from: private */
    public boolean previewEnable;

    public static class PhotoViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public View cover;
        /* access modifiers changed from: private */
        public ImageView ivPhoto;
        /* access modifiers changed from: private */
        public View vSelected;

        public PhotoViewHolder(@NonNull View view) {
            super(view);
            this.ivPhoto = (ImageView) view.findViewById(C4558R.C4560id.iv_photo);
            this.vSelected = view.findViewById(C4558R.C4560id.v_selected);
            this.cover = view.findViewById(C4558R.C4560id.cover);
        }
    }

    public PhotoGridAdapter(@NonNull Context context, RequestManager requestManager, List<PhotoDirectory> list, int i) {
        this.mOnItemCheckStateChangedListener = null;
        this.onPhotoClickListener = null;
        this.onCameraClickListener = null;
        this.hasCamera = true;
        this.previewEnable = true;
        this.mMaxSelectedCount = 9;
        this.checkStateEnable = true;
        this.columnNumber = 3;
        this.photoDirectories = list;
        this.mGlide = requestManager;
        setColumnNumber(context, this.columnNumber);
        this.mMaxSelectedCount = i;
    }

    public PhotoGridAdapter(@NonNull Context context, RequestManager requestManager, List<PhotoDirectory> list, @Nullable ArrayList<String> arrayList, int i, int i2) {
        this(context, requestManager, list, i2);
        setColumnNumber(context, i);
        this.selectedPhotos = new ArrayList();
        if (arrayList != null) {
            this.selectedPhotos.addAll(arrayList);
        }
    }

    private void setColumnNumber(Context context, int i) {
        this.columnNumber = i;
        if (((WindowManager) context.getSystemService("window")) != null) {
            this.imageSize = UIUtil.getDisplayWidth(context) / i;
        }
    }

    public int getItemViewType(int i) {
        return (!showCamera() || i != 0) ? 101 : 100;
    }

    public void setCompositeDisposable(@NonNull CompositeDisposable compositeDisposable) {
        this.mCompositeDisposable = compositeDisposable;
    }

    @NonNull
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_picker_item_photo, viewGroup, false));
        if (i == 100) {
            photoViewHolder.vSelected.setVisibility(8);
            photoViewHolder.ivPhoto.setScaleType(ScaleType.CENTER);
            photoViewHolder.ivPhoto.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (PhotoGridAdapter.this.onCameraClickListener != null) {
                        PhotoGridAdapter.this.onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return photoViewHolder;
    }

    public void onBindViewHolder(@NonNull final PhotoViewHolder photoViewHolder, int i) {
        final Photo photo;
        if (getItemViewType(i) == 101) {
            List currentPhotos = getCurrentPhotos();
            if (showCamera()) {
                photo = (Photo) currentPhotos.get(i - 1);
            } else {
                photo = (Photo) currentPhotos.get(i);
            }
            if (AndroidLifecycleUtils.canLoadImage(photoViewHolder.ivPhoto.getContext())) {
                RequestOptions requestOptions = new RequestOptions();
                RequestOptions dontAnimate = requestOptions.centerCrop().dontAnimate();
                int i2 = this.imageSize;
                dontAnimate.override(i2, i2).placeholder(C4558R.C4559drawable.zm_image_placeholder).error(C4558R.C4559drawable.zm_image_download_error);
                if (OsUtil.isAtLeastQ()) {
                    this.mGlide.setDefaultRequestOptions(requestOptions).load(photo.getUri()).thumbnail(0.5f).into(photoViewHolder.ivPhoto);
                } else {
                    this.mGlide.setDefaultRequestOptions(requestOptions).load(new File(photo.getPath())).thumbnail(0.5f).into(photoViewHolder.ivPhoto);
                }
            }
            boolean isSelected = isSelected(photo);
            photoViewHolder.vSelected.setSelected(isSelected);
            photoViewHolder.ivPhoto.setSelected(isSelected);
            photoViewHolder.ivPhoto.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (PhotoGridAdapter.this.onPhotoClickListener != null) {
                        int adapterPosition = photoViewHolder.getAdapterPosition();
                        if (PhotoGridAdapter.this.previewEnable) {
                            PhotoGridAdapter.this.onPhotoClickListener.onClick(view, adapterPosition, PhotoGridAdapter.this.showCamera());
                        } else {
                            photoViewHolder.vSelected.performClick();
                        }
                    }
                }
            });
            photoViewHolder.vSelected.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    final int adapterPosition = photoViewHolder.getAdapterPosition();
                    if (PhotoGridAdapter.this.mCompositeDisposable != null) {
                        PhotoGridAdapter.this.mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<Boolean>() {
                            /* JADX WARNING: Code restructure failed: missing block: B:7:0x003b, code lost:
                                if (r0.getSize() > 8388608) goto L_0x006b;
                             */
                            /* Code decompiled incorrectly, please refer to instructions dump. */
                            public void subscribe(p015io.reactivex.ObservableEmitter<java.lang.Boolean> r8) throws java.lang.Exception {
                                /*
                                    r7 = this;
                                    boolean r0 = p021us.zoom.androidlib.util.OsUtil.isAtLeastQ()
                                    r1 = 1
                                    r2 = 8388608(0x800000, double:4.144523E-317)
                                    r4 = 0
                                    if (r0 == 0) goto L_0x0040
                                    com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
                                    com.zipow.videobox.photopicker.PhotoGridAdapter$3 r5 = com.zipow.videobox.photopicker.PhotoGridAdapter.C32043.this
                                    com.zipow.videobox.photopicker.entity.Photo r5 = r6
                                    android.net.Uri r5 = r5.getUri()
                                    java.lang.String r0 = p021us.zoom.androidlib.util.FileUtils.guessContentTypeFromUri(r0, r5)
                                    java.lang.String r5 = "image/gif"
                                    boolean r0 = r5.equals(r0)
                                    if (r0 == 0) goto L_0x003e
                                    com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
                                    com.zipow.videobox.photopicker.PhotoGridAdapter$3 r5 = com.zipow.videobox.photopicker.PhotoGridAdapter.C32043.this
                                    com.zipow.videobox.photopicker.entity.Photo r5 = r6
                                    android.net.Uri r5 = r5.getUri()
                                    us.zoom.androidlib.data.FileInfo r0 = p021us.zoom.androidlib.util.FileUtils.dumpImageMetaData(r0, r5)
                                    if (r0 == 0) goto L_0x003e
                                    long r5 = r0.getSize()
                                    int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
                                    if (r0 <= 0) goto L_0x003e
                                    goto L_0x006b
                                L_0x003e:
                                    r1 = 0
                                    goto L_0x006b
                                L_0x0040:
                                    java.lang.String r0 = "image/gif"
                                    com.zipow.videobox.photopicker.PhotoGridAdapter$3 r5 = com.zipow.videobox.photopicker.PhotoGridAdapter.C32043.this
                                    com.zipow.videobox.photopicker.entity.Photo r5 = r6
                                    java.lang.String r5 = r5.getPath()
                                    java.lang.String r5 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r5)
                                    boolean r0 = r0.equals(r5)
                                    if (r0 == 0) goto L_0x006a
                                    java.io.File r0 = new java.io.File
                                    com.zipow.videobox.photopicker.PhotoGridAdapter$3 r5 = com.zipow.videobox.photopicker.PhotoGridAdapter.C32043.this
                                    com.zipow.videobox.photopicker.entity.Photo r5 = r6
                                    java.lang.String r5 = r5.getPath()
                                    r0.<init>(r5)
                                    long r5 = r0.length()
                                    int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
                                    if (r0 < 0) goto L_0x006a
                                    goto L_0x006b
                                L_0x006a:
                                    r1 = 0
                                L_0x006b:
                                    java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)
                                    r8.onNext(r0)
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.photopicker.PhotoGridAdapter.C32043.C32062.subscribe(io.reactivex.ObservableEmitter):void");
                            }
                        }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer<? super T>) new Consumer<Boolean>() {
                            public void accept(Boolean bool) throws Exception {
                                int i = 1;
                                if (bool == null || bool.booleanValue()) {
                                    Toast.makeText(VideoBoxApplication.getNonNullInstance(), C4558R.string.zm_msg_img_too_large, 1).show();
                                    return;
                                }
                                if (PhotoGridAdapter.this.canChangeCheckState(PhotoGridAdapter.this.getSelectedPhotos().size() + (PhotoGridAdapter.this.isSelected(photo) ? -1 : 1)) && PhotoGridAdapter.this.mMaxSelectedCount > 1) {
                                    PhotoGridAdapter.this.toggleSelection(photo);
                                    PhotoGridAdapter.this.notifyItemChanged(adapterPosition);
                                }
                                if (PhotoGridAdapter.this.mMaxSelectedCount <= 1) {
                                    PhotoGridAdapter.this.clearSelection();
                                    PhotoGridAdapter.this.toggleSelection(photo);
                                    PhotoGridAdapter.this.notifyDataSetChanged();
                                }
                                if (PhotoGridAdapter.this.mOnItemCheckStateChangedListener != null) {
                                    OnItemCheckStateChangedListener access$700 = PhotoGridAdapter.this.mOnItemCheckStateChangedListener;
                                    int i2 = adapterPosition;
                                    Photo photo = photo;
                                    int size = PhotoGridAdapter.this.getSelectedPhotos().size();
                                    if (PhotoGridAdapter.this.isSelected(photo)) {
                                        i = -1;
                                    }
                                    access$700.onItemCheckStateChanged(i2, photo, size + i);
                                }
                                boolean isCheckStateEnable = PhotoGridAdapter.this.isCheckStateEnable();
                                if (isCheckStateEnable != PhotoGridAdapter.this.checkStateEnable) {
                                    PhotoGridAdapter.this.notifyDataSetChanged();
                                    PhotoGridAdapter.this.checkStateEnable = isCheckStateEnable;
                                }
                            }
                        }));
                    }
                }
            });
            boolean z = this.checkStateEnable;
            if (!z) {
                z = isSelected;
            }
            photoViewHolder.vSelected.setClickable(z);
            photoViewHolder.ivPhoto.setClickable(z);
            photoViewHolder.cover.setVisibility(z ? 8 : 0);
            return;
        }
        photoViewHolder.ivPhoto.setImageResource(C4558R.C4559drawable.zm_picker_camera);
    }

    public boolean isCheckStateEnable() {
        int selectedItemCount = getSelectedItemCount();
        int i = this.mMaxSelectedCount;
        return selectedItemCount < i || i <= 1;
    }

    public boolean canChangeCheckState(int i) {
        int i2 = this.mMaxSelectedCount;
        return i <= i2 || i2 <= 1;
    }

    public void resetCheckState() {
        this.checkStateEnable = isCheckStateEnable();
    }

    public int getItemCount() {
        int size = this.photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        return showCamera() ? size + 1 : size;
    }

    public void setOnItemCheckStateChangedListener(@Nullable OnItemCheckStateChangedListener onItemCheckStateChangedListener) {
        this.mOnItemCheckStateChangedListener = onItemCheckStateChangedListener;
    }

    public void setOnPhotoClickListener(@Nullable OnPhotoClickListener onPhotoClickListener2) {
        this.onPhotoClickListener = onPhotoClickListener2;
    }

    public void setOnCameraClickListener(@Nullable OnClickListener onClickListener) {
        this.onCameraClickListener = onClickListener;
    }

    @NonNull
    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> arrayList = new ArrayList<>(getSelectedItemCount());
        for (String add : this.selectedPhotos) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public void setSelectedPhotoPaths(@NonNull List<String> list) {
        if (this.selectedPhotos != null) {
            this.selectedPhotos.clear();
        } else {
            this.selectedPhotos = new ArrayList();
        }
        this.selectedPhotos.addAll(list);
        notifyDataSetChanged();
    }

    public void setShowCamera(boolean z) {
        this.hasCamera = z;
    }

    public void setPreviewEnable(boolean z) {
        this.previewEnable = z;
    }

    public boolean showCamera() {
        return this.hasCamera && this.currentDirectoryIndex == 0;
    }

    public void onViewRecycled(@NonNull PhotoViewHolder photoViewHolder) {
        this.mGlide.clear((View) photoViewHolder.ivPhoto);
        super.onViewRecycled(photoViewHolder);
    }
}
