package com.hanu.util.newscrawler;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class NewsFormat {
    private @NonNull String articleSelector;
    private @NonNull String headerSelector;
    private @NonNull String imgSelector;
}