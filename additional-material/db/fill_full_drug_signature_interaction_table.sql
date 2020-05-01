INSERT INTO full_drug_signature_interaction (drugCommonName, drugSourceDb, drugSourceName, drugStatus, drugMoa, drugDss, drugTargetGenes, upFdr, downFdr, tau, interactionType, signatureType, signatureName, signatureExperimentalDesign, signatureOrganism, signatureSourceDb, signatureSourceDbUrl, signatureArticlePubmedId, signatureDisease, signatureCellTypeA, signatureCellSubTypeA, signatureCellTypeB, signatureCellSubTypeB, signatureDiseaseA, signatureDiseaseb, signatureTreatmentA, signatureTreatmentB, signatureStateA, signatureStateB, signatureLocalisationA, signatureLocalisationB)
SELECT
  drug.commonName as drugCommonName, drug.sourceDb as drugSourceDb, drug.sourceName as drugSourceName, drug.status as drugStatus, drug.moa as drugMoa, drug.dss as drugDss, drug.targetGenes as drugTargetGenes, interactions.upFdr, interactions.downFdr, interactions.tau, interactions.interactionType, interactions.signatureType, interactions.signatureName, interactions.experimentalDesign as signatureExperimentalDesign, interactions.organism as signatureOrganism, interactions.sourceDb as signatureSourceDb, interactions.sourceDbUrl as signatureSourceDbUrl, interactions.article_pubmedId as signatureArticlePubmedId, interactions.disease as signatureDisease, interactions.cellTypeA as signatureCellTypeA, interactions.cellSubTypeA as signatureCellSubTypeA, interactions.cellTypeB as signatureCellTypeB, interactions.cellSubTypeB as signatureCellSubTypeB, interactions.diseaseA as signatureDiseaseA, interactions.diseaseB as signatureDiseaseB, interactions.treatmentA as signatureTreatmentA, interactions.treatmentB as signatureTreatmentB, interactions.stateA as signatureStateA, interactions.stateB as signatureStateB, interactions.localisationA as signatureLocalisationA, interactions.localisationB as signatureLocalisationB
FROM
    (
        SELECT drug_moa_join.*, GROUP_CONCAT(gene ORDER BY gene ASC SEPARATOR '##') AS targetGenes 
        FROM drug_target_genes RIGHT JOIN (
            SELECT drug.*, GROUP_CONCAT(moa ORDER BY moa ASC SEPARATOR '##') AS moa 
            FROM drug LEFT JOIN drug_moa ON drug.id = drug_moa.id GROUP BY drug.id
        ) AS drug_moa_join 
        ON drug_moa_join.id = drug_target_genes.id GROUP BY id
    ) AS drug RIGHT JOIN (
        SELECT drug_signature_interaction.drugId, drug_signature_interaction.upFdr, drug_signature_interaction.downFdr, drug_signature_interaction.tau, drug_signature_interaction.interactionType, signature_join.*
        FROM drug_signature_interaction LEFT JOIN (
            SELECT signature_cell_subtype_b_join .*, GROUP_CONCAT(cellSubType ORDER BY cellSubType ASC SEPARATOR '##') AS cellSubTypeB 
            FROM signature_cell_subtype_b RIGHT JOIN (
                SELECT signature_cell_type_b_join .*, GROUP_CONCAT(cellType ORDER BY cellType ASC SEPARATOR '##') AS cellTypeB FROM signature_cell_type_b RIGHT JOIN (
                    SELECT signature_cell_subtype_a_join .*, GROUP_CONCAT(cellSubType ORDER BY cellSubType ASC SEPARATOR '##') AS cellSubTypeA
                    FROM signature_cell_subtype_a RIGHT JOIN (
                        SELECT signature_treatment_b_join .*, GROUP_CONCAT(treatmentB ORDER BY treatmentB ASC SEPARATOR '##') AS treatmentB
                        FROM signature_treatment_b RIGHT JOIN (
                            SELECT signature_treatment_a_join .*, GROUP_CONCAT(treatmentA ORDER BY treatmentA ASC SEPARATOR '##') AS treatmentA
                            FROM signature_treatment_a RIGHT JOIN (
                                SELECT signature_disease_b_join .*, GROUP_CONCAT(diseaseB ORDER BY diseaseB ASC SEPARATOR '##') AS diseaseB
                                FROM signature_disease_b RIGHT JOIN (
                                    SELECT signature_disease_a_join .*, GROUP_CONCAT(diseaseA ORDER BY diseaseA ASC SEPARATOR '##') AS diseaseA
                                    FROM signature_disease_a RIGHT JOIN (
                                        SELECT signature_disease_join.*, GROUP_CONCAT(cellType ORDER BY cellType ASC  SEPARATOR '##') AS cellTypeA
                                        FROM signature_cell_type_a RIGHT JOIN (
                                            SELECT signature.*, GROUP_CONCAT(disease ORDER BY disease ASC SEPARATOR '##') AS disease
                                            FROM signature LEFT JOIN 
                                                signature_disease ON signature.signatureName = signature_disease.signatureName GROUP BY signatureName
                                        ) AS signature_disease_join
                                        ON signature_cell_type_a.signatureName = signature_disease_join.signatureName GROUP BY signatureName
                                    ) AS signature_disease_a_join
                                    ON signature_disease_a.signatureName = signature_disease_a_join.signatureName GROUP BY signatureName
                                ) AS signature_disease_b_join
                                ON signature_disease_b.signatureName = signature_disease_b_join.signatureName GROUP BY signatureName
                            ) AS signature_treatment_a_join
                            ON signature_treatment_a.signatureName = signature_treatment_a_join.signatureName GROUP BY signatureName
                        ) AS signature_treatment_b_join
                        ON signature_treatment_b.signatureName = signature_treatment_b_join.signatureName GROUP BY signatureName
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
