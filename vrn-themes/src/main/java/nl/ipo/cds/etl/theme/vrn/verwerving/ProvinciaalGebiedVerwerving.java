package nl.ipo.cds.etl.theme.vrn.verwerving;

import nl.ipo.cds.etl.db.annotation.Column;


/**
 * Created by reinoldp on 11/26/2014.
 */
public class ProvinciaalGebiedVerwerving extends AbstractGebiedVerwerving{
	
	 @Column(name = "doel_verwerving")
	 private String doelVerwerving;
	
}
