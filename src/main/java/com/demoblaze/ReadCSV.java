package com.demoblaze;

import java.io.File;
import java.util.Scanner;

public class ReadCSV {
    public String[] Read(String args) throws Exception
    {
        String[] Arr1=new String[7];
        int counter=0;
        //parsing a CSV file into Scanner class constructor
        StringBuilder Get= new StringBuilder();
        Scanner sc = new Scanner(new File(args));
        sc.useDelimiter(",");   //sets the delimiter pattern
        while (sc.hasNext())  //returns a boolean value
        {
            //Get.append(sc.next());  //find and returns the next complete token from this scanner
            Arr1[counter]=sc.next();
            counter++;

        }
        sc.close();  //closes the scanner
        return Arr1;
    }



}
