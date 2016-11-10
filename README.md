# RFIDServer Project


# Description

This project is a part of the Context-Aware Internet of Things fourth year project.

This project encapsulates the full stack of the RFID inventory management system.  The RFIDServer project handles the database storing item and location information, the mapping of database objects to POJOs, and the implementation of a REST API hosted on a Tomcat server.

# Libraries/Tools

Development of this project used Maven for dependency management, but all required jar files should also be present in the WEB-INF/LIB folder.

asm-3.3.1.jar - Required by the Jersey services

gson-2.6.2.jar - Used to convert objects to/from JSON, and to parse JSON requests

jersey-bundle-1.17.1.jar - Used to route and handle REST requests, and to respond appropriately

sqlite-jdbc-3.14.2.1.jar - Used to interface with the database. SQLite was chose for it's simplicity and portability, and with the fact that this project is most likely to be used for demo purposes than a full scale application.

# DB Schema

items

| id       | name           | type  |description |location  |
| ------------- |:-------------:| :-----:|:-----:|:-----:|
| int (Primary Key)| text | text| text |int |

locations

| id       | name           | description  |address |lat  |lon|
| ------------- |:-------------:| :-----:|:-----:|:-----:|:-----:|
| int (Primary Key)| text | text| text |text |text|
