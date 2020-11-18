create schema if not exists metrics_schema;

grant all privileges on all tables in schema metrics_schema to metricsapp;
grant all privileges on all tables in schema metrics_schema to flyway;

create table if not exists host
(
    id                         uuid                                not null constraint host_pkey primary key,
    hostname                   varchar(100)                        not null,
    ip_address                 inet,
    system_load_average        bigint                              not null,
    total_physical_memory_size bigint                              not null,
    free_physical_memory_size  bigint                              not null,
    total_swap_space_size      bigint                              not null,
    free_swap_space_size       bigint                              not null,
    time_stamp                 timestamp default CURRENT_TIMESTAMP not null
) without oids;

grant all on host to metricsapp;
