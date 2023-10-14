package com.demoblaze;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class CSVGEN {
   //utility class
    public void Gen1() {
       try {


          String[] Names = {"Hassan1", "Mahmoud1", "Taqy1", "Saleem1", "Noura1", "Amaan1"};
          String[] Passes = {"123456789", "234144456", "123423456", "123423412", "564325643", "123242324"};
          String Pass;
          String Name;
          String BankCard = "";
          String Month;
          String Year;
          String[] Countries = {"Egypt", "England", "Oman"};
          String Country;
          String City;
          String[] Cities = {"Cairo", "London", "Muscat"};

          PrintWriter Pw = new PrintWriter(new File("./FilesAndDrivers/PersonalData.csv"));
          StringBuilder NewCSV = new StringBuilder();

          int RandomConst = new Random().nextInt(0, 3);
          Name = Names[new Random().nextInt(0, 6)];
          NewCSV.append(Name);
          NewCSV.append(",");
          //====================================================
          Pass = Passes[new Random().nextInt(0, 6)];
          NewCSV.append(Pass);
          NewCSV.append(",");
          //====================================================
          for (int i = 0; i <= 15; i++) {
             BankCard += String.valueOf(new Random().nextInt(10));
          }
          NewCSV.append(BankCard);
          NewCSV.append(",");
          //====================================================
          Country = Countries[RandomConst];
          NewCSV.append(Country);
          NewCSV.append(",");
          //====================================================
          City = Cities[RandomConst];
          NewCSV.append(City);
          NewCSV.append(",");
          //====================================================
          Month = String.valueOf(new Random().nextInt(1, 13));
          NewCSV.append(Month);
          NewCSV.append(",");
          //====================================================
          Year = String.valueOf(new Random().nextInt(1900, 2024));
          NewCSV.append(Year);
          //====================================================
          Pw.write(NewCSV.toString());
          Pw.close();
          System.out.println("finished");

//            System.out.println(Name);
//            System.out.println(Pass);
//            System.out.println(BankCard);
//            System.out.println(Country);
//            System.out.println(City);
//            System.out.println(Month);
//            System.out.println(Year);
       } catch (Exception ignored) {
       }

    }
}
