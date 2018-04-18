package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;

/**
 * The Enum EtPICategory, which represents category of point of interest.
 */
public enum EtPICategory  implements JIntIs{

	gas,
    food,
    music,
    movie,
    other;
	
	public PtBoolean is(){
		return new PtBoolean(
				this.name() == EtPICategory.gas.name() ||
				this.name() == EtPICategory.food.name() ||
				this.name() == EtPICategory.music.name() ||
				this.name() == EtPICategory.movie.name() ||
				this.name() == EtPICategory.other.name());
	}
}