package laboratory_8.project.model;

public enum CommandPatterns {
    CREATE_TABLE(
            "CREATE TABLE [a-zA-Z_]+ " +
                    "\\((?:[a-zA-Z_]+\\s+[a-zA-Z_]+\\s*(?:,\\s*[a-zA-Z_]+\\s+[a-zA-Z_]+\\s*)*)\\);"
    ),
    INSERT(
            "INSERT\\s+" +
                    "INTO\\s+[a-zA-Z_]+\\s+" +
                    "VALUES\\s+\\([^)]+\\)(?:,\\s*\\([^)]+\\))*\\s*;"
    ),

    SELECT(
            "SELECT\\s+(\\*|(?:[a-zA-Z_]+(?:,\\s*[a-zA-Z_]+)*))\\s+" +
                    "FROM\\s+[a-zA-Z_]+(?:\\s+" +
                    "WHERE\\s+[a-zA-Z_]+\\s*=\\s*'[^;]+'\\s*)?;"
    ),
    DELETE(
            "DELETE FROM [a-zA-Z_]+(?:\\s+" +
                    "WHERE\\s+[a-zA-Z_]+\\s*=\\s*'.*')?;"
    ),
    UPDATE(
            "UPDATE [a-zA-Z_]+ " +
                    "SET (?:[a-zA-Z_]+\\s*=\\s*[a-zA-Z0-9'\\s]+\\s*(?:,\\s*[a-zA-Z_]+\\s*=\\s*[a-zA-Z0-9'\\s]+\\s*)*)" +
                    "(?:\\s+WHERE\\s+[a-zA-Z_]+\\s*=\\s*('[a-zA-Z0-9'\\s]+'|\\d+)\\s*)?;"
    ),

    SHOW_TABLES(
            "SHOW TABLES;"
    ),
    JOIN(
            "SELECT\\s+[a-zA-Z_]+\\.[a-zA-Z_]+,\\s+[a-zA-Z_]+\\.[a-zA-Z_]+,\\s+[a-zA-Z_]+\\.[a-zA-Z_]+\\s+" +
                    "FROM\\s+[a-zA-Z_]+\\s+" +
                    "INNER\\s+JOIN\\s+[a-zA-Z_]+\\s+" +
                    "ON\\s+[a-zA-Z_]+\\.[a-zA-Z_]+\\s*=\\s*[a-zA-Z_]+\\.[a-zA-Z_]+\\s*;"

    );

    private final String command;

    CommandPatterns(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}