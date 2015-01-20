/**
 * 
 */
package nl.ipo.cds.etl.theme.vrn.validation;

import java.util.Map;

import nl.ipo.cds.etl.theme.vrn.Context;
import nl.ipo.cds.etl.theme.vrn.Message;
import nl.ipo.cds.etl.theme.vrn.domain.LandelijkGebiedInrichting;
import nl.ipo.cds.validation.Validator;
import nl.ipo.cds.validation.execute.CompilerException;

/**
 * @author annes
 *
 */
public class LandelijkGebiedInrichtingValidator extends AbstractGebiedInrichtingValidator<LandelijkGebiedInrichting> {

	

	/**
	 * @param validatorMessages
	 * @throws CompilerException
	 */
	public LandelijkGebiedInrichtingValidator(Map<Object, Object> validatorMessages) throws CompilerException {
		super(validatorMessages, LandelijkGebiedInrichting.class);
		compile();
	}

	/**
	 * Doelinrichting is mandatory for "landelijk".
	 * @return
	 */
	@Override
	public Validator<Message, Context> getDoelInrichtingValidator() {
		return validate(
				and(
						validate(not(doelInrichting.isNull())).message(Message.ATTRIBUTE_NULL,
								constant(doelInrichting.name)),
						validate(not(isBlank(doelInrichting.code()))).message(Message.ATTRIBUTE_EMPTY,
								constant(doelInrichting.name)),
						validate(doelInrichting.hasCodeSpace(doelInrichtingCodeSpace)).message(
								Message.ATTRIBUTE_CODE_CODESPACE_INVALID, doelInrichting.codeSpace(),
								constant(doelInrichting.name), doelInrichtingCodeSpace),
						validate(doelInrichting.isValid()).message(Message.ATTRIBUTE_CODE_INVALID,
								doelInrichting.code(), constant(doelInrichting.name), doelInrichtingCodeSpace))
						.shortCircuit());
	}

	/**
	 * All data uploaded by provinces should be within their geometry bounds.
	 */
	public Validator<Message, Context> getGeometryWithinBronhouderGeometryValidator() {
		return getGeometryWithinBronhouderGeometryHelper();
	}
}
