package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Column;
import laboratory_8.project.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableInterfaceImpl implements TableInterface {
    public void createTable(List<Table> tableList, String command) {
        String tableName = extractTableName(command);

        if (tableExists(tableList, tableName)) {
            throw new InvalidCommandException("Table with name '" + tableName + "' already exists in the database");
        }

        List<Column> columns = extractColumns(command);
        validateDataTypes(columns);
        Table table = new Table(tableName, new ArrayList<>(), columns);
        tableList.add(table);
        System.out.println("Command completed successfully.");
    }

    private List<Column> extractColumns(String command) {
        int startIndex = command.indexOf("(") + 1;
        int endIndex = command.lastIndexOf(")");
        String columnsString = command.substring(startIndex, endIndex).trim();

        return getColumns(columnsString);
    }

    private boolean tableExists(List<Table> tableList, String tableName) {
        for (Table table : tableList) {
            if (tableName.equals(table.getTableName())) {
                return true;
            }
        }
        return false;
    }

    private static List<Column> getColumns(String columnsString) {
        List<Column> columns = new ArrayList<>();
        Arrays
                .stream(columnsString.split("\\s*,\\s*"))
                .forEach(columnInfo -> {
                    String[] columnParts = columnInfo.split("\\s+");
                    if (columnParts.length == 2) {
                        columns.add(new Column(columnParts[0], columnParts[1]));
                    }
                });
        return columns;
    }

    private void validateDataTypes(List<Column> columns) {
        for (Column column : columns) {
            String dataType = column.getDataType();
            if (!INT.equals(dataType) && !VARCHAR.equals(dataType) && !DATE.equals(dataType)) {
                throw new InvalidCommandException("Cannot find data type: " + dataType);
            }
        }
    }
}
