package laboratory_8.project.services;

import laboratory_8.project.model.Table;

import java.util.List;

public interface JoinTablesService {
    void joinTables(List<Table> tableList, String joinCondition);
}