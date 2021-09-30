package no.hvl.dat110.rest.counters;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static Counters counters = null;
	
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		
		// Just used for testing
		ArrayList<Integer> testIds = new ArrayList<Integer>();
		List<Integer> ids = Arrays.asList( 1,2,3,4,5,6,7,8);
		testIds.addAll(ids);
		
        Random random = new Random();
        Map<String, Todo> todos = new HashMap<String, Todo>();
        Gson gson = new Gson();
        
        
        post("/todos", (req, res) -> {
        	
            Todo todo = gson.fromJson(req.body(), Todo.class);
            
            int id;
            if (!testIds.isEmpty())  {
            	id = testIds.get(0);
            	testIds.remove(0);
            }
            
            else {
            	id = random.nextInt();
            	while (todos.containsKey(String.valueOf(id))) {
            		id = random.nextInt();
            	}
            }
            
         
            todos.put(String.valueOf(id), todo);
            todo.setId(String.valueOf(id));
        
            return todo.toString();
        });

       
        get("/todos/:id", (req, res) -> {
     	
        	String id = req.params(":id");
      
        	
        	if (todos.containsKey(id)) {
                return todos.get(id).toString();
            } 
        	
        	else {
                return "Invalid key";
            }
        });

        

        put("/todos/:id", (req, res) -> {
            String id = req.params(":id");
            
            if (todos.containsKey(id)) {
            	
            	Todo todo = todos.get(id);
            	
            	String summary = gson.fromJson(req.body(), Todo.class).getSummary();
            	String description =  gson.fromJson(req.body(), Todo.class).getDescription();
               
            	todo.setSummary(summary);
            	todo.setDescription(description);
            	
            	
                return "Changes to Todo: " + id ;
            } 
            
            else {
                
                return "Invalid key";
            }
        });

       
        delete("/todos/:id", (req, res) -> {
            String id = req.params(":id");
     
            if (todos.containsKey(id)) {
            	todos.remove(id);
                return "Todo removed";
            } 
            
            else {
 
                return "Invalid key";
            }
        });

     
        get("/todos", (req, response) -> {
        	Map<String, String> getId = req.params();
   
            String output = "";
       
            for (Todo todo : todos.values()) {
            	output += todo.toString() +"   With Id: "+ todo.getId() + "\n";
            }
            return output;
        });
        
 
    }
    
}
