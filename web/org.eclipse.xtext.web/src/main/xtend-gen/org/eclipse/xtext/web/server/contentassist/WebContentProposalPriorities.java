/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.web.server.contentassist;

import com.google.common.base.Objects;
import com.google.inject.Singleton;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.web.server.contentassist.ContentAssistResult;
import org.eclipse.xtext.xbase.lib.Pure;

@Singleton
@Accessors({ AccessorType.PROTECTED_GETTER, AccessorType.PROTECTED_SETTER })
@SuppressWarnings("all")
public class WebContentProposalPriorities {
  private int crossRefPriority = 500;
  
  private int defaultPriority = 400;
  
  private int keywordPriority = 300;
  
  protected int adjustPriority(final ContentAssistResult.Entry entry, final int priority) {
    int adjustedPriority = priority;
    String _proposal = entry.getProposal();
    char _charAt = _proposal.charAt(0);
    boolean _isLetter = Character.isLetter(_charAt);
    boolean _not = (!_isLetter);
    if (_not) {
      int _adjustedPriority = adjustedPriority;
      adjustedPriority = (_adjustedPriority - 30);
    }
    String _proposal_1 = entry.getProposal();
    String _prefix = entry.getPrefix();
    boolean _equals = Objects.equal(_proposal_1, _prefix);
    if (_equals) {
      int _adjustedPriority_1 = adjustedPriority;
      adjustedPriority = (_adjustedPriority_1 - 20);
    }
    return adjustedPriority;
  }
  
  public int getCrossRefPriority(final IEObjectDescription objectDesc, final ContentAssistResult.Entry entry) {
    return this.adjustPriority(entry, this.crossRefPriority);
  }
  
  public int getDefaultPriority(final ContentAssistResult.Entry entry) {
    return this.adjustPriority(entry, this.defaultPriority);
  }
  
  public int getKeywordPriority(final String keyword, final ContentAssistResult.Entry entry) {
    return this.adjustPriority(entry, this.keywordPriority);
  }
  
  @Pure
  protected int getCrossRefPriority() {
    return this.crossRefPriority;
  }
  
  protected void setCrossRefPriority(final int crossRefPriority) {
    this.crossRefPriority = crossRefPriority;
  }
  
  @Pure
  protected int getDefaultPriority() {
    return this.defaultPriority;
  }
  
  protected void setDefaultPriority(final int defaultPriority) {
    this.defaultPriority = defaultPriority;
  }
  
  @Pure
  protected int getKeywordPriority() {
    return this.keywordPriority;
  }
  
  protected void setKeywordPriority(final int keywordPriority) {
    this.keywordPriority = keywordPriority;
  }
}