-- liquibase formatted sql

-- changeset liquibase:1

create schema if not exists master_data;

create table if not exists master_data.blogstack_question_master
(
    bsqm_seq_id bigserial not null ,
    bsqm_question_id character varying(75) not null,
    bsqm_question text,
    bsqm_province_id character varying(75),
    bsqm_remark text,
    bsqm_status character varying(25),
    bsqm_created_by character varying(50),
    bsqm_created_date timestamp without time zone,
    bsqm_modified_by character varying(50),
    bsqm_modified_date timestamp without time zone,
    constraint bsqm_seq_id_pk primary key (bsqm_seq_id)
);
