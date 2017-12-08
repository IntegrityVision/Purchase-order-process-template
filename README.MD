# PURCHASE ORDER PROCESS TEMPLATE

![Purchase order process template](documentation/concept.png)

Welcome to purchase order process template by [Integrity Vision](https://www.integrity.com.ua/).

What can be better than smart business process? Only an automated business process!

If you want to optimize your time and resources, demonstrate loyalty to your employees and increase the speed of repetitive tasks processing — just try to use our automated business process of equipment order with Camunda BPM. 

Table of contents:
- [Goal](#goal)
- [Getting Started](#getting-started)
- [Running](#running)
- [Deployment with docker](#deployment-with-docker)
- [Test](#test) 
- [Let us know](#let-us-know)
- [Authors](#authors)
- [License](#license)

## Goal

This project was started with the automation of the Equipment purchase order process using [BPM Camunda](https://camunda.org/).
Camunda is an open source platform for workflow and business process management.

![bpm schema](documentation/bpmn_scheme.jpeg?raw=true)

Project contains additional `docker-compose.yml` for running with docker-compose (located in the folder `docker`).

## Getting Started

Before running project you have to set up developer profile in `application.yml`.

Example:

```
spring:
  profiles: development
  #Mail setting for sending notification
  mail:
    host: smtp.gmail.com
    port: 587
    username: user@integrity.com.ua
    password: userpassword
    protocol: smtp
    properties.mail.smtp:
      auth: true
      starttls.enable: true
      ssl.trust: smtp.gmail.com
camunda:
  #For correct link in email
  host: localhost:8080
  #All message will be redirect to this email. Set empty for sending to orginal recipient
  mailTo: user@integrity.com.ua
  # You can get example of this file from {project directory}/docker/data
  msExcelPath: D:/TestUsers.xlsx
  database:
    url: jdbc:postgresql://127.0.0.1:5432/camunda
    user: postgres
    password: postgres
  
```

For running with docker-compose you have to set up demo profile in `docker/config/application.yml` (this file is reduced because it has pre-setup setting).

Camunda users will be created from `{msExcelPath}` file (in docker configuration it's `docker/data/TestUsers.xlsx`). User passwords are also in this file.

## Running

```
mvn spring-boot:run
```

## Deployment with docker

Before running the project in docker you have to build the project.

```
 mvn clean package 
```

After successful building there will be `iv-camunda-demo.jar` in `docker/app`.

Make sure `docker/config/application.yml` is configured, and
run next command in `docker` folder for create and start two containers (**integrity-camunda**, **integrity-camunda-db**).

```
docker-compose up -d
```

Stop:

```
docker stop integrity-camunda  integrity-camunda-db
```

Start:

```
docker start integrity-camunda  integrity-camunda-db
```

Remove:

```
docker rm -f integrity-camunda  integrity-camunda-db
```

## Test 

[Test case](documentation/scenario.md)

## Let us know!

If you have any questions, issues or proposals, please create a new issue in this repository. We’d be glad if you sent us some feedback about using this process in your company. Just mail <github@integrity.com.ua> and do let us know if you have any suggestion regarding the process.

We’re going to publish more usefully processes to make your work even easier.

Stay tuned! Connect us:
 
[![Facebook](https://www.integrity.com.ua/wp-content/uploads/2017/12/FB_1_50.png)](https://www.facebook.com/integrity.vision/)  [![Linkedin](https://www.integrity.com.ua/wp-content/uploads/2017/12/Linkedin_1_50.png)](https://www.linkedin.com/company/integrity-vision/)  [![YouTube](https://www.integrity.com.ua/wp-content/uploads/2017/12/YouTube_circle_50.png)](https://www.youtube.com/user/integrityvision)
 
## Authors

[![Integrity Vision](https://www.integrity.com.ua/wp-content/uploads/2017/12/Integrity-vision-logo-png_small.png)](http://bit.ly/ivgithub)

## License

This project is licensed under the [Apache License](http://www.apache.org/licenses/LICENSE-2.0)
