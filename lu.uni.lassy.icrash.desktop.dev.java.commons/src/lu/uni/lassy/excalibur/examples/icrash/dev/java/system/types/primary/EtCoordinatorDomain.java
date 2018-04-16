/*******************************************************************************
 * Copyright (c) 2014-2015 University of Luxembourg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alfredo Capozucca - initial API and implementation
 *     Christophe Kamphaus - Remote implementation of Actors
 *     Thomas Mortimer - Updated client to MVC and added new design patterns
 ******************************************************************************/
package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;

/**
 * The Enum EtCrisisDomain which holds the different domains that could be set as a crisis domain.
 */
public enum EtCoordinatorDomain implements JIntIs {
	
	/** A domain is set as none when the coordinator has no domain. */
	none, 
	/** A domain is set as fire if a coordinator's speciality involved fire. */
	fire, 
	/** A domain is set as chemicalSubstance if a coordinator's speciality involved some kind of chemical substances. */
	chemicalSubstance,
	/** A domain is set as naturalCase if a coordinator's speciality involved some kind of natural event. */
	naturalCase,
	/** A domain is set as unknownSubstance if a coordinator's speciality involved identifing some kind of unknown substance. */
	unknownSubstance;
	
	/* (non-Javadoc)
	 * @see lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.DtIs#is()
	 */
	public PtBoolean is(){
		return new PtBoolean(this.name() == EtCoordinatorDomain.none.name() ||
				this.name() == EtCoordinatorDomain.fire.name() || this.name() == EtCoordinatorDomain.chemicalSubstance.name() ||
				this.name() == EtCoordinatorDomain.naturalCase.name() || this.name() == EtCoordinatorDomain.unknownSubstance.name());
	}
}
