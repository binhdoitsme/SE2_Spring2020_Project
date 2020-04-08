package com.hanu.util.newscrawler;

public final class NewsFormat {
    private String articleSelector;
    private String headerSelector;
    private String imgSelector;

    public NewsFormat(String articleSelector, String headerSelector, String imgSelector) {
        this.articleSelector = articleSelector;
        this.headerSelector = headerSelector;
        this.imgSelector = imgSelector;
    }

    public String getArticleSelector() {
        return articleSelector;
    }

    public String getHeaderSelector() {
        return headerSelector;
    }

    public String getImgSelector() {
        return imgSelector;
    }
}