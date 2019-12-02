package com.lyloou.test.github;

import java.util.List;

public class Repository {

    /**
     * author : home-assistant
     * name : home-assistant
     * avatar : https://github.com/home-assistant.png
     * url : https://github.com/home-assistant/home-assistant
     * description : 🏡 Open source home automation that puts local control and privacy first
     * language : Python
     * languageColor : #3572A5
     * stars : 29115
     * forks : 8555
     * currentPeriodStars : 180
     * builtBy : [{"username":"balloob","href":"https://github.com/balloob","avatar":"https://avatars1.githubusercontent.com/u/1444314"},{"username":"fabaff","href":"https://github.com/fabaff","avatar":"https://avatars0.githubusercontent.com/u/116184"},{"username":"pvizeli","href":"https://github.com/pvizeli","avatar":"https://avatars1.githubusercontent.com/u/15338540"},{"username":"Danielhiversen","href":"https://github.com/Danielhiversen","avatar":"https://avatars1.githubusercontent.com/u/650502"},{"username":"robbiet480","href":"https://github.com/robbiet480","avatar":"https://avatars1.githubusercontent.com/u/18516"}]
     */

    private String author;
    private String name;
    private String avatar;
    private String url;
    private String description;
    private String language;
    private String languageColor;
    private int stars;
    private int forks;
    private int currentPeriodStars;
    private List<BuiltBy> builtBy;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    public void setCurrentPeriodStars(int currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }

    public List<BuiltBy> getBuiltBy() {
        return builtBy;
    }

    public void setBuiltBy(List<BuiltBy> builtBy) {
        this.builtBy = builtBy;
    }

    public static class BuiltBy {
        /**
         * username : balloob
         * href : https://github.com/balloob
         * avatar : https://avatars1.githubusercontent.com/u/1444314
         */

        private String username;
        private String href;
        private String avatar;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
