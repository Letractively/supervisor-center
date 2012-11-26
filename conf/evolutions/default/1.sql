# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table groups (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  description               varchar(255) not null,
  constraint pk_groups primary key (id))
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
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  login                     varchar(255),
  password                  varchar(255),
  role                      varchar(255) not null,
  constraint pk_users primary key (id))
;


create table groups_supervisors (
  groups_id                      bigint not null,
  supervisors_id                 bigint not null,
  constraint pk_groups_supervisors primary key (groups_id, supervisors_id))
;



alter table groups_supervisors add constraint fk_groups_supervisors_groups_01 foreign key (groups_id) references groups (id) on delete restrict on update restrict;

alter table groups_supervisors add constraint fk_groups_supervisors_supervi_02 foreign key (supervisors_id) references supervisors (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists groups;

drop table if exists groups_supervisors;

drop table if exists supervisors;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

