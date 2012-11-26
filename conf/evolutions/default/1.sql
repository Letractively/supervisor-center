# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table groupes (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  description               varchar(255) not null,
  constraint pk_groupes primary key (id))
;

create table supervisors (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  host                      varchar(255) not null,
  authentification          boolean,
  login                     varchar(255),
  password                  varchar(255),
  place                     varchar(255),
  comment                   varchar(255),
  constraint pk_supervisors primary key (id))
;

create table users (
  id_user                   bigint auto_increment not null,
  name                      varchar(255) not null,
  login                     varchar(255),
  password                  varchar(255),
  role                      varchar(255) not null,
  constraint pk_users primary key (id_user))
;


create table groupes_supervisors (
  groupes_id                     bigint not null,
  supervisors_id                 bigint not null,
  constraint pk_groupes_supervisors primary key (groupes_id, supervisors_id))
;



alter table groupes_supervisors add constraint fk_groupes_supervisors_groupe_01 foreign key (groupes_id) references groupes (id) on delete restrict on update restrict;

alter table groupes_supervisors add constraint fk_groupes_supervisors_superv_02 foreign key (supervisors_id) references supervisors (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists groupes;

drop table if exists groupes_supervisors;

drop table if exists supervisors;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

