# Project 2 NoSQL Database - DynamoDB
Tan Wang
## Introduction
Amazon DynamoDB is a fully managed NoSQL database service that provides fast and predictable
performance with seamless scalability. DynamoDB lets you offload the administrative burdens
of operating and scaling a distributed database so that you don't have to worry about hardware
provisioning, setup and configuration, replication, software patching, or cluster scaling. DynamoDB also
offers encryption at rest, which eliminates the operational burden and complexity involved in protecting
sensitive data.

With DynamoDB, you can create database tables that can store and retrieve any amount of data and
serve any level of request traffic. You can scale up or scale down your tables' throughput capacity
without downtime or performance degradation. You can use the AWS Management Console to monitor
resource utilization and performance metrics.

## Core Component of Amazon DynamoDB
In DynamoDB, tables, items, and attributes are the core components that you work with. A table is a
collection of items, and each item is a collection of attributes. DynamoDB uses primary keys to uniquely
identify each item in a table and secondary indexes to provide more querying flexibility. You can use
DynamoDB Streams to capture data modification events in DynamoDB tables.

- Tables – Similar to other database systems, DynamoDB stores data in tables. A table is a collection of
data. For example, see the example table called People that you could use to store personal contact
information about friends, family, or anyone else of interest. You could also have a Cars table to store
information about vehicles that people drive.
- Items – Each table contains zero or more items. An item is a group of attributes that is uniquely
identifiable among all of the other items. In a People table, each item represents a person. For a Cars
table, each item represents one vehicle. Items in DynamoDB are similar in many ways to rows, records,
or tuples in other database systems. In DynamoDB, there is no limit to the number of items you can
store in a table.
- Attributes – Each item is composed of one or more attributes. An attribute is a fundamental data
element, something that does not need to be broken down any further. For example, an item in a
People table contains attributes called PersonID, LastName, FirstName, and so on. For a Department
table, an item might have attributes such as DepartmentID, Name, Manager, and so on. Attributes in
DynamoDB are similar in many ways to fields or columns in other database systems.

Installation Process
Sample code(CRUD)
```
Look! You can see my backticks.
```
