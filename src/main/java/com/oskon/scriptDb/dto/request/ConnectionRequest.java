package com.oskon.scriptDb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Represents the connection details for a database")
public class ConnectionRequest {

    @Schema(description = "The server address where the database is hosted",
            example = "192.168.0.98")
    private String server;

    @Schema(description = "The port number to connect to the database",
            example = "1433")
    private int port;

    @Schema(description = "The username for database authentication",
            example = "sa")
    private String username;

    @Schema(description = "The password for database authentication",
            example = "P@ssw0rd")
    private String password;
}