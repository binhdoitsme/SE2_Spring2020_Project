package com.hanu.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter @ToString
public class Link {
    private @NonNull String href;
    private @NonNull String rel;
    private @NonNull String method;
}