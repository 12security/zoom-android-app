package com.zipow.videobox.util;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
    @Nullable
    public static Node getNodeByName(@Nullable NodeList nodeList, @NonNull String str) {
        if (nodeList != null && nodeList.getLength() > 0 && !TextUtils.isEmpty(str)) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().equalsIgnoreCase(str)) {
                    return nodeList.item(i);
                }
                if ("p".equalsIgnoreCase(nodeList.item(i).getNodeName())) {
                    Node item = nodeList.item(i);
                    if (!(item == null || item.getChildNodes() == null || item.getChildNodes().getLength() <= 0)) {
                        for (int i2 = 0; i2 < item.getChildNodes().getLength(); i2++) {
                            if (str.equalsIgnoreCase(item.getChildNodes().item(i2).getNodeName())) {
                                return item.getChildNodes().item(i2);
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public static NodeList getNodeListIgnoreP(@Nullable Node node) {
        if (node == null) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes == null || childNodes.getLength() <= 0) {
            return null;
        }
        for (int i = 0; i < childNodes.getLength(); i++) {
            if ("p".equalsIgnoreCase(childNodes.item(i).getNodeName())) {
                NodeList childNodes2 = childNodes.item(i).getChildNodes();
                if (childNodes2 == null || childNodes2.getLength() <= 0) {
                    return null;
                }
                return childNodes.item(i).getChildNodes();
            }
        }
        return childNodes;
    }

    @Nullable
    public static String getAttributeContentByName(@Nullable Node node, String str) {
        if (node == null || TextUtils.isEmpty(str)) {
            return null;
        }
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null || attributes.getLength() <= 0) {
            return null;
        }
        Node namedItem = attributes.getNamedItem(str);
        if (namedItem == null) {
            return null;
        }
        return namedItem.getTextContent();
    }
}
