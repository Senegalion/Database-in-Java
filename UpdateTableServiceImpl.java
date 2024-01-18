package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.List;

public class UpdateTableServiceImpl implements UpdateTableService, TableInterface {
    @Override
    public void updateTable(List<Table> tableList, String command) {
        String tableName = extractTableName(command);
        Table table = findTable(tableList, tableName);

        boolean hasWhereCondition = command.contains(WHERE);

        if (hasWhereCondition) {
            updateWithCondition(table, command);
        } else {
            updateAllRows(table, command);
        }
        System.out.println("Command completed successfully.");
    }

    @Override
    public String extractTableName(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length > 1) {
            return parts[1]
                    .replace(";", "")
                    .trim();
        } else {
            throw new InvalidCommandException("Table name not found in the UPDATE command");
        }
    }

    private void updateAllRows(Table table, String command) {
        String setClause = command.substring(command.indexOf("SET") + "SET".length()).trim();
        String[] setPairs = setClause.split(",");
        for (Row row : table.getRows()) {
            updateRow(row, table, setPairs);
        }
    }

    private void updateRow(Row row, Table table, String[] setPairs) {
        for (String setPair : setPairs) {
            String[] pair = setPair.trim().split("=");
            if (pair.length != 2) {
                throw new InvalidCommandException("Invalid SET clause in the UPDATE command");
            }
            String columnName = pair[0].trim();
            String value = pair[1]
                    .trim()
                    .replace("'", "")
                    .replace(";", "");
            int columnIndex = getColumnIndex(table, columnName);
            row.setCell(columnIndex, value);
        }
    }

    private void updateWithCondition(Table table, String command) {
        String setClause = command.substring(command.indexOf("SET") + "SET".length(), command.indexOf("WHERE")).trim();
        String conditionClause = command.substring(command.indexOf(WHERE) + WHERE.length()).trim();

        String[] setPairs = setClause.split(",");
        updateRowsWithCondition(table, conditionClause, setPairs);
    }

    private void updateRowsWithCondition(Table table, String conditionClause, String[] setPairs) {
        String[] conditionParts = conditionClause.split("=");
        if (conditionParts.length != 2) {
            throw new InvalidCommandException("Invalid WHERE clause in the UPDATE command");
        }

        String conditionColumn = conditionParts[0].trim();
        String conditionValue = conditionParts[1].trim().replace("'", "").replace(";", "");

        for (Row row : table.getRows()) {
            if (matchCondition(row, table, conditionColumn, conditionValue)) {
                updateRow(row, table, setPairs);
            }
        }
    }

    private boolean matchCondition(Row row, Table table, String conditionColumn, String conditionValue) {
        int columnIndex = getColumnIndex(table, conditionColumn);
        String cellValue = row.getCells().get(columnIndex);
        return cellValue.equalsIgnoreCase(conditionValue);
    }
}
