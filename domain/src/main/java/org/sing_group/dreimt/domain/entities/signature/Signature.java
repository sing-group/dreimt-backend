package org.sing_group.dreimt.domain.entities.signature;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "signature")
public class Signature implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String signatureName;

  private String cellTypeA;
  private String cellTypeB;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_pubmedId", referencedColumnName = "pubmedId", nullable = true)
  private ArticleMetadata articleMetadata;

  private String sourceDb;

  @Enumerated(EnumType.STRING)
  private ExperimentalDesign experimentalDesign;

  private String organism;
  private String disease;

  @OneToMany(mappedBy = "signature", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SignatureGene> signatureGenes;

  Signature() {}

  public String getSignatureName() {
    return signatureName;
  }

  public String getCellTypeA() {
    return cellTypeA;
  }

  public String getCellTypeB() {
    return cellTypeB;
  }

  public ArticleMetadata getArticleMetadata() {
    return articleMetadata;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public String getDisease() {
    return disease;
  }

  public List<SignatureGene> getSignatureGenes() {
    return signatureGenes;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((signatureName == null) ? 0 : signatureName.hashCode());
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
    Signature other = (Signature) obj;
    if (signatureName == null) {
      if (other.signatureName != null)
        return false;
    } else if (!signatureName.equals(other.signatureName))
      return false;
    return true;
  }
}
