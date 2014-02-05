/*
 * Copyright 2002-2013 the original author or authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.endpoint.spatial;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.hibernate.Session;
import org.hibernatespatial.GeometryUserType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class JpaBarRepository implements BarRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Bar> findByLocation(Coordinate startPoint, int range) {
        Session session = (Session)entityManager.getDelegate();
        org.hibernate.Query query = session.createQuery("SELECT b from Bar b where within(location, :filter) = true");
        //Query query = this.entityManager.createNamedQuery("Bar.findByLocation");
        Geometry circle = createCircleWithinRange(startPoint, range);
        query.setParameter("filter", circle, GeometryUserType.TYPE);
        return query.list();
    }

    private Geometry createCircleWithinRange(final Coordinate centerPoint, final int range) {
        GeometricShapeFactory gsf = new GeometricShapeFactory();
        gsf.setSize(range);
        gsf.setNumPoints(100);
        gsf.setCentre(new Coordinate(centerPoint));
        return gsf.createCircle();
    }

}
