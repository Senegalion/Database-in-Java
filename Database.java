package laboratory_8.project;

import laboratory_8.project.exceptions.InvalidCommandException;
import laboratory_8.project.exceptions.NoTablesException;
import laboratory_8.project.model.CommandPatterns;
import laboratory_8.project.model.Table;
import laboratory_8.project.services.CommandExecutor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    public static final String EXIT = "EXIT;";
    private static final String DATA_DIRECTORY = "tables/";
    private static final String TABLE_FILE_EXTENSION = ".tables";

    public static void main(String[] args) {
        Database database = new Database();
        List<Table> tableList = new ArrayList<>();
        database.loadTables(tableList);
        database.run(tableList);
        database.saveTables(tableList);
    }

    private void run(List<Table> tableList) {
        printWelcomeText();
        startCommandLoop(tableList);
    }

    private void printWelcomeText() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Welcome to a library offering functionality of a simple file-based database");
        System.out.println("---------------------------------------------------------------------------");
    }

    private void startCommandLoop(List<Table> tableList) {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            try {
                String command = scanner.nextLine().trim();
                isRunning = executeCommand(command, tableList);
            } catch (InvalidCommandException | NoTablesException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean executeCommand(String command, List<Table> tableList) {
        CommandExecutor commandExecutor = new CommandExecutor();
        if (command.matches(CommandPatterns.CREATE_TABLE.getCommand())) {
            commandExecutor.createTable(tableList, command);
        } else if (command.matches(CommandPatterns.INSERT.getCommand())) {
            commandExecutor.insertData(tableList, command);
        } else if (command.matches(CommandPatterns.SHOW_TABLES.getCommand())) {
            commandExecutor.displayTables(tableList);
        } else if (command.matches(CommandPatterns.SELECT.getCommand())) {
            commandExecutor.selectColumns(tableList, command);
        } else if (command.matches(CommandPatterns.DELETE.getCommand())) {
            commandExecutor.deleteData(tableList, command);
        } else if (command.matches(CommandPatterns.UPDATE.getCommand())) {
            commandExecutor.updateTable(tableList, command);
        } else if (command.matches(CommandPatterns.JOIN.getCommand())) {
            commandExecutor.joinTables(tableList, command);
        } else if (EXIT.equals(command)) {
            return false;
        } else {
            throw new InvalidCommandException("Could not find stored procedure '" + command + "'.");
        }
        return true;
    }

    private void loadTables(List<Table> tableList) {
        File dataDirectory = new File(DATA_DIRECTORY);
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }

        File[] tableFiles = dataDirectory.listFiles((dir, name) -> name.endsWith(TABLE_FILE_EXTENSION));
        if (tableFiles != null) {
            for (File tableFile : tableFiles) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tableFile))) {
                    Table table = (Table) ois.readObject();
                    tableList.add(table);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void saveTables(List<Table> tableList) {
        for (int i = 0; i < tableList.size(); i++) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(DATA_DIRECTORY + "table" + i + TABLE_FILE_EXTENSION))
            ) {
                oos.writeObject(tableList.get(i));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
