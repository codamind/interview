INSERT INTO customer (customer_id,first_name, last_name, email, address, postcode,city,ssn,credit_score)
VALUES (1001, 'Michael', 'Ende', 'michael@end.com', 'LaLaLand 3', '833 22', 'SomeCity',
        '20041116-6911', 78.5);

INSERT INTO TRANSACTION (customer_id, amount, transaction_date, transaction_type)
VALUES (1001,500.00,'2023-01-15 10:30:00',0);
INSERT INTO TRANSACTION (customer_id, amount, transaction_date, transaction_type)
VALUES (1001,1200.50,'2023-02-20 14:15:00',1);
INSERT INTO TRANSACTION (customer_id, amount, transaction_date, transaction_type)
VALUES (1001,300.75,'2023-03-05 08:50:00',0);
