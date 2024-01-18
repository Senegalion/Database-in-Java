package laboratory_8.project.services;

import laboratory_8.project.model.Table;

import java.util.List;

public interface DeleteDataService {
    void deleteData(List<Table> tableList, String command);
}
