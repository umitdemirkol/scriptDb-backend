package com.oskon.scriptDb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScriptTablesRequest {

    private List<String> tableName;
    private String databaseName;
    private LocalDate date;

}
