package com.tasks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TestGson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String password = "sdfsd";
		System.out.println(password);
		String response = "{ \"password\" : \"sdfsd\" }";
		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);    
		String result = jobj.get("password").toString();
		System.out.println("RESULT" + result);
		if(result.equalsIgnoreCase("\""+password+"\""))
		{
			System.out.println("true");
		}

	}

}
