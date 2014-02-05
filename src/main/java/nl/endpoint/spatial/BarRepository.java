package nl.endpoint.spatial;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.List;

public interface BarRepository {
    public Bar get(int id);

    public List<Bar> findByLocation(Coordinate startPoint, int range);
}
