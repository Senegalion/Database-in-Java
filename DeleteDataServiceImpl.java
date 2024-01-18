package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.Iterator;
import java.util.List;

public class DeleteDataServiceImpl implements DeleteDataService, TableInterface {
    @Override
    public void deleteData(List<Table> tableList, String command) {
        String tableName = extractTableName(command);
        Table table = findTable(tableList, tableName);

        if (command.contains(WHERE)) {
            deleteWithCondition(table, command);
        } else {
            deleteAllRows(table);
        }
        System.out.println("Command completed successfully.");
    }

    private void deleteAllRows(Table table) {
        List<Row> rows = table.getRows();
        rows.clear();
    }

    private void deleteWithCondition(Table table, String command) {
        String condition = command.substring(command.indexOf(WHERE) + WHERE.length()).trim();
        String[] conditionParts = condition.split("=");

        if (conditionParts.length != 2) {
            throw new InvalidCommandException("Invalid WHERE clause in the DELETE command");
        }

        String columnName = conditionParts[0].trim();
        String value = conditionParts[1]
                .trim()
                .replace("'", "")
                .replace(";", "");

        Iterator<Row> iterator = table.getRows().iterator();
        iterateThroughRows(table, iterator, columnName, value);
    }

    private void iterateThroughRows(Table table, Iterator<Row> iterator, String columnName, String value) {
        while (iterator.hasNext()) {
            Row row = iterator.next();
            int columnIndex = getColumnIndex(table, columnName);
            String cellValue = row.getCells().get(columnIndex);

            if (cellValue.equalsIgnoreCase(value)) {
                iterator.remove();
            }
        }
    }
}
