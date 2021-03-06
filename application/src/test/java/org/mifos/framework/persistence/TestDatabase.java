/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.framework.persistence;

import static org.mifos.framework.persistence.DatabaseVersionPersistence.FIRST_NUMBERED_VERSION;
import static org.mifos.framework.util.helpers.DatabaseSetup.executeScript;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;

import org.mifos.accounts.financial.util.helpers.FinancialInitializer;
import org.mifos.framework.hibernate.helper.StaticHibernateUtil;
import org.mifos.framework.util.StandardTestingService;

public class TestDatabase {

    public static void runUpgradeScripts(Connection connection) throws Exception {
        runUpgradeScripts(FIRST_NUMBERED_VERSION, connection);
    }

    public static void runUpgradeScripts(int fromVersion, Connection connection) throws Exception {
        DatabaseVersionPersistence persistence = new DatabaseVersionPersistence(connection);
        Assert.assertEquals(fromVersion, persistence.read());
        persistence.upgradeDatabase();
    }

    /**
     * Create a database and upgrade it to the first database version with a
     * number. Should be run on an empty database (no tables).
     *
     * @throws IOException
     */
    public static void upgradeToFirstNumberedVersion(Connection connection) throws SQLException, IOException {
        executeScript("mifosdbcreationscript.sql", connection);
        executeScript("mifosmasterdata.sql", connection);
        executeScript("rmpdbcreationscript.sql", connection);
        executeScript("rmpmasterdata.sql", connection);
        executeScript("Iteration13-DBScripts25092006.sql", connection);
        executeScript("Iteration14-DDL-DBScripts10102006.sql", connection);
        executeScript("Iteration14-DML-DBScripts10102006.sql", connection);
        executeScript("Iteration15-DDL-DBScripts24102006.sql", connection);
        executeScript("Iteration15-DBScripts20061012.sql", connection);
        executeScript("add-version.sql", connection);
        executeScript("Index.sql", connection);
    }

    /**
     * This method was added to work around integration test inter- and
     * intra-dependencies. Once these dependencies in main code are eliminated,
     *  we should be able to use Spring managed testing environment (transaction)
     *  this method should be eliminated as well.
     */
    public static void resetMySQLDatabase() throws Exception {
        StaticHibernateUtil.flushAndClearSession();
        truncateMySQLDatabase();
        insertTestData();

        // If the database is ever blown away, we must re-populate chart of
        // accounts data since some unit tests rely on its presence. It must
        // be created via this method since adding it via an sql script would
        // invalidate *other* unit tests that assume this method has been
        // called.
        FinancialInitializer.initialize();
    }

    public static void createMySQLTestDatabase() throws Exception {
        dropMySQLDatabase();
        createLatestSchema();
        insertTestData();
    }

    public static void createLatestSchema() throws Exception {
        Connection connection = getJDBCConnection();
        connection.setAutoCommit(false);
        executeScript("latest-schema.sql", connection);
        connection.commit();
        connection.close();
    }

    public static void insertTestData() throws Exception {
        Connection connection = getJDBCConnection();
        connection.setAutoCommit(false);
        executeScript("latest-data.sql", connection);
        executeScript("custom_data.sql", connection);
        executeScript("testdbinsertionscript.sql", connection);
        connection.commit();
        connection.close();
    }

    /*public static void createNotMappedTables() throws Exception {
        Connection connection = getJDBCConnection();
        String sql = "CREATE TABLE if not exists DATABASE_VERSION ( DATABASE_VERSION INTEGER ) ENGINE=InnoDB CHARACTER SET utf8";
        connection.createStatement().execute(sql);
        sql = "ALTER TABLE LOOKUP_ENTITY ADD COLUMN DESCRIPTION VARCHAR(200)";
        connection.createStatement().execute(sql);
        connection.close();
    }*/

    public static void dropMySQLDatabase() throws Exception {
        Connection connection = getJDBCConnection();
        connection.setAutoCommit(false);
        executeScript("mifosdroptables.sql", connection);
        connection.commit();
        connection.close();
    }

    public static void truncateMySQLDatabase() throws Exception {
        Connection connection = getJDBCConnection();
        connection.setAutoCommit(false);
        executeScript("truncate_tables.sql", connection);
        connection.commit();
        connection.close();
    }

    /**
     * MySQL specific schema dump generation
     *
     * @return database structure as String dump
     * @throws Exception
     */
    public static String getAllTablesStructureDump() throws Exception {
        String tablesDumpList = "";
        Connection connection = getJDBCConnection();
        ResultSet rs = connection.createStatement().executeQuery("SHOW TABLES");
        while (rs.next()) {
            String tableDump = getTableStructureDump(rs.getString(1), connection);
            tablesDumpList += tableDump;
        }
        connection.close();
        return tablesDumpList;
    }

    private static String getTableStructureDump(String tableName, Connection connection) throws Exception {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = connection.createStatement().executeQuery("DESCRIBE " + tableName);
        sb.append(tableName).append(" ");
        while (rs.next()) {
            sb.append("\n").append(rs.getString(1)).append(" ");
            sb.append(rs.getString(2)).append(" ");
            sb.append(rs.getString(3)).append(" NULL ");
            sb.append(rs.getString(4)).append(" KEY ");
            if(rs.getString(5) != null) {
            sb.append(rs.getString(5)).append(" ");
            }
            if(rs.getString(6) != null) {
            sb.append(rs.getString(6)).append(",");
            }
        }
        sb.append(";").append("\n");
        return sb.toString();
    }

    // FIXME Use Spring Managed Connection
    /**
     * Foreign key disabled connection
     */
    private static Connection getJDBCConnection() throws Exception {
        final Properties databaseSettings = new StandardTestingService().getDatabaseConnectionSettings();
        final String url = databaseSettings.getProperty("hibernate.connection.url");
        final String param = "&sessionVariables=FOREIGN_KEY_CHECKS=0";
        final String user = databaseSettings.getProperty("hibernate.connection.username");
        final String password = databaseSettings.getProperty("hibernate.connection.password");

        return DriverManager.getConnection(url + param, user, password);
    }
}
