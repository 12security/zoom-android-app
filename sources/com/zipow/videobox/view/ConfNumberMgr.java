package com.zipow.videobox.view;

import android.text.Editable;
import android.text.Selection;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.CmmSavedMeeting;
import com.zipow.videobox.ptapp.PTApp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfNumberMgr {
    private static final String DELIMITER = ",";
    private static final int MAX_CONF_NUMBER_CAPACITY = 10;

    private static boolean checkEditTextRange(int i, int i2, int i3) {
        return i2 >= 0 && i3 >= 0 && i3 >= i2 && i2 <= i && i3 <= i;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    @NonNull
    public static List<CmmSavedMeeting> loadConfNumberFromDB() {
        ArrayList arrayList = new ArrayList();
        ArrayList savedMeetingList = PTApp.getInstance().getSavedMeetingList();
        if (savedMeetingList.size() == 0) {
            return arrayList;
        }
        Iterator it = savedMeetingList.iterator();
        while (it.hasNext()) {
            CmmSavedMeeting cmmSavedMeeting = (CmmSavedMeeting) it.next();
            String str = cmmSavedMeeting.getmConfID();
            if (!StringUtil.isEmptyOrNull(str) && !isValidVanityUrl(str)) {
                arrayList.add(cmmSavedMeeting);
            }
        }
        return arrayList;
    }

    @NonNull
    public static List<CmmSavedMeeting> loadVanityUrlFromDB() {
        ArrayList arrayList = new ArrayList();
        ArrayList savedMeetingList = PTApp.getInstance().getSavedMeetingList();
        if (savedMeetingList.size() == 0) {
            return arrayList;
        }
        Iterator it = savedMeetingList.iterator();
        while (it.hasNext()) {
            CmmSavedMeeting cmmSavedMeeting = (CmmSavedMeeting) it.next();
            String str = cmmSavedMeeting.getmConfID();
            if (!StringUtil.isEmptyOrNull(str) && isValidVanityUrl(str)) {
                arrayList.add(cmmSavedMeeting);
            }
        }
        return arrayList;
    }

    public static boolean isValidVanityUrl(@Nullable String str) {
        if (str == null || str.length() > 40 || str.length() < 5) {
            return false;
        }
        char charAt = str.charAt(0);
        if (charAt > 'z' || charAt < 'a') {
            return false;
        }
        return true;
    }

    public static ArrayList<CmmSavedMeeting> loadConfUrlFromDB() {
        return PTApp.getInstance().getSavedMeetingList();
    }

    public static void formatText(@Nullable Editable editable, int i) {
        if (editable != null) {
            int i2 = 0;
            int i3 = 0;
            while (i3 < editable.length()) {
                if (!isNumber(editable.charAt(i3))) {
                    editable.delete(i3, i3 + 1);
                    i3--;
                }
                i3++;
            }
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            while (editable.length() > 11) {
                int i4 = selectionStart - 1;
                if (!checkEditTextRange(editable.length(), i4, selectionEnd)) {
                    break;
                }
                editable.delete(i4, selectionEnd);
                selectionStart--;
                selectionEnd--;
            }
            int i5 = 3;
            if (i == 2) {
                i5 = 4;
            }
            int i6 = 8;
            if (!(i == 1 || i == 2 || editable.length() >= 11)) {
                i6 = 7;
            }
            while (true) {
                if (i2 >= editable.length()) {
                    break;
                }
                char charAt = editable.charAt(i2);
                if (i2 == editable.length() - 1 && !isNumber(charAt)) {
                    editable.delete(i2, i2 + 1);
                    break;
                }
                if (i2 == i5 || i2 == i6) {
                    if (isNumber(charAt)) {
                        editable.insert(i2, OAuth.SCOPE_DELIMITER);
                    } else if (charAt != ' ') {
                        editable.replace(i2, i2 + 1, OAuth.SCOPE_DELIMITER);
                    }
                } else if (!isNumber(charAt)) {
                    editable.delete(i2, i2 + 1);
                    i2--;
                }
                i2++;
            }
        }
    }
}
