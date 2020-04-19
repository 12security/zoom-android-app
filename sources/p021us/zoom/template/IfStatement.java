package p021us.zoom.template;

import java.util.Map;

/* renamed from: us.zoom.template.IfStatement */
/* compiled from: Template */
class IfStatement implements IStatement {
    private String mConditionVariable;
    private Template mElseTemplate;
    private Template mIfTemplate;
    private boolean mIsNegative = false;

    public IfStatement(String str, Template template, Template template2) {
        if (str.startsWith("!")) {
            this.mIsNegative = true;
            this.mConditionVariable = str.substring(1);
        } else {
            this.mConditionVariable = str;
        }
        this.mIfTemplate = template;
        this.mElseTemplate = template2;
    }

    public String format(Map<String, String> map) {
        String str = map == null ? null : (String) map.get(this.mConditionVariable);
        boolean z = str != null && !"false".equals(str);
        if (this.mIsNegative) {
            z = !z;
        }
        if (z) {
            return this.mIfTemplate.format(map);
        }
        Template template = this.mElseTemplate;
        return template != null ? template.format(map) : "";
    }
}
