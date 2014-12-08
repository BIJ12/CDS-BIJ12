package nl.ipo.vrn.etl.theme.verwerving;

import nl.ipo.cds.etl.PersistableFeature;
import nl.ipo.cds.etl.db.annotation.Column;
import nl.ipo.cds.etl.db.annotation.Table;
import nl.ipo.cds.etl.theme.annotation.CodeSpace;
import nl.ipo.cds.etl.theme.annotation.MappableAttribute;

import nl.ipo.vrn.etl.theme.AbstractGebied;
import org.deegree.commons.tom.ows.CodeType;
import org.deegree.geometry.Geometry;

@Table(name = "hazard_area", schema = "bron")
public abstract  class AbstractGebiedVerwerving extends AbstractGebied {

    @Column(name = "status_verwerving")
    private String statusVerwerving;

    @Column(name = "doel_verwerving")
    private String doelVerwerving;

    @Column(name = "type_eigenaar")
    private String typeEigenaar;


}
