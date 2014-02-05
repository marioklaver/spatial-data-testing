package nl.endpoint.spatial.spring;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import nl.endpoint.spatial.SpatialAwareXmlDataSet;
import org.dbunit.dataset.IDataSet;
import org.springframework.core.io.Resource;

public class SpatialAwareDataSetLoader extends AbstractDataSetLoader {
    @Override
    protected IDataSet createDataSet(final Resource resource) throws Exception {
        //return new SpatialAwareFlatXmlDataSet(new FlatXmlProducer(new InputSource(resource.getInputStream())));
        return new SpatialAwareXmlDataSet(resource.getInputStream());
    }
}
