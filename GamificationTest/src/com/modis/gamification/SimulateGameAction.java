package com.modis.gamification;


import java.io.FileReader;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.basic.api.ExecutionControllerApi;
import it.smartcommunitylab.basic.api.PlayerControllerApi;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.model.ext.ExecutionDataDTO;
import it.smartcommunitylab.model.ext.GameConcept;
import it.smartcommunitylab.model.ext.PointConcept;
import it.smartcommunitylab.basic.api.PlayerControllerApi;





public class SimulateGameAction {
	 private static ApiClient client;
	 //private static final Logger logger = LogManager.getLogger(SubmitCodeController.class);

	  public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	
		try {
	       
	       
	     // GAME ID of the game to simulate  
		 String gameID = "61e1493d08813b000102ea25";
		 // ID of the player that executes a game action
		 String playerID = "ID1";
		 // Game Action executed by the player
		 String actionType = "NewAction";
		 
		 
		 // data in input
	     JSONParser parser = new JSONParser();
	      //Use JSONObject for simple JSON and JSONArray for array of JSON.
	     JSONObject data = (JSONObject) parser.parse(
	     new FileReader("./jsons/data.json"));//path to the JSON file.
		  
	     
	     String json = data.toJSONString();
	     //System.out.println("json: "+json + "type: " + json.getClass());
	        JSONObject json1 = (JSONObject)parser.parse(json);
	   
	       
	        JSONObject data1 = (JSONObject) json1.get("data");
	      
	        
	        
	        Map solutionMap = ((Map)data1);
	        System.out.println("SolutionMap: " + solutionMap+ " type: " + solutionMap.getClass());
		

	        Action action = new Action();
	        action.setGameId(gameID);
	        action.setPlayerId(playerID);
	        action.setId(actionType);
	        action.setParams(new HashMap<>());
	        action.getParams().put("gameID", gameID);
	        action.getParams().put("playerID", playerID);
	        action.getParams().put("solution", solutionMap);

	        
	        // CONNECTION AT THE GAMIFICATION ENGINE
	
	        
	        String GEUrl = "https://dev.smartcommunitylab.it/gamification-v3/";
	        String username = "papyrus";
	        String password ="papyrus0704!" ;
	    	
	    	

	    	 client = new ApiClient(GEUrl);
	         client.setUsername(username);
	         client.setPassword(password);
	         // execute player Action
	         ExecuteAction(action);
	        
	         // retrieve player State
	         PlayerStateDTO state = null;
	         try {
	      
	        	 state = new PlayerControllerApi(client).readStateUsingGET(gameID, playerID);
	             String level = (String) state.getCustomData().get("level");

	             System.out.println("livello del giocatore: "+level);
	      
	         } catch (ApiException | IOException e) {
	             //logger.error("Exception calling gamification-engine API");
	             throw new Exception(e);
	         }
	         
	       
	       // System.out.println(ClassStructureMetrics);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}

	
	public static void ExecuteAction(Action action) throws Exception {
	        ExecutionDataDTO executionData = new ExecutionDataDTO();
	        executionData.setGameId(action.getGameId());
	        executionData.setData(action.getParams());
	        executionData.setPlayerId(action.getPlayerId());
	        System.out.println("Executing Action....");
	        executionData.setExecutionMoment(new Date());
	        try {
	            new ExecutionControllerApi(client).executeActionUsingPOST(action.getGameId(),
	                    "PinAnswerCompleted", executionData);
	        } catch (ApiException e) {
	           
	           throw new Exception(e);
	        }
	}

}



