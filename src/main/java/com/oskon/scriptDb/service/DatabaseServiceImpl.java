package com.oskon.scriptDb.service;

import com.oskon.scriptDb.dto.ColumnInfo;
import com.oskon.scriptDb.dto.request.ConnectionRequest;
import com.oskon.scriptDb.dto.request.GetDatabaseTableRequest;
import com.oskon.scriptDb.dto.TableInfo;
import com.oskon.scriptDb.dto.request.ScriptTablesRequest;
import com.oskon.scriptDb.exception.GlobalException;
import com.oskon.scriptDb.service.impl.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    private Connection connection;

    public List<String> getDatabaseNames(ConnectionRequest connectionRequest) throws Exception {
        String url = "jdbc:sqlserver://" + connectionRequest.getServer() + ":" + connectionRequest.getPort() +
                ";encrypt=true;trustServerCertificate=true";
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(
                    url,
                    connectionRequest.getUsername(),
                    connectionRequest.getPassword()
            );
        }
        try (Connection conn = DriverManager.getConnection(url, connectionRequest.getUsername(), connectionRequest.getPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sys.databases")) {

            List<String> databases = new ArrayList<>();
            while (rs.next()) {
                databases.add(rs.getString("name"));
            }
            return databases;
        }
    }

    public List<TableInfo> getTableNames(GetDatabaseTableRequest databaseName) throws SQLException,RuntimeException,Exception {
        List<TableInfo> tableInfoList = new ArrayList<>();
        checkConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("USE " + databaseName.getTableName());
            ResultSet tablesRs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'");

            while (tablesRs.next()) {
                String tableName = tablesRs.getString("TABLE_NAME");
                List<ColumnInfo> columns = new ArrayList<>();

                try (Statement columnStmt = connection.createStatement()) {
                    ResultSet columnsRs = columnStmt.executeQuery(
                            "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'"
                    );

                    while (columnsRs.next()) {
                        String columnName = columnsRs.getString("COLUMN_NAME");
                        String columnType = columnsRs.getString("DATA_TYPE");
                        columns.add(new ColumnInfo(columnName, columnType));
                    }
                }

                tableInfoList.add(new TableInfo(tableName, columns));
            }
        } catch (SQLException e) {
            throw new GlobalException(500L,"Sql Exception : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return tableInfoList;
    }

    private void checkConnection() throws SQLException, GlobalException {
        if(this.connection == null || this.connection.isClosed()) {
            throw new GlobalException(500L,"Database connection not Found !!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Map<String, String> getCreateTableScripts(ScriptTablesRequest request) throws SQLException, RuntimeException, Exception {
        checkConnection();

        Map<String, String> tableScripts = new HashMap<>();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("USE " + request.getDatabaseName());

            for (String tableName : request.getTableName()) {
                StringBuilder createTableScript = new StringBuilder();

                createTableScript.append("CREATE TABLE ").append(tableName).append(" (");

                ResultSet rs = stmt.executeQuery(
                        "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE " +
                                "FROM INFORMATION_SCHEMA.COLUMNS " +
                                "WHERE TABLE_NAME = '" + tableName + "'");

                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("DATA_TYPE");
                    String isNullable = rs.getString("IS_NULLABLE");
                    int maxLength = rs.getInt("CHARACTER_MAXIMUM_LENGTH");

                    createTableScript.append("    ").append(columnName).append(" ").append(dataType);

                    if (maxLength > 0 && !"int".equalsIgnoreCase(dataType)) {
                        createTableScript.append("(").append(maxLength).append(")");
                    }

                    if ("NO".equals(isNullable)) {
                        createTableScript.append(" NOT NULL");
                    }

                    createTableScript.append(", ");
                }

                int lastCommaIndex = createTableScript.lastIndexOf(",");
                if (lastCommaIndex != -1) {
                    createTableScript.deleteCharAt(lastCommaIndex);
                }

                createTableScript.append(");");

                tableScripts.put(tableName, createTableScript.toString());
            }
        } catch (SQLException e) {
            throw new GlobalException(500L, "Sql Exception : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return tableScripts;
    }

    public Map<String, List<String>> getInsertStatements(ScriptTablesRequest request) throws  Exception {
        checkConnection();

        Map<String, List<String>> tableInsertStatements = new HashMap<>();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("USE " + request.getDatabaseName());
            stmt.setQueryTimeout(60);


            for (String tableName : request.getTableName()) {
                List<String> insertStatements = new ArrayList<>();

                String checkColumnQuery = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = 'READ_TIME'";
                ResultSet columnCheckRs = stmt.executeQuery(checkColumnQuery);
                if (!columnCheckRs.next()) {
                    throw new GlobalException(500L, "Table " + tableName + " does not contain 'READ_TIME' column.", HttpStatus.BAD_REQUEST);
                }

                String query = "SELECT * FROM " + tableName + " WHERE READ_TIME >= DATEADD(day, -1, '" + request.getDate() + "')";
                ResultSet rs = stmt.executeQuery(query);

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    StringBuilder insertStatement = new StringBuilder("INSERT INTO " + tableName + " (");

                    for (int i = 1; i <= columnCount; i++) {
                        insertStatement.append(metaData.getColumnName(i));
                        if (i < columnCount) {
                            insertStatement.append(", ");
                        }
                    }

                    insertStatement.append(") VALUES (");

                    for (int i = 1; i <= columnCount; i++) {
                        insertStatement.append("'").append(rs.getString(i)).append("'");
                        if (i < columnCount) {
                            insertStatement.append(", ");
                        }
                    }

                    insertStatement.append(");");

                    insertStatements.add(insertStatement.toString());
                }

                tableInsertStatements.put(tableName, insertStatements);
            }
        } catch (SQLException e) {
            throw new GlobalException(500L, "Sql Exception : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return tableInsertStatements;
    }




}