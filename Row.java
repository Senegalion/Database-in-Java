package laboratory_8.project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row implements Serializable {
    private final List<String> cells;

    public Row(List<String> cells) {
        this.cells = new ArrayList<>(cells);
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCell(int index, String value) {
        cells.set(index, value);
    }

    public String getValue(int index) {
        return cells.get(index);
    }
    public void addCell(String value) {
        cells.add(value);
    }

    @Override
    public String toString() {
        return "Row{" +
                "cells=" + cells +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Row row = (Row) object;
        return Objects.equals(cells, row.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells);
    }
}
