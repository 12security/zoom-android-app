package com.zipow.videobox.photopicker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.bumptech.glide.RequestManager;
import com.zipow.videobox.photopicker.entity.Photo;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ZMGlideUtil;
import com.zipow.videobox.util.photopicker.ImageCaptureManager;
import com.zipow.videobox.util.photopicker.MediaStoreHelper;
import com.zipow.videobox.util.photopicker.MediaStoreHelper.PhotosResultCallback;
import com.zipow.videobox.util.photopicker.PermissionsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p015io.reactivex.disposables.CompositeDisposable;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhotoPickerFragment extends Fragment {
    public static int COUNT_MAX_PHOTO_DIRECTORY = 6;
    private static final String TAG_WAITING = "photoPickerFragment_loadAllPicPath";
    /* access modifiers changed from: private */
    public int SCROLL_THRESHOLD = 30;
    private TextView mBtnPhotoDirectory;
    @Nullable
    private ImageCaptureManager mCaptureManager;
    /* access modifiers changed from: private */
    public CheckBox mCheckBox_Source;
    int mColumn;
    @NonNull
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    /* access modifiers changed from: private */
    public RequestManager mGlideRequestManager;
    /* access modifiers changed from: private */
    @Nullable
    public PopupDirectoryListAdapter mListAdapter;
    int mMaxSelectedCount;
    /* access modifiers changed from: private */
    @Nullable
    public List<PhotoDirectory> mPhotoDirectories;
    /* access modifiers changed from: private */
    @Nullable
    public ListPopupWindow mPhotoDirsPopupWindow;
    /* access modifiers changed from: private */
    @Nullable
    public PhotoGridAdapter mPhotoGridAdapter;
    private boolean mSourceChecked = false;
    private TextView mTextView_Preview;
    private TextView mTextView_Send;
    private TextView mTextView_Title;

    @NonNull
    public static PhotoPickerFragment newInstance(boolean z, boolean z2, boolean z3, int i, int i2, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PhotoPicker.EXTRA_SHOW_CAMERA, z);
        bundle.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, z2);
        bundle.putBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, z3);
        bundle.putInt(PhotoPicker.EXTRA_GRID_COLUMN, i);
        bundle.putInt("MAX_COUNT", i2);
        bundle.putStringArrayList("android.speech.extra.ORIGIN", arrayList);
        PhotoPickerFragment photoPickerFragment = new PhotoPickerFragment();
        photoPickerFragment.setArguments(bundle);
        return photoPickerFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        this.mGlideRequestManager = ZMGlideUtil.getGlideRequestManager((Fragment) this);
        this.mPhotoDirectories = new ArrayList();
        Bundle arguments = getArguments();
        this.mMaxSelectedCount = arguments.getInt("MAX_COUNT", 9);
        this.mColumn = arguments.getInt(PhotoPicker.EXTRA_GRID_COLUMN, 4);
        boolean z = arguments.getBoolean(PhotoPicker.EXTRA_SHOW_CAMERA, true);
        boolean z2 = arguments.getBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);
        PhotoGridAdapter photoGridAdapter = new PhotoGridAdapter(getActivity(), this.mGlideRequestManager, this.mPhotoDirectories, arguments.getStringArrayList("android.speech.extra.ORIGIN"), this.mColumn, this.mMaxSelectedCount);
        this.mPhotoGridAdapter = photoGridAdapter;
        this.mPhotoGridAdapter.setShowCamera(z);
        this.mPhotoGridAdapter.setPreviewEnable(z2);
        this.mPhotoGridAdapter.setOnItemCheckStateChangedListener(new OnItemCheckStateChangedListener() {
            public void onItemCheckStateChanged(int i, Photo photo, int i2) {
                if (PhotoPickerFragment.this.isAdded()) {
                    PhotoPickerFragment.this.updateSelectState();
                }
            }
        });
        this.mPhotoGridAdapter.setCompositeDisposable(this.mCompositeDisposable);
        this.mListAdapter = new PopupDirectoryListAdapter(this.mGlideRequestManager, this.mPhotoDirectories);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, getArguments().getBoolean(PhotoPicker.EXTRA_SHOW_GIF));
        MediaStoreHelper.getPhotoDirs(getActivity(), bundle2, new PhotosResultCallback() {
            public void onLoadStart() {
                UIUtil.showWaitingDialog(PhotoPickerFragment.this.getFragmentManager(), C4558R.string.zm_msg_waiting, PhotoPickerFragment.TAG_WAITING);
            }

            public void onResultCallback(@NonNull List<PhotoDirectory> list) {
                UIUtil.dismissWaitingDialog(PhotoPickerFragment.this.getFragmentManager(), PhotoPickerFragment.TAG_WAITING);
                if (PhotoPickerFragment.this.mPhotoDirectories != null) {
                    PhotoPickerFragment.this.mPhotoDirectories.clear();
                    PhotoPickerFragment.this.mPhotoDirectories.addAll(list);
                    PhotoPickerFragment.this.mPhotoGridAdapter.notifyDataSetChanged();
                    PhotoPickerFragment.this.setCurrentPhotoDirectory(0);
                    PhotoPickerFragment.this.mListAdapter.notifyDataSetChanged();
                    PhotoPickerFragment.this.adjustHeight();
                }
            }

            public void onLoadError(String str) {
                UIUtil.dismissWaitingDialog(PhotoPickerFragment.this.getFragmentManager(), PhotoPickerFragment.TAG_WAITING);
                if (PhotoPickerFragment.this.getContext() != null) {
                    Toast.makeText(PhotoPickerFragment.this.getContext(), C4558R.string.zm_pbx_switch_to_carrier_error_des_102668, 1).show();
                }
            }
        });
        this.mCaptureManager = new ImageCaptureManager(getActivity());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_picker_fragment_photo_picker, viewGroup, false);
        this.mTextView_Send = (TextView) inflate.findViewById(C4558R.C4560id.btnSend);
        this.mTextView_Send.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PhotoPickerActivity photoPickerActivity = (PhotoPickerActivity) PhotoPickerFragment.this.getActivity();
                if (photoPickerActivity != null) {
                    photoPickerActivity.completeSelect(PhotoPickerFragment.this.mCheckBox_Source.isChecked(), PhotoPickerFragment.this.mPhotoGridAdapter.getSelectedPhotoPaths());
                }
            }
        });
        this.mTextView_Title = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mTextView_Title.setText(getString(C4558R.string.zm_picker_photos_title));
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentActivity activity = PhotoPickerFragment.this.getActivity();
                if (activity != null) {
                    activity.onBackPressed();
                }
            }
        });
        this.mTextView_Preview = (TextView) inflate.findViewById(C4558R.C4560id.btnPreview);
        this.mCheckBox_Source = (CheckBox) inflate.findViewById(C4558R.C4560id.rbSource);
        this.mTextView_Preview.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ArrayList selectedPhotoPaths = PhotoPickerFragment.this.mPhotoGridAdapter.getSelectedPhotoPaths();
                PhotoPickerFragment.this.openPagerFragment(0, selectedPhotoPaths, selectedPhotoPaths);
            }
        });
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(C4558R.C4560id.rv_photos);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(this.mColumn, 1);
        staggeredGridLayoutManager.setGapStrategy(2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(this.mPhotoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mBtnPhotoDirectory = (TextView) inflate.findViewById(C4558R.C4560id.button);
        this.mPhotoDirsPopupWindow = new ListPopupWindow(getActivity());
        this.mPhotoDirsPopupWindow.setWidth(-1);
        this.mPhotoDirsPopupWindow.setAnchorView(inflate.findViewById(C4558R.C4560id.bottomBar));
        this.mPhotoDirsPopupWindow.setAdapter(this.mListAdapter);
        this.mPhotoDirsPopupWindow.setModal(true);
        if (OsUtil.isAtLeastKLP()) {
            this.mPhotoDirsPopupWindow.setDropDownGravity(80);
        }
        this.mPhotoDirsPopupWindow.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PhotoPickerFragment.this.mPhotoDirsPopupWindow.dismiss();
                PhotoPickerFragment.this.setCurrentPhotoDirectory(i);
                PhotoPickerFragment.this.mPhotoGridAdapter.setCurrentDirectoryIndex(i);
                PhotoPickerFragment.this.mPhotoGridAdapter.notifyDataSetChanged();
            }
        });
        this.mPhotoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            public void onClick(View view, int i, boolean z) {
                if (z) {
                    i--;
                }
                PhotoPickerFragment photoPickerFragment = PhotoPickerFragment.this;
                photoPickerFragment.openPagerFragment(i, photoPickerFragment.mPhotoGridAdapter.getCurrentPhotoPaths(), PhotoPickerFragment.this.mPhotoGridAdapter.getSelectedPhotoPaths());
            }
        });
        this.mPhotoGridAdapter.setOnCameraClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (PermissionsUtils.checkCameraPermission(PhotoPickerFragment.this) && PermissionsUtils.checkWriteStoragePermission(PhotoPickerFragment.this)) {
                    PhotoPickerFragment.this.openCamera();
                }
            }
        });
        this.mBtnPhotoDirectory.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FragmentActivity activity = PhotoPickerFragment.this.getActivity();
                if (PhotoPickerFragment.this.mPhotoDirsPopupWindow.isShowing()) {
                    PhotoPickerFragment.this.mPhotoDirsPopupWindow.dismiss();
                } else if (activity != null && !activity.isFinishing()) {
                    PhotoPickerFragment.this.adjustHeight();
                    PhotoPickerFragment.this.mPhotoDirsPopupWindow.show();
                }
            }
        });
        recyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (Math.abs(i2) > PhotoPickerFragment.this.SCROLL_THRESHOLD) {
                    PhotoPickerFragment.this.mGlideRequestManager.pauseRequests();
                } else {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                if (i == 0) {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void setCurrentPhotoDirectory(int i) {
        FragmentActivity activity = getActivity();
        if (activity instanceof PhotoPickerActivity) {
            List<PhotoDirectory> list = this.mPhotoDirectories;
            if (list == null || list.isEmpty()) {
                ((PhotoPickerActivity) activity).setSelectedPhotoDirectory(null);
                return;
            }
            ((PhotoPickerActivity) activity).setSelectedPhotoDirectory((PhotoDirectory) this.mPhotoDirectories.get(i));
            updateSelectedDirectory();
        }
    }

    /* access modifiers changed from: private */
    public void openCamera() {
        try {
            ActivityStartHelper.startActivityForResult((Fragment) this, this.mCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            if (this.mCaptureManager == null) {
                this.mCaptureManager = new ImageCaptureManager(getActivity());
            }
            this.mCaptureManager.galleryAddPic();
            if (this.mPhotoDirectories.size() > 0) {
                String currentPhotoPath = this.mCaptureManager.getCurrentPhotoPath();
                PhotoDirectory photoDirectory = (PhotoDirectory) this.mPhotoDirectories.get(0);
                photoDirectory.getPhotos().add(0, new Photo(currentPhotoPath.hashCode(), currentPhotoPath));
                photoDirectory.setCoverPath(currentPhotoPath);
                this.mPhotoGridAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (iArr.length > 0 && iArr[0] == 0) {
            if ((i == 1 || i == 3) && PermissionsUtils.checkWriteStoragePermission(this) && PermissionsUtils.checkCameraPermission(this)) {
                openCamera();
            }
        }
    }

    @Nullable
    public PhotoGridAdapter getPhotoGridAdapter() {
        return this.mPhotoGridAdapter;
    }

    public void updateSelectState() {
        PhotoGridAdapter photoGridAdapter = this.mPhotoGridAdapter;
        int selectedItemCount = photoGridAdapter != null ? photoGridAdapter.getSelectedItemCount() : 0;
        TextView textView = this.mTextView_Preview;
        if (textView != null) {
            textView.setEnabled(selectedItemCount > 0);
            this.mTextView_Preview.setText(getString(C4558R.string.zm_picker_preview_with_count, Integer.valueOf(selectedItemCount)));
        }
        TextView textView2 = this.mTextView_Send;
        if (textView2 != null) {
            textView2.setEnabled(selectedItemCount > 0);
            this.mTextView_Send.setText(getString(C4558R.string.zm_picker_done_with_count, Integer.valueOf(selectedItemCount)));
        }
    }

    private void updateSelectedDirectory() {
        FragmentActivity activity = getActivity();
        if (activity instanceof PhotoPickerActivity) {
            PhotoDirectory selectedPhotoDirectory = ((PhotoPickerActivity) activity).getSelectedPhotoDirectory();
            if (selectedPhotoDirectory != null) {
                this.mBtnPhotoDirectory.setText(selectedPhotoDirectory.getName());
            }
        }
    }

    public void onResume() {
        super.onResume();
        updateSelectState();
        updateSelectedDirectory();
        this.mCheckBox_Source.setChecked(this.mSourceChecked);
    }

    public void onPause() {
        super.onPause();
        this.mSourceChecked = this.mCheckBox_Source.isChecked();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        this.mCaptureManager.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    public void onViewStateRestored(Bundle bundle) {
        this.mCaptureManager.onRestoreInstanceState(bundle);
        super.onViewStateRestored(bundle);
    }

    @NonNull
    public ArrayList<String> getSelectedPhotoPaths() {
        return this.mPhotoGridAdapter.getSelectedPhotoPaths();
    }

    public void adjustHeight() {
        PopupDirectoryListAdapter popupDirectoryListAdapter = this.mListAdapter;
        if (popupDirectoryListAdapter != null) {
            int count = popupDirectoryListAdapter.getCount();
            int i = COUNT_MAX_PHOTO_DIRECTORY;
            if (count >= i) {
                count = i;
            }
            ListPopupWindow listPopupWindow = this.mPhotoDirsPopupWindow;
            if (listPopupWindow != null) {
                listPopupWindow.setHeight(count * getResources().getDimensionPixelOffset(C4558R.dimen.zm_picker_item_directory_height));
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        List<PhotoDirectory> list = this.mPhotoDirectories;
        if (list != null) {
            for (PhotoDirectory photoDirectory : list) {
                photoDirectory.getPhotoPaths().clear();
                photoDirectory.getPhotos().clear();
                photoDirectory.setPhotos(null);
            }
            this.mPhotoDirectories.clear();
            this.mPhotoDirectories = null;
            this.mCompositeDisposable.dispose();
        }
    }

    /* access modifiers changed from: private */
    public void resumeRequestsIfNotDestroyed() {
        if (AndroidLifecycleUtils.canLoadImage((Fragment) this)) {
            this.mGlideRequestManager.resumeRequests();
        }
    }

    public void resetSelectedPhotoPaths(@NonNull List<String> list) {
        PhotoGridAdapter photoGridAdapter = this.mPhotoGridAdapter;
        if (photoGridAdapter != null) {
            photoGridAdapter.setSelectedPhotoPaths(list);
            this.mPhotoGridAdapter.resetCheckState();
        }
    }

    public void setSourceChecked(boolean z) {
        this.mSourceChecked = z;
    }

    /* access modifiers changed from: private */
    public void openPagerFragment(int i, @NonNull List<String> list, @NonNull List<String> list2) {
        PhotoPickerActivity photoPickerActivity = (PhotoPickerActivity) getActivity();
        if (photoPickerActivity != null) {
            photoPickerActivity.addImagePagerFragment(PhotoPagerFragment.newInstance(list, i, list2, this.mMaxSelectedCount, this.mCheckBox_Source.isChecked(), true));
        }
    }
}
