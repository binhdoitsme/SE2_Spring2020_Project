package com.hanu.domain.dto;

public final class Article {
    private String provider;
    private Link link;
    private String title;
    private Link imgUrl;

    public Article() { }

    public Article(String provider, Link link, String title, Link imgUrl) {
        this.provider = provider;
        this.link = link;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getProvider() {
        return provider;
    }

    public String getTitle() {
        return title;
    }

    public Link getLink() {
        return link;
    }

    public Link getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":(" + "provider=" + provider
                                            + ", title=" + title
                                            + ", link=" + link.toString()
                                            + ", imgUrl=" + imgUrl.toString()
                                            + ")";
    }
}