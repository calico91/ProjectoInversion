-- liquibase formatted sql

-- changeset blandon:1

ALTER TABLE `permisos`
    ADD COLUMN `endpoint` VARCHAR(50) NULL AFTER `descripcion`;



