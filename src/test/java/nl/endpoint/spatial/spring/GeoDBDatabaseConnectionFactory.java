package nl.endpoint.spatial.spring;


import geodb.GeoDB;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

public class GeoDBDatabaseConnectionFactory implements FactoryBean<Object> {
    private static Logger logger = LoggerFactory.getLogger(GeoDBDatabaseConnectionFactory.class);

    private DataSource dataSource;

    private IDatabaseConnection databaseConnection;

    public GeoDBDatabaseConnectionFactory() {
    }

    public GeoDBDatabaseConnectionFactory(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // this method is automatically called by Spring after configuration to perform a dependency check and init
    @PostConstruct
    public void init() {
        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "The name of the test database to create is required");
        }
        initDatabaseConnection();
    }

    // implementing FactoryBean

    // this method is automatically called by Spring to expose the DataSource as a bean
    public Object getObject() throws Exception {
        return getDatabaseConnection();
    }

    public IDatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public Class<?> getObjectType() {
        return IDatabaseConnection.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private void initDatabaseConnection() {
        try {
            this.databaseConnection = new DatabaseConnection(dataSource.getConnection());
            this.databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
            GeoDB.InitGeoDB(dataSource.getConnection());
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}