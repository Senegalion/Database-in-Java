package laboratory_8.project.services;

import laboratory_8.project.model.Column;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.List;

public class DisplayTablesImpl implements DisplayTables {
    @Override
    public void displayTable(Table table) {
        System.out.println("Table Name: " + table.getTableName());

        displayColumns(table);

        displaySeparatorBetweenColumnsAndRows(table);
        System.out.println();

        displayRows(table);
        System.out.println("-------------------------------------------------------");
    }

    @Override
    public void displayTable(Table table, List<String> columnNames) {
        // Display table header with selected columns
        System.out.println("Table: " + table.getTableName());
        System.out.println("+-----------------------+"); // Adjust based on the width of the columns

        // Display column names
        for (String columnName : columnNames) {
            System.out.print("| " + columnName + "\t");
        }
        System.out.println("|");

        // Display table data
        for (int i = 0; i < table.getRows().size(); i++) {
            System.out.println("+-----------------------+");
            for (String columnName : columnNames) {
                int columnIndex = table.getColumnIndex(columnName);
                System.out.print("| " + table.getRows().get(i).getValue(columnIndex) + "\t");
            }
            System.out.println("|");
        }

        System.out.println("+-----------------------+");
    }

    private void displayColumns(Table table) {
        System.out.print("|");
        for (Column column : table.getColumns()) {
            System.out.printf(" %-15s |", column.getColumnName());
        }
        System.out.println();
    }

    private void displaySeparatorBetweenColumnsAndRows(Table table) {
        System.out.print("+");
        for (Column ignored : table.getColumns()) {
            System.out.print("-----------------+");
        }
    }

    private void displayRows(Table table) {
        for (Row row : table.getRows()) {
            List<String> cells = row.getCells();
            System.out.print("|");
            for (String cell : cells) {
                System.out.printf(" %-15s |", cell);
            }
            System.out.println();
        }
    }
}