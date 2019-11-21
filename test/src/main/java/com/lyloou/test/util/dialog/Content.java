
package com.lyloou.test.util.dialog;

public class Content {
    private String hint;
    private String title;
    private String defaultContext;
    private boolean focus;


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

    public String getDefaultContext() {
        return defaultContext;
    }

    public boolean isFocus() {
        return focus;
    }

    private void setFocus(boolean focus) {
        this.focus = focus;
    }

    private void setDefaultContext(String defaultContext) {
        this.defaultContext = defaultContext;
    }

    public static final class Builder {
        String hint;
        String title;
        String defaultContext;
        boolean focus;


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

        public Builder defaultContext(String defaultContext) {
            this.defaultContext = defaultContext;
            return this;
        }

        public Content build() {
            Content content = new Content();
            content.setHint(hint);
            content.setTitle(title);
            content.setFocus(focus);
            content.setDefaultContext(defaultContext);
            return content;
        }
    }
}