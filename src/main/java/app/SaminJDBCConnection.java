package app;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SaminJDBCConnection {

    public static final String DATABASE = "jdbc:sqlite:database/WasteRecycling.db";
    public static final String LGAST2ALGAORGANICTABLEHEADER = "select c.LGAName AS LGAName, b.Population AS Population, b.HouseholdSurveyed AS HouseholdSurveyed, (REPLACE(a.KerbsideFOGOCollected, ',', '') + REPLACE(a.KerbsideCollected, ',', '') + REPLACE(a.CleanUpCollected, ',', '') + REPLACE(a.DropOffCollected, ',', '')  + REPLACE(a.OtherCouncilCollected, ',', '')) AS TotalCollected, (REPLACE(a.KerbsideFOGORecycled, ',', '') + REPLACE(a.KerbsideRecycled, ',', '') + REPLACE(a.CleanUpRecycled, ',', '') + REPLACE(a.DropOffRecycled, ',', '')  + REPLACE(a.OtherCouncilRecycled, ',', '')) AS TotalRecycled from LGAOrganicStatistics as a ";
    public static final String LGAST2ALGARECYCLETABLEHEADER = "select c.LGAName AS LGAName, b.Population AS Population, b.HouseholdSurveyed AS HouseholdSurveyed, (REPLACE(a.CDSCollected, ',', '') + REPLACE(a.KerbsideCollected, ',', '') + REPLACE(a.CleanUpCollected, ',', '') + REPLACE(a.DropOffCollected, ',', '') ) AS TotalCollected, (REPLACE(a.CDSRecycled, ',', '') + REPLACE(a.KerbsideRecycled, ',', '') + REPLACE(a.CleanUpRecycled, ',', '') + REPLACE(a.DropOffRecycled, ',', '') ) AS TotalRecycled from LGARecyclingStatistics as a ";
    public static final String LGAST2ALGAWASTETABLEHEADER = "select c.LGAName AS LGAName, b.Population AS Population, b.HouseholdSurveyed AS HouseholdSurveyed, ( REPLACE(a.KerbsideCollected, ',', '') + REPLACE(a.CleanUpCollected, ',', '') + REPLACE(a.DropOffCollected, ',', '') ) AS TotalCollected, ( REPLACE(a.KerbsideRecycled, ',', '') + REPLACE(a.CleanUpRecycled, ',', '') + REPLACE(a.DropOffRecycled, ',', '') ) AS TotalRecycled from LGAWasteStatistics as a ";

    public SaminJDBCConnection() {
        System.out.println("Created JDBC Connection Object");
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

    public ArrayList<ST2AResult> getResult2A(String LGAname, String resource, String subType, String sort,
            String sortBy) {

        ArrayList<ST2AResult> result = new ArrayList<ST2AResult>();

        Connection connection = null;
        String Cat1 = "";

        if (resource.equals("Organic")) {
            Cat1 = LGAST2ALGAORGANICTABLEHEADER;
        }
        if (resource.equals("Recyclable")) {
            Cat1 = LGAST2ALGARECYCLETABLEHEADER;
        }
        if (resource.equals("Waste")) {
            Cat1 = LGAST2ALGAWASTETABLEHEADER;
        }

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // String query =
            // "Select *, (TotalRecycled * 100 / TotalCollected) AS
            // AveragePercentageRecycled, (100 * TotalCollected / HouseholdSurveyed) AS
            // AverageWastePerHousehold from " +
            // "(select c.LGAName AS LGAName, b.Population AS Population,
            // b.HouseholdSurveyed AS HouseholdSurveyed, " + LGAST2ALGAORGANICTABLEHEADER +
            // "INNER JOIN LGAStatistics b ON b.LGACode = a.LGACode AND b.Period = a.Period
            // "+
            // "INNER JOIN LGA AS c ON c.LGACode = a.LGACode AND c.LGAName = '" + LGAname +
            // "'"+
            // "WHERE a.Period = '2019-2020' "+
            // ") ";

            String query = "Select *, (TotalRecycled * 100 / TotalCollected) AS AveragePercentageRecycled, (100 * TotalCollected / HouseholdSurveyed) AS AverageWastePerHousehold from "
                    +
                    "( " +
                    Cat1 +
                    "INNER JOIN LGAStatistics b ON b.LGACode = a.LGACode AND b.Period = a.Period " +
                    "INNER JOIN LGA AS c ON c.LGACode = a.LGACode " +
                    "WHERE a.Period = '2019-2020' AND c.LGAName = '" + LGAname + "' " +
                    ") ";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                ST2AResult ReturnedResult = new ST2AResult(results.getString("LGAName"), results.getInt("Population"),
                        results.getInt("HouseholdSurveyed"), results.getInt("TotalCollected"),
                        results.getInt("TotalRecycled"), results.getInt("AveragePercentageRecycled"),
                        results.getInt("AverageWastePerHousehold"));
                result.add(ReturnedResult);

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
        return result;
    }

    public ArrayList<result3Awaste> getResult3Awaste(String LGAname, String resource, String period, int cutOff,
            String sort, String sortBy) {

        ArrayList<result3Awaste> result = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = """
                    WITH UserRow AS (
                        SELECT
                            CASE
                                WHEN LGAWasteStatistics.KerbsideCollected = 0 OR LGAWasteStatistics.KerbsideCollected IS NULL THEN 0
                                ELSE (LGAWasteStatistics.KerbsideRecycled * 100) / LGAWasteStatistics.KerbsideCollected
                            END AS Kerbside,
                            CASE
                                WHEN LGAWasteStatistics.DropOffCollected = 0 OR LGAWasteStatistics.DropOffCollected IS NULL THEN 0
                                ELSE (LGAWasteStatistics.DropOffRecycled * 100) / LGAWasteStatistics.DropOffCollected
                            END AS DropOff,
                            CASE
                                WHEN LGAWasteStatistics.CleanUpCollected = 0 OR LGAWasteStatistics.CleanUpCollected IS NULL THEN 0
                                ELSE (LGAWasteStatistics.CleanUpRecycled * 100) / LGAWasteStatistics.CleanUpCollected
                            END AS CleanUp
                        FROM LGA NATURAL JOIN LGAWasteStatistics
                        WHERE LGA.LGAName = '"""
                    + LGAname + "'"
                    + """
                            ),
                            RecyclingPercentages AS (
                                SELECT
                                    LGA.LGAName AS LGA,
                                    CASE
                                        WHEN LGAWasteStatistics.KerbsideCollected = 0 OR LGAWasteStatistics.KerbsideCollected IS NULL THEN 0
                                        ELSE (LGAWasteStatistics.KerbsideRecycled * 100) / LGAWasteStatistics.KerbsideCollected
                                    END AS Kerbside,
                                    CASE
                                        WHEN LGAWasteStatistics.DropOffCollected = 0 OR LGAWasteStatistics.DropOffCollected IS NULL THEN 0
                                        ELSE (LGAWasteStatistics.DropOffRecycled * 100) / LGAWasteStatistics.DropOffCollected
                                    END AS DropOff,
                                    CASE
                                        WHEN LGAWasteStatistics.CleanUpCollected = 0 OR LGAWasteStatistics.CleanUpCollected IS NULL THEN 0
                                        ELSE (LGAWasteStatistics.CleanUpRecycled * 100) / LGAWasteStatistics.CleanUpCollected
                                    END AS CleanUp
                                FROM LGA NATURAL JOIN LGAWasteStatistics
                                WHERE LGAWasteStatistics.period = '"""
                    + period + "' AND LGA.LGAName <> '" + LGAname + "'"
                    + """
                            )
                            SELECT * FROM (
                                SELECT LGA,
                                Kerbside,
                                DropOff,
                                CleanUp,
                                ROUND(
                                    CASE
                                        WHEN (sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp) = 0)
                                             OR (sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp FROM UserRow)) = 0)
                                        THEN 0
                                        ELSE (
                                            (Kerbside * (SELECT Kerbside FROM UserRow)) +
                                            (DropOff * (SELECT DropOff FROM UserRow)) +
                                            (CleanUp * (SELECT CleanUp FROM UserRow))
                                        ) / (
                                            sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp) *
                                            sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp FROM UserRow))
                                        )
                                    END, 3
                                ) AS Similarity
                            FROM RecyclingPercentages
                            ORDER BY Similarity DESC LIMIT
                            """
                    + cutOff + ") ORDER BY " + sort + " " + sortBy + ";";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                result3Awaste wasteObj = new result3Awaste(results.getString("LGA"), results.getDouble("Similarity"),
                        results.getInt("Kerbside"),
                        results.getInt("DropOff"), results.getInt("CleanUp"));
                result.add(wasteObj);
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
        return result;
    }

    public ArrayList<result3Arecyclable> getResult3Arecyclable(String LGAname, String resource, String period,
            int cutOff,
            String sort, String sortBy) {

        ArrayList<result3Arecyclable> result = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = """
                    WITH UserRow AS (
                        SELECT
                            CASE
                                WHEN LGARecyclingStatistics.KerbsideCollected = 0 OR LGARecyclingStatistics.KerbsideCollected IS NULL THEN 0
                                ELSE (LGARecyclingStatistics.KerbsideRecycled * 100) / LGARecyclingStatistics.KerbsideCollected
                            END AS Kerbside,
                            CASE
                                WHEN LGARecyclingStatistics.DropOffCollected = 0 OR LGARecyclingStatistics.DropOffCollected IS NULL THEN 0
                                ELSE (LGARecyclingStatistics.DropOffRecycled * 100) / LGARecyclingStatistics.DropOffCollected
                            END AS DropOff,
                            CASE
                                WHEN LGARecyclingStatistics.CleanUpCollected = 0 OR LGARecyclingStatistics.CleanUpCollected IS NULL THEN 0
                                ELSE (LGARecyclingStatistics.CleanUpRecycled * 100) / LGARecyclingStatistics.CleanUpCollected
                            END AS CleanUp,
                            CASE
                                WHEN LGARecyclingStatistics.CDSCollected = 0 OR LGARecyclingStatistics.CDSCollected IS NULL THEN 0
                                ELSE (LGARecyclingStatistics.CDSRecycled * 100) / LGARecyclingStatistics.CDSCollected
                            END AS CDS
                        FROM LGA
                        NATURAL JOIN LGARecyclingStatistics
                        WHERE LGARecyclingStatistics.period = '"""
                    + period + "'" +
                    "AND LGA.LGAName = '" + LGAname + "'" +
                    """
                                  ),
                            RecyclingPercentages AS (
                                SELECT
                                    LGA.LGAName AS LGA,
                                    CASE
                                        WHEN LGARecyclingStatistics.KerbsideCollected = 0 OR LGARecyclingStatistics.KerbsideCollected IS NULL THEN 0
                                        ELSE (LGARecyclingStatistics.KerbsideRecycled * 100) / LGARecyclingStatistics.KerbsideCollected
                                    END AS Kerbside,
                                    CASE
                                        WHEN LGARecyclingStatistics.DropOffCollected = 0 OR LGARecyclingStatistics.DropOffCollected IS NULL THEN 0
                                        ELSE (LGARecyclingStatistics.DropOffRecycled * 100) / LGARecyclingStatistics.DropOffCollected
                                    END AS DropOff,
                                    CASE
                                        WHEN LGARecyclingStatistics.CleanUpCollected = 0 OR LGARecyclingStatistics.CleanUpCollected IS NULL THEN 0
                                        ELSE (LGARecyclingStatistics.CleanUpRecycled * 100) / LGARecyclingStatistics.CleanUpCollected
                                    END AS CleanUp,
                                    CASE
                                        WHEN LGARecyclingStatistics.CDSCollected = 0 OR LGARecyclingStatistics.CDSCollected IS NULL THEN 0
                                        ELSE (LGARecyclingStatistics.CDSRecycled * 100) / LGARecyclingStatistics.CDSCollected
                                    END AS CDS
                                FROM LGA
                                NATURAL JOIN LGARecyclingStatistics
                                WHERE LGARecyclingStatistics.period = '"""
                    + period + "'" +
                    "AND LGA.LGAName <> '" + LGAname + "'" +
                    """
                            )
                            SELECT * FROM (
                                SELECT LGA,
                                Kerbside,
                                DropOff,
                                CleanUp,
                                CDS,
                                ROUND(
                                    CASE
                                        WHEN (sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + CDS * CDS) = 0)
                                             OR (sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + CDS * CDS FROM UserRow)) = 0)
                                        THEN 0
                                        ELSE (
                                            (Kerbside * (SELECT Kerbside FROM UserRow)) +
                                            (DropOff * (SELECT DropOff FROM UserRow)) +
                                            (CleanUp * (SELECT CleanUp FROM UserRow)) +
                                            (CDS * (SELECT CDS FROM UserRow))
                                        ) / (
                                            sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + CDS * CDS) *
                                            sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + CDS * CDS FROM UserRow))
                                        )
                                    END, 3
                                ) AS Similarity
                            FROM RecyclingPercentages
                            ORDER BY Similarity DESC LIMIT
                            """
                    + cutOff + ") ORDER BY " + sort + " " + sortBy + ";";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                result3Arecyclable recyclableObj = new result3Arecyclable(results.getString("LGA"),
                        results.getDouble("Similarity"),
                        results.getInt("Kerbside"), results.getInt("CDS"), results.getInt("DropOff"),
                        results.getInt("CleanUp"));
                result.add(recyclableObj);
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
        return result;
    }

    public ArrayList<result3Aorganic> getResult3Aorganic(String LGAname, String resource, String period,
            int cutOff,
            String sort, String sortBy) {

        ArrayList<result3Aorganic> result = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = """
                    WITH UserRow AS (
                        SELECT
                            CASE
                                WHEN LGAOrganicStatistics.KerbsideCollected = 0 OR LGAOrganicStatistics.KerbsideCollected IS NULL THEN 0
                                ELSE (LGAOrganicStatistics.KerbsideRecycled * 100) / LGAOrganicStatistics.KerbsideCollected
                            END AS Kerbside,
                            CASE
                                WHEN LGAOrganicStatistics.DropOffCollected = 0 OR LGAOrganicStatistics.DropOffCollected IS NULL THEN 0
                                ELSE (LGAOrganicStatistics.DropOffRecycled * 100) / LGAOrganicStatistics.DropOffCollected
                            END AS DropOff,
                            CASE
                                WHEN LGAOrganicStatistics.CleanUpCollected = 0 OR LGAOrganicStatistics.CleanUpCollected IS NULL THEN 0
                                ELSE (LGAOrganicStatistics.CleanUpRecycled * 100) / LGAOrganicStatistics.CleanUpCollected
                            END AS CleanUp,
                            CASE
                                WHEN LGAOrganicStatistics.KerbsideFOGOCollected = 0 OR LGAOrganicStatistics.KerbsideFOGOCollected IS NULL THEN 0
                                ELSE (LGAOrganicStatistics.KerbsideFOGORecycled * 100) / LGAOrganicStatistics.KerbsideFOGOCollected
                            END AS KerbsideFOGO,
                            CASE
                                WHEN LGAOrganicStatistics.OtherCouncilCollected = 0 OR LGAOrganicStatistics.OtherCouncilCollected IS NULL THEN 0
                                ELSE (LGAOrganicStatistics.OtherCouncilRecycled * 100) / LGAOrganicStatistics.OtherCouncilCollected
                            END AS OtherCouncil
                        FROM LGAOrganicStatistics NATURAL JOIN LGA
                        WHERE LGA.LGAName = '"""
                    + LGAname + "' AND LGAOrganicStatistics.period = '" + period + "'"
                    + """
                            ),
                            RecyclingPercentages AS (
                                SELECT
                                    LGA.LGAName AS LGA,
                                    CASE
                                        WHEN LGAOrganicStatistics.KerbsideCollected = 0 OR LGAOrganicStatistics.KerbsideCollected IS NULL THEN 0
                                        ELSE (LGAOrganicStatistics.KerbsideRecycled * 100) / LGAOrganicStatistics.KerbsideCollected
                                    END AS Kerbside,
                                    CASE
                                        WHEN LGAOrganicStatistics.DropOffCollected = 0 OR LGAOrganicStatistics.DropOffCollected IS NULL THEN 0
                                        ELSE (LGAOrganicStatistics.DropOffRecycled * 100) / LGAOrganicStatistics.DropOffCollected
                                    END AS DropOff,
                                    CASE
                                        WHEN LGAOrganicStatistics.CleanUpCollected = 0 OR LGAOrganicStatistics.CleanUpCollected IS NULL THEN 0
                                        ELSE (LGAOrganicStatistics.CleanUpRecycled * 100) / LGAOrganicStatistics.CleanUpCollected
                                    END AS CleanUp,
                                    CASE
                                        WHEN LGAOrganicStatistics.KerbsideFOGOCollected = 0 OR LGAOrganicStatistics.KerbsideFOGOCollected IS NULL THEN 0
                                        ELSE (LGAOrganicStatistics.KerbsideFOGORecycled * 100) / LGAOrganicStatistics.KerbsideFOGOCollected
                                    END AS KerbsideFOGO,
                                    CASE
                                        WHEN LGAOrganicStatistics.OtherCouncilCollected = 0 OR LGAOrganicStatistics.OtherCouncilCollected IS NULL THEN 0
                                        ELSE (LGAOrganicStatistics.OtherCouncilRecycled * 100) / LGAOrganicStatistics.OtherCouncilCollected
                                    END AS OtherCouncil
                                FROM LGAOrganicStatistics NATURAL JOIN LGA
                                WHERE LGAOrganicStatistics.period = '"""
                    + period + "' AND LGA.LGAName <> '" + LGAname + "'"
                    + """
                            )
                            SELECT * FROM (
                                SELECT LGA,
                                Kerbside,
                                DropOff,
                                CleanUp,
                                KerbsideFOGO,
                                OtherCouncil,
                                ROUND(
                                    CASE
                                        WHEN
                                            (sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + KerbsideFOGO * KerbsideFOGO + OtherCouncil * OtherCouncil) = 0)
                                            OR
                                            (sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + KerbsideFOGO * KerbsideFOGO + OtherCouncil * OtherCouncil FROM UserRow)) = 0)
                                        THEN 0
                                        ELSE (
                                            (Kerbside * (SELECT Kerbside FROM UserRow)) +
                                            (DropOff * (SELECT DropOff FROM UserRow)) +
                                            (CleanUp * (SELECT CleanUp FROM UserRow)) +
                                            (KerbsideFOGO * (SELECT KerbsideFOGO FROM UserRow)) +
                                            (OtherCouncil * (SELECT OtherCouncil FROM UserRow))
                                        ) / (
                                            sqrt(Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + KerbsideFOGO * KerbsideFOGO + OtherCouncil * OtherCouncil) *
                                            sqrt((SELECT Kerbside * Kerbside + DropOff * DropOff + CleanUp * CleanUp + KerbsideFOGO * KerbsideFOGO + OtherCouncil * OtherCouncil FROM UserRow))
                                        )
                                    END, 3
                                ) AS Similarity
                            FROM
                                RecyclingPercentages
                            ORDER BY Similarity DESC LIMIT
                            """
                    + cutOff + ") ORDER BY " + sort + " " + sortBy + ";";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                result3Aorganic organicObj = new result3Aorganic(results.getString("LGA"),
                        results.getDouble("Similarity"),
                        results.getInt("Kerbside"), results.getInt("KerbsideFOGO"), results.getInt("DropOff"),
                        results.getInt("CleanUp"), results.getInt("OtherCouncil"));
                result.add(organicObj);
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
        return result;
    }

    public int getHousesByPeriod(String period) {

        int result = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(HouseHoldSurveyed) AS Houses FROM LGAStatistics WHERE period='" + period + "';";
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                result = results.getInt("Houses");
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
        return result;
    }

    public int getLGAcountByPeriod(String period) {

        int result = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT COUNT(LGACode) AS LGAnum FROM LGAStatistics WHERE period='" + period + "';";
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                result = results.getInt("LGAnum");
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
        return result;
    }

    public ArrayList<result2A> get2A(ArrayList<String> LGAs, String type, ArrayList<String> subType, String sort,
            String sortBy) {

        ArrayList<result2A> result = new ArrayList<>();
        int count = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "WITH Totals AS (\r\n" + //
                    "SELECT LGA.LGACode AS LGACode, LGAStatistics.HouseholdSurveyed AS Houses,";
            count = 0;
            for (String sub : subType) {
                if (count == 0)
                    query += " " + type + "." + sub + "Collected";
                else
                    query += " + " + type + "." + sub + "Collected";
                count++;
            }

            query += " AS TotalCollected,";
            count = 0;
            for (String sub : subType) {
                if (count == 0)
                    query += " " + type + "." + sub + "Recycled";
                else
                    query += " + " + type + "." + sub + "Recycled";
                count++;
            }
            query += " AS TotalRecycled FROM LGAStatistics NATURAL JOIN LGA NATURAL JOIN " + type
                    + " WHERE LGAStatistics.period='2019-2020' AND (";
            count = 0;
            for (String LGAname : LGAs) {
                if (count == 0)
                    query += "LGA.LGAName='" + LGAname + "'";
                else
                    query += " OR LGA.LGAName='" + LGAname + "'";
                count++;
            }
            query += "))\r\n";
            query += "SELECT LGA.LGAName AS LGA, LGAStatistics.Population AS Population, Totals.Houses AS Houses, Totals.TotalCollected, Totals.TotalRecycled,\r\n"
                    + //
                    "CASE WHEN (Totals.TotalCollected=0) THEN 0 ELSE ((Totals.TotalRecycled * 100) / Totals.TotalCollected) END AS AvgRecycled,\r\n"
                    + //
                    "ROUND (CASE WHEN (Totals.Houses=0) THEN 0 ELSE (CAST(Totals.TotalCollected AS Float) / CAST(Totals.Houses AS Float)) END, 2) AS AvgWastePerHousehold\r\n"
                    + //
                    "FROM LGAStatistics NATURAL JOIN LGA NATURAL JOIN " + type
                    + " JOIN Totals ON LGA.LGACode=Totals.LGACode WHERE LGAStatistics.period='2019-2020' AND (";
            count = 0;
            for (String LGAname : LGAs) {
                if (count == 0)
                    query += "LGA.LGAName='" + LGAname + "'";
                else if (count > 0)
                    query += " OR LGA.LGAName='" + LGAname + "'";
                count++;
            }
            query += ") ORDER BY " + sort + " " + sortBy + ";";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String LGAname = results.getString("LGA");
                int population = results.getInt("Population");
                int houses = results.getInt("Houses");
                int collected = results.getInt("TotalCollected");
                int recycled = results.getInt("TotalRecycled");
                int avgRecycled = results.getInt("AvgRecycled");
                double avgWastePerHousehold = results.getDouble("AvgWastePerHousehold");
                result2A resultObj = new result2A(LGAname, population, houses, collected, recycled, avgRecycled,
                        avgWastePerHousehold);
                result.add(resultObj);
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
        return result;
    }

}
