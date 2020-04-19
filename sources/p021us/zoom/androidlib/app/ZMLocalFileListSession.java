package p021us.zoom.androidlib.app;

import android.os.Environment;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.app.ZMLocalFileListSession */
class ZMLocalFileListSession {
    private File mDir = null;
    private List<File> mFileList = new ArrayList();
    private String mFileListPath;
    private FilenameFilter mFileNameFilter = null;
    private ArrayList<String> pathDirsList = new ArrayList<>();

    public ZMLocalFileListSession() {
        init(null);
    }

    public ZMLocalFileListSession(String str) {
        init(str);
    }

    public ZMLocalFileListSession(String str, FilenameFilter filenameFilter) {
        init(str);
        this.mFileNameFilter = filenameFilter;
    }

    public void setFileNameFilter(FilenameFilter filenameFilter) {
        this.mFileNameFilter = filenameFilter;
    }

    public FilenameFilter getFileNameFilter() {
        return this.mFileNameFilter;
    }

    public void setDir(String str) {
        init(str);
        loadFileList();
    }

    private void init(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            File file = new File(str);
            if (file.isDirectory()) {
                this.mDir = file;
            }
        }
        if (this.mDir == null) {
            if (!Environment.getExternalStorageDirectory().isDirectory() || !Environment.getExternalStorageDirectory().canRead()) {
                this.mDir = new File("/");
            } else {
                this.mDir = Environment.getExternalStorageDirectory();
            }
        }
        parseDirectoryPath();
    }

    private void parseDirectoryPath() {
        if (this.mDir != null) {
            this.pathDirsList.clear();
            String[] split = this.mDir.getAbsolutePath().split("/");
            for (String add : split) {
                this.pathDirsList.add(add);
            }
        }
    }

    private boolean loadFileList() {
        if (this.mDir == null) {
            return false;
        }
        this.mFileList.clear();
        this.mFileListPath = null;
        try {
            getDirectoryFileList(this.mDir.getPath(), this.mFileNameFilter, this.mFileList);
            this.mFileListPath = this.mDir.getPath();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public File getCurrentDir() {
        return this.mDir;
    }

    @Nullable
    public String getCurrentDirPath() {
        File file = this.mDir;
        if (file == null) {
            return null;
        }
        return file.getPath();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0010, code lost:
        if (r0.equals(r1.getPath()) != false) goto L_0x001a;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.io.File> getCurrentFileList() {
        /*
            r2 = this;
            java.lang.String r0 = r2.mFileListPath
            if (r0 == 0) goto L_0x0012
            java.io.File r1 = r2.mDir
            if (r1 == 0) goto L_0x0012
            java.lang.String r1 = r1.getPath()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x001a
        L_0x0012:
            boolean r0 = r2.loadFileList()
            if (r0 != 0) goto L_0x001a
            r0 = 0
            return r0
        L_0x001a:
            java.util.List<java.io.File> r0 = r2.mFileList
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.app.ZMLocalFileListSession.getCurrentFileList():java.util.List");
    }

    public void upCurrentDirectory() {
        if (this.mDir != null) {
            if (this.pathDirsList.size() == 0) {
                this.mDir = new File("/");
            } else {
                ArrayList<String> arrayList = this.pathDirsList;
                String substring = this.mDir.toString().substring(0, this.mDir.toString().lastIndexOf((String) arrayList.remove(arrayList.size() - 1)));
                if (StringUtil.isEmptyOrNull(substring)) {
                    this.mDir = new File("/");
                } else {
                    this.mDir = new File(substring);
                }
            }
            loadFileList();
        }
    }

    public void getDirFileList(String str, List<File> list) throws Exception {
        getDirectoryFileList(str, this.mFileNameFilter, list);
    }

    public int getDirFilesCount(String str) throws Exception {
        ArrayList arrayList = new ArrayList();
        getDirectoryFileList(str, this.mFileNameFilter, arrayList);
        return arrayList.size();
    }

    public static void getDirectoryFileList(String str, FilenameFilter filenameFilter, List<File> list) {
        File[] fileArr;
        if (!StringUtil.isEmptyOrNull(str)) {
            File file = new File(str);
            if (file.exists() && file.canRead() && file.isDirectory()) {
                if (filenameFilter == null) {
                    fileArr = file.listFiles();
                } else {
                    fileArr = file.listFiles(filenameFilter);
                }
                if (list != null) {
                    for (File file2 : fileArr) {
                        if (file2.canRead() && !file2.isHidden() && file2.exists() && (file2.isFile() || file2.isDirectory())) {
                            list.add(file2);
                        }
                    }
                }
            }
        }
    }
}
