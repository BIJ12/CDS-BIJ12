package nl.ipo.cds.etl.theme.vrn.validation;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import nl.ipo.cds.domain.EtlJob;
import nl.ipo.cds.etl.AbstractValidator;
import nl.ipo.cds.etl.PersistableFeature;
import nl.ipo.cds.etl.log.EventLogger;
import nl.ipo.cds.etl.postvalidation.IBulkValidator;
import nl.ipo.cds.etl.postvalidation.IGeometryStore;
import nl.ipo.cds.etl.theme.vrn.Context;
import nl.ipo.cds.etl.theme.vrn.Message;
import nl.ipo.cds.etl.theme.vrn.domain.AbstractGebied;
import nl.ipo.cds.validation.AttributeExpression;
import nl.ipo.cds.validation.ValidationReporter;
import nl.ipo.cds.validation.Validator;
import nl.ipo.cds.validation.callbacks.UnaryCallback;
import nl.ipo.cds.validation.constants.Constant;
import nl.ipo.cds.validation.execute.CompilerException;
import nl.ipo.cds.validation.geometry.GeometryExpression;
import nl.ipo.cds.validation.gml.CodeExpression;
import nl.ipo.cds.validation.gml.codelists.CodeListFactory;

import org.deegree.geometry.Geometry;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author annes
 *
 *         Base class for IMNa validation. Specifies the validations that are
 *         required for all IMNa themes
 * @param <T>
 */
public class AbstractVrnValidator<T extends AbstractGebied> extends
		AbstractValidator<T, Message, Context> {

	private IGeometryStore<? extends PersistableFeature> geometryStore;
	private IBulkValidator bulkValidator;
	private String jdbcConnectionString;
	private HibernateTemplate hibernateTemplate;
	
	private final GeometryExpression<Message, Context, Geometry> geometrie = geometry("geometrie");

	// private final Constant<Message, Context, String> doelRealisatieCodeSpace
	// = constant("doelRealisatie");
	private final Constant<Message, Context, String> imnaBronhouderCodeSpace = constant("imnaBronhouder");
	// private final Constant<Message, Context, String> typeBeheerderCodeSpace =
	// constant("typeBeheerder");

	private final AttributeExpression<Message, Context, Timestamp> begintijd = timestampAttr("begintijd");
	private final AttributeExpression<Message, Context, Timestamp> eindtijd = timestampAttr("eindtijd");
	private final AttributeExpression<Message, Context, String> identificatie = stringAttr("identificatie");
	private final AttributeExpression<Message, Context, Integer> relatienummer = intAttr("relatienummer");
	private final AttributeExpression<Message, Context, Integer> contractnummer = intAttr("contractnummer");
	/**
	 * codelijst doel realisatie is voor zowel doelbeheer als doelverwerving als
	 * doelinrichting
	 */
	// private final CodeExpression<Message, Context> doelRealisatie =
	// code("doelRealisatie");
	private final CodeExpression<Message, Context> imnaBronhouder = code("imnaBronhouder");

	// private final CodeExpression<Message, Context> typeBeheerder =
	// code("typeBeheerder");

	public AbstractVrnValidator(final Map<Object, Object> validatorMessages,
			Class<T> clazz) throws CompilerException {
		super(Context.class, clazz, validatorMessages);
	}

	@Override
	public Context beforeJob(final EtlJob job,
			final CodeListFactory codeListFactory,
			final ValidationReporter<Message, Context> reporter) {
		
		// create h2 database
		// add reference to context
		String uuId = UUID.randomUUID().toString();
		geometryStore.createStore(uuId);
		
		return new Context(codeListFactory, reporter, uuId);
	}

	/*
	 * TODO: IMNa validatie op attribuut, type, kardinaliteit, codeList (xsd
	 * validatie)
	 */

	public Validator<Message, Context> getBegintijdValidator() {
		return validate(not(begintijd.isNull())).message(
				Message.ATTRIBUTE_EMPTY);
	}

	public Validator<Message, Context> getEindtijdValidator() {
		return validate(not(eindtijd.isNull()))
				.message(Message.ATTRIBUTE_EMPTY);
	}

	public Validator<Message, Context> getIdentificatieValidator() {
		return validate(ifExp(
				identificatie.isNull(),
				constant(false),
				validate(not(isBlank(identificatie))).message(
						Message.ATTRIBUTE_EMPTY)));
	}

	public Validator<Message, Context> getRelatieNummerValidator() {
		return validate(not(relatienummer.isNull())).message(
				Message.ATTRIBUTE_EMPTY);
	}

	public Validator<Message, Context> getContractNummerValidator() {
		return validate(not(contractnummer.isNull())).message(
				Message.ATTRIBUTE_EMPTY);
	}

	/*
	 * codeLijst validaties
	 */
	/*
	 * public Validator<Message, Context> getDoelRealisatieValidator() { return
	 * validate(ifExp(doelRealisatie.isNull(), constant(false), and(
	 * validate(doelRealisatie
	 * .hasCodeSpace(doelRealisatieCodeSpace)).message(Message
	 * .ATTRIBUTE_CODE_CODESPACE_INVALID, doelRealisatie.codeSpace(),
	 * constant(doelRealisatie.name), doelRealisatieCodeSpace),
	 * validate(not(isBlank
	 * (doelRealisatie.code()))).message(Message.ATTRIBUTE_EMPTY,
	 * constant(doelRealisatie.name)),
	 * validate(doelRealisatie.isValid()).message
	 * (Message.ATTRIBUTE_CODE_INVALID, doelRealisatie.code(),
	 * constant(doelRealisatie.name),
	 * doelRealisatieCodeSpace)).shortCircuit())); }
	 */
	public Validator<Message, Context> getImnaBronhouderValidator() {
		return validate(ifExp(
				imnaBronhouder.isNull(),
				constant(false),
				and(
						validate(
								imnaBronhouder
										.hasCodeSpace(imnaBronhouderCodeSpace))
								.message(
										Message.ATTRIBUTE_CODE_CODESPACE_INVALID,
										imnaBronhouder.codeSpace(),
										constant(imnaBronhouder.name),
										imnaBronhouderCodeSpace),
						validate(not(isBlank(imnaBronhouder.code()))).message(
								Message.ATTRIBUTE_EMPTY,
								constant(imnaBronhouder.name)),
						validate(imnaBronhouder.isValid()).message(
								Message.ATTRIBUTE_CODE_INVALID,
								imnaBronhouder.code(),
								constant(imnaBronhouder.name),
								imnaBronhouderCodeSpace)).shortCircuit()));
	}

	/*
	 * public Validator<Message, Context> getTypeBeheerderValidator() { return
	 * validate(ifExp(typeBeheerder.isNull(), constant(false), and(
	 * validate(typeBeheerder
	 * .hasCodeSpace(typeBeheerderCodeSpace)).message(Message
	 * .ATTRIBUTE_CODE_CODESPACE_INVALID, typeBeheerder.codeSpace(),
	 * constant(typeBeheerder.name), typeBeheerderCodeSpace),
	 * validate(not(isBlank
	 * (typeBeheerder.code()))).message(Message.ATTRIBUTE_EMPTY,
	 * constant(typeBeheerder.name)),
	 * validate(typeBeheerder.isValid()).message(Message.ATTRIBUTE_CODE_INVALID,
	 * typeBeheerder.code(), constant(typeBeheerder.name),
	 * typeBeheerderCodeSpace)).shortCircuit())); }
	 */

	/*
	 * Valideer aanwezigheid metadata gedaan door:
	 * VerifyDataSchema.processDataset or .run
	 */

	/**
	 * Geometrische validatie: alleen (single) polygon, alleen valide geometrie.
	 * FO bijlage 4, regel 3
	 */
	public Validator<Message, Context> getGeometryValidator() {
		return getNotNullSurfaceGeometryValidator(geometrie);
	}

	/**
	 * @param surfaceGeometry
	 * @return
	 */
	private Validator<Message, Context> getNotNullSurfaceGeometryValidator(
			GeometryExpression<Message, Context, Geometry> surfaceGeometry) {
		return validate(

		and(
		// The following validations short-circuit, there must be a non-empty,
		// Surface geometry:
		validate(not(geometrie.isNull())).message(
				Message.ATTRIBUTE_NULL,
				constant(surfaceGeometry.name),
				// Single polygon
				validate(not(geometrie.isPolygon())).message(
						Message.GEOMETRY_ONLY_SURFACE)),

		// Short circuit to prevent the interiorDisconnected validation if
		// any of the other validations fail:
				and(
						// Hole Outside Shell
						validate(
								not(surfaceGeometry
										.hasInteriorRingOutsideExterior()))
								.message(
										Message.GEOMETRY_INTERIOR_RING_OUTSIDE_EXTERIOR,
										lastLocation()),
						// Nested Holes
						validate(not(surfaceGeometry.hasInteriorRingsWithin()))
								.message(Message.GEOMETRY_INTERIOR_RINGS_WITHIN),
						// Disconnected Interior
						validate(not(surfaceGeometry.isInteriorDisconnected()))
								.message(Message.GEOMETRY_INTERIOR_DISCONNECTED),
						// self-intersection
						validate(
								not(surfaceGeometry.hasCurveSelfIntersection()))
								.message(Message.GEOMETRY_SELF_INTERSECTION),
						// Ring Self Intersection
						validate(not(surfaceGeometry.hasRingSelfIntersection()))
								.message(
										Message.GEOMETRY_RING_SELF_INTERSECTION),
						// Ring Not Closed
						validate(not(surfaceGeometry.hasUnclosedRing()))
								.message(Message.GEOMETRY_RING_NOT_CLOSED),
						validate(not(surfaceGeometry.hasCurveDiscontinuity()))
								.message(Message.GEOMETRY_DISCONTINUITY))
						.shortCircuit(),
				// Invalid coordinate
				/*
				 * TODO: create validation!
				 */

				// SRS validations:
				and(
						validate(surfaceGeometry.hasSrs()).message(
								Message.GEOMETRY_SRS_NULL),
						validate(surfaceGeometry.isSrs(constant("28992")))
								.message(Message.GEOMETRY_SRS_NOT_RD,
										surfaceGeometry.srsName()))
						.shortCircuit()));

	}

	/**
	 * Multiparts validation (1 deel met uniek IMNa Id per polygon)
	 * store h2 each feature and set context entry
	 */
	public Validator<Message, Context> getGeometryIntersectionValidator() {
		
		final UnaryCallback<Message, Context, Boolean, Geometry> saveFeatureCallback = new UnaryCallback<Message, Context, Boolean, Geometry>() {
			
			/**
			 * Wordt per feature uitgevoerd.
			 */
			@Override
			public Boolean call(final Geometry geometry, final Context context)
					throws Exception {
				
                // TODO: add feaure information, for now null.
				geometryStore.addToStore(context.getUuid(), geometry, null);
				
				return true;
			}
		};
		
		/**
		 * Hier wordt nog niets uitgevoerd, maar de expressie wordt alleen opgebouwd.
		 */
		return validate (
			//TODO: get complete Feature to store, not only Geometry
			callback (Boolean.class, geometrie, saveFeatureCallback)				
			);		
	}

	/**
	 * Overlap percelen validatie 
	 */
	@Override
	public void afterJob (final EtlJob job, final EventLogger<Message> logger, final Context context) {
		bulkValidator.overlapValidation(context.getUuid());		 
	}

	public IBulkValidator getBulkValidator() {
		return bulkValidator;
	}

	public void setBulkValidator(IBulkValidator bulkValidator) {
		this.bulkValidator = bulkValidator;
	}

	public String getJdbcConnectionString() {
		return jdbcConnectionString;
	}

	public void setJdbcConnectionString(String jdbcConnectionString) {
		this.jdbcConnectionString = jdbcConnectionString;
	}

}
