package nl.endpoint.spatial;

import com.vividsolutions.jts.geom.Coordinate;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class JpaBarRepository2Test {

    @Autowired
    private BarRepository barRepository;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() throws DatabaseUnitException, IOException, SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        IDatabaseConnection dbUnitCon = new DatabaseConnection(con);
        IDataSet dataSet = new SpatialAwareXmlDataSet(
                        getClass().getResourceAsStream("/nl/endpoint/spatial/sampleData2.xml"));
        dbUnitCon.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());

        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    @Test
    public void getShouldReturnCorrectBar() {
        Bar bar = this.barRepository.get(0);
        assertThat(bar.getName()).isEqualTo("Cafe 't Haantje");
    }

    @Test
    public void findByLocationShouldReturnAllBarsWithinRange() throws Exception {
        List<Bar> bars = this.barRepository.findByLocation(new Coordinate(100, 100), 50);
        assertThat(extractProperty("name").from(bars))
                .containsOnly("Cafe 't Haantje", "Cafe The Hide Away");
    }

}

