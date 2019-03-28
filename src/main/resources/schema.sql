drop table if exists prestaciones;

create table prestaciones (
  id integer primary key,
  patient varchar(100),
  exam varchar(100),
  dateIn date,
  maxDaysToBeforeInform int,
  informDate date,
  examImageId varchar(100),
  type integer,
  medicalRecord varchar(100)
);