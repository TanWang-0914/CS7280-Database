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

## Installation Process
DynamoDB is a web service, to use DynamoDB:
1. Sign up for AWS. To use the DynamoDB service, you must have an AWS account. If you don't already have an account, you
are prompted to create one when you sign up. You're not charged for any AWS services that you sign up
for unless you use them.
2. Get an AWS access key (p. 57) (used to access DynamoDB programmatically). 
Before you can access DynamoDB programmatically or through the AWS Command Line Interface (AWS CLI), 
you must have an AWS access key. You don't need an access key if you plan to use the DynamoDB
console only.
3. Configure your credentials (p. 58) (used to access DynamoDB programmatically).
Before you can access DynamoDB programmatically or through the AWS CLI, you must configure your
credentials to enable authorization for your applications.
There are several ways to do this. For example, you can manually create the credentials file to store your
access key ID and secret access key. You also can use the aws configure command of the AWS CLI to
automatically create the file. Alternatively, you can use environment variables. For more information
about configuring your credentials, see the programming-specific AWS SDK developer guide.

### Getting Started with DynamoDB and AWS SDKs(Java)
AWS SDKs are available for a wide variety of languages. For a complete list, see [Tools for
Amazon Web Services](https://aws.amazon.com/tools/).


• Download and run DynamoDB on your computer. For more information, see Setting Up DynamoDB
Local (Downloadable Version) (p. 50).
DynamoDB (downloadable version) is also available as part of the AWS Toolkit for Eclipse. For more
information, see AWS Toolkit for Eclipse.
• Set up an AWS access key to use the AWS SDKs. For more information, see Setting Up DynamoDB (Web
Service).
• Set up the AWS SDK for Java:
API Version 2012-08-10
84
Amazon DynamoDB Developer Guide
Step 1: Create a Table
• Install a Java development environment. If you are using the Eclipse IDE, install the AWS Toolkit for
Eclipse.
• Install the AWS SDK for Java.
• Set up your AWS security credentials for use with the SDK for Java.
For instructions, see Getting Started in the AWS SDK for Java Developer Guide.

### Sample code(CRUD)
#### Create a Table 
```
import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class MoviesCreateTable {

    public static void main(String[] args) throws Exception {
    
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new
             AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();
            
    DynamoDB dynamoDB = new DynamoDB(client);
    
    String tableName = "Movies";
    
    try {
        System.out.println("Attempting to create table; please wait...");
        Table table = dynamoDB.createTable(tableName,
        Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition key
            new KeySchemaElement("title", KeyType.RANGE)), // Sort key
            
        Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
            new AttributeDefinition("title", ScalarAttributeType.S)),
            new ProvisionedThroughput(10L, 10L));
        table.waitForActive();
        System.out.println("Success. Table status: " +
            table.getDescription().getTableStatus());
     }
     catch (Exception e) {
          System.err.println("Unable to create table: ");
          System.err.println(e.getMessage());
      }
   }
}
```

#### Create a New Item
```
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

public class MoviesItemOps01 {

    public static void main(String[] args) throws Exception {
    
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new
            AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();

        DynamoDB dynamoDB = new DynamoDB(client);
        
        Table table = dynamoDB.getTable("Movies");
        
        int year = 2015;
        String title = "The Big New Movie";
        
        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);
        
        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                .putItem(new Item().withPrimaryKey("year", year, "title",
                title).withMap("info", infoMap));
                
            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
        }
        catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
```
#### Read an Item
```
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class MoviesItemOps02 {

    public static void main(String[] args) throws Exception {
    
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new
            AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();
            
        DynamoDB dynamoDB = new DynamoDB(client);
        
        Table table = dynamoDB.getTable("Movies");
        
        int year = 2015;
        String title = "The Big New Movie";
        
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);
        
        try {
            System.out.println("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);
        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
```
#### Update an Item
```
import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class MoviesItemOps03 {

    public static void main(String[] args) throws Exception {
    
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new
            AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();
            
        DynamoDB dynamoDB = new DynamoDB(client);
        
        Table table = dynamoDB.getTable("Movies");
        
        int year = 2015;
        String title = "The Big New Movie";
        
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("year",
            year, "title", title)
            .withUpdateExpression("set info.rating = :r, info.plot=:p, info.actors=:a")
            .withValueMap(new ValueMap().withNumber(":r", 5.5).withString(":p",
            "Everything happens all at once.")
            .withList(":a", Arrays.asList("Larry", "Moe", "Curly")))
            .withReturnValues(ReturnValue.UPDATED_NEW);
            
        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" +
                outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
```
#### Delete an Item
```
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class MoviesItemOps06 {

    public static void main(String[] args) throws Exception {
    
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new
            AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .build();
            
        DynamoDB dynamoDB = new DynamoDB(client);
        
        Table table = dynamoDB.getTable("Movies");
        
        int year = 2015;
        String title = "The Big New Movie";
        
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
            .withPrimaryKey(new PrimaryKey("year", year, "title",
            title)).withConditionExpression("info.rating <= :val")
            .withValueMap(new ValueMap().withNumber(":val", 5.0));
        // Conditional delete (we expect this to fail)
        
        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
```
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
