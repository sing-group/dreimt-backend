INSERT INTO full_drug_signature_interaction (drugCommonName, drugSourceDb, drugSourceName, drugStatus, drugMoa, upFdr, downFdr, tau, interactionType, signatureType, signatureName, signatureExperimentalDesign, signatureOrganism, signatureSourceDb, signatureArticlePubmedId, signatureDisease, signatureCellTypeA, signatureCellSubTypeA, signatureCellTypeB, signatureCellSubTypeB)
SELECT
  drug.commonName as drugCommonName, drug.sourceDb as drugSourceDb, drug.sourceName as drugSourceName, drug.status as drugStatus, drug.moa as drugMoa,
  interactions.upFdr, interactions.downFdr, interactions.tau, interactions.interactionType, interactions.signatureType,
  interactions.signatureName, interactions.experimentalDesign as signatureExperimentalDesign, interactions.organism as signatureOrganism, interactions.sourceDb as signatureSourceDb, interactions.article_pubmedId as signatureArticlePubmedId, interactions.disease as signatureDisease, interactions.cellTypeA as signatureCellTypeA, interactions.cellSubTypeA as signatureCellSubTypeA, interactions.cellTypeB as signatureCellTypeB, interactions.cellSubTypeB as signatureCellSubTypeB
FROM drug RIGHT JOIN (
  SELECT drug_signature_interaction.drugId, drug_signature_interaction.upFdr, drug_signature_interaction.downFdr, drug_signature_interaction.tau, drug_signature_interaction.interactionType, signature_join.*
  FROM drug_signature_interaction LEFT JOIN (
    SELECT signature_cell_subtype_b_join .*, GROUP_CONCAT(cellSubType ORDER BY cellSubType ASC SEPARATOR ' ### ') AS cellSubTypeB FROM signature_cell_subtype_b RIGHT JOIN (
      SELECT signature_cell_type_b_join .*, GROUP_CONCAT(cellType ORDER BY cellType ASC SEPARATOR ' ### ') AS cellTypeB FROM signature_cell_type_b RIGHT JOIN (
        SELECT signature_cell_subtype_a_join .*, GROUP_CONCAT(cellSubType ORDER BY cellSubType ASC SEPARATOR ' ### ') AS cellSubTypeA FROM signature_cell_subtype_a RIGHT JOIN (
            SELECT signature_disease_join.*, GROUP_CONCAT(cellType ORDER BY cellType ASC  SEPARATOR ' ### ') AS cellTypeA FROM signature_cell_type_a RIGHT JOIN (
              SELECT signature.*, GROUP_CONCAT(disease ORDER BY disease ASC SEPARATOR ' ### ') AS disease FROM signature LEFT JOIN signature_disease
              ON signature.signatureName = signature_disease.signatureName GROUP BY signatureName
            ) AS signature_disease_join
            ON signature_cell_type_a.signatureName = signature_disease_join.signatureName GROUP BY signatureName
          ) AS signature_cell_subtype_a_join
        ON signature_cell_subtype_a.signatureName = signature_cell_subtype_a_join.signatureName GROUP BY signatureName
      ) AS signature_cell_type_b_join
      ON signature_cell_type_b.signatureName = signature_cell_type_b_join.signatureName GROUP BY signatureName
    ) AS signature_cell_subtype_b_join
    ON signature_cell_subtype_b.signatureName = signature_cell_subtype_b_join.signatureName GROUP BY signatureName
  ) AS signature_join
  ON signature_join.signatureName  = drug_signature_interaction.signature
) AS interactions
ON interactions.drugId = drug.id;
