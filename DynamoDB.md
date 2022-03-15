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

### Core Component of Amazon DynamoDB
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

![DynamoDB Architecture](/DynamoDB_Table.png "DynamoDB Architecture")

### Primary Key
When you create a table, in addition to the table name, you must specify the primary key of the table.
The primary key uniquely identifies each item in the table, so that no two items can have the same key.

DynamoDB supports two different kinds of primary keys:
- Partition key – A simple primary key, composed of one attribute known as the partition key. 
DynamoDB uses the partition key's value as input to an internal hash function. The output from the hash 
function determines the partition (physical storage internal to DynamoDB) in which the item will be stored.

- Partition key and sort key – Referred to as a composite primary key, this type of key is composed of
two attributes. The first attribute is the partition key, and the second attribute is the sort key.
DynamoDB uses the partition key value as input to an internal hash function. The output from the
hash function determines the partition (physical storage internal to DynamoDB) in which the item will
be stored. All items with the same partition key value are stored together, in sorted order by sort key
value.

### Secondary Indexes
You can create one or more secondary indexes on a table. A secondary index lets you query the data
in the table using an alternate key, in addition to queries against the primary key. DynamoDB doesn't
require that you use indexes, but they give your applications more flexibility when querying your data.
After you create a secondary index on a table, you can read data from the index in much the same way as
you do from the table.

## DynamoDB Features
### Performance at scale
- Millions of request per second
- Microsecond latency
- Automated global replication
### No servers to manage
- Maintenance free
- Automatic scaling
- On-demand capacity mode
### Enterprice ready
- ACID transactions
- Encryption at rest
- On-demand backup and restore
- NoSQL Workbench
