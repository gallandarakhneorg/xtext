package org.eclipse.xtext.web.server.hover;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend.lib.annotations.ToString;
import org.eclipse.xtext.web.server.IServiceResult;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@ToString(skipNulls = true)
@SuppressWarnings("all")
public class HoverResult implements IServiceResult {
  private final String title;
  
  private final String content;
  
  public HoverResult(final String title, final String content) {
    super();
    this.title = title;
    this.content = content;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.title== null) ? 0 : this.title.hashCode());
    result = prime * result + ((this.content== null) ? 0 : this.content.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HoverResult other = (HoverResult) obj;
    if (this.title == null) {
      if (other.title != null)
        return false;
    } else if (!this.title.equals(other.title))
      return false;
    if (this.content == null) {
      if (other.content != null)
        return false;
    } else if (!this.content.equals(other.content))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.skipNulls();
    b.add("title", this.title);
    b.add("content", this.content);
    return b.toString();
  }
  
  @Pure
  public String getTitle() {
    return this.title;
  }
  
  @Pure
  public String getContent() {
    return this.content;
  }
}