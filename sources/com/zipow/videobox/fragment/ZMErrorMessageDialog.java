package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMErrorMessageDialog extends ZMDialogFragment {
    @NonNull
    private static String ARGS_DIALOG_MESSAGE = "args_dialog_message";
    @NonNull
    private static String ARGS_DIALOG_TITLE = "args_dialog_title";
    private ErrorMessageAdapter mAdapter;

    public class ErrorMessageAdapter extends BaseAdapter {
        private Context mContext;
        @NonNull
        private ArrayList<String> mMessages = new ArrayList<>();

        public long getItemId(int i) {
            return (long) i;
        }

        public ErrorMessageAdapter(Context context, @Nullable ArrayList<String> arrayList) {
            this.mContext = context;
            if (arrayList != null) {
                this.mMessages.addAll(arrayList);
            }
        }

        public int getCount() {
            return this.mMessages.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i >= getCount()) {
                return null;
            }
            return this.mMessages.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (item == null) {
                return null;
            }
            LayoutInflater from = LayoutInflater.from(this.mContext);
            if (from == null) {
                return null;
            }
            if (view == null || !"errorMessageItem".equals(view.getTag())) {
                view = from.inflate(C4558R.layout.zm_error_message_item, viewGroup, false);
                view.setTag("errorMessageItem");
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.message);
            String str = (String) item;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mContext.getString(C4558R.string.zm_big_dot));
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(str);
            textView.setText(sb.toString());
            return view;
        }
    }

    public static void show(@Nullable FragmentManager fragmentManager, String str, ArrayList<String> arrayList, String str2) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ARGS_DIALOG_TITLE, str);
            bundle.putStringArrayList(ARGS_DIALOG_MESSAGE, arrayList);
            ZMErrorMessageDialog zMErrorMessageDialog = new ZMErrorMessageDialog();
            zMErrorMessageDialog.setArguments(bundle);
            if (StringUtil.isEmptyOrNull(str2)) {
                zMErrorMessageDialog.show(fragmentManager, ZMErrorMessageDialog.class.getName());
            } else {
                zMErrorMessageDialog.show(fragmentManager, str2);
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_error_message_listview, null, false);
        ListView listView = (ListView) inflate.findViewById(C4558R.C4560id.messageList);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(ARGS_DIALOG_TITLE);
        this.mAdapter = new ErrorMessageAdapter(getActivity(), arguments.getStringArrayList(ARGS_DIALOG_MESSAGE));
        listView.setAdapter(this.mAdapter);
        return new Builder(getActivity()).setTitle((CharSequence) string).setView(inflate).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        this.mAdapter.notifyDataSetChanged();
    }
}
