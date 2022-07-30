package ca.yorku.eecs;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Relationship implements HttpHandler{

	@Override
	public void handle(HttpExchange request) throws IOException {
		
		
		try {
            if (request.getRequestMethod().equals("PUT")) {
                handleGet(request);
          
            } else
            	sendString(request, "Unimplemented method\n", 500);
        } catch (Exception e) {
        	e.printStackTrace();
        	sendString(request, "Server error\n", 500);
        }
		
	}
	
	private void sendString(HttpExchange request, String data, int restCode) 
			throws IOException {
		request.sendResponseHeaders(restCode, data.length());
        OutputStream os = request.getResponseBody();
        os.write(data.getBytes());
        os.close();
	}
	
	 private void handleGet(HttpExchange request) throws IOException, NullPointerException {
		 URI uri = request.getRequestURI();
	        String query = uri.getQuery();
	        System.out.println(query);
	       
	        try {
	        Map<String, String> queryParam = splitQuery(query);
	        System.out.println(queryParam);
	        String from = queryParam.get("from");
	        String fromId = queryParam.get("fromId");
	        String to = queryParam.get("to");
	        String toId = queryParam.get("toId");
	        String rltnType = queryParam.get("relationType");
	        
	      //http://localhost:8080/api/addRelationship?from=Actor&fromId=xyz12&to=Movie&movieId=nm700&relationType=ACTED_IN
	        
	        if(from.isEmpty() || to.isEmpty() || fromId.isEmpty() || toId.isEmpty()) {
	        	throw new NullPointerException();
	        }
	        
	        
	        neo4jProject method = new neo4jProject();
		 
	        
//	        if(!method.checkDup(id)) {
//	        	 method.addMovie(name, id);
//	        }else {
//	        	sendString(request, "Duplicate Movie Id!!\n", 400);
//	        }
	        
	       
		 //   String response ="added movie = " + name + "\n";
	      //  sendString(request, response, 200);
	        
	        
	        }catch (Exception e) {
	        	sendString(request, "Missing required information or body improperly formatted\n", 400);
	        }
	        
	       	       
	 }
	 
	  private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
	        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	        String[] pairs = query.split("&");
	        for (String pair : pairs) {
	            int idx = pair.indexOf("=");
	            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	        }
	        return query_pairs;
	    }

}
