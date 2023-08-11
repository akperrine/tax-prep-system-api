# tax-prep-system-api

### Description
This is a REST api server for the Tax-Prepped web application. It is secured with Native Spring Security and is designed for users to be able to create accounts for tax e-filing.

### Table of Contents
* [Installation](#installation)
* [Usage](#usage)
* [Credits](#credits)

### Installation
1. Clone the repository.
2. Package with Maven or run locally.
3. It is specifically designed to run the Tax-Prepped front end.

### Usage
- Once Cloned:
    - If you are running from your IDE, make sure maven can build the application. 
    - If you are running from a console, you can build and run using "mvn spring-boot:run"
    - If you'd rather run from a JAR you can use "mvn package clean" and run the JAR from the target sub directory


### Credits
* spring-boot-starter-web
* spring-boot-starter-data-mongodb
* spring-boot-devtools
* spring-boot-starter-test
* lombok
* spring-boot-starter-aop
* h2
* javafaker
* spring-boot-starter-security