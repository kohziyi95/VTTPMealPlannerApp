drop schema if exists mealplanner;

create schema mealplanner;

use mealplanner;

create table user(
    user_id varchar(8) not null unique,
    username varchar(32) not null unique,
    password varchar(128) not null,
    email varchar(128) not null unique,
    primary key(user_id)
);

create table shopping_list(
    id int not null auto_increment,
    user_id varchar(8) not null,
    item_name varchar(64) not null,
    quantity varchar(64),
    primary key(id)
);

create table recipes(
    id int not null auto_increment,
    recipe_name varchar(64) not null,
    user_id varchar(8) not null,
    primary key(id)
);

