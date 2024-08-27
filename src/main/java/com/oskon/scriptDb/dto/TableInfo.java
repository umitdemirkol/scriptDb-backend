package com.oskon.scriptDb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

        private String tableName;
        private List<ColumnInfo> columns;

}
