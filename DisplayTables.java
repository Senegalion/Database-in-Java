package laboratory_8.project.services;

import laboratory_8.project.model.Table;

import java.util.List;

public interface DisplayTables {
    void displayTable(Table table);
    void displayTable(Table table, List<String> columnNames);
}
