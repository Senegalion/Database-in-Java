package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Column;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectColumnsServiceImpl implements SelectColumnsService, TableInterface {
    @Override
    public void selectAndDisplayColumns(List<Table> tableList, String command) {
        String tableName = extractTableName(command);
        Table table = findTable(tableList, tableName);

        List<String> selectedColumns = extractSelectedColumns(command, table);
        validateSelectedColumns(table, selectedColumns);

        List<Row> selectedRows = applyWhereClause(table, command);
        displaySelectedColumns(selectedColumns, selectedRows, table);
    }

    public String extractTableName(String command) {
        String[] parts = command.split("\\s+");
        String tableName = null;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("FROM") && i + 1 < parts.length) {
                tableName = parts[i + 1].replace(";", "").trim();
                break;
            }
        }
        return tableName;
    }

    private List<String> extractSelectedColumns(String command, Table table) {
        int startIndex = command.indexOf("SELECT") + "SELECT".length();
        int endIndex = command.indexOf("FROM");
        String columnsString = command.substring(startIndex, endIndex).trim();

        if (columnsString.equals("*") && table != null) {
            return table.getColumns().stream()
                    .map(Column::getColumnName)
                    .collect(Collectors.toList());
        }

        String[] columnNames = columnsString.split(",");

        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = columnNames[i].trim();
        }

        return Arrays.asList(columnNames);
    }

    private void validateSelectedColumns(Table table, List<String> selectedColumns) {
        for (String column : selectedColumns) {
            if (!containsColumn(table, column)) {
                throw new InvalidCommandException("Column: [" + column + "] does not exist in the table");
            }
        }
    }

    private boolean containsColumn(Table table, String columnName) {
        for (Column column : table.getColumns()) {
            if (columnName.equals(column.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    private List<Row> applyWhereClause(Table table, String command) {
        if (!command.contains(WHERE)) {
            return table.getRows();
        }

        String whereClause = command.substring(command.indexOf(WHERE) + WHERE.length()).trim();
        String[] conditionParts = whereClause.split("=");
        if (conditionParts.length != 2) {
            throw new InvalidCommandException("Invalid WHERE clause in the SELECT command: " + command);
        }
        String conditionColumn = conditionParts[0].trim();
        String conditionValue = conditionParts[1]
                .trim()
                .replace("'", "")
                .replace(";", "");

        return table.getRows().stream()
                .filter(row -> matchesCondition(row, table, conditionColumn, conditionValue))
                .collect(Collectors.toList());
    }

    private boolean matchesCondition(Row row, Table table, String conditionColumn, String conditionValue) {
        int columnIndex = getColumnIndex(table, conditionColumn);
        String cellValue = row.getCells().get(columnIndex);
        return cellValue.equalsIgnoreCase(conditionValue);
    }

    private void displaySelectedColumns(List<String> selectedColumns, List<Row> selectedRows, Table table) {
        System.out.println("Selected Columns");

        displaySeparatorBetweenColumns(selectedColumns);
        displayHeader(selectedColumns);
        displaySeparatorBetweenColumns(selectedColumns);

        for (Row row : selectedRows) {
            System.out.print("|");
            for (String column : selectedColumns) {
                int columnIndex = getColumnIndex(table, column);
                String cellValue = row.getCells().get(columnIndex);
                System.out.printf(" %-15s |", cellValue);
            }
            System.out.println();
            displaySeparatorBetweenColumns(selectedColumns);
        }
    }

    private void displaySeparatorBetweenColumns(List<String> selectedColumns) {
        System.out.print("+");
        for (String ignored : selectedColumns) {
            for (int i = 0; i < 17; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }

    private void displayHeader(List<String> selectedColumns) {
        System.out.print("|");
        for (String column : selectedColumns) {
            System.out.printf(" %-15s |", column);
        }
        System.out.println();
    }
}