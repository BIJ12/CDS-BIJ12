package nl.ipo.cds.etl.theme.vrn.domain;

import java.io.PrintWriter;
import java.io.StringWriter;

import nl.ipo.cds.etl.db.DBWriter;
import nl.ipo.cds.etl.db.DBWriterFactory;

import org.deegree.commons.tom.ows.CodeType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.io.ParseException;

public class GebiedBeheerTest extends AbstractGebiedTest<LandelijkGebiedBeheer> {

private DBWriterFactory<LandelijkGebiedBeheer> dbWriterFactory;	
	
	
	
	@Before
	public void setUp() {
		dbWriterFactory = new DBWriterFactory<LandelijkGebiedBeheer>(LandelijkGebiedBeheer.class, "dataset_id", TEST_DATASET_ID);
	}
	
	@Test
	public void test() throws ParseException {
		
		LandelijkGebiedBeheer gebied = new LandelijkGebiedBeheer();
		gebied.setId(TEST_DATASET_ID);
		gebied.setDoelRealisatie(new CodeType ("CodeDoelBeheer", "http://www.namespace.com"));
		gebied.setBeheerpakket(new CodeType ("CodeBeheerPakket", "http://www.namespace.com"));
		gebied.setStatusBeheer(new CodeType ("CodeStatusBeheer", "http://www.namespace.com"));
		gebied.setTypeBeheerder(new CodeType ("CodeTypeBeheerder", "http://www.namespace.com"));
		gebied.setEenheidnummer("eenheidNummer");
		writeGebied(gebied);
		StringWriter stringWriter = new StringWriter();
		DBWriter<LandelijkGebiedBeheer> dbWriter = dbWriterFactory.getDBWriter(new PrintWriter(stringWriter));	
		dbWriter.writeObject(gebied);
		System.out.println(stringWriter.getBuffer().toString());
		String output = "\"0\",\"CodeStatusBeheer\",\"CodeBeheerPakket\",\"CodeDoelBeheer\",\"CodeTypeBeheerder\",\"eenheidNummer\",\"2014-12-15 14:57:27.094\",\"2014-12-15 14:59:55.565\",\"TEST.ID.0\",\"imnaBronhouder\",\"2\",\"0103000020407100000100000005000000000000006835FB4000000000944A214100000000385AFB4000000000944A214100000000385AFB4000000000CC4C2141000000006835FB4000000000CC4C2141000000006835FB4000000000944A2141\",\"23\",\"0\""+System.lineSeparator(); 
		Assert.assertEquals(output, stringWriter.getBuffer().toString());
		
	}
	
	

}
