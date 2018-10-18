package org.sing_group.dreimt.rest.entity.query;

public class ListingOptionsData {
  private Integer page;
  private Integer pageSize;

  ListingOptionsData() {}

  public ListingOptionsData(
    Integer page, Integer pageSize
  ) {
    if (page == null ^ pageSize == null) {
      throw new IllegalArgumentException("page and pageSize must be used together");
    }

    this.page = page;
    this.pageSize = pageSize;
  }

  public Integer getPage() {
    return page;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public boolean hasPagination() {
    return this.page != null && this.pageSize != null;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((page == null) ? 0 : page.hashCode());
    result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ListingOptionsData other = (ListingOptionsData) obj;
    if (page == null) {
      if (other.page != null)
        return false;
    } else if (!page.equals(other.page))
      return false;
    if (pageSize == null) {
      if (other.pageSize != null)
        return false;
    } else if (!pageSize.equals(other.pageSize))
      return false;
    return true;
  }
}
