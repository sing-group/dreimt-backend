SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `jaccard_result_updown_genes_down`;
TRUNCATE TABLE `jaccard_result_updown_genes_up`;
TRUNCATE TABLE `jaccard_result_geneset_genes`;
TRUNCATE TABLE `jaccard_result_geneset`;
TRUNCATE TABLE `jaccard_result_updown`;
TRUNCATE TABLE `jaccard_result_gene_overlap`;
TRUNCATE TABLE `jaccard_result`;

TRUNCATE TABLE `work_step`;
TRUNCATE TABLE `work`;

TRUNCATE TABLE `drug_signature_interaction`;

TRUNCATE TABLE `signature_updown_genes`;
TRUNCATE TABLE `signature_geneset_genes`;
TRUNCATE TABLE `signature_cell_type_a`;
TRUNCATE TABLE `signature_cell_type_b`;
TRUNCATE TABLE `signature`;

TRUNCATE TABLE `genes`;
TRUNCATE TABLE `drug`;
TRUNCATE TABLE `article_metadata`;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `work` AUTO_INCREMENT = 1;
ALTER TABLE `work_step` AUTO_INCREMENT = 1;

ALTER TABLE `article_metadata` AUTO_INCREMENT = 1;

ALTER TABLE `drug_signature_interaction` AUTO_INCREMENT = 1;

ALTER TABLE `jaccard_result_updown_genes_down` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_updown_genes_up` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_geneset_genes` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_updown` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result_gene_overlap` AUTO_INCREMENT = 1;
ALTER TABLE `jaccard_result` AUTO_INCREMENT = 1;

ALTER TABLE `signature_updown_genes` AUTO_INCREMENT = 1;
ALTER TABLE `signature_geneset_genes` AUTO_INCREMENT = 1;
ALTER TABLE `signature_cell_type_a` AUTO_INCREMENT = 1;
ALTER TABLE `signature_cell_type_b` AUTO_INCREMENT = 1;
ALTER TABLE `signature` AUTO_INCREMENT = 1;
ALTER TABLE `genes` AUTO_INCREMENT = 1;
ALTER TABLE `drug` AUTO_INCREMENT = 1;
