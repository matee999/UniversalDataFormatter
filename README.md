[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Universal Data Formatter - UDF
============

Universal Data Formatter is a software component which handles and manipulates data in different data formats (JSON, YAML, ..). 
It's written in Java and has an example JavaFX GUI application for doing CRUD operations on entities.

---

## Features
- Storing universal entities in different data formats
- Auto-increment system for data integrity
- Nesting multiple entities
- Search and query functionality
- Sort operations on all attributes
- Cascade deletion of entities
- Configuration of maximum number of entities to store per file

![User Features](https://i.imgur.com/ju5OunT.png)

 
![Admin Features](https://i.imgur.com/rdM3trh.png)


#### There are 3 data format implementations:
- **JSON**
- **YAML**
- **CUSTOM** (custom data format implemented for demonstration purposes)

Every data format implementation is a runtime dependency and can be switched without re-compiling anything.

---

## Setup
Clone this repo to your desktop and do a Maven update on all projects to get and install all the dependencies.

---

## Usage
After you clone this repo to your desktop, do a Maven update on all projects to get and install all the dependencies.

Once the dependencies are installed, you can run  Main.java in GUI project to start the application. Choose the storage folder and use the application.

Enjoy!
