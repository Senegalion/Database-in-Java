package laboratory_8.project.model;

import java.io.Serializable;
import java.util.Objects;

public class Column implements Serializable {
    private final String columnName;
    private final String dataType;

    public Column(String columnName, String dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Column column = (Column) object;
        return Objects.equals(columnName, column.columnName) && Objects.equals(dataType, column.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, dataType);
    }
}
