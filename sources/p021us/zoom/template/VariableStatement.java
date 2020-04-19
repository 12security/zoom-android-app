package p021us.zoom.template;

import java.util.Map;

/* renamed from: us.zoom.template.VariableStatement */
/* compiled from: Template */
class VariableStatement implements IStatement {
    private String mVariable;

    public VariableStatement(String str) {
        this.mVariable = str;
    }

    public String getVariable() {
        return this.mVariable;
    }

    public String format(Map<String, String> map) {
        String str = map == null ? null : (String) map.get(this.mVariable);
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("${");
        sb.append(this.mVariable);
        sb.append("}");
        return sb.toString();
    }
}
