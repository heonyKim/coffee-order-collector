create table member
(
    id         INTEGER default 0 not null
        primary key,
    name       VARCHAR(50)       not null,
    created_at BIGINT            not null
);

create index member_created_at
    on member (created_at);

create index member_name
    on member (name);

create table menu
(
    id           INTEGER      not null
        primary key,
    brand        VARCHAR(200) not null,
    category     VARCHAR(50)  not null,
    name         VARCHAR(100) not null,
    temperatures VARCHAR(100),
    sizes        VARCHAR(100),
    created_at   BIGINT       not null,
    image_url    VARCHAR(500)
);

create index brand_category_name
    on menu (brand, category, name);

create table order_history
(
    id             INTEGER      not null
        primary key,
    member_id      INTEGER      not null,
    member_name    VARCHAR(50)  not null,
    brand          VARCHAR(50)  not null,
    menu_name      VARCHAR(100) not null,
    created_at     BIGINT       not null,
    created_origin VARCHAR(300) not null,
    updated_at     BIGINT,
    updated_origin VARCHAR(300)
);

create index brand
    on order_history (brand);

create index created_at_updated_at
    on order_history (created_at, updated_at);

create index member_id
    on order_history (member_id);


