package com.oskon.scriptDb.controller;

import com.oskon.scriptDb.dto.request.ConnectionRequest;
import com.oskon.scriptDb.dto.request.GetDatabaseTableRequest;
import com.oskon.scriptDb.dto.TableInfo;
import com.oskon.scriptDb.dto.request.ScriptTablesRequest;
import com.oskon.scriptDb.service.DatabaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/connectDatabase")
    public List<String> getDatabases(@RequestBody ConnectionRequest connectionRequest) throws Exception {
            return databaseService.getDatabaseNames(connectionRequest);
    }
    @PostMapping("/getTables")
    public List<TableInfo> getTables(@RequestBody GetDatabaseTableRequest databaseRequest) throws Exception {
            return databaseService.getTableNames(databaseRequest);
    }
    @PostMapping("/getScript")
    public Map<String, String> getTables(@RequestBody ScriptTablesRequest request) throws Exception {
        return databaseService.getCreateTableScripts(request);
    }

    @PostMapping("/get-insert-statements")
    public Map<String, List<String>> getInsertStatements(@RequestBody ScriptTablesRequest request) throws Exception {
            return databaseService.getInsertStatements(request);
    }
}