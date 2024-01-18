package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Column;
import laboratory_8.project.model.Table;

import java.util.List;

public interface TableInterface {
    String VARCHAR = "varchar";
    String INT = "int";
    String DATE = "date";
    String WHERE = "WHERE";

    default String extractTableName(String command) {
        String[] parts = command.split("\\s+");
        return parts.length > 2 ? parts[2].replace(";", "") : null;
    }

    default Table findTable(List<Table> tableList, String tableName) {
        for (Table table : tableList) {
            if (tableName.equals(table.getTableName())) {
                return table;
            }
        }
        throw new InvalidCommandException("Table: [" + tableName + "] does not exist in the database");
    }

    default int getColumnIndex(Table table, String columnName) {
        List<Column> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getColumnName().equals(columnName)) {
                return i;
            }
        }
        throw new InvalidCommandException("Column: [" + columnName + "] not found in the table");
    }
}
