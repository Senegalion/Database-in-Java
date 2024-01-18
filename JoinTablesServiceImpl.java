package laboratory_8.project.services;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.model.Column;
import laboratory_8.project.model.Row;
import laboratory_8.project.model.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JoinTablesServiceImpl implements JoinTablesService, TableInterface {
    @Override
    public void joinTables(List<Table> tables, String joinCondition) {
        validateJoinCondition(joinCondition);
        String[] tableNames = extractTableNamesFromQuery(joinCondition);

        String[] selectAndJoinParts = joinCondition.split("\\s+FROM\\s+");
        String selectPart = selectAndJoinParts[0].replace("SELECT", "").trim();

        List<String> displayedColumns = extractDisplayedColumns(selectPart);

        Table leftTable = findTable(tables, tableNames[0]);
        Table rightTable = findTable(tables, tableNames[1]);

        joinTablesAndDisplay(leftTable, rightTable, displayedColumns);
    }

    private void validateJoinCondition(String joinCondition) {
        String[] parts = joinCondition.split("\\s+JOIN\\s+|\\s+INNER\\s+JOIN\\s+|\\s+LEFT\\s+JOIN\\s+|\\s+RIGHT\\s+JOIN\\s+");
        if (parts.length != 2) {
            throw new InvalidCommandException("Invalid JOIN condition: " + joinCondition);
        }
    }

    private String[] extractTableNamesFromQuery(String query) {
        String[] parts = query.split("\\s+FROM\\s+|\\s+JOIN\\s+|\\s+INNER\\s+JOIN\\s+|\\s+LEFT\\s+JOIN\\s+|\\s+RIGHT\\s+JOIN\\s+");
        if (parts.length < 2) {
            throw new InvalidCommandException("Invalid query: " + query);
        }

        String[] tableNames = Arrays.copyOfRange(parts, 1, parts.length);
        return Arrays.stream(tableNames)
                .map(name -> name.trim().split("\\s+")[0].trim())
                .toArray(String[]::new);
    }

    private List<String> extractDisplayedColumns(String selectPart) {
        String[] columnParts = selectPart.split(",");

        List<String> displayedColumns = new ArrayList<>();
        for (String columnPart : columnParts) {
            String[] parts = columnPart.trim().split("\\.");
            if (parts.length != 2) {
                throw new InvalidCommandException("Invalid column reference: " + columnPart);
            }

            displayedColumns.add(parts[1].trim());
        }

        return displayedColumns;
    }

    private void joinTablesAndDisplay(Table leftTable, Table rightTable, List<String> displayedColumns) {
        List<Row> joinedRows = new ArrayList<>();
        List<Column> joinedColumns = new ArrayList<>();
        boolean columnsAdded = false;

        for (Row leftRow : leftTable.getRows()) {
            Row joinedRow = new Row(new ArrayList<>());

            for (int i = 0; i < leftTable.getColumns().size(); i++) {
                Column leftColumn = leftTable.getColumns().get(i);

                joinRows(rightTable, leftRow, leftColumn, i, joinedRow, columnsAdded, joinedColumns);
            }
            joinedRows.add(joinedRow);
            columnsAdded = true;
        }

        displayJoinedTable(joinedRows, joinedColumns, displayedColumns);
    }

    private void joinRows(Table rightTable, Row leftRow, Column leftColumn, int i, Row joinedRow, boolean columnsAdded, List<Column> joinedColumns) {
        if (rightTable.getColumns().contains(leftColumn)) {
            int rightIndex = getColumnIndex(rightTable.getColumns(), leftColumn);

            Row matchingRow = findMatchingRow(rightTable, rightIndex, getCellValue(leftRow, i));

            addRowsAndColumns(rightTable, joinedRow, columnsAdded, joinedColumns, matchingRow);
        } else {
            joinedRow.addCell(getCellValue(leftRow, i));
            if (!columnsAdded) {
                joinedColumns.add(leftColumn);
            }
        }
    }

    private static void addRowsAndColumns(Table rightTable, Row joinedRow, boolean columnsAdded, List<Column> joinedColumns, Row matchingRow) {
        if (matchingRow != null) {
            joinedRow.getCells().addAll(matchingRow.getCells());
            if (!columnsAdded) {
                joinedColumns.addAll(rightTable.getColumns());
            }
        } else {
            joinedRow.addCell("");
        }
    }

    private Row findMatchingRow(Table table, int columnIndex, String value) {
        for (Row row : table.getRows()) {
            if (getCellValue(row, columnIndex).equals(value)) {
                return row;
            }
        }
        return null;
    }

    private int getColumnIndex(List<Column> columnList, Column column) {
        for (int i = 0; i < columnList.size(); i++) {
            if (column.equals(columnList.get(i))) {
                return i;
            }
        }
        throw new InvalidCommandException("Table does not contain column: " + column.getColumnName());
    }

    private String getCellValue(Row row, int index) {
        return row.getValue(index);
    }

    private void displayJoinedTable(List<Row> joinedRows, List<Column> joinedColumns, List<String> displayedColumns) {
        List<Column> displayedJoinedColumns = new ArrayList<>();

        for (Column column : joinedColumns) {
            if (displayedColumns.contains(column.getColumnName())) {
                displayedJoinedColumns.add(column);
            }
        }

        printTableLine(displayedJoinedColumns.size());
        printTableRow(displayedJoinedColumns.stream()
                .map(Column::getColumnName)
                .collect(Collectors.toList())
        );
        printTableLine(displayedJoinedColumns.size());

        displayRows(joinedRows, joinedColumns, displayedColumns);

        printTableLine(displayedJoinedColumns.size());
    }

    private void displayRows(List<Row> joinedRows, List<Column> joinedColumns, List<String> displayedColumns) {
        for (Row row : joinedRows) {
            List<String> displayedCells = new ArrayList<>();
            for (String displayedColumn : displayedColumns) {
                int columnIndex = -1;
                for (int i = 0; i < joinedColumns.size(); i++) {
                    if (joinedColumns.get(i).getColumnName().equals(displayedColumn)) {
                        columnIndex = i;
                        break;
                    }
                }
                if (columnIndex != -1) {
                    displayedCells.add(row.getValue(columnIndex));
                }
            }
            printTableRow(displayedCells);
        }
    }

    private void printTableLine(int numColumns) {
        System.out.println("+" + "-".repeat(17).repeat(numColumns) + "+");
    }

    private void printTableRow(List<String> cells) {
        System.out.print("| ");
        cells.forEach(cell -> System.out.printf("%-15s| ", cell));
        System.out.println();
    }
}