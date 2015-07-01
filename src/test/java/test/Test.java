package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		try{   

	         // To connect to mongodb server
	         MongoClient mongoClient = new MongoClient( "polux.det.uvigo.es" , 27117 );

	         // Now connect to your databases
	         DB db = mongoClient.getDB("datasets");
	         System.out.println("Connect to database successfully");

	         DBCollection collection = db.getCollection("prueba_rubius_file");
	         System.out.println("Collection test created successfully");
	         
	        /*
	        FileInputStream fis = new FileInputStream("C:\\Users\\David\\Desktop\\test.json");
	        
	    	//Construct BufferedReader from InputStreamReader
	    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	     
	    	String line = null;
	    	while ((line = br.readLine()) != null) {
	    		DBObject dbObject = (DBObject)JSON.parse(line);  
				collection.insert(dbObject);
	    	}
	     
	    	br.close();
	        */
	        
	         File file = new File("C:\\Users\\David\\Desktop\\test.json");
	         
	         BufferedReader br = new BufferedReader(new FileReader(file));
	         
	     	String line = null;
	     	while ((line = br.readLine()) != null) {
	     		DBObject dbObject = (DBObject)JSON.parse(line);  
				collection.insert(dbObject);
	     	}
	      
	     	br.close();
	         
	         
	         
	        PrintWriter out = new PrintWriter("C:\\Users\\David\\Desktop\\rubius.json");
	        
	        
	        DBCursor cursor = collection.find();
	        while( cursor.hasNext() ){
	            DBObject obj = cursor.next();
	  	      	out.println(obj.toString());

	        }
	        

	       }catch(Exception e){
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       }
		
		
		
		
	}

}
