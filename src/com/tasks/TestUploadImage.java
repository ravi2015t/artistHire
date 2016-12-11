package com.tasks;

import java.io.File;
import java.io.IOException;

public class TestUploadImage {

	public static void main(String[] args) {
		File file = new File("ravi/username/tui.png");
	File file1 = null;
		try {
			file1 = File.createTempFile(file.getName(), "");
			System.out.println(file1.getName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		/*String simpleFileName = file.getName();
		String fileName = "profile"+ file.separator+"ravi"+ file.separator+"tej.png";
		String[] split = fileName.split(file.separator);
		*/
		
		
	}

}
