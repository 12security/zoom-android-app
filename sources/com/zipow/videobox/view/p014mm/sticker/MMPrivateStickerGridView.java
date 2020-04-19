package com.zipow.videobox.view.p014mm.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.StickerInfo;
import com.zipow.videobox.ptapp.IMProtos.StickerInfoList;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.LazyLoadDrawable;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper;
import com.zipow.videobox.view.p014mm.PendingFileDataHelper.PendingFileInfo;
import com.zipow.videobox.view.p014mm.sticker.StickerManager.PrivateStickerComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.sticker.MMPrivateStickerGridView */
public class MMPrivateStickerGridView extends GridView implements OnItemClickListener {
    private MMPrivateStickerFragment mFragment;
    @NonNull
    private ArrayList<String> mSelectStickers = new ArrayList<>();
    private StickerAdapter mStickerAdapter;

    /* renamed from: com.zipow.videobox.view.mm.sticker.MMPrivateStickerGridView$ItemBean */
    static class ItemBean {
        public static final int TYPE_ACTION_ADD_STICKER = 1;
        public static final int TYPE_DATA = 0;
        public static final int TYPE_UPLOAD = 2;
        /* access modifiers changed from: private */
        @Nullable
        public String path;
        /* access modifiers changed from: private */
        public int ratio;
        /* access modifiers changed from: private */
        public String stickerId;
        /* access modifiers changed from: private */
        public int type;

        ItemBean(int i, String str) {
            this.type = i;
            this.stickerId = str;
        }

        /* access modifiers changed from: 0000 */
        public boolean isSameBean(@Nullable ItemBean itemBean) {
            boolean z = false;
            if (itemBean == null) {
                return false;
            }
            if (this.type == itemBean.type && StringUtil.isSameString(this.stickerId, itemBean.stickerId)) {
                z = true;
            }
            return z;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.sticker.MMPrivateStickerGridView$StickerAdapter */
    static class StickerAdapter extends BaseAdapter {
        private static final String TAG_ADD_STICKER = "addSticker";
        private static final String TAG_STICKER = "sticker";
        private static final String TAG_UPLOAD_STICKER = "uploadSticker";
        private Context mContext;
        @NonNull
        private List<String> mSelectStickers = new ArrayList();
        private List<ItemBean> mStickers;

        public long getItemId(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 3;
        }

        StickerAdapter(List<ItemBean> list, Context context) {
            this.mStickers = list;
            this.mContext = context;
        }

        public void setSelectStickes(@Nullable List<String> list) {
            if (list == null) {
                this.mSelectStickers.clear();
            } else {
                this.mSelectStickers = list;
            }
        }

        public void addSelectSticker(String str) {
            this.mSelectStickers.add(str);
        }

        public boolean hasSticker(String str) {
            for (int i = 0; i < this.mStickers.size(); i++) {
                if (StringUtil.isSameString(((ItemBean) this.mStickers.get(i)).stickerId, str)) {
                    return true;
                }
            }
            return false;
        }

        public void updateOrAddSticker(@Nullable ItemBean itemBean) {
            if (itemBean != null) {
                if (this.mStickers == null) {
                    this.mStickers = new ArrayList();
                }
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= this.mStickers.size()) {
                        break;
                    } else if (StringUtil.isSameString(((ItemBean) this.mStickers.get(i)).stickerId, itemBean.stickerId)) {
                        this.mStickers.set(i, itemBean);
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!z) {
                    this.mStickers.add(itemBean);
                }
            }
        }

        public void updateOrAddUploadSticker(String str, int i) {
            List<ItemBean> list = this.mStickers;
            if (list != null) {
                ItemBean itemBean = null;
                Iterator it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ItemBean itemBean2 = (ItemBean) it.next();
                    if (itemBean2.type == 2 && StringUtil.isSameString(str, itemBean2.stickerId)) {
                        itemBean = itemBean2;
                        break;
                    }
                }
                if (itemBean == null) {
                    itemBean = new ItemBean(2, str);
                    this.mStickers.add(itemBean);
                }
                itemBean.ratio = i;
            }
        }

        public void onAddSticker(ItemBean itemBean) {
            if (this.mStickers == null) {
                this.mStickers = new ArrayList();
            }
            this.mStickers.add(itemBean);
        }

        public void OnNewStickerUploaded(String str, int i, String str2) {
            ItemBean itemBean;
            Iterator it = this.mStickers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    itemBean = null;
                    break;
                }
                itemBean = (ItemBean) it.next();
                if (itemBean.type == 2 && StringUtil.isSameString(str, itemBean.stickerId)) {
                    break;
                }
            }
            if (i != 0) {
                if (itemBean != null) {
                    this.mStickers.remove(itemBean);
                }
                return;
            }
            if (itemBean != null) {
                itemBean.type = 0;
                itemBean.stickerId = str2;
            } else {
                this.mStickers.add(new ItemBean(0, str2));
            }
        }

        @Nullable
        public ItemBean removeSticker(String str) {
            ItemBean itemBean = null;
            if (this.mStickers == null) {
                return null;
            }
            int i = 0;
            while (i < this.mStickers.size()) {
                ItemBean itemBean2 = (ItemBean) this.mStickers.get(i);
                if (itemBean2 == null) {
                    this.mStickers.remove(i);
                    i--;
                } else if (StringUtil.isSameString(str, itemBean2.stickerId)) {
                    this.mStickers.remove(i);
                    i--;
                    itemBean = itemBean2;
                }
                i++;
            }
            return itemBean;
        }

        public int getItemViewType(int i) {
            ItemBean item = getItem(i);
            if (item == null) {
                return 0;
            }
            return item.type;
        }

        public int getCount() {
            List<ItemBean> list = this.mStickers;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Nullable
        public ItemBean getItem(int i) {
            List<ItemBean> list = this.mStickers;
            if (list == null || list.size() == 0 || this.mStickers.size() <= i) {
                return null;
            }
            return (ItemBean) this.mStickers.get(i);
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            ItemBean item = getItem(i);
            if (item == null) {
                return new View(this.mContext);
            }
            if (item.type == 0) {
                return createStickerView(item, view, viewGroup);
            }
            if (item.type == 1) {
                return createAddStickerView(view, viewGroup);
            }
            if (item.type == 2) {
                return createUploadView(item, view, viewGroup);
            }
            return new View(this.mContext);
        }

        @NonNull
        private View createUploadView(@Nullable ItemBean itemBean, @Nullable View view, ViewGroup viewGroup) {
            LinearLayout linearLayout;
            ImageView imageView;
            if (itemBean == null) {
                return new View(this.mContext);
            }
            if (view == null || !TAG_UPLOAD_STICKER.equals(view.getTag())) {
                linearLayout = new LinearLayout(this.mContext);
                linearLayout.setTag(TAG_UPLOAD_STICKER);
                linearLayout.setGravity(17);
                linearLayout.setPadding(0, UIUtil.dip2px(this.mContext, 5.0f), 0, UIUtil.dip2px(this.mContext, 5.0f));
                LinearLayout linearLayout2 = new LinearLayout(this.mContext);
                linearLayout2.setGravity(17);
                linearLayout.addView(linearLayout2, new LayoutParams(UIUtil.dip2px(this.mContext, 80.0f), UIUtil.dip2px(this.mContext, 80.0f)));
                int dip2px = UIUtil.dip2px(this.mContext, 20.0f);
                float[] fArr = new float[8];
                for (int i = 0; i < fArr.length; i++) {
                    fArr[i] = (float) dip2px;
                }
                ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(fArr, null, null));
                shapeDrawable.getPaint().setColor(this.mContext.getResources().getColor(C4558R.color.zm_gray_3));
                if (OsUtil.isAtLeastJB()) {
                    linearLayout2.setBackground(shapeDrawable);
                } else {
                    linearLayout2.setBackgroundDrawable(shapeDrawable);
                }
                imageView = new ImageView(this.mContext);
                linearLayout2.addView(imageView);
            } else {
                linearLayout = (LinearLayout) view;
                imageView = (ImageView) ((LinearLayout) linearLayout.getChildAt(0)).getChildAt(0);
            }
            Object tag = imageView.getTag();
            if ((tag instanceof Integer) && ((Integer) tag).intValue() == itemBean.ratio) {
                return linearLayout;
            }
            int dip2px2 = UIUtil.dip2px(this.mContext, 25.0f);
            int i2 = dip2px2 * 2;
            Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.mContext.getResources().getColor(C4558R.color.zm_white));
            float f = (float) dip2px2;
            canvas.drawCircle(f, f, f, paint);
            paint.setColor(this.mContext.getResources().getColor(C4558R.color.zm_gray_3));
            int dip2px3 = UIUtil.dip2px(this.mContext, 5.0f);
            float f2 = (float) dip2px3;
            float f3 = (float) (i2 - dip2px3);
            float access$100 = ((float) itemBean.ratio) * 3.6f;
            canvas.drawArc(new RectF(f2, f2, f3, f3), access$100 - 0.049804688f, 360.0f - access$100, true, paint);
            imageView.setImageBitmap(createBitmap);
            return linearLayout;
        }

        @NonNull
        private View createAddStickerView(@Nullable View view, ViewGroup viewGroup) {
            if (view != null && TAG_ADD_STICKER.equals(view.getTag())) {
                return view;
            }
            LinearLayout linearLayout = new LinearLayout(this.mContext);
            linearLayout.setTag(TAG_ADD_STICKER);
            linearLayout.setMinimumHeight(UIUtil.dip2px(this.mContext, 90.0f));
            linearLayout.setMinimumWidth(UIUtil.dip2px(this.mContext, 80.0f));
            ImageView imageView = new ImageView(this.mContext);
            imageView.setImageResource(C4558R.C4559drawable.zm_mm_sticker_add);
            linearLayout.addView(imageView);
            linearLayout.setGravity(17);
            return linearLayout;
        }

        @NonNull
        private View createStickerView(@Nullable ItemBean itemBean, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !TAG_STICKER.equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_sticker_setting_item, null);
                view.setTag(TAG_STICKER);
            }
            if (itemBean == null || StringUtil.isEmptyOrNull(itemBean.stickerId)) {
                return view;
            }
            String access$000 = itemBean.path;
            View findViewById = view.findViewById(C4558R.C4560id.progressBar);
            View findViewById2 = view.findViewById(C4558R.C4560id.selectBGView);
            View findViewById3 = view.findViewById(C4558R.C4560id.selectBGLineView);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgSticker);
            boolean contains = this.mSelectStickers.contains(itemBean.stickerId);
            findViewById2.setVisibility(contains ? 0 : 4);
            findViewById3.setVisibility(contains ? 0 : 4);
            if (StringUtil.isEmptyOrNull(access$000) || !ImageUtil.isValidImageFile(access$000)) {
                findViewById.setVisibility(0);
                imageView.setVisibility(4);
            } else {
                findViewById.setVisibility(4);
                imageView.setVisibility(0);
                LazyLoadDrawable lazyLoadDrawable = new LazyLoadDrawable(access$000);
                lazyLoadDrawable.setMaxArea(UIUtil.dip2px(this.mContext, 6400.0f));
                imageView.setImageDrawable(lazyLoadDrawable);
            }
            return view;
        }
    }

    public MMPrivateStickerGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMPrivateStickerGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMPrivateStickerGridView(Context context) {
        super(context);
        init();
    }

    public void setParentFragment(MMPrivateStickerFragment mMPrivateStickerFragment) {
        this.mFragment = mMPrivateStickerFragment;
    }

    public void onAddSticker(@Nullable String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            StickerAdapter stickerAdapter = this.mStickerAdapter;
            boolean z = true;
            if (NetworkUtil.getDataNetworkType(getContext()) != 1) {
                z = false;
            }
            stickerAdapter.onAddSticker(createStickerCell(zoomFileContentMgr, str, z, PTApp.getInstance().getZoomPrivateStickerMgr()));
            this.mStickerAdapter.notifyDataSetChanged();
        }
    }

    public void OnNewStickerUploaded(String str, int i, @Nullable String str2) {
        this.mStickerAdapter.removeSticker(str);
        if (i == 0) {
            onAddSticker(str2);
        }
        this.mStickerAdapter.notifyDataSetChanged();
    }

    public void onRemoveSticker(String str) {
        if (this.mStickerAdapter.removeSticker(str) != null) {
            this.mStickerAdapter.notifyDataSetChanged();
        }
    }

    public void updateOrAddUploadSticker(@Nullable String str, int i) {
        boolean z = false;
        for (PendingFileInfo reqId : PendingFileDataHelper.getInstance().getUploadPendingStickerInfos()) {
            if (StringUtil.isSameString(reqId.getReqId(), str) && str != null) {
                this.mStickerAdapter.updateOrAddUploadSticker(str, i);
                z = true;
            }
        }
        if (z) {
            this.mStickerAdapter.notifyDataSetChanged();
        }
    }

    public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
        if (i != 0) {
            this.mStickerAdapter.removeSticker(str2);
        }
    }

    public void removeSticker(@NonNull List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            while (list.size() > 0) {
                String str = (String) list.get(0);
                this.mStickerAdapter.removeSticker(str);
                this.mSelectStickers.remove(str);
            }
            this.mStickerAdapter.notifyDataSetChanged();
            MMPrivateStickerFragment mMPrivateStickerFragment = this.mFragment;
            if (mMPrivateStickerFragment != null) {
                mMPrivateStickerFragment.onStickerSelected(this.mSelectStickers);
            }
        }
    }

    public void onUpdateSticker(@Nullable String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            boolean z = true;
            if (NetworkUtil.getDataNetworkType(getContext()) != 1) {
                z = false;
            }
            this.mStickerAdapter.updateOrAddSticker(createStickerCell(zoomFileContentMgr, str, z, PTApp.getInstance().getZoomPrivateStickerMgr()));
            this.mStickerAdapter.notifyDataSetChanged();
        }
    }

    public boolean hasSticker(String str) {
        return this.mStickerAdapter.hasSticker(str);
    }

    @NonNull
    public List<String> getSelectStickers() {
        return this.mSelectStickers;
    }

    public void refreshData() {
        ItemBean itemBean;
        MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
        if (zoomPrivateStickerMgr != null) {
            StickerInfoList stickers = zoomPrivateStickerMgr.getStickers();
            if (stickers != null) {
                boolean z = NetworkUtil.getDataNetworkType(getContext()) == 1;
                ArrayList arrayList = new ArrayList();
                arrayList.add(new ItemBean(1, null));
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    ArrayList<StickerEvent> arrayList2 = new ArrayList<>();
                    for (int i = 0; i < stickers.getStickersCount(); i++) {
                        StickerInfo stickers2 = stickers.getStickers(i);
                        if (stickers2 != null) {
                            StickerEvent stickerEvent = new StickerEvent(stickers2.getFileId());
                            stickerEvent.setStickerPath(stickers2.getUploadingPath());
                            stickerEvent.setStatus(stickers2.getStatus());
                            arrayList2.add(stickerEvent);
                        }
                    }
                    Collections.sort(arrayList2, new PrivateStickerComparator());
                    for (StickerEvent stickerEvent2 : arrayList2) {
                        String stickerId = stickerEvent2.getStickerId();
                        String stickerPath = stickerEvent2.getStickerPath();
                        if (StringUtil.isEmptyOrNull(stickerPath)) {
                            itemBean = createStickerCell(zoomFileContentMgr, stickerId, z, zoomPrivateStickerMgr);
                        } else {
                            ItemBean itemBean2 = new ItemBean(0, stickerId);
                            itemBean2.path = stickerPath;
                            itemBean = itemBean2;
                        }
                        if (itemBean != null) {
                            arrayList.add(itemBean);
                        }
                    }
                    List<PendingFileInfo> uploadPendingStickerInfos = PendingFileDataHelper.getInstance().getUploadPendingStickerInfos();
                    if (!CollectionsUtil.isListEmpty(uploadPendingStickerInfos)) {
                        for (PendingFileInfo pendingFileInfo : uploadPendingStickerInfos) {
                            ItemBean itemBean3 = new ItemBean(2, pendingFileInfo.getReqId());
                            itemBean3.ratio = pendingFileInfo.getRatio();
                            arrayList.add(itemBean3);
                        }
                    }
                    this.mStickerAdapter = new StickerAdapter(arrayList, getContext());
                    setAdapter(this.mStickerAdapter);
                    this.mStickerAdapter.setSelectStickes(this.mSelectStickers);
                }
            }
        }
    }

    @Nullable
    private ItemBean createStickerCell(@NonNull MMFileContentMgr mMFileContentMgr, @Nullable String str, boolean z, @Nullable MMPrivateStickerMgr mMPrivateStickerMgr) {
        ZoomFile fileWithWebFileID = mMFileContentMgr.getFileWithWebFileID(str);
        if (fileWithWebFileID == null) {
            return null;
        }
        MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithWebFileID, mMFileContentMgr);
        ItemBean itemBean = new ItemBean(0, str);
        String picturePreviewPath = initWithZoomFile.getPicturePreviewPath();
        String localPath = initWithZoomFile.getLocalPath();
        if (mMPrivateStickerMgr != null && z && StringUtil.isEmptyOrNull(localPath) && !StickerManager.isStickerLocalPathDownloading(str)) {
            StickerManager.addStickerLocalPathReqId(str, mMPrivateStickerMgr.downloadSticker(str, PendingFileDataHelper.getContenFilePath(str, fileWithWebFileID.getFileName())));
        }
        if (StringUtil.isEmptyOrNull(picturePreviewPath)) {
            if (mMPrivateStickerMgr != null && !StickerManager.isStickerPreviewDownloading(str)) {
                StickerManager.addStickerPreviewReqId(str, mMPrivateStickerMgr.downloadStickerPreview(str));
            }
            if (!StringUtil.isEmptyOrNull(localPath)) {
                itemBean.path = localPath;
            }
        } else {
            itemBean.path = picturePreviewPath;
        }
        return itemBean;
    }

    public void onStickerDownloaded(String str) {
        String stickerPreviewFileIdByReqId = StickerManager.getStickerPreviewFileIdByReqId(str);
        if (StringUtil.isEmptyOrNull(stickerPreviewFileIdByReqId)) {
            stickerPreviewFileIdByReqId = StickerManager.getStickerLocalPathFileIdByReqId(str);
        }
        if (!StringUtil.isEmptyOrNull(stickerPreviewFileIdByReqId)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                StickerAdapter stickerAdapter = this.mStickerAdapter;
                boolean z = true;
                if (NetworkUtil.getDataNetworkType(getContext()) != 1) {
                    z = false;
                }
                stickerAdapter.updateOrAddSticker(createStickerCell(zoomFileContentMgr, stickerPreviewFileIdByReqId, z, PTApp.getInstance().getZoomPrivateStickerMgr()));
                this.mStickerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void init() {
        setColumnWidth(UIUtil.dip2px(getContext(), 80.0f));
        setNumColumns(-1);
        setStretchMode(2);
        setVerticalSpacing(UIUtil.dip2px(getContext(), 10.0f));
        setHorizontalSpacing(UIUtil.dip2px(getContext(), 5.0f));
        setOnItemClickListener(this);
        setSelector(new ColorDrawable(getResources().getColor(C4558R.color.zm_transparent)));
        refreshData();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ItemBean item = this.mStickerAdapter.getItem(i);
        if (item != null) {
            switch (item.type) {
                case 0:
                    onStickerSelect(item.stickerId);
                    break;
                case 1:
                    onAddStickerClick();
                    break;
            }
        }
    }

    private void onAddStickerClick() {
        MMPrivateStickerFragment mMPrivateStickerFragment = this.mFragment;
        if (mMPrivateStickerFragment != null) {
            mMPrivateStickerFragment.onAddStickerClick();
        }
    }

    private void onStickerSelect(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (this.mSelectStickers.contains(str)) {
                this.mSelectStickers.remove(str);
            } else {
                this.mSelectStickers.add(str);
            }
            this.mStickerAdapter.setSelectStickes(this.mSelectStickers);
            this.mStickerAdapter.notifyDataSetChanged();
            MMPrivateStickerFragment mMPrivateStickerFragment = this.mFragment;
            if (mMPrivateStickerFragment != null) {
                mMPrivateStickerFragment.onStickerSelected(this.mSelectStickers);
            }
        }
    }
}
