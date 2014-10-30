package assign1;
/*
 * Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * This sample demonstrates how to make basic requests to Amazon SimpleDB using
 * the AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon SimpleDB. For more information on
 * Amazon SimpleDB, see http://aws.amazon.com/simpledb.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in the
 *                   AwsCredentials.properties file before you try to run this
 *                   sample.
 * http://aws.amazon.com/security-credentials
 */
public class simpledb {

	public static void main(String[] args) throws InterruptedException {
		ArrayList<record> result=new ArrayList<record>();
        AWSCredentials credentials = null;
        try {
            credentials = new PropertiesCredentials(
            		simpledb.class.getResourceAsStream("AwsCredentials.properties"));
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/huanglin/.aws/credentials), and is in valid format.",
                    e);
        }
        
        AmazonSimpleDB sdb = new AmazonSimpleDBClient(credentials);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SimpleDB");
        System.out.println("===========================================\n");

        try {
            String myDomain = "MyStore";

            String selectExpression = "select count(*) from " + myDomain+" where content like '%news%'";
            System.out.println("Selecting: " + selectExpression + "\n");
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            for (Item item : sdb.select(selectRequest).getItems()) {
            	System.out.println("    itemIndex: " + item.getName());
            	for (Attribute attribute : item.getAttributes()) {
                    System.out.println("      Attribute");
                    System.out.println("        Name:  " + attribute.getName());
                    System.out.println("        Value: " + attribute.getValue());
                }
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SimpleDB, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
	}
    public List<record> query(String word) throws Exception {
        /*
         * Important: Be sure to fill in your AWS access credentials in the
         *            AwsCredentials.properties file before you try to run this
         *            sample.
         * http://aws.amazon.com/security-credentials*/
    	
    	ArrayList<record> result=new ArrayList<record>();
        AWSCredentials credentials = null;
        try {
            credentials = new PropertiesCredentials(
            		simpledb.class.getResourceAsStream("AwsCredentials.properties"));
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/Users/huanglin/.aws/credentials), and is in valid format.",
                    e);
        }
        
        AmazonSimpleDB sdb = new AmazonSimpleDBClient(credentials);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SimpleDB");
        System.out.println("===========================================\n");

        String myDomain = "MyStore";

            String filter=word;
            String nextToken=null;
            SelectResult selectResult = null;
            do {
            	String selectExpression="";
            	if (word.equals("all"))
            		selectExpression="select * from MyStore LIMIT 2500";
            	else 
            		selectExpression = "select * from " + myDomain+" where content like '%"+filter+"%' LIMIT 2500";
            System.out.println("Selecting: " + selectExpression + "\n");
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            selectRequest.setNextToken(nextToken);
            selectResult=sdb.select(selectRequest);
            nextToken=selectResult.getNextToken();
            List<Item> list=selectResult.getItems();
            for (Item item : list) {
                System.out.println("    itemIndex: " + item.getName());
                	List<Attribute> attributeTmp=new ArrayList<Attribute>();
                	attributeTmp=item.getAttributes();
                	record tmp=new record();
                	for (int i=0;i<5;i++) {
                		if (attributeTmp.get(i).getName().equals("content"))
                			tmp.content=attributeTmp.get(i).getValue();
                		else if (attributeTmp.get(i).getName().equals("geoLat"))
                			tmp.x=Double.parseDouble(attributeTmp.get(i).getValue());
                		else if (attributeTmp.get(i).getName().equals("Username"))
                			tmp.username=attributeTmp.get(i).getValue();
                		else if (attributeTmp.get(i).getName().equals("Location"))
                			tmp.location=attributeTmp.get(i).getValue();
                		else tmp.y=Double.parseDouble(attributeTmp.get(i).getValue());
                	}
                	result.add(tmp);
            }
            }
        while (nextToken!=null);
        	
        return result;
    }
}

