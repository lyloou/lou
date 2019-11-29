
package com.lyloou.test.util.dialog;

import android.text.InputType;

public class Content {
    private String hint;
    private String title;
    private String defaultText;
    private boolean focus;
    private Type type = Type.TEXT;

    public static enum Type {
        TEXT, PASSWORD, NUMBER;
    }

    public int getInputType() {
        if (Type.NUMBER.equals(type)) {
            return InputType.TYPE_CLASS_NUMBER;
        }
        if (Type.PASSWORD.equals(type)) {
            return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        }
        return InputType.TYPE_CLASS_TEXT;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getHint() {
        return hint;
    }

    private void setHint(String hint) {
        this.hint = hint;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public boolean isFocus() {
        return focus;
    }

    private void setFocus(boolean focus) {
        this.focus = focus;
    }

    private void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public static final class Builder {
        String hint;
        String title;
        String defaultText;
        boolean focus;
        private Type type = Type.TEXT;


        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder focus(boolean focus) {
            this.focus = focus;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder defaultText(String defaultText) {
            this.defaultText = defaultText;
            return this;
        }

        public Content build() {
            Content content = new Content();
            content.setHint(hint);
            content.setTitle(title);
            content.setFocus(focus);
            content.setDefaultText(defaultText);
            content.setType(type);
            return content;
        }
    }
}