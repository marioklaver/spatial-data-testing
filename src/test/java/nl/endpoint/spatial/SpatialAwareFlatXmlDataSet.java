package nl.endpoint.spatial;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;

/**
 * If the value starts with GEOM_ the rest of the value will be converted to a geometry object.
 * It follows the Well Known Text (WKT) standard.
 *
 * Examples:
 * POINT (30 10)
 * LINESTRING (30 10, 10 30, 40 40)
 * POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))
 * POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10), (20 30, 35 35, 30 20, 20 30))  (Polygon with hole)
 */
public class SpatialAwareFlatXmlDataSet extends FlatXmlDataSet {
    public SpatialAwareFlatXmlDataSet(final FlatXmlProducer flatXmlProducer) throws DataSetException {
        super(flatXmlProducer);
    }

    @Override
    public void row(final Object[] values) throws DataSetException {
        convertGeometryColumnsFromTextToBinary(values);
        super.row(values);
    }

    private void convertGeometryColumnsFromTextToBinary(final Object[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && values[i].toString().startsWith("GEOM_")) {
                values[i] = GeometryUtil.convertFromTextToBinary(values[i].toString().substring(5));
            }
        }
    }
}
