package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

/**
 * The Class of datatype Point of Interest Title.
 */

public class DtPITitle  extends DtString implements JIntIs{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 227L;

	/**
	 * Instantiates a new datatype Point of interest Title.
	 *
	 * @param s The string to use to create the title of Point of interest
	 */
	public DtPITitle(PtString s) {
		super(s);
	}
	
	/** The minimum length of a title of Point of interest that is not acceptable. */
	private int _minLength = 1;
	
	/** The maximum length of a title of Point of interest that is acceptable. */
	private int _maxLength = 20;

	public PtBoolean is(){
		return new PtBoolean(this.value.getValue().length() > _minLength && this.value.getValue().length() <= _maxLength);
	}

}
