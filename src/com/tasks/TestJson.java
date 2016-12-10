/*package com.tasks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.models.User;

public class TestJson {

	public static void main(String[] args) {
		try {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "{'name' : 'mkyong'}";
		Reader reader = new FileReader("C:\\Users\\tejco\\Documents\\GitHub\\artistHire\\src\\user.json");
		Gson gson = new Gson();
		User response = gson.fromJson(reader, User.class);
		//JSON from file to Object
		
			System.out.println("firstName" + response.getFirstname());
			System.out.println(response.getAddress().getCity());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
*/