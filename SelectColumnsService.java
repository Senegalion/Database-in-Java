package laboratory_8.project.services;

import laboratory_8.project.model.Table;

import java.util.List;

public interface SelectColumnsService {
    void selectAndDisplayColumns(List<Table> tableList, String command);
}
