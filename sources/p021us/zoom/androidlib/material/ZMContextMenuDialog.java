package p021us.zoom.androidlib.material;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.C0971R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.BaseRecyclerViewAdapter.OnRecyclerViewListener;

/* renamed from: us.zoom.androidlib.material.ZMContextMenuDialog */
public class ZMContextMenuDialog extends BottomSheetDialogFragment implements OnRecyclerViewListener {
    private static final String TAG = "ZMContextMenuDialog";
    private ZMContextMenuListAdapter adapter;
    private Context context;
    private boolean hasItemDecoration;
    private OnContextMenuClickListener listener;
    private RecyclerView recyclerView;

    /* renamed from: us.zoom.androidlib.material.ZMContextMenuDialog$Builder */
    public static class Builder {
        /* access modifiers changed from: private */
        public ZMContextMenuListAdapter adapter;
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public boolean hasItemDecoration = true;
        /* access modifiers changed from: private */
        public OnContextMenuClickListener listener;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setAdapter(ZMContextMenuListAdapter zMContextMenuListAdapter, OnContextMenuClickListener onContextMenuClickListener) {
            this.adapter = zMContextMenuListAdapter;
            this.listener = onContextMenuClickListener;
            return this;
        }

        public Builder setHasItemDecoration(boolean z) {
            this.hasItemDecoration = z;
            return this;
        }

        public ZMContextMenuDialog build() {
            return ZMContextMenuDialog.newInstance(this);
        }

        public ZMContextMenuDialog show(FragmentManager fragmentManager) {
            ZMContextMenuDialog build = build();
            build.show(fragmentManager);
            return build;
        }
    }

    /* renamed from: us.zoom.androidlib.material.ZMContextMenuDialog$OnContextMenuClickListener */
    public interface OnContextMenuClickListener {
        void onContextMenuClick(View view, int i);
    }

    public boolean onItemLongClick(View view, int i) {
        return false;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.listener = onContextMenuClickListener;
    }

    /* access modifiers changed from: private */
    public static ZMContextMenuDialog newInstance(Builder builder) {
        ZMContextMenuDialog zMContextMenuDialog = new ZMContextMenuDialog();
        zMContextMenuDialog.setHasItemDecoration(builder.hasItemDecoration);
        zMContextMenuDialog.setAdapter(builder.adapter);
        zMContextMenuDialog.setListener(builder.listener);
        zMContextMenuDialog.setContext(builder.context);
        return zMContextMenuDialog;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.context, C4409R.style.SheetDialog);
        bottomSheetDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                try {
                    FrameLayout frameLayout = (FrameLayout) ((BottomSheetDialog) dialogInterface).findViewById(C0971R.C0973id.design_bottom_sheet);
                    if (frameLayout != null) {
                        BottomSheetBehavior.from(frameLayout).setState(3);
                    }
                } catch (Exception unused) {
                }
            }
        });
        return bottomSheetDialog;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C4409R.layout.zm_context_menu_dialog, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.adapter.setOnRecyclerViewListener(this);
        this.recyclerView = (RecyclerView) view.findViewById(C4409R.C4411id.menu_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.adapter);
        if (this.hasItemDecoration) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.context, 1);
            dividerItemDecoration.setDrawable(getResources().getDrawable(C4409R.C4410drawable.zm_divider_line_decoration));
            this.recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    public void show(FragmentManager fragmentManager) {
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(TAG);
        if (findFragmentByTag != null) {
            beginTransaction.remove(findFragmentByTag);
        }
        beginTransaction.addToBackStack(null);
        show(beginTransaction, TAG);
    }

    private void setAdapter(ZMContextMenuListAdapter zMContextMenuListAdapter) {
        this.adapter = zMContextMenuListAdapter;
    }

    private void setListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.listener = onContextMenuClickListener;
    }

    private void setContext(Context context2) {
        this.context = context2;
    }

    private void setHasItemDecoration(boolean z) {
        this.hasItemDecoration = z;
    }

    public static Builder builder(Context context2) {
        return new Builder(context2);
    }

    public void onItemClick(View view, int i) {
        OnContextMenuClickListener onContextMenuClickListener = this.listener;
        if (onContextMenuClickListener != null) {
            onContextMenuClickListener.onContextMenuClick(view, i);
        }
        dismiss();
    }
}
