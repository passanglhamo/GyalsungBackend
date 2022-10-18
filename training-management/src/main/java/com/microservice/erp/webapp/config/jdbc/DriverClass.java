package com.microservice.erp.webapp.config.jdbc;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public enum DriverClass {
    MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://", "3306", "/"),
    PostgresQLv7("org.postgresql.Driver", "jdbc:postgresql://", "5432", "/"),
    DB2("COM.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2://", "446", "/"),
    OracleOCI9i("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@", "", "/"),
    SQLServer("com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:sqlserver://", "1433", "/"),
    H2_EMBEDDED("org.h2.Driver", "jdbc:h2:mem:", "", ""),
    H2_FILE("org.h2.Driver", "jdbc:h2:file:", "", ""),
    H2_SERVER("org.h2.Driver", "jdbc:h2:tcp://", "8084", ""),
    H2_SERVER_TLS("org.h2.Driver", "jdbc:h2:ssl://", "8085", ""),
    HSQL_EMBEDDED("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://", "9001", ""),
    DERBY("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://", "1527", "/"),
    DERBY_EMBEDDED("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:", "1527", ""),
    DERBY_MEM("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:memory:", "1527", ""),
    JDBC_ODBC("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc://", "", "/");

    private final String driverClassName;
    private final String urlSchema;
    private final String defaultPort;
    private final String pathPrefix;

    private DriverClass(String driverClassName, String urlSchema, String defaultPort, String pathPrefix) {
        this.driverClassName = driverClassName;
        this.urlSchema = urlSchema;
        this.defaultPort = defaultPort;
        this.pathPrefix = pathPrefix;
    }

    public String toString() {
        return this.driverClassName;
    }

    public String urlSchema() {
        return this.urlSchema;
    }

    public String defaultPort() {
        return this.defaultPort;
    }

    public String pathPrefix() {
        return this.pathPrefix;
    }

    public static DriverClass getMatchedDriver(String connectionURL) {
        DriverClass result = JDBC_ODBC;
        ArrayList all = new ArrayList(EnumSet.allOf(DriverClass.class));
        Iterator var3 = all.iterator();

        while(var3.hasNext()) {
            DriverClass driverClass = (DriverClass)var3.next();
            if(connectionURL.startsWith(driverClass.urlSchema())) {
                result = driverClass;
                break;
            }
        }

        return result;
    }
}
