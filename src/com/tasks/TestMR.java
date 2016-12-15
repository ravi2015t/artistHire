package com.tasks;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.models.Rating;


public class TestMR  {

	static ArrayList<Rating> photographer = new ArrayList<Rating>();
	ArrayList<Rating> florist = new ArrayList<Rating>();
	ArrayList<Rating> mehendiArtist = new ArrayList<Rating>();
	
    public static void main(String[] args) {

   Rating rating = new Rating();
   rating.setPrice((long) 15000);
   rating.setRating(3);  
   photographer.add(rating);
   
   Rating rating2 = new Rating();
   
   rating2.setPrice((long)19000);
   rating2.setRating(3.9);
   photographer.add(rating2);
   
   Rating rating3 = new Rating();
   
   rating3.setPrice((long)18000);
   rating3.setRating(4.0);
   photographer.add(rating3);
   
   Rating rating4 = new Rating();
   
   rating4.setPrice((long)16000);
   rating4.setRating(4.4);
   photographer.add(rating4);
   
   Rating rating5 = new Rating();
   
   rating5.setPrice((long)17000);
   rating5.setRating(4.1);
   photographer.add(rating5);
  
   Rating rating6 = new Rating();
   
   rating6.setPrice((long)20000);
   rating6.setRating(4.4);
   photographer.add(rating6);
   
   
   Collections.sort(photographer, new Comparator<Rating>() {
	    @Override
		public int compare(Rating cr1,Rating cr2) {
			
			 return  Double.compare(cr2.getRating(), cr1.getRating());
		}
		
	});
   
   for( Rating rating1: photographer)
   {
	   System.out.println(rating1.getRating());
   }
   }
}