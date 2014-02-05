package nl.endpoint.spatial.spring;

import geodb.GeoDB;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

public class GeoDbDataSource implements FactoryBean<DataSource> {

    private String testDatabaseName;

    private String username = "sa";

    private String password = "";

    private DataSource dataSource;

    public GeoDbDataSource() {
    }

    public GeoDbDataSource(String testDatabaseName) {
        setTestDatabaseName(testDatabaseName);
    }

    public GeoDbDataSource(String testDatabaseName, String username, String password) {
        setTestDatabaseName(testDatabaseName);
        setUsername(username);
        setPassword(password);
    }

    public void setTestDatabaseName(String testDatabaseName) {
        this.testDatabaseName = testDatabaseName;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public DataSource getObject() throws Exception {
        return getDataSource();
    }

    @Override
    public Class<DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PostConstruct
    public void init() throws SQLException {
        if (testDatabaseName == null) {
            throw new IllegalArgumentException(
                    "The name of the test database to create is required");
        }
        initDataSource();
    }

    private void initDataSource() throws SQLException {
        this.dataSource = createDataSource();
        GeoDB.InitGeoDB(dataSource.getConnection());
    }

    private DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:" + testDatabaseName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
