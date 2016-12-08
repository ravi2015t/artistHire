package com.tasks;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.daemonservices.WeddingPlannerExecutor;

public class CrudDynamoDB {
	private static final Properties awsCredentialsFile = WeddingPlannerExecutor.
			getPropertiesFile("AwsCredentials.properties");
	static BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsCredentialsFile.getProperty("accessKey"), awsCredentialsFile.getProperty("secretKey"));


	static AmazonDynamoDBClient client = new AmazonDynamoDBClient(awsCreds);

	  
	  //  static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
	    //        new ProfileCredentialsProvider()));
		static DynamoDB dynamoDB = new DynamoDB(client);


    static String user = "user";
    static String planWedding= "planWeddding";
    static String confirmedEvents = "confirmEvents"; 
    public static void main(String[] args) throws IOException {

        //createItems();

        retrieveItem();

        // Perform various updates.
       // updateMultipleAttributes();
        //updateAddNewAttribute();
       // updateExistingAttributeConditionally();

        // Delete the item.
//        deleteItem();

    }

    private static void createItems() {

        Table table = dynamoDB.getTable(user);
        Table table2 = dynamoDB.getTable(planWedding);
        Table table3 = dynamoDB.getTable(confirmedEvents);
        
        try {

            Item item = new Item()
                .withPrimaryKey("username", "rambit")
                .withString("firstName", "Matt")
                .withString("lastName", "Daemon")
                .withMap( "Address", 
                    new ValueMap()
                    .withString("street", "86th Street")
                    .withString("city", "Brooklyn")
                    .withString("state", "NY")
                    .withString("zip", "11209"))
                .withString("phoneNumber", "9008305270");
                 table.putItem(item);

                 
                  Date d = new Date();
                  item = new Item()
                         .withPrimaryKey("username", "rambit")
                         .withString("date", d.toString())
                         .withNumber("budget", 1234)
                         .withMap( "Address", 
                             new ValueMap()
                             .withString("street", "86th Street")
                             .withString("city", "Brooklyn")
                             .withString("state", "NY")
                             .withString("zip", "11209"))
                         .withString("preferenceOrder", "photographer,florist,makeupArtist");
                          
                  table2.putItem(item);
                  
                  item = new Item()
                                  .withPrimaryKey("username", "rambit")
                                  .withString("date", d.toString())
                                  .withNumber("price", 1234)
                                  .withMap( "Address", 
                                      new ValueMap()
                                      .withString("street", "86th Street")
                                      .withString("city", "Brooklyn")
                                      .withString("state", "NY")
                                      .withString("zip", "11209"))
                                  .withString("Vendor", "photographer")
                                  .withString("Name", "RAVAN");
                                   table3.putItem(item);
                          
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }

    private static void retrieveItem() {
        Table table = dynamoDB.getTable(user);

        try {

            Item item = table.getItem("username", "sam",  "firstname, lastname, phoneNumber, Address", null);

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.toJSONPretty());

        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }   

    }

    private static void updateAddNewAttribute() {
        Table table1 = dynamoDB.getTable("ProductCatalog");

        try {

            Map<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#na", "NewAttribute");

            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("Id", 121)
            .withUpdateExpression("set #na = :val1")
            .withNameMap(new NameMap()
                .with("#na", "NewAttribute"))
            .withValueMap(new ValueMap()
                .withString(":val1", "Some value"))
            .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome =  table1.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after adding new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());           

        }   catch (Exception e) {
            System.err.println("Failed to add new attribute in " + user);
            System.err.println(e.getMessage());
        }        
    }

    private static void updateMultipleAttributes() {

        Table table = dynamoDB.getTable(user);

        try {

           UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("Id", 120)
            .withUpdateExpression("add #a :val1 set #na=:val2")
            .withNameMap(new NameMap()
                .with("#a", "Authors")
                .with("#na", "NewAttribute"))
            .withValueMap(new ValueMap()
                .withStringSet(":val1", "Author YY", "Author ZZ")
                .withString(":val2", "someValue"))
            .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out
            .println("Printing item after multiple attribute update...");
            System.out.println(outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println("Failed to update multiple attributes in "
                    + user);
            System.err.println(e.getMessage());

        }
    }

    private static void updateExistingAttributeConditionally() {

        Table table = dynamoDB.getTable(user);

        try {

            // Specify the desired price (25.00) and also the condition (price =
            // 20.00)

            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("Id", 120)
            .withReturnValues(ReturnValue.ALL_NEW)
            .withUpdateExpression("set #p = :val1")
            .withConditionExpression("#p = :val2")
            .withNameMap(new NameMap()
                .with("#p", "Price"))
            .withValueMap(new ValueMap()
                .withNumber(":val1", 25)
                .withNumber(":val2", 20));

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out
            .println("Printing item after conditional update to new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println("Error updating item in " + user);
            System.err.println(e.getMessage());
        }
    }

    private static void deleteItem() {

        Table table = dynamoDB.getTable(user);

        try {

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
            .withPrimaryKey("Id", 120)
            .withConditionExpression("#ip = :val")
            .withNameMap(new NameMap()
                .with("#ip", "InPublication"))
            .withValueMap(new ValueMap()
            .withBoolean(":val", false))
            .withReturnValues(ReturnValue.ALL_OLD);

            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);

            // Check the response.
            System.out.println("Printing item that was deleted...");
            System.out.println(outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println("Error deleting item in " + user);
            System.err.println(e.getMessage());
        }
    }
} 