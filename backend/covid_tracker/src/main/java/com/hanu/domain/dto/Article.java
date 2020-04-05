package com.hanu.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter @ToString
public final class Article {
    private @NonNull String provider;
    private @NonNull Link link;
    private @NonNull String title;
    private @NonNull Link imgUrl;
}