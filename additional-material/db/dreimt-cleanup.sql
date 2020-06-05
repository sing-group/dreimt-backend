SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `jaccard_result_updown_genes_down`;
TRUNCATE TABLE `jaccard_result_updown_genes_up`;
TRUNCATE TABLE `jaccard_result_geneset_genes`;
TRUNCATE TABLE `jaccard_result_geneset`;
TRUNCATE TABLE `jaccard_result_updown`;
TRUNCATE TABLE `jaccard_result_gene_overlap`;
TRUNCATE TABLE `jaccard_result`;

TRUNCATE TABLE `cmap_result_updown_genes_down`;
TRUNCATE TABLE `cmap_result_updown_genes_up`;
TRUNCATE TABLE `cmap_result_geneset_genes`;
TRUNCATE TABLE `cmap_result_geneset_drug_interactions`;
TRUNCATE TABLE `cmap_result_updown_drug_interactions`;
TRUNCATE TABLE `cmap_result_geneset`;
TRUNCATE TABLE `cmap_result_updown`;
TRUNCATE TABLE `cmap_result`;

TRUNCATE TABLE `precalculated_example_jaccard_updown`;
TRUNCATE TABLE `precalculated_example_jaccard_geneset`;
TRUNCATE TABLE `precalculated_example_jaccard`;
TRUNCATE TABLE `precalculated_example_cmap_updown`;
TRUNCATE TABLE `precalculated_example_cmap_geneset`;
TRUNCATE TABLE `precalculated_example_cmap`;
TRUNCATE TABLE `precalculated_example`;

TRUNCATE TABLE `work_step`;
TRUNCATE TABLE `work`;

TRUNCATE TABLE `drug_signature_interaction`;
TRUNCATE TABLE `full_drug_signature_interaction`;

TRUNCATE TABLE `signature_updown_genes`;
TRUNCATE TABLE `signature_geneset_genes`;
TRUNCATE TABLE `signature_disease`;
TRUNCATE TABLE `signature_disease_a`;
TRUNCATE TABLE `signature_disease_b`;
TRUNCATE TABLE `signature_treatment_a`;
TRUNCATE TABLE `signature_treatment_b`;
TRUNCATE TABLE `signature`;

TRUNCATE TABLE `drug_moa`;
TRUNCATE TABLE `drug_target_genes`;
TRUNCATE TABLE `drug`;
TRUNCATE TABLE `genes`;
TRUNCATE TABLE `article_metadata`;

TRUNCATE TABLE `database_versions`;
TRUNCATE TABLE `dreimt_information`;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `work` AUTO_INCREMENT = 1;
ALTER TABLE `work_step` AUTO_INCREMENT = 1;

ALTER TABLE `drug_signature_interaction` AUTO_INCREMENT = 1;
ALTER TABLE `full_drug_signature_interaction` AUTO_INCREMENT = 1;

ALTER TABLE `jaccard_result_updown_genes_down` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_updown_genes_up` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_geneset_genes` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_updown` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_gene_overlap` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result` AUTO_INCREMENT = 1;

ALTER TABLE `cmap_result_updown_genes_down` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_updown_genes_up` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_geneset_genes` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_geneset_drug_interactions` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_updown_drug_interactions` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_geneset` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result_updown` AUTO_INCREMENT = 1;
ALTER TABLE `cmap_result` AUTO_INCREMENT = 1;

ALTER TABLE `precalculated_example_jaccard_updown` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example_jaccard_geneset` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example_jaccard` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example_cmap_updown` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example_cmap_geneset` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example_cmap` AUTO_INCREMENT = 1;
ALTER TABLE `precalculated_example` AUTO_INCREMENT = 1;

ALTER TABLE `signature_updown_genes` AUTO_INCREMENT = 1;
ALTER TABLE `signature_geneset_genes` AUTO_INCREMENT = 1;
ALTER TABLE `signature_disease` AUTO_INCREMENT = 1;
ALTER TABLE `signature_disease_a` AUTO_INCREMENT = 1;
ALTER TABLE `signature_disease_b` AUTO_INCREMENT = 1;
ALTER TABLE `signature_treatment_a` AUTO_INCREMENT = 1;
ALTER TABLE `signature_treatment_b` AUTO_INCREMENT = 1;
ALTER TABLE `signature` AUTO_INCREMENT = 1;

ALTER TABLE `drug_moa` AUTO_INCREMENT = 1;
ALTER TABLE `drug_target_genes` AUTO_INCREMENT = 1;
ALTER TABLE `drug` AUTO_INCREMENT = 1;
ALTER TABLE `genes` AUTO_INCREMENT = 1;
ALTER TABLE `article_metadata` AUTO_INCREMENT = 1;

ALTER TABLE `database_versions` AUTO_INCREMENT = 1;
ALTER TABLE `dreimt_information` AUTO_INCREMENT = 1;
