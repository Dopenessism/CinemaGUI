/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author 
 */
public class DataControl {
private ArrayList users = new ArrayList();
private ArrayList movieScreening = new ArrayList();
private ArrayList allTickets = new ArrayList();
private int Total;
private int Available;
private String connectionString = "jdbc:mysql://localhost:3306/kino";
private String user = "Case";
private String password = "Esac";
     public DataControl(){
      try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT * FROM tbllogin");
        while(rs.next())
        {
            
            users.add(rs.getString("l_brukernavn"));

            users.add(rs.getString("l_pinkode"));

            users.add(rs.getString("l_erPlanlegger"));

        }
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    }
    public ArrayList getusers(){
    return users;
    }
    public ArrayList getmovies(){
    try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT tblvisning.v_kinosalnr,tblvisning.v_visningnr, tblfilm.f_filmnavn, tblkinosal.k_kinonavn, tblvisning.v_dato, tblvisning.v_starttid, tblvisning.v_pris FROM tblvisning INNER JOIN tblfilm ON tblvisning.v_filmnr=tblfilm.f_filmnr INNER JOIN tblkinosal ON tblvisning.v_kinosalnr=tblkinosal.k_kinosalnr WHERE tblvisning.v_dato>=CURDATE()");
        while(rs.next())
        {
            
            movieScreening.add(rs.getString("v_visningnr"));
            movieScreening.add(rs.getString("f_filmnavn"));
            movieScreening.add(rs.getString("k_kinonavn"));
            movieScreening.add(rs.getString("v_dato"));
            movieScreening.add(rs.getString("v_starttid"));
            movieScreening.add(rs.getString("v_pris"));
            movieScreening.add(rs.getString("v_kinosalnr"));
            

        }
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
        return movieScreening;
    }
    public ArrayList getAvailableSeats(String hallNo, String screenNo){
    ArrayList availableSeats = new ArrayList();
    ArrayList allSeats = new ArrayList();
    
    ArrayList bookedSeats = new ArrayList();
       try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT * FROM tblplass WHERE p_kinosalnr='"+hallNo+"'");
        while(rs.next())
        {
            
            allSeats.add(rs.getString("p_radnr"));
            
            allSeats.add(rs.getString("p_setenr"));
            
        }
        rs = stmt.executeQuery("SELECT b_billettkode FROM tblbillett WHERE b_visningsnr='"+screenNo+"'");
        while(rs.next())
        {
            
            allTickets.add(rs.getString("b_billettkode"));


        }
        for(int i = 0; i< allTickets.size(); i++)
        {
           rs = stmt.executeQuery("SELECT * FROM tblplassbillett WHERE pb_billettkode='"+(String)allTickets.get(i)+"'");
            
           while(rs.next())
            {
                
                bookedSeats.add(rs.getString("pb_radnr"));
                bookedSeats.add(rs.getString("pb_setenr"));

            } 
        }
           
        int row = 0;
        int seat = 1;
        int rowa = 0;
        int seata = 1;
        boolean condition = false;
        for(int i=0; i<allSeats.size()/2; i++)
        {
            for(int j=0; j< bookedSeats.size()/2;j++)
            {
                if(bookedSeats.get(row).equals(allSeats.get(rowa))&&bookedSeats.get(seat).equals(allSeats.get(seata)))
                {
                    condition = true;
                    break;
                }

                row += 2;
                seat += 2;
            }
            if(condition){
                condition = false;
            }
            else{
                availableSeats.add(allSeats.get(rowa));
                availableSeats.add(allSeats.get(seata));
            }
            row = 0;
            seat = 1;
            seata += 2;
            rowa += 2;
        }
        
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      } 
       Available = availableSeats.size()/2;
       Total = allSeats.size()/2;
    return availableSeats;
    }
    public int getAvailableSeatsNumber(){
     return Available;
    }
    public int getTotalSeatsNumber(){
     return Total;
    }
    public void generateTicket(String hallNo, String screenNo, String Row, String Seat){
        Random rand = new Random();
        int char1 = 0;
        int char2 = 0;
        int char3 = 0;
        int char4 = 0;
        int char5 = 0;
        int char6 = 0;
        String ticketNumber = "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String nums = "1234567890";
        boolean flag = true;
        boolean posFlag = false;
        while(flag){
            char1 = rand.nextInt((51 - 0) + 1) + 0;
            char2 = rand.nextInt((51 - 0) + 1) + 0;
            char3 = rand.nextInt((51 - 0) + 1) + 0;
            char4 = rand.nextInt((51 - 0) + 1) + 0;
            char5 = rand.nextInt((9 - 0) + 1) + 0;
            char6 = rand.nextInt((9 - 0) + 1) + 0;
            ticketNumber = ""+chars.charAt(char1);
            ticketNumber += chars.charAt(char2);
            ticketNumber += chars.charAt(char3);
            ticketNumber += chars.charAt(char4);
            ticketNumber += nums.charAt(char5);
            ticketNumber += nums.charAt(char6);
            if(allTickets.size()==0){
             flag = false;
            }
            for(int i=0; i< allTickets.size();i++){
                if(ticketNumber.equals(allTickets.get(i))){
                   posFlag = true;
                }
            }
            if(!posFlag){
                flag = false;
            }
            
        }
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
        
        
        try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        stmt.executeUpdate("INSERT INTO tblbillett VALUES ('"+ticketNumber+"','"+screenNo+"','0')");
        stmt.executeUpdate("INSERT INTO tblplassbillett VALUES ('"+Row+"','"+Seat+"','"+hallNo+"','"+ticketNumber+"')");
        JOptionPane.showMessageDialog(null,"Your Ticket Is Booked Successfully\nTicket No:"+ticketNumber+"\nRow No:"+Row+"\nSeat No:"+Seat);
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    }
    public void collectTicket(String ticket){
        try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        int check = stmt.executeUpdate("UPDATE tblbillett SET b_erBetalt=1 WHERE b_billettkode='"+ticket+"'");   
            if (check==1) {
                JOptionPane.showMessageDialog(null,"Your Ticket Is Paid Successfully\nTicket No:"+ticket);
            }
            else{
                JOptionPane.showMessageDialog(null,"Your Ticket Number Is Not Correct");
            }
        
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    }
    public void deleteAllNonPaid(){
        try{
        Class.forName("com.mysql.jdbc.Driver");
        ArrayList allnonpaid = new ArrayList();
        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT * FROM tblbillett WHERE b_erBetalt=0");
        while(rs.next())
        {
            
            allnonpaid.add(rs.getString("b_billettkode"));

        }
        int check = stmt.executeUpdate("DELETE tblbillett, tblplassbillett FROM tblbillett INNER JOIN tblplassbillett ON tblbillett.b_billettkode = tblplassbillett.pb_billettkode WHERE tblbillett.b_erBetalt=0");   
           JOptionPane.showMessageDialog(null,"Unpaid tickets deleted successfully");
          if(allnonpaid.size()>0){
              BufferedWriter bw = null;
           try {
         // APPEND MODE SET HERE
         bw = new BufferedWriter(new FileWriter("delete.dat", true));
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
//	System.out.println(dateFormat.format(date));
	 bw.write("Time Stamp: "+dateFormat.format(date)+" Number of Records Deleted "+" Tickets "+allnonpaid);
	 bw.newLine();
	 bw.flush();
      } catch (IOException ioe) {
	 ioe.printStackTrace();
      } finally {                       // always close the file
	 if (bw != null) try {
	    bw.close();
	 } catch (IOException ioe2) {
	    // just ignore it
	 }
      }
          }
           

        
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    }
    public void addNewFilm(String Name){
        try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        int check = stmt.executeUpdate("INSERT INTO tblfilm(`f_filmnavn`) VALUES ('"+Name+"')");   
            
                JOptionPane.showMessageDialog(null,"Your Film Successfully Added:\nFilm Name: "+Name);
            
        
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    }
    public ArrayList getHallNames(){
    ArrayList hallname = new ArrayList();
    try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT k_kinonavn FROM tblkinosal");
        while(rs.next())
        {
            
            hallname.add(rs.getString("k_kinonavn"));
        }
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    return hallname;
    }
    public ArrayList getMovieNames(){
    ArrayList movieName = new ArrayList();
    try{
        Class.forName("com.mysql.jdbc.Driver");

        Connection con=DriverManager.getConnection(connectionString, user, password);
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT f_filmnavn FROM tblfilm");
        while(rs.next())
        {
            
            movieName.add(rs.getString("f_filmnavn"));
        }
        con.close();

        }
      catch(Exception e)
      { 
          System.out.println(e);
      }
    return movieName;    
    }
}
