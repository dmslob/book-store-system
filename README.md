# book-store-system
Spring Boot, ActiveMQ

#To run system:
###1. To start ActiveMQ 
Within root dir run the command: docker-compose up 

###2. To start book-service
GO to book-service dir, run commands:
- mvn install
- java -jar build/libs/book-service-1.0-SNAPSHOT.jar

###3. To start warehouse-service
GO to warehouse-service dir, run commands:
- mvn install
- java -jar build/libs/warehouse-service-1.0-SNAPSHOT.jar

###4. GO to http://localhost:9001/books/ to submit form
###5. GO to http://localhost:8161/admin/queues.jsp to login to
ActiveMQ admin page and monitor queues.