package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

/**
 * The Class of datatype Point of Interest ID.
 */

public class DtPIID extends DtString implements JIntIs{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 227L;

	/**
	 * Instantiates a new datatype Point of interest ID.
	 *
	 * @param s The string to use to create the Point of interest ID
	 */
	public DtPIID(PtString s) {
		super(s);
	}
	
	/** The minimum length of a Point of interest ID that is not acceptable. */
	private int _minLength = 0;
	
	/** The maximum length of a Point of interest ID that is acceptable. */
	private int _maxLength = 20;

	public PtBoolean is(){
		return new PtBoolean(this.value.getValue().length() > _minLength && this.value.getValue().length() <= _maxLength);
	}
}
