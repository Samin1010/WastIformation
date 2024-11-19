package app;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 */

public class JDBCConnection {

    // Name of database file (contained in database folder)
    public static final String DATABASE = "jdbc:sqlite:database/WasteRecycling.db";

    public static final String RegionalOrganicStatisticsColumnHeaders = "select a.Period, (a.KerbsideCollected + a.KerbsideFOGOCollected + a.CleanUpCollected + a.DropOffCollected + a.OtherCouncilCollected) AS Collected, (a.KerbsideRecycled + a.KerbsideFOGORecycled + a.CleanUpRecycled + a.DropOffRecycled + a.OtherCouncilRecycled) AS Recycled, ((a.KerbsideCollected + a.KerbsideFOGOCollected + a.CleanUpCollected + a.DropOffCollected + a.OtherCouncilCollected) - (a.KerbsideRecycled + a.KerbsideFOGORecycled + a.CleanUpRecycled + a.DropOffRecycled + a.OtherCouncilRecycled)) AS Disposed,  (100 * ((a.KerbsideFOGOCollected + a.CleanUpCollected + a.DropOffCollected + a.OtherCouncilCollected) - (a.KerbsideFOGORecycled + a.CleanUpRecycled + a.DropOffRecycled + a.OtherCouncilRecycled)) / ( SELECT sum(((c.KerbsideFOGOCollected + c.CleanUpCollected + c.DropOffCollected + c.OtherCouncilCollected) - (c.KerbsideFOGORecycled + c.CleanUpRecycled + c.DropOffRecycled + c.OtherCouncilRecycled))) AS Disposed  from RegionalGroup as d ";
    public static final String RegionalRecyclingStatisticsColumnHeaders = "select a.Period, (a.CDSCollected + a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) AS Collected, (a.CDSCollected + a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled) AS Recycled, ((a.CDSCollected + a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) - (a.CDSCollected + a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled)) AS Disposed, (100 * ((a.CDSCollected + a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) - (a.CDSCollected + a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled)) / ( SELECT sum(((c.CDSCollected + c.KerbsideCollected + c.CleanUpCollected + c.DropOffCollected) - (c.CDSCollected + c.KerbsideRecycled + c.CleanUpRecycled + c.DropOffRecycled))) AS k  from RegionalGroup as d ";
    public static final String RegionalWasteStatisticsColumnHeaders = "select a.Period, ( a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) AS Collected, ( a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled) AS Recycled, (( a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) - ( a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled)) AS Disposed, (100 * (( a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected) - ( a.KerbsideRecycled + a.CleanUpRecycled + a.DropOffRecycled)) / ( SELECT sum((( c.KerbsideCollected + c.CleanUpCollected + c.DropOffCollected) - ( c.KerbsideRecycled + c.CleanUpRecycled + c.DropOffRecycled))) AS k  from RegionalGroup as d ";
    public static final String RegionalOrganicStatisticsColumnHeaders2 = "(a.KerbsideCollected + a.KerbsideFOGOCollected + a.CleanUpCollected + a.DropOffCollected + a.OtherCouncilCollected) AS Collected, (a.KerbsideRecycled + a.KerbsideFOGORecycled + a.CleanUpRecycled + a.DropOffRecycled + a.OtherCouncilRecycled) AS Recycled from RegionalGroup as b ";
    public static final String RegionalRecyclingStatisticsColumnHeaders2 = "(a.KerbsideCollected + a.CDSCollected + a.CleanUpCollected + a.DropOffCollected)  AS Collected, (a.KerbsideRecycled + a.CDSRecycled + a.CleanUpRecycled + a.DropOffRecycled)  AS Recycled from RegionalGroup as b ";
    public static final String RegionalWasteStatisticsColumnHeaders2 = "(a.KerbsideCollected + a.CleanUpCollected + a.DropOffCollected)  AS Collected, (a.KerbsideRecycled  + a.CleanUpRecycled + a.DropOffRecycled ) AS Recycled from RegionalGroup as b ";

    public static ArrayList<String> SelectedLGAST2A = new ArrayList<String>();
    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Countries in the database.
     * @return
     *    Returns an ArrayList of LGA objects
     */
    public ArrayList<LGA> getAllCountries() {
        // Create the ArrayList of LGA objects to return
        ArrayList<LGA> lgas = new ArrayList<LGA>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM lga";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                int code     = results.getInt("code");
                String name  = results.getString("name");

                // Create an LGA Object
                LGA lga = new LGA(code, name);

                // Add the LGA object to the array
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return lgas;
    }

    public ArrayList<String> getRegionalGroups() {
        // Create the ArrayList to return - of Strings for the movie titles
        ArrayList<String> regionalGroups = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = """
                    select RegionalGroupName from RegionalGroup;
                    """;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query resultss

            while (results.next()) {

                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String name = results.getString("RegionalGroupName");
                // Store the movieName in the ArrayList to return
                regionalGroups.add(name);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movie titles
        return regionalGroups;
    }

    public ArrayList<Integer> GetStartYears() {
        // Create the ArrayList to return - of Strings for the movie titles
        ArrayList<Integer> startYear = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = """
                    select StartYear from AnnualPeriod;
                    """;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query resultss

            while (results.next()) {

                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                Integer year = results.getInt("StartYear");
                // Store the movieName in the ArrayList to return
                startYear.add(year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movie titles
        return startYear;
    }

    public ArrayList<Integer> GetEndYears() {
        // Create the ArrayList to return - of Strings for the movie titles
        ArrayList<Integer> endYear = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = """
                    select EndYear from AnnualPeriod;
                    """;

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query resultss

            while (results.next()) {

                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                Integer year = results.getInt("EndYear");
                // Store the movieName in the ArrayList to return
                endYear.add(year);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movie titles
        return endYear;
    }

    public ArrayList<ST2BResult> GetST2BResults(String RegionalGroupName, String ResourceType, String WasteStatistic, int MinWaste, String OrderAttribute, String OrderBy ) {
        // Create the ArrayList of LGA objects to return
        ArrayList<ST2BResult> FilteredResults = new ArrayList<ST2BResult>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "";
            String cat1 = "";

            if(ResourceType.equals("Recycling")){
                cat1 = RegionalRecyclingStatisticsColumnHeaders;
            }
            if(ResourceType.equals("Organic")){
                cat1 = RegionalOrganicStatisticsColumnHeaders;
            }
            if(ResourceType.equals("Waste")){
                cat1 = RegionalWasteStatisticsColumnHeaders;
            }
            query += cat1;
            query = 
                query + 
                "INNER JOIN Regional" + ResourceType +"Statistics AS c ON c.RegionalGroup = d.RegionalGroupName " +
                "WHERE c.Period = a.Period " +
                " )) AS Percentage from RegionalGroup as b " +
                "INNER JOIN Regional" + ResourceType +"Statistics AS a ON a.RegionalGroup = b.RegionalGroupName " +
                "WHERE b.RegionalGroupName == '" + RegionalGroupName +"' AND " + WasteStatistic + " > " + MinWaste + " " + 
                "ORDER BY " + OrderAttribute + " " +OrderBy;
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                // Create an LGA Object
                ST2BResult result = new ST2BResult(results.getString("Period"),results.getInt("Collected"), results.getInt("Recycled"), results.getInt("Disposed"), results.getInt("Percentage"));

                // Add the LGA object to the array
                FilteredResults.add(result);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return FilteredResults;
    }

    public ArrayList<String> getColumnNames(String ColumnName) {
        // Create the ArrayList to return - of Strings for the movie titles
        ArrayList<String> names = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "PRAGMA table_info(" + ColumnName + ")";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query resultss

            while (results.next()) {

                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String name = results.getString("name");
                // Store the movieName in the ArrayList to return
                names.add(name);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movie titles
        return names;
    }

    public ST3BResult GetST3BResults(String RegionalGroup, String ResourceType, String StartYear, String EndYear, String ChangeType) {

        Connection connection = null;
        ST3BResult finalResult = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "";
            String cat1 = "";

            if(ResourceType.equals("Recycling")){
                cat1 = RegionalRecyclingStatisticsColumnHeaders2;
            }
            if(ResourceType.equals("Organic")){
                cat1 = RegionalOrganicStatisticsColumnHeaders2;
            }
            if(ResourceType.equals("Waste")){
                cat1 = RegionalWasteStatisticsColumnHeaders2;
            }
            // query += cat1;
            // query = 
            //     "WITH SubQuery AS ( " +
            //     "select " +
            //     cat1 + 
            //     "INNER JOIN Regional" + ResourceType +"Statistics AS a ON a.RegionalGroup = b.RegionalGroupName " +
            //     "INNER JOIN AnnualPeriod as h ON a.Period = h.Period " +
            //     "WHERE (b.RegionalGroupName == '" + RegionalGroup +"') AND ((h.EndYear = " + EndYear +") OR (h.StartYear = " + StartYear + ")) " +
            //     // "ORDER BY h.Period " +
            //     ") " +
            //     "Select * from ( " +
            //     "select Collected AS StartCollected, Recycled AS StartRecycled from SubQuery "+
            //     "Limit 1 Offset 0 " +
            //     ") " +
            //     "CROSS JOIN ( " +
            //     "select Collected AS EndCollected, Recycled AS EndRecycled from SubQuery " +
            //     "Limit 1 Offset 1 " +
            //     ") "
            // ;
            query = 
                "WITH SubQuery AS ( " +
                "select h.StartYear AS Start, h.EndYear AS End, " +
                cat1 + 
                "INNER JOIN Regional" + ResourceType +"Statistics AS a ON a.RegionalGroup = b.RegionalGroupName " +
                "INNER JOIN AnnualPeriod as h ON a.Period = h.Period " +
                "WHERE (b.RegionalGroupName == '" + RegionalGroup +"') AND ((h.EndYear = " + EndYear +") OR (h.StartYear = " + StartYear + ")) " +
                // "ORDER BY h.Period " +
                ") " +
                "Select * from ( " +
                "select Collected AS StartCollected, Recycled AS StartRecycled from SubQuery "+
                "WHERE Start = " + StartYear + " " +
                ") " +
                "CROSS JOIN ( " +
                "select Collected AS EndCollected, Recycled AS EndRecycled from SubQuery " +
                "WHERE End = " + EndYear + " " +
                ") "
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            results.next();
            int ResultStartCollected = results.getString("StartCollected") == null ? 0 : results.getInt("StartCollected");
            int ResultStartRecycled = results.getString("StartRecycled") == null ? 0 : results.getInt("StartRecycled");
            int ResultEndCollected = results.getString("EndCollected") == null ? 0 : results.getInt("EndCollected");
            int ResultEndRecycled = results.getString("EndRecycled") == null ? 0 : results.getInt("EndRecycled");
            boolean isPercent = false;
            if(ChangeType.equals("Percentage")){
                isPercent = true;
            }else{
                isPercent = false;
            }

            finalResult = new ST3BResult(ResultStartCollected, ResultStartRecycled, ResultEndCollected, ResultEndRecycled, isPercent);


            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return finalResult;
    }

    public ArrayList<Student> GetStudentDetails() {
        // Create the ArrayList of LGA objects to return
        ArrayList<Student> FilteredResults = new ArrayList<Student>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "select * from Student"
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                // Create an LGA Object
                Student result = new Student(results.getString("StudentName"),results.getString("StudentID"), results.getString("ImagePath"));

                // Add the LGA object to the array
                FilteredResults.add(result);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return FilteredResults;
    }

    public void StoreLGAST2A (String LGAName){
        this.SelectedLGAST2A.add(LGAName);
    }
    public ArrayList<String> GetLGAST2A (){
        return this.SelectedLGAST2A;
    }
    public void ClearLGAST2A (){
        this.SelectedLGAST2A = new ArrayList<String>();
    }

    public ArrayList<String> getLGAnames() {

        ArrayList<String> lgas = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT LGAName FROM lga";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String name = results.getString("LGAName");
                lgas.add(name);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return lgas;
    }

    public ArrayList<Integer> GetPersonaID() {
        // Create the ArrayList of LGA objects to return
        ArrayList<Integer> names = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "select personaID from persona"
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                
                // Add the LGA object to the array
                names.add(results.getInt("personaID"));
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return names;
    }

    public String GetPersonaName(int ID) {
        // Create the ArrayList of LGA objects to return
        String name = "";

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "select personaName from persona WHERE personaID = " + ID;
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            results.next();
            name = results.getString("personaName");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return name;
    }

    public String GetPersonaImage(int ID) {
        // Create the ArrayList of LGA objects to return
        String name = "";

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "select ImagePath from persona WHERE personaID = " + ID;
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            results.next();
            name = results.getString("ImagePath");

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return name;
    }

    public ArrayList<String> GetAttributes(int personaID, String AttributeType) {
        // Create the ArrayList of LGA objects to return
        ArrayList<String> names = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = 
                "select AttributeType, AttributeDesc from persona as a " +
                "inner join personaattribute as c on c.PersonaID = a.PersonaID " +
                "where (a.PersonaID = " + personaID + ") AND (AttributeType = '" + AttributeType + "') "
            ;
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need

                
                // Add the LGA object to the array
                names.add(results.getString("AttributeDesc"));
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return names;
    }
}
