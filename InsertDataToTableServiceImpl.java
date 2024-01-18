package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Column;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertDataToTableServiceImpl implements InsertDataToTableService, TableInterface {
    @Override
    public void insertData(List<Table> tableList, String command) {
        String tableName = extractTableName(command);
        Table table = findTable(tableList, tableName);
        List<String> rows = extractRows(command);
        int addedRows = 0;

        for (String rowValues : rows) {
            List<String> values = extractValues(rowValues);
            validateInsertedValues(table, values);
            addRowToTable(table, values);
            addedRows++;
        }

        if (addedRows != 1) {
            System.out.println(addedRows + " rows affected");
        } else {
            System.out.println(addedRows + " row affected");
        }
    }

    private List<String> extractRows(String command) {
        int startIndex = command.indexOf("VALUES (") + "VALUES (".length();
        int endIndex = command.lastIndexOf(");");
        String valuesString = command.substring(startIndex, endIndex).trim();
        return Arrays.asList(valuesString.split("\\),\\s*\\("));
    }

    private List<String> extractValues(String valuesString) {
        valuesString = valuesString.trim();
        if (valuesString.startsWith("(") && valuesString.endsWith(")")) {
            valuesString = valuesString.substring(1, valuesString.length() - 1);
        }

        List<String> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("'(.*?)'").matcher(valuesString);

        while (matcher.find()) {
            values.add(matcher.group(1));
        }

        return values;
    }

    private void validateInsertedValues(Table table, List<String> values) {
        List<Column> columns = table.getColumns();

        checkNumberOfInsertedValues(values, columns);
        checkDataTypes(values, columns);
    }

    private void checkDataTypes(List<String> values, List<Column> columns) {
        for (int i = 0; i < columns.size(); i++) {
            String dataType = columns.get(i).getDataType();
            String value = values.get(i);

            if (dataType.equals(INT) && isNotInt(value)) {
                throw new InvalidCommandException(
                        "Invalid data type for column '" + columns.get(i).getColumnName() + "'. Expected INT."
                );
            } else if (dataType.equals(VARCHAR) && !isString(value)) {
                throw new InvalidCommandException(
                        "Invalid data type for column '" + columns.get(i).getColumnName() + "'. Expected VARCHAR."
                );
            } else if (dataType.equals(DATE) && !isDate(value)) {
                throw new InvalidCommandException(
                        "Invalid data type for column '" + columns.get(i).getColumnName() + "'. Expected DATE."
                );
            }
        }
    }

    private static void checkNumberOfInsertedValues(List<String> values, List<Column> columns) {
        int numberOfColumns = columns.size();
        int numberOfInsertedValues = values.size();

        if (numberOfColumns != numberOfInsertedValues) {
            throw new InvalidCommandException(
                    "Number of values: [" + numberOfInsertedValues + "] " +
                            "does not match the number of columns: [" + numberOfColumns + "].");
        }
    }

    private boolean isNotInt(String value) {
        try {
            Integer.parseInt(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean isString(String value) {
        return isNotInt(value);
    }

    private boolean isDate(String value) {
        String dateFormatRegex = "\\d{4}-\\d{2}-\\d{2}";

        if (value.matches(dateFormatRegex)) {
            try {
                int year = Integer.parseInt(value.substring(0, 4));
                int month = Integer.parseInt(value.substring(5, 7));
                int day = Integer.parseInt(value.substring(8, 10));

                if (year >= 0 && month >= 1 && month <= 12 && day >= 1 && day <= 31) {
                    return true;
                }
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                return false;
            }
        }
        return false;
    }

    private void addRowToTable(Table table, List<String> values) {
        List<Row> rows = table.getRows();
        rows.add(new Row(values));
    }
}
