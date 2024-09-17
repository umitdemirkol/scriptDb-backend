package com.oskon.scriptDb.service.impl;

import com.oskon.scriptDb.dto.TableInfo;
import com.oskon.scriptDb.dto.request.ConnectionRequest;
import com.oskon.scriptDb.dto.request.GetDatabaseTableRequest;
import com.oskon.scriptDb.dto.request.ScriptTablesRequest;
import com.oskon.scriptDb.exception.GlobalException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseService {

    public List<String> getDatabaseNames (ConnectionRequest request) throws SQLException, Exception;
    public List<TableInfo> getTableNames(GetDatabaseTableRequest databaseName) throws SQLException,RuntimeException,Exception, GlobalException;
    public Map<String, String> getCreateTableScripts(ScriptTablesRequest request) throws SQLException, RuntimeException, Exception ;
    public Map<String, List<String>> getInsertStatements(ScriptTablesRequest request) throws SQLException, RuntimeException, Exception;
}
