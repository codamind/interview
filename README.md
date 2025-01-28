## Interview Test

This application uses Postgres for storing data. It uses TestContainers for the purpose of executing Integration Tests.
Please follow the instructions below

#### Requirements
- Running docker engine 


### Run Application
- To start the database, please execute ```docker-compose up -d ```
- Afterwards you should be able to start the application with ```./gradlew bootRun ```
- The documentation you can find [HERE](http://localhost:9090/swagger-ui/index.html)
- I exported postman json to help to create customers and insert new transactions