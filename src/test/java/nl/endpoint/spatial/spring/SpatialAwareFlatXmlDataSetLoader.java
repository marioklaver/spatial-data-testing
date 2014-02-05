package nl.endpoint.spatial.spring;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import nl.endpoint.spatial.SpatialAwareFlatXmlDataSet;
import org.dbunit.dataset.IDataSet;
import org.springframework.core.io.Resource;

public class SpatialAwareFlatXmlDataSetLoader extends AbstractDataSetLoader {
    @Override
    protected IDataSet createDataSet(final Resource resource) throws Exception {
        return new SpatialAwareFlatXmlDataSet(resource.getInputStream());
    }
}
