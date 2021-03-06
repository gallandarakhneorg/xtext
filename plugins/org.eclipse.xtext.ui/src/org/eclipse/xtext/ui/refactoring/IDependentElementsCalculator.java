/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.refactoring;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.refactoring.impl.DefaultDependentElementsCalculator;

import com.google.inject.ImplementedBy;

/**
 * Calculates elements whose name changes with the rename of a <code>baseElement</code>. Cross-references to these
 * elements will have to be updated, too.
 * 
 * @author Jan Koehnlein - Initial contribution and API
 */
@ImplementedBy(DefaultDependentElementsCalculator.class)
public interface IDependentElementsCalculator {

	Iterable<URI> getDependentElementURIs(EObject baseElement, IProgressMonitor monitor);

}
