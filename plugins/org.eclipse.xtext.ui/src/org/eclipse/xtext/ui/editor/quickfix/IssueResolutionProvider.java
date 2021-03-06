/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.quickfix;

import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.validation.Issue;

/**
 * @author Heiko Behrens - Initial contribution and API
 * @author Jan Koehnlein
 */
public interface IssueResolutionProvider {

	boolean hasResolutionFor(String issueCode);

	public List<IssueResolution> getResolutions(Issue issue);

	public static class NullImpl implements IssueResolutionProvider {

		@Override
		public List<IssueResolution> getResolutions(Issue issue) {
			return Collections.emptyList();
		}

		@Override
		public boolean hasResolutionFor(String issueCode) {
			return false;
		}

	}
}
