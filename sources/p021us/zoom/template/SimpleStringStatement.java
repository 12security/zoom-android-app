package p021us.zoom.template;

import java.util.Map;

/* renamed from: us.zoom.template.SimpleStringStatement */
/* compiled from: Template */
class SimpleStringStatement implements IStatement {
    private String mString;

    public SimpleStringStatement(String str) {
        this.mString = str;
    }

    public String format(Map<String, String> map) {
        return this.mString;
    }
}
