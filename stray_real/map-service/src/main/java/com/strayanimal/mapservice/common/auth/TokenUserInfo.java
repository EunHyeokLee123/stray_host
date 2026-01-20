package com.strayanimal.mapservice.common.auth;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String clientId;
    private String ipHash;

}
