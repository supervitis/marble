package test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
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

	         DBCollection school = db.getCollection("vamosalla");
	         System.out.println("Collection test created successfully");
	         

				String json = "{'database' : 'dineshonjavaDB','table' : 'employees'," +  
			 "'detail' : {'empId' : 10001, 'empName' : 'Dinesh', 'salary' : 70000}}}";  
			  
			DBObject dbObject = (DBObject)JSON.parse(json);  
			   
			school.insert(dbObject);
	        System.out.println("a tope"); 

	       }catch(Exception e){
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       }
		
		
		
		
	}

}
