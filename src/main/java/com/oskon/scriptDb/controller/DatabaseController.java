package com.oskon.scriptDb.controller;

import com.oskon.scriptDb.dto.request.ConnectionRequest;
import com.oskon.scriptDb.dto.request.GetDatabaseTableRequest;
import com.oskon.scriptDb.dto.TableInfo;
import com.oskon.scriptDb.dto.request.ScriptTablesRequest;
import com.oskon.scriptDb.service.DatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Database API's", description = "İstediğin bir database den istediğin tablolar ve datalar için script alabilirsin ")
@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private  DatabaseService databaseService;


    @Operation(summary = "Connect to Database", description = "Connect to database")
    @PostMapping("/connectDatabase")
    public List<String> getDatabases(@RequestBody ConnectionRequest connectionRequest) throws Exception {
        return this.databaseService.getDatabaseNames(connectionRequest);
    }

    @Operation(summary = "Get database tables", description = "Retrieves the list of tables from the specified database.")
    @PostMapping("/getTables")
    public List<TableInfo> getTables(@RequestBody GetDatabaseTableRequest databaseRequest) throws Exception {
        return this.databaseService.getTableNames(databaseRequest);
    }

    @Operation(summary = "Get table creation scripts", description = "Generates SQL scripts for creating tables.")
    @PostMapping("/getTableScripts")
    public Map<String, String> getCreateTableScripts(@RequestBody ScriptTablesRequest request) throws Exception {
        return this.databaseService.getCreateTableScripts(request);
    }

    @Operation(summary = "Get insert statements", description = "Generates SQL insert statements for the given tables.")
    @PostMapping("/get-insert-statements")
    public Map<String, List<String>> getInsertStatements(@RequestBody ScriptTablesRequest request) throws Exception {
        return this.databaseService.getInsertStatements(request);
    }
}