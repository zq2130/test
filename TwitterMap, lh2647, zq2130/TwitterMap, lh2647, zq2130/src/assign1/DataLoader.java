package assign1; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;


public class DataLoader {
	static String myDomain;
	static AmazonSimpleDB sdb;
	public static void main(String[] args) throws TwitterException, IOException, JSONException {
				initialDB();
			    stream();
	}
		        
		  public static void stream() throws TwitterException, IOException, JSONException{
			
		       StatusListener listener = new StatusListener(){
		    	   int i=0;
		       
		       public void onStatus(Status status) {
		    	    
		    	    String userName = status.getUser().getName();
		    	   	String text = status.getText();
		    	   	String location = null;
		    	   	GeoLocation geoLocation=null;
		    	   	double geoLocationLat = 361 ;
		    	   	double geoLocationLong =361 ;
		    	   	String geoLat = null;
		    	   	String geoLong = null;
		            
		            if( status.getPlace()!=null&&status.getPlace().getFullName()!=null){
		            	 String place = status.getPlace().getFullName();		            	
		       
		            }
		            if( status.getUser().getLocation()!=null){
		            	 location = status.getUser().getLocation();
		            }
		            if( status.getGeoLocation()!=null&& (text.contains("food") || text.contains("movie") || text.contains("sport") || text.contains("news") ) ){
		            	 geoLocation = status.getGeoLocation();
		            	 geoLocationLat=status.getGeoLocation().getLatitude();
		            	 geoLat = String.valueOf(geoLocationLat);
		            	 geoLocationLong =status.getGeoLocation().getLongitude();
		            	 geoLong = String.valueOf(geoLocationLong);
		            	 System.out.println(userName + " : " + text);
		            	 System.out.println("geoLocation:  "+ geoLocationLat + "  "+ geoLocationLong);
		            }
		           
		            if(geoLocation!=null){
		            	String itemIndex =((Integer)(i++)).toString();
		            List<ReplaceableItem> sampleData = new ArrayList<ReplaceableItem>();

		            sampleData.add(new ReplaceableItem().withName(itemIndex).withAttributes(
		                    new ReplaceableAttribute().withName("Username").withValue(userName),
		                    new ReplaceableAttribute().withName("content").withValue(text),
		                    new ReplaceableAttribute().withName("Location").withValue(location),
		                    new ReplaceableAttribute().withName("geoLat").withValue(geoLat),
		                    new ReplaceableAttribute().withName("geoLong").withValue(geoLong)
		                    ));
		            sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, sampleData) );        
		            }
		            
		          /**************************/
		          /**   save file		  **/
		          /**                     **/
		          /**************************/
		            JSONObject saveStream = new JSONObject();
		            File twitterFile = new File("twitter data");
		            if(geoLocation!=null){
		        	   try{
		            	saveStream.put("username", userName);
		            	saveStream.put("text", text);
		            	saveStream.put("location", location);
		            	saveStream.put("geolocationLat", geoLocationLat);
		            	saveStream.put("geoLocationLong", geoLocationLong);
		         
		        	   }catch(JSONException e){
		        		   e.printStackTrace();
		        	   }
		            
		       
		            if(saveStream!=null){
		            	try{
		            	
		            	FileWriter fw = new FileWriter(twitterFile,true);
		            	BufferedWriter bufferWrite = new BufferedWriter(fw);
		            	PrintWriter pw = new PrintWriter (bufferWrite);
		            	pw.println(saveStream.toString());
		            	pw.close();
		            	bufferWrite.close();
		            	fw.close();
		            	}catch(IOException e){
		            	 e.printStackTrace();
		            	}
		            }
		           }
		           
		        }
		        public void onDeletionNotice1(StatusDeletionNotice statusDeletionNotice) {}
		        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
		        public void onException(Exception ex) {
		            ex.printStackTrace();
		        }
				@Override
				public void onDeletionNotice(StatusDeletionNotice arg0) {}
			
				@Override
				public void onScrubGeo(long arg0, long arg1) {}
				
				@Override
				public void onStallWarning(StallWarning arg0) {}
		    };
		    FilterQuery fq = new FilterQuery();
		    
	        String keywords[] = {"food","news","sport","movie"};

	        fq.track(keywords);
		    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		    twitterStream.addListener(listener);
		    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
		    twitterStream.sample();
		    twitterStream.filter(fq); 
	}
	public static void initialDB(){
		 AWSCredentials credentials = null;
         try {
             credentials = new ProfileCredentialsProvider("default").getCredentials();
         } catch (Exception e) {
             throw new AmazonClientException(
                     "Cannot load the credentials from the credential profiles file. " +
                     "Please make sure that your credentials file is at the correct " +
                     "location (/Users/huanglin/.aws/credentials), and is in valid format.",
                     e);
         }
          sdb = new AmazonSimpleDBClient(credentials);
        
         
         System.out.println("===========================================");
         System.out.println("Getting Started with Amazon SimpleDB");
         System.out.println("===========================================\n");
         
         try {
        	 String preDomain = null;
        	 System.out.println("Listing all domains in your account:\n");
             for (String domainName : sdb.listDomains().getDomainNames()) {
                 System.out.println("  " + domainName);
                 preDomain = domainName;
             }
             System.out.println();
             System.out.println("Deleting " + preDomain + " domain.\n");
             sdb.deleteDomain(new DeleteDomainRequest(preDomain));
             // Create a domain
              myDomain = "MyStore";
             System.out.println("Creating domain called " + myDomain + ".\n");
             sdb.createDomain(new CreateDomainRequest(myDomain));

             // List domains
             System.out.println("Listing all domains in your account:\n");
             for (String domainName : sdb.listDomains().getDomainNames()) {
                 System.out.println("  " + domainName);
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

}
