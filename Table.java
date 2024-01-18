package laboratory_8.project.model;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {
    private final String tableName;
    private final List<Row> rows;
    private final List<Column> columns;

    public Table(String tableName, List<Row> rows, List<Column> columns) {
        this.tableName = tableName;
        this.rows = rows;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getColumnName().equals(columnName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                ", rows=" + rows +
                ", columns=" + columns +
                '}';
    }
}
