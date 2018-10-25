/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 * 			Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;

public class ArticleMetadataData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int pubmedId;
  private String title;
  private String authors;
  private String articleAbstract;

  ArticleMetadataData() {}

  public ArticleMetadataData(int pubmedId, String title, String authors, String articleAbstract) {
    super();
    this.pubmedId = pubmedId;
    this.title = title;
    this.authors = authors;
    this.articleAbstract = articleAbstract;
  }

  public int getPubmedId() {
    return pubmedId;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthors() {
    return authors;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }
}
