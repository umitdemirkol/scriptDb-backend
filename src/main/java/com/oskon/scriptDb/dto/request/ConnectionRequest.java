package com.oskon.scriptDb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConnectionRequest {
    private String server;
    private int port;
    private String username;
    private String password;

}
