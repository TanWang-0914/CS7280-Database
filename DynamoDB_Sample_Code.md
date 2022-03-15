# Project 2 NoSQL Database - DynamoDB
Tan Wang

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
