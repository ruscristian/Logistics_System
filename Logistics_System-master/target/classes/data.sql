INSERT INTO DESTINATIONS (name, distance)
SELECT *
FROM CSVREAD('src/main/resources/destinations.csv', 'name,distance', null);

CREATE TABLE IF NOT EXISTS ORDERBUFF (destination varchar, delivery_date varchar)
AS SELECT * FROM CSVREAD('src/main/resources/orders.csv', 'destination, delivery_date', null);

INSERT INTO ORDERS
   (delivery_date, destination_id, status)
SELECT
    orderbuff.delivery_date, destinations.id, 'NEW'
FROM
  ORDERBUFF JOIN DESTINATIONS
    ON destinations.name = orderbuff.destination ;

DROP TABLE IF EXISTS ORDERBUFF;
