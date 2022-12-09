HTTP API with the use of Spring Boot to work with clients and their orders. The data is stored in the MySQL database. Changes in the DB are tracked with the Liquibase. The API is covered with integration tests (using MockMvc). All logs are stored in a stand-alone folder.

The Client entity is related to the Order entity as one-to-many

Client entity fields: name, iin (Individual Identification Number), phone, Address: region code, district name, city, street, house, building, office. Order entity fields: number (id), date of creation, free description of purchased goods and services, cost. 

Note that Order entity actually is spelled as [sic] “Orderr” and the description field is spelled [sic] “descriptionn” because it turns out that “order” and “description” are MySQL reserved words. So. anytime I tried to access an order from the DB Hibernate had troubles executing automatically generated SQL-queries. 

The API allows to create customer records in the database, issue single records for the client, update single records for the client, fetch a list of clients, delete single records for a customer along with all records of orders of this customer, create customer order records, issue single order records from the customer, update single order records at the customer, display a list of all orders for a customer, delete single order records from a customer.
