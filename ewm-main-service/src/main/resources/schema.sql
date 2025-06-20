create table if not exists users (
    id bigint generated by default as identity not null,
    name varchar(255) not null,
    email varchar(255) not null,
    constraint pk_users primary key (id),
    constraint uq_user_email unique (email)
);
create table if not exists categories (
    id bigint generated by default as identity not null,
    name varchar(255) not null,
    constraint pk_categories primary key (id),
    constraint uq_category_name unique (name)
);
create table if not exists compilations (
    id bigint generated by default as identity not null,
    title varchar(255) not null,
    pinned boolean not null default false,
    constraint pk_compilations primary key (id)
);
create table if not exists requests (
    id bigint generated by default as identity not null,
    event_id bigint not null,
    requester_id bigint not null,
    status varchar(15) not null,
    created timestamp not null
);
create table if not exists events (
    id bigint generated by default as identity not null,
    created_on timestamp without time zone,
    published_on timestamp without time zone,
    event_date timestamp without time zone,
    annotation varchar(2000),
    description varchar(7000),
    title varchar(1200),
    state varchar(64),
    initiator bigint,
    category bigint,
    confirmed_requests integer,
    participant_limit integer,
    views integer,
    paid boolean,
    request_moderation boolean,
    location_lat numeric,
    location_lon numeric,
    constraint pk_events primary key (id),
    foreign key (initiator) references users(id)
);
create table if not exists compilation_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    constraint pk_compilation_events primary key (compilation_id, event_id),
    constraint fk_comp_event_to_compilation foreign key (compilation_id) references compilations(id) on delete cascade,
    constraint fk_comp_event_to_event foreign key (event_id) references events(id) on delete cascade
);
create table if not exists comments (
	id bigint generated by default as identity primary key,
    created_date timestamp not null,
    modify_date timestamp not null,
    event_id bigint not null,
    user_id bigint not null,
    user_message varchar(2000) not null,
    admin_message varchar(2000),
    accepted boolean not null default false,
    foreign key (event_id) references events(id),
    foreign key (user_id) references users(id)
);