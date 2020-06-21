# client-db-vaadin

Simple application for database connection and information about tables, columns and content

## Overview

Application is based on Spring Boot and Vaadin.
Client for [server-db-spring](https://github.com/Givee10/server-db-spring/) application. 
After running in authorization form put PostgreSQL credentials and database name to connect. 
After successful authentication will be shown all tables and its content.

## Installation

Use the [maven](https://maven.apache.org/) compiler to install or make a war-file.
To run application use ``vaadin:compile`` for theme compilation (CSS files) and default ``spring-boot:run``