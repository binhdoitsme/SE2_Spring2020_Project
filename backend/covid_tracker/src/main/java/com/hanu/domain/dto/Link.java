package com.hanu.domain.dto;

public class Link {
    private String href;
    private String rel;
    private String method;

    public Link() { }

    public Link(String href, String rel, String method) {
        this.href = href;
        this.rel = rel;
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public String getMethod() {
        return method;
    }

    public String getRel() {
        return rel;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":(" + "href=" + href
                                            + ", rel=" + rel
                                            + ", method=" + method
                                            + ")";
    }
}