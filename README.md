# marvel-api
This is a intermediate service to call the marvel api service that retrieve and transform the data fetched from the api.

### Prerequisite to run the project
* Java verion 11 JDK (or higher) install
* Set JAVA_HOME to the directory where jdk is installed
* Gradle 6 or above 

### Commands to build the project
* make sure to be in the root directory
* ```gradlew assemble``` : compile the code of your application and your test and fail if there are errors
* ```gradlew build``` : compile the code of your application and your test. It will then run your test and let you know if any fail

### Commands to run the application
* ```gradlew bootrun```: run the ```main``` method from ```Application.java```
* Alternatively you can run the ```main``` method in ```Application.java``` in your chosen IDE e.g. ```IntelliJ```

### Endpoints
GET ```${host}:${port}/characters```
fetch a list of a Marvel characterse id

GET ```${host}:${port}/characters/{characterId}``` fetch the detail of a Marvel character according to ID