-- ============================================
-- 1) CORE TABLES
-- ============================================

create table if not exists brand
(
    id             integer generated always as identity
        constraint brand_pkey primary key,
    name           varchar(255) not null
        constraint brand_name_key unique,
    country        varchar(255),
    founded_year   integer,
    trigger_column varchar(200)
);

create table if not exists model
(
    id        integer generated always as identity
        constraint model_pkey primary key,
    brand_id  integer
        constraint fk_model_brand references brand (id),
    name      varchar(255) not null,
    category  varchar(255) not null,
    year_from integer,
    year_to   integer
);

create table if not exists car
(
    id                  integer generated always as identity
        constraint car_pkey primary key,
    model_id            integer     not null
        constraint fk_car_model references model (id),
    vin                 varchar(20) not null,
    registration_number varchar(10) not null,
    mileage_km          integer     not null,
    production_year     integer
);

-- Also: car_id unique for true 1-1, registration_code unique separately
create table if not exists cardetails
(
    id                integer generated always as identity
        constraint cardetails_pkey primary key,

    car_id            integer      not null
        constraint fk_car_details_car references car (id)
        constraint uc_cardetails_car unique,

    engine_number     varchar(20)  not null,

    registration_code varchar(20)  not null
        constraint uc_registration_code unique,

    fuel_type         varchar(20)  not null,
    engine_capacity   varchar(200) not null,
    color             varchar(255),
    insurance_number  varchar(255)
);

create table if not exists service_visit
(
    id           integer generated always as identity
        constraint service_visit_pkey primary key,
    car_id       integer      not null
        constraint fk_service_visit_car references car (id),
    service_date timestamp    not null,
    odometer_km  integer      not null,
    service_type varchar(255) not null,
    notes        varchar(255)
);

create table if not exists part
(
    id          integer generated always as identity
        constraint part_pkey primary key,
    name        varchar(255),
    part_code   varchar(255),
    description varchar(255),
    unit_price  numeric(10, 2),
    brand       varchar(255)
);

create table if not exists service_part
(
    id         integer generated always as identity
        constraint service_part_pkey primary key,
    service_id integer not null
        constraint fk_service_part_service_visit references service_visit (id),
    part_id    integer not null
        constraint fk_service_part_part references part (id),
    quantity   integer not null
);

create table if not exists feature
(
    id          integer generated always as identity
        constraint feature_pkey primary key,
    feature_id  integer
        constraint feature_feature_id_key unique,
    name        varchar(255),
    description varchar(255),
    category    varchar(255)
);

create table if not exists car_feature
(
    car_id     integer not null
        constraint fk_car_feature_car references car (id),
    feature_id integer not null
        constraint fk_car_feature_feature references feature (id),
    constraint pk_car_feature primary key (car_id, feature_id)
);

-- ============================================
-- 2) TRIGGER FOR BRAND
-- ============================================

create or replace function brand_update_trigger_fn()
    returns trigger
    language plpgsql
as
$$
begin
    new.trigger_column :=
            'UPDATED_AT_' || to_char(current_timestamp, 'YYYY-MM-DD HH24:MI:SS');
    return new;
end;
$$;

drop trigger if exists brand_before_update_trigger on brand;

create trigger brand_before_update_trigger
    before update
    on brand
    for each row
execute procedure brand_update_trigger_fn();


-- Brands
insert into brand (name, country, founded_year, trigger_column)
select 'Brand_' || i,
       case
           when i % 5 = 0 then 'Germany'
           when i % 5 = 1 then 'USA'
           when i % 5 = 2 then 'Japan'
           when i % 5 = 3 then 'France'
           else 'Italy'
           end,
       1920 + (i % 100),
       null
from generate_series(1, 100) as s(i)
on conflict (name) do nothing;

-- Models
insert into model (brand_id, name, category, year_from, year_to)
select (i % 100) + 1,
       'Model_' || i,
       case
           when i % 4 = 0 then 'Sedan'
           when i % 4 = 1 then 'SUV'
           when i % 4 = 2 then 'Truck'
           else 'Hatchback'
           end,
       1990 + (i % 30),
       2020 + (i % 5)
from generate_series(1, 300) as s(i);

-- Cars
insert into car (model_id, vin, registration_number, mileage_km, production_year)
select (i % 300) + 1,
       left('VIN' || md5(i::text), 17) || lpad(i::text, 3, '0'), -- total 20 chars
       'REG' || lpad(i::text, 7, '0'),                           -- total 10 chars
       10000 + (i * 7 % 200000),
       2000 + (i % 25)
from generate_series(1, 500) as s(i);

-- CarDetails (FIXED: select from car, not brand)
insert into cardetails (car_id, engine_number, registration_code, fuel_type, engine_capacity, color, insurance_number)
select c.id,
       left('ENG' || md5(c.id::text), 20),
       lpad('RC' || c.id::text, 20, '0'),
       case
           when c.id % 3 = 0 then 'Petrol'
           when c.id % 3 = 1 then 'Diesel'
           else 'Electric'
           end,
       (1000 + (c.id % 2500)) || 'cc',
       case
           when c.id % 4 = 0 then 'Red'
           when c.id % 4 = 1 then 'Black'
           when c.id % 4 = 2 then 'Blue'
           else 'White'
           end,
       'INS' || c.id || '-' || left(md5(c.id::text), 10)
from car c
on conflict (car_id) do nothing;

-- Service visits (uses car ids)
insert into service_visit (car_id, service_date, odometer_km, service_type, notes)
select (i % 500) + 1,
       now() - (i || ' days')::interval,
       5000 + ((i * 100) % 200000),
       case
           when i % 3 = 0 then 'Maintenance'
           when i % 3 = 1 then 'Repair'
           else 'Inspection'
           end,
       'Note for service ' || i
from generate_series(1, 1000) as s(i);

-- Parts
insert into part (name, part_code, description, unit_price, brand)
select 'Part_' || i,
       'PCODE' || lpad(i::text, 6, '0'),
       'Description of part ' || i,
       round((10 + random() * 490)::numeric, 2),
       case
           when i % 5 = 0 then 'Bosch'
           when i % 5 = 1 then 'Delphi'
           when i % 5 = 2 then 'Valeo'
           when i % 5 = 3 then 'Denso'
           else 'Magneti Marelli'
           end
from generate_series(1, 200) as s(i);

-- Service-Part links
insert into service_part (service_id, part_id, quantity)
select (i % 1000) + 1,
       (i % 200) + 1,
       1 + (i % 5)
from generate_series(1, 1500) as s(i);

-- Features
insert into feature (feature_id, name, description, category)
select i,
       'Feature_' || i,
       'This is feature number ' || i,
       case
           when i % 3 = 0 then 'Safety'
           when i % 3 = 1 then 'Comfort'
           else 'Entertainment'
           end
from generate_series(1, 100) as s(i);

-- Car-Feature random links (FIXED FK now points to car)
insert into car_feature (car_id, feature_id)
select distinct ((random() * 499 + 1)::int),
                ((random() * 99 + 1)::int)
from generate_series(1, 2000);

-- ============================================
-- 3) AUTH TABLES (RBAC)
-- ============================================

create table if not exists app_user
(
    id            integer generated always as identity primary key,
    username      varchar(50) not null unique,
    full_name     varchar(100),
    email         varchar(100) unique,
    password_hash text        not null,
    is_active     boolean   default true,
    created_at    timestamp default current_timestamp
);

create table if not exists role
(
    id          integer generated always as identity primary key,
    name        varchar(50) not null unique,
    description text
);

create table if not exists user_role
(
    user_id integer not null references app_user (id) on delete cascade,
    role_id integer not null references role (id) on delete cascade,
    primary key (user_id, role_id)
);

create table if not exists permission
(
    id              integer generated always as identity primary key,
    http_method     varchar(10)  not null,
    path_pattern    varchar(255) not null,
    permission_code varchar(100) not null
);

create table if not exists role_permission
(
    role_id       integer not null references role (id) on delete cascade,
    permission_id integer not null references permission (id) on delete cascade,
    primary key (role_id, permission_id)
);

-- Seed roles
insert into role (name, description)
values ('Admin', 'Full access to all modules'),
       ('Mechanic', 'Limited to service and brand updates'),
       ('Viewer', 'Read-only access')
on conflict (name) do nothing;

-- Seed permissions
insert into permission (http_method, path_pattern, permission_code)
values ('GET', '/brands', 'brand:read'),
       ('POST', '/brands', 'brand:update'),
       ('GET', '/users', 'user:manage'),
       ('POST', '/services', 'service:create')
on conflict do nothing;

-- ============================================
-- END
-- ============================================