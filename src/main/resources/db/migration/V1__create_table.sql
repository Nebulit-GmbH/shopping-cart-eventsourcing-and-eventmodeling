CREATE TABLE IF NOT EXISTS aggregates (
    aggregate_id varchar(255) PRIMARY KEY,
    version BIGINT,
    discriminator varchar
);

CREATE SEQUENCE events_seq START 101;

CREATE TABLE events (
    id INT PRIMARY KEY,
    aggregate_id varchar(200),
    value varchar,
    version int,
    created date default current_date
);

CREATE TABLE IF NOT EXISTS event_publication
(
  id               UUID NOT NULL,
  listener_id      TEXT NOT NULL,
  event_type       TEXT NOT NULL,
  serialized_event TEXT NOT NULL,
  publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
  completion_date  TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (id)
);

CREATE TABLE inventories
(
    inventory_id UUID NOT NULL,
    inventory   int  not null
);

CREATE TABLE cart_sessions
(
    cart_id UUID NOT NULL
);
CREATE TABLE cart_session_items
(
    cart_id UUID NOT NULL,
    cart_item_id UUID NOT NULL ,
    product_id UUID NOT NULL

);

CREATE TABLE requestedproductrevocations_product_revocations(
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL
)
