package laboratory_8.project.services;

import laboratory_8.project.exceptions.NoTablesException;
import laboratory_8.project.model.Table;

import java.util.List;

public class CommandExecutor {

    public void createTable(List<Table> tableList, String command) {
        TableInterfaceImpl tableCreator = new TableInterfaceImpl();
        tableCreator.createTable(tableList, command);
    }

    public void insertData(List<Table> tableList, String command) {
        InsertDataToTableService insertDataToTableService = new InsertDataToTableServiceImpl();
        insertDataToTableService.insertData(tableList, command);
    }

    public void displayTables(List<Table> tableList) {
        if (tableList.isEmpty()) {
            throw new NoTablesException("No tables to display");
        }
        for (Table table : tableList) {
            DisplayTables displayTables = new DisplayTablesImpl();
            displayTables.displayTable(table);
        }
    }

    public void selectColumns(List<Table> tableList, String command) {
        SelectColumnsService selectColumnsService = new SelectColumnsServiceImpl();
        selectColumnsService.selectAndDisplayColumns(tableList, command);
    }

    public void deleteData(List<Table> tableList, String command) {
        DeleteDataService deleteDataService = new DeleteDataServiceImpl();
        deleteDataService.deleteData(tableList, command);
    }

    public void updateTable(List<Table> tableList, String command) {
        UpdateTableService updateTableService = new UpdateTableServiceImpl();
        updateTableService.updateTable(tableList, command);
    }

    public void joinTables(List<Table> tableList, String command) {
        JoinTablesService joinTablesService = new JoinTablesServiceImpl();
        joinTablesService.joinTables(tableList, command);
    }
}