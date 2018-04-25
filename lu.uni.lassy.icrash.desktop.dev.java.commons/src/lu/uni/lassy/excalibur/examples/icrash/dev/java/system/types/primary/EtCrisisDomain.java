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
 * The Enum EtCrisisDomain which holds the different domains that could be set
 * as a crisis domain.
 */
public enum EtCrisisDomain implements JIntIs {

	/**
	 * A crisis is set as regular when it a new crisis created or when no other
	 * domain value makes sense.
	 */
	regular,
	/** A crisis is set as fire if fire is involved in the crisis. */
	fire,
	/**
	 * A crisis is set as chemicalSubstance if some kind of chemical substance is
	 * involved in the crisis.
	 */
	chemicalSubstance,
	/** A crisis is set as naturalCase if a natural event has caused the crisis. */
	naturalCase,
	/**
	 * A crisis is set as unknownSubstance if one or several objects, fluids or
	 * other substance is involved in the crisis.
	 */
	unknownSubstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.DtIs#is()
	 */
	public PtBoolean is() {
		return new PtBoolean(this.name() == EtCrisisDomain.regular.name() || this.name() == EtCrisisDomain.fire.name()
				|| this.name() == EtCrisisDomain.chemicalSubstance.name()
				|| this.name() == EtCrisisDomain.naturalCase.name()
				|| this.name() == EtCrisisDomain.unknownSubstance.name());
	}
}
