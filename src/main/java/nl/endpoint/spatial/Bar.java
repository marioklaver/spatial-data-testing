package nl.endpoint.spatial;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Bar.findByLocation", query = "SELECT b from Bar b where within(location, ?) = true") })
public class Bar {

	@Id
	private int id;

	private String name;

    //@Type(type="java.sql.Blob")
    //@Type(type="org.hibernate.spatial.GeometryType")
    @Type(type="org.hibernatespatial.GeometryUserType")
    private Geometry location;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Geometry getLocation() {
        return location;
    }

    public void setLocation(final Geometry location) {
        this.location = location;
    }
}
