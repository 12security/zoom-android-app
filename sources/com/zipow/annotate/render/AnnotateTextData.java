package com.zipow.annotate.render;

public class AnnotateTextData {
    private int fontColor;
    private int height;
    private boolean isBold;
    private boolean isItalic;
    private int lineCount;
    private int padding;
    private int posX;
    private int posY;
    private String text;
    private int textAlignment;
    private int textHeight;
    private int textLength;
    private int textSize;
    private int textWidth;
    private int width;

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public int getPadding() {
        return this.padding;
    }

    public void setPadding(int i) {
        this.padding = i;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int i) {
        this.posX = i;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int i) {
        this.posY = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public int getTextWidth() {
        return this.textWidth;
    }

    public void setTextWidth(int i) {
        this.textWidth = i;
    }

    public int getTextHeight() {
        return this.textHeight;
    }

    public void setTextHeight(int i) {
        this.textHeight = i;
    }

    public int getTextAlignment() {
        return this.textAlignment;
    }

    public void setTextAlignment(int i) {
        this.textAlignment = i;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int i) {
        this.textSize = i;
    }

    public int getTextLength() {
        return this.textLength;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public void setLineCount(int i) {
        this.lineCount = i;
    }

    public void setTextLength(int i) {
        this.textLength = i;
    }

    public boolean isBold() {
        return this.isBold;
    }

    public void setBold(boolean z) {
        this.isBold = z;
    }

    public boolean isItalic() {
        return this.isItalic;
    }

    public void setItalic(boolean z) {
        this.isItalic = z;
    }

    public int getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(int i) {
        this.fontColor = i;
    }
}
