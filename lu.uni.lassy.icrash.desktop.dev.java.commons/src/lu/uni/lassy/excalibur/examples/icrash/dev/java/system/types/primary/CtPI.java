package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import java.io.Serializable;
import java.rmi.RemoteException;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.environment.actors.ActCoordinator;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.secondary.DtSMS;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtDateAndTime;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

/**
 * The Class CtPI, that hold details about the Point of Interest (PI) instance.
 */
public class CtPI implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 227L;

	/** The id of the PI. */
	public DtPIID id;
	
	/** The location of the PI. */
	public DtGPSLocation location;
	
	/** The date and time when PI was created. */
	public DtDateAndTime instant;
	
	/** The title of PI. */
	public DtPITitle title;
	
	/** The category of PI. */
	public EtPICategory category;

	/**
	 * Initialises the PI.
	 *
	 * @param aId the ID of the PI
	 * @param aLocation the location of the PI
	 * @param aInstant the date and time when PI was created
	 * @param aTitle the title of PI
	 * @param aCategory the category of PI
	 * @return the success of the initialisation of the PI
	 */
	public PtBoolean init(DtPIID aId, DtGPSLocation aLocation, DtDateAndTime aInstant,
			DtPITitle aTitle, EtPICategory aCategory) {
			
		id = aId;
		location = aLocation;
		instant = aInstant;
		title = aTitle;
		category = aCategory;
		return new PtBoolean(true);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return this.id.value.getValue();
	}
	
	public DtSMS toSMS() {
		String latit = String.valueOf(this.location.latitude.value.getValue());
		String longit = String.valueOf(this.location.latitude.value.getValue());
		
		PtString message = new PtString("PI: " + this.title.toString() +
										". Category: " + this.category.toString() +
										". Location: " + latit +
										", " + longit);
		DtSMS sms = new DtSMS(message);
		return sms;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof CtPI))
			return false;
		CtPI aCtPI = (CtPI)obj;
		if (!(aCtPI.id.value.getValue().equals(this.id.value.getValue())))
			return false;
		if (!(aCtPI.instant.toString().equals(this.instant.toString())))
			return false;
		if (aCtPI.location.latitude.value.getValue() != this.location.latitude.value.getValue()) 
			return false;
		if (aCtPI.location.longitude.value.getValue() != this.location.longitude.value.getValue()) 
			return false;
		if (!(aCtPI.title.value.getValue().equals(this.title.value.getValue())))
			return false;
		if (!(aCtPI.category.toString().equals(this.category.toString())))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return this.id.value.getValue().length();
	}

}
