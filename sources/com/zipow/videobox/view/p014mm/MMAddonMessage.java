package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.XmlUtil;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* renamed from: com.zipow.videobox.view.mm.MMAddonMessage */
public class MMAddonMessage {
    private static final String GITHUB_TEMPLATE_ID = "{E8FB8897-5E18-424f-954C-A950E8C7FFE2}";
    private static final String GITLAB_TEMPLATE_ID = "{6D1D4AC6-3689-44eb-9E2C-3CCED0EC0943}";
    private static final String JIRA_TEMPLATE_ID = "{F1551076-E1E1-4ec8-A89D-7F4D4E4AA917}";
    public static final int TYPE_GITHUB = 2;
    public static final int TYPE_GITLAB = 3;
    public static final int TYPE_JIRA = 1;
    public static final int TYPE_UNKOWN = 0;
    private List<AddonNode> action;
    private List<AddonNode> body;
    private boolean isPlainText;
    private String plainText;
    private List<AddonNode> summary;
    private String templateId;
    private List<AddonNode> title;
    private int titleIcon;
    private String titleImg;
    private int type;

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$ActionNode */
    public static class ActionNode extends AddonNode {
        private String action;

        public String getAction() {
            return this.action;
        }

        public void setAction(String str) {
            this.action = str;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$AddonNode */
    public static class AddonNode {
        private NamedNodeMap attrs;
        private String value;

        public NamedNodeMap getAttrs() {
            return this.attrs;
        }

        public void setAttrs(NamedNodeMap namedNodeMap) {
            this.attrs = namedNodeMap;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String str) {
            this.value = str;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeBR */
    public static class NodeBR extends AddonNode {
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeFooter */
    public static class NodeFooter extends AddonNode {
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeImg */
    public static class NodeImg extends AddonNode {
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeLabel */
    public static class NodeLabel extends AddonNode {
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeMsgHref */
    public static class NodeMsgHref extends AddonNode {
        private String url;

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMAddonMessage$NodeP */
    public static class NodeP extends AddonNode {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:80|79|83|84|(0)|88) */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0146, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0157, code lost:
        r1 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0162, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0163, code lost:
        r6 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x0148 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:83:0x0165 */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0146 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:28:0x0068] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0172 A[SYNTHETIC, Splitter:B:86:0x0172] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0178 A[SYNTHETIC, Splitter:B:91:0x0178] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.view.p014mm.MMAddonMessage initFromMsgBody(@androidx.annotation.NonNull java.lang.String r10) {
        /*
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r10)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            r0 = 1
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0165 }
            byte[] r3 = r10.getBytes()     // Catch:{ Exception -> 0x0165 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0165 }
            javax.xml.parsers.DocumentBuilderFactory r3 = javax.xml.parsers.DocumentBuilderFactory.newInstance()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            javax.xml.parsers.DocumentBuilder r3 = r3.newDocumentBuilder()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            org.w3c.dom.Document r4 = r3.parse(r2)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            org.w3c.dom.NodeList r4 = r4.getChildNodes()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            if (r4 == 0) goto L_0x0159
            int r5 = r4.getLength()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            if (r5 != 0) goto L_0x002c
            goto L_0x0159
        L_0x002c:
            r5 = 0
            org.w3c.dom.Node r4 = r4.item(r5)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            java.lang.String r4 = r4.getTextContent()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            r2.close()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            if (r6 == 0) goto L_0x0042
            r2.close()     // Catch:{ IOException -> 0x0041 }
        L_0x0041:
            return r1
        L_0x0042:
            java.lang.String r6 = new java.lang.String     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            byte[] r4 = android.util.Base64.decode(r4, r5)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            r6.<init>(r4)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            if (r4 == 0) goto L_0x0055
            r2.close()     // Catch:{ IOException -> 0x0054 }
        L_0x0054:
            return r1
        L_0x0055:
            java.lang.String r4 = "<robot>%s</robot>"
            java.lang.Object[] r7 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            r7[r5] = r6     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            java.lang.String r4 = java.lang.String.format(r4, r7)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            byte[] r7 = r4.getBytes()     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            r6.<init>(r7)     // Catch:{ Exception -> 0x0160, all -> 0x015d }
            org.w3c.dom.Document r2 = r3.parse(r6)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.NodeList r2 = r2.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.Node r2 = r2.item(r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.NodeList r2 = r2.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            int r3 = r2.getLength()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r3 != r0) goto L_0x00a5
            org.w3c.dom.Node r2 = r2.item(r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r3 = "message"
            java.lang.String r5 = r2.getNodeName()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            boolean r3 = r3.equals(r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r3 == 0) goto L_0x00a1
            com.zipow.videobox.view.mm.MMAddonMessage r1 = new com.zipow.videobox.view.mm.MMAddonMessage     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.<init>()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setIsPlainText(r0)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r2 = r2.getTextContent()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setPlainText(r2)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r6.close()     // Catch:{ IOException -> 0x00a0 }
        L_0x00a0:
            return r1
        L_0x00a1:
            r6.close()     // Catch:{ IOException -> 0x00a4 }
        L_0x00a4:
            return r1
        L_0x00a5:
            com.zipow.videobox.view.mm.MMAddonMessage r1 = new com.zipow.videobox.view.mm.MMAddonMessage     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.<init>()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.Node r3 = r2.item(r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r3 = r3.getTextContent()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setTemplateId(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r5 = "{F1551076-E1E1-4ec8-A89D-7F4D4E4AA917}"
            boolean r5 = r5.equals(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r7 = 3
            r8 = 2
            if (r5 == 0) goto L_0x00c8
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_addon_jira     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setTitleIcon(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setType(r0)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            goto L_0x00e9
        L_0x00c8:
            java.lang.String r5 = "{E8FB8897-5E18-424f-954C-A950E8C7FFE2}"
            boolean r5 = r5.equals(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r5 == 0) goto L_0x00d9
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_addon_github     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setTitleIcon(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setType(r8)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            goto L_0x00e9
        L_0x00d9:
            java.lang.String r5 = "{6D1D4AC6-3689-44eb-9E2C-3CCED0EC0943}"
            boolean r3 = r5.equals(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r3 == 0) goto L_0x00e9
            int r3 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_addon_gitlab     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setTitleIcon(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setType(r7)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
        L_0x00e9:
            org.w3c.dom.Node r3 = r2.item(r0)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.NodeList r3 = r3.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r5 = "img"
            org.w3c.dom.Node r5 = com.zipow.videobox.util.XmlUtil.getNodeByName(r3, r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r9 = "src"
            java.lang.String r5 = com.zipow.videobox.util.XmlUtil.getAttributeContentByName(r5, r9)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r5 == 0) goto L_0x0102
            r1.setTitleImg(r5)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
        L_0x0102:
            java.util.List r3 = parseAddonNodeFromNodeList(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setTitle(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.Node r3 = r2.item(r8)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.NodeList r3 = r3.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.util.List r3 = parseAddonNodeFromNodeList(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setSummary(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.Node r3 = r2.item(r7)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            org.w3c.dom.NodeList r3 = r3.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.util.List r3 = parseAddonNodeFromNodeList(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setBody(r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.lang.String r3 = "actions"
            org.w3c.dom.Node r3 = com.zipow.videobox.util.XmlUtil.getNodeByName(r2, r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            if (r3 != 0) goto L_0x0135
            java.lang.String r3 = "action"
            org.w3c.dom.Node r3 = com.zipow.videobox.util.XmlUtil.getNodeByName(r2, r3)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
        L_0x0135:
            if (r3 == 0) goto L_0x0142
            org.w3c.dom.NodeList r2 = r3.getChildNodes()     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            java.util.List r2 = parseActionAddonNode(r2)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
            r1.setAction(r2)     // Catch:{ Exception -> 0x0148, all -> 0x0146 }
        L_0x0142:
            r6.close()     // Catch:{ IOException -> 0x0145 }
        L_0x0145:
            return r1
        L_0x0146:
            r10 = move-exception
            goto L_0x0176
        L_0x0148:
            com.zipow.videobox.view.mm.MMAddonMessage r1 = new com.zipow.videobox.view.mm.MMAddonMessage     // Catch:{ Exception -> 0x0157, all -> 0x0146 }
            r1.<init>()     // Catch:{ Exception -> 0x0157, all -> 0x0146 }
            r1.setIsPlainText(r0)     // Catch:{ Exception -> 0x0157, all -> 0x0146 }
            r1.setPlainText(r4)     // Catch:{ Exception -> 0x0157, all -> 0x0146 }
            r6.close()     // Catch:{ IOException -> 0x0156 }
        L_0x0156:
            return r1
        L_0x0157:
            r1 = r6
            goto L_0x0165
        L_0x0159:
            r2.close()     // Catch:{ IOException -> 0x015c }
        L_0x015c:
            return r1
        L_0x015d:
            r10 = move-exception
            r6 = r2
            goto L_0x0176
        L_0x0160:
            r1 = r2
            goto L_0x0165
        L_0x0162:
            r10 = move-exception
            r6 = r1
            goto L_0x0176
        L_0x0165:
            com.zipow.videobox.view.mm.MMAddonMessage r2 = new com.zipow.videobox.view.mm.MMAddonMessage     // Catch:{ all -> 0x0162 }
            r2.<init>()     // Catch:{ all -> 0x0162 }
            r2.setIsPlainText(r0)     // Catch:{ all -> 0x0162 }
            r2.setPlainText(r10)     // Catch:{ all -> 0x0162 }
            if (r1 == 0) goto L_0x0175
            r1.close()     // Catch:{ IOException -> 0x0175 }
        L_0x0175:
            return r2
        L_0x0176:
            if (r6 == 0) goto L_0x017b
            r6.close()     // Catch:{ IOException -> 0x017b }
        L_0x017b:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMAddonMessage.initFromMsgBody(java.lang.String):com.zipow.videobox.view.mm.MMAddonMessage");
    }

    @NonNull
    private static List<AddonNode> parseActionAddonNode(@Nullable NodeList nodeList) {
        ArrayList arrayList = new ArrayList();
        if (nodeList == null || nodeList.getLength() <= 0) {
            return arrayList;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if ("p".equalsIgnoreCase(item.getNodeName()) && item.getChildNodes() != null && item.getChildNodes().getLength() > 0) {
                arrayList.addAll(parseActionAddonNode(item.getChildNodes()));
            }
            String attributeContentByName = XmlUtil.getAttributeContentByName(item, "onclick");
            if (attributeContentByName != null) {
                ActionNode actionNode = new ActionNode();
                actionNode.setAction(attributeContentByName);
                actionNode.setAttrs(item.getAttributes());
                actionNode.setValue(item.getTextContent());
                arrayList.add(actionNode);
            }
        }
        return arrayList;
    }

    @NonNull
    private static List<AddonNode> parseAddonNodeFromNodeList(@Nullable NodeList nodeList) {
        ArrayList arrayList = new ArrayList();
        if (nodeList == null || nodeList.getLength() == 0) {
            return arrayList;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            String nodeName = item.getNodeName();
            if ("a".equalsIgnoreCase(nodeName)) {
                NodeMsgHref nodeMsgHref = new NodeMsgHref();
                nodeMsgHref.setValue(item.getTextContent());
                nodeMsgHref.setAttrs(item.getAttributes());
                nodeMsgHref.setUrl(item.getAttributes().getNamedItem("href").getTextContent());
                arrayList.add(nodeMsgHref);
            } else if ("p".equalsIgnoreCase(nodeName)) {
                if (item.getChildNodes() != null && item.getChildNodes().getLength() > 0) {
                    arrayList.addAll(parseAddonNodeFromNodeList(item.getChildNodes()));
                }
                if (i != nodeList.getLength() - 1) {
                    arrayList.add(new NodeBR());
                }
            } else if ("br".equalsIgnoreCase(nodeName)) {
                arrayList.add(new NodeBR());
            } else if ("label".equalsIgnoreCase(nodeName)) {
                NodeLabel nodeLabel = new NodeLabel();
                nodeLabel.setValue(item.getTextContent());
                nodeLabel.setAttrs(item.getAttributes());
                arrayList.add(nodeLabel);
            } else if ("footer".equalsIgnoreCase(nodeName)) {
                NodeFooter nodeFooter = new NodeFooter();
                nodeFooter.setValue(item.getTextContent());
                nodeFooter.setAttrs(item.getAttributes());
                arrayList.add(nodeFooter);
            }
        }
        return arrayList;
    }

    public int getTitleIcon() {
        return this.titleIcon;
    }

    public void setTitleIcon(int i) {
        this.titleIcon = i;
    }

    public List<AddonNode> getTitle() {
        return this.title;
    }

    public void setTitle(List<AddonNode> list) {
        this.title = list;
    }

    public List<AddonNode> getBody() {
        return this.body;
    }

    public void setBody(List<AddonNode> list) {
        this.body = list;
    }

    public List<AddonNode> getSummary() {
        return this.summary;
    }

    public void setSummary(List<AddonNode> list) {
        this.summary = list;
    }

    public List<AddonNode> getAction() {
        return this.action;
    }

    public void setAction(List<AddonNode> list) {
        this.action = list;
    }

    public boolean isPlainText() {
        return this.isPlainText;
    }

    public void setIsPlainText(boolean z) {
        this.isPlainText = z;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String str) {
        this.templateId = str;
    }

    public String getTitleImg() {
        return this.titleImg;
    }

    public void setTitleImg(String str) {
        this.titleImg = str;
    }

    public String getPlainText() {
        return this.plainText;
    }

    public void setPlainText(String str) {
        this.plainText = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
