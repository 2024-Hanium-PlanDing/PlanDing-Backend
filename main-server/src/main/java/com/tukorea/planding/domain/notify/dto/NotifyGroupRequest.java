package com.tukorea.planding.domain.notify.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotifyGroupRequest{
    String targetToken;
    String title;
    String body;
}
