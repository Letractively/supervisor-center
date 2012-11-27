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
  port                      integer not null,
  authentification          boolean,
  login                     varchar(255),
  password                  varchar(255),
  place                     varchar(255),
  comment                   varchar(255),
  group_id                  bigint,
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

alter table supervisors add constraint fk_supervisors_group_1 foreign key (group_id) references groups (id) on delete restrict on update restrict;
create index ix_supervisors_group_1 on supervisors (group_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists groups;

drop table if exists supervisors;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

