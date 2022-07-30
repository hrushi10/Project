package ca.yorku.eecs;
import static org.neo4j.driver.v1.Values.parameters;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

public class neo4jProject {
	private Driver driver;
	private String uriDb;
	
	public neo4jProject() {
		uriDb = "bolt://localhost:7687"; // may need to change if you used a different port for your DBMS
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);
	}
	
	public void addMovie(String name, String movieId) {
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Movie {name: $x, movieId: $y})", 
					parameters("x" , name,"y", movieId)));
			session.close();
		}
	}
		public Boolean checkDup(String type,String Id) {
			try (Session session = driver.session())
	        {
	        	try (Transaction tx = session.beginTransaction()) {
	        		String tId = type.toLowerCase() + "Id";
	        		
	        		StatementResult result = tx.run("match (n:"+type+"{"+tId+":$y}) return n;"
							,parameters("y", Id) );
	        		
	        		if (result.hasNext()) {
	        			return true;
	        		}else {
	        			return false;
	        		}
	        	}
	        }
			}
		
		public Boolean checkDup(String from, String fromId, String to,  String toId, String type) {
			
			try (Session session = driver.session())
	        {
	        	try (Transaction tx = session.beginTransaction()) {
	        		String tId = type.toLowerCase() + "Id";
	        		
	        		StatementResult result = tx.run("MATCH (a: "+from+"), (m: ) WHERE a.id = \"$x\" AND m.id = \"$y\" RETURN exists((a)-[:ACTED_IN]-(m));"
							,parameters("y", fromId) );
	        		
	        		if (result.hasNext()) {
	        			return true;
	        		}else {
	        			return false;
	        		}
	        	}
	        }
		}
		
		public Boolean createR (String from, String fromId, String to,  String toId) {
			 String fi = from + "Id";
			 String ti = to + "Id";
			try (Session session = driver.session()){
				session.writeTransaction(tx -> tx.run("MATCH (a:"+from+" {"+fi+":$x}),"
						+ "(t:"+to+" {"+ti+":$y})\n" + 
						 "MERGE (a)-[r:ACTED_IN]->(t)\n" + 
						 "RETURN r", parameters( "x", fromId,"y", toId)));
				session.close();
				return true;
			}catch (Exception e) {
				System.out.println(e.toString());
				return false;
			}
		
		}
		
	
}
