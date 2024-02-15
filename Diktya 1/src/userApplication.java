import ithakimodem.*;         //some of these imports were not used in the end, they were added when I was trying to figure
							//out how to implement some parts of the code

import java.lang.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.io.FileOutputStream; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;

public class userApplication {
	public static void main(String[] param) {
		(new userApplication()).demo();
	}
	
	public void demo() {
		
		
		// Let's start by typing the codes...
		String E_code = "E6330";
		String M_code = "M6561";
		String G_code = "G4710";
		String P_code = "P1662";
		String Q_code = "Q6624";
		String R_code = "R6875";
		
		int k;
		Modem modem;
		modem=new Modem();
		modem.setSpeed(80000);
		modem.setTimeout(2000);
		modem.open("ithaki");
		for (;;) {
			try {
				k=modem.read();
				if (k==-1) break;
				System.out.print((char)k);
			} catch (Exception x) {
				break;
			}
		}
		
		
		String code_echo;
		ArrayList <Long> EchoTimeList = new ArrayList <>();     //ArrayList to add each loop's time and then use it to save these times in a text file
		
		long EchoCurrentTime = System.currentTimeMillis();      //this function gets the current time in the system, used for looping for 5 minutes
		 		
		
		long EchoTotalTime = 0;                                 //initializing the Total time of all loops 
		
		//using a do-while method to run the for loop for 5 minutes
		do {
			
			long EchoStartTime = System.currentTimeMillis();
			code_echo = E_code + "\r";                          //adding "\r" in the E code that I set in the beginning of the application 
			modem.write(code_echo.getBytes());					//Sending the code in the form of bytes to the virtual modem
			
		//In the for loop we set i from 0 to 35 because the chars of the message are 35
		for (int i = 0; i < 35; i++) {
            try {                       //using try-catch to catch exception
                k = modem.read(); // reading package from server
                if (k == -1) break;   //break if error 
                System.out.print((char) k); // print message, 1 character for each loop
            }
            catch (Exception x) {    // catch Exception and break
                break;
            }
        }
		
		
		
		long EchoEndTime = System.currentTimeMillis();      //getting the time in the end of the loop
		long EchoTimePassed =  EchoEndTime - EchoStartTime; //calculating the time that each loop has lasted
		EchoStartTime = System.currentTimeMillis();         //resetting the start time of the loop that sends the packages
		EchoTimeList.add(EchoTimePassed);					//adding each loop's time in an arraylist
		
		EchoTotalTime =System.currentTimeMillis() - EchoCurrentTime;         //calculating the total time from the moment the first loop started
		
		System.out.println("");     						// going to the next line
		}	
		while (EchoTotalTime < 300000); //EchoPachet loop runs for 5 minutes ( 5 * 60000 ms)
		
		
		
		 
		//Saving ECHO time info to a text file: 
        String long_to_string_EchoTime;         //used to convert the long variable to string type
      
        try {
        FileWriter writer_EchoTme = new FileWriter("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\EchoTime_text.txt");    //Setting the EchoTime_text saving path 
  		
  		for(long EchoTime_info : EchoTimeList ) {
  		 
  			long_to_string_EchoTime = String.valueOf(EchoTime_info);                //converting the long variable to string type
  			writer_EchoTme.write(long_to_string_EchoTime + System.lineSeparator()); //writing and going to the next line
  		}
  		writer_EchoTme.close();                     //closing the specific writer
  		}
  		catch(Exception e){System.out.println(e);}    
     System.out.println("EchoTime Success...");    //just print that everything works fine
		
		
		
		
		
		//Image without ERROR
		
		
		int im1;                                   //int variable to store the integers that Ithaki sends
		String image_request;                      //String for storing the code 
		image_request = M_code + "\r";             //add \r to the M code I set in the beginning
		modem.write(image_request.getBytes());     //sending the M code to Ithaki in the form of bytes
		ArrayList<Integer> image_noError_ints = new ArrayList<Integer>();               //ArrayList used later for creating the image
		for (;;) {                                 //infinite loop
			try {								   //using try-catch to catch exception
				
				 
						im1 = modem.read();		   //reading the information that Ithaki sends back
				image_noError_ints.add(im1);       //adding this information to the arraylist
				
				if (im1 == -1) break;              //break the loop if error
	
			
				}
			catch (Exception x) {
			break;
			}
		}
		
		
		byte[] imageBYTES = new byte[image_noError_ints.size()]; //creating a byte array that has the size of the arraylist the information is stored to 
		
		for (int j = 0; j < image_noError_ints.size(); j++) {    
			
			imageBYTES[j] =  image_noError_ints.get(j).byteValue(); //store the information bytes to the byte array
			}
		
		try{    
            FileOutputStream NoErrorOut= new FileOutputStream("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\NoError.jpg");  //setting the path in which the no error immage will be saved  
            NoErrorOut.write(imageBYTES);    							 //write the bytes to a jpg file
            NoErrorOut.close();    										 // close the writer
            System.out.println("\nNo error Image succesfully saved! ");  //just print a sign that everything works fine  
           }
			catch(Exception e)
				{
				System.out.println(e);
				    }    
		
		
		//Image with ERROR
		
		int im2;                                              //int variable for storing the Integer data ithaki sends
		String image_request2;							      
		image_request2 = G_code + "\r";
		modem.write(image_request2.getBytes());               //sending the bytes of the G code to Ithaki
		ArrayList<Integer> image_withError_ints = new ArrayList<Integer>();
		for (;;) {                                        //infinite loop
			try {
				
				 
						im2 = modem.read();               //reading the data that Ithaki sends back to me
				image_withError_ints.add(im2);            //adding these data to an arraylist 
				
				if (im2 == -1) break;
	
				//i++;
				}
			catch (Exception x) {
			break;
			}
		}
		
		//procedure for creating the jpg file from the data that Ithaki sent
		byte[] imageBYTES2 = new byte[image_withError_ints.size()];   //creating the byte array exactly the same way as in the "No error image" procedure 
		
		for (int j = 0; j < image_withError_ints.size(); j++) {       
			
			imageBYTES2[j] =  image_withError_ints.get(j).byteValue();
			}
		
		try{    
            FileOutputStream WithErrorOut= new FileOutputStream("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\With Error.jpg");  //setting the path in which the error immage will be saved  
            WithErrorOut.write(imageBYTES2);    
            WithErrorOut.close();    
            System.out.println("Image with error succesfully saved! ");  //just print a sign that everything works fine  
           }
			catch(Exception e)
				{
				System.out.println(e);
				    }
		
		modem.close();              //close the modem
		
		
		
		//GPS
				
		Modem gps_modem;                        
		gps_modem=new Modem();					//open a new modem with the name of gps_modem
		gps_modem.setSpeed(80000);              //setting its speed
		gps_modem.setTimeout(2000);				//its timeout
		gps_modem.open("ithaki");
		
		
		//Skipping welcome message:
		for (;;) {
			try {
				k=gps_modem.read();
				if (k==-1) break;
				
			} catch (Exception x) {
				break;
			}
		}
		
		
		String g = "";                       //blank String variable, used later for storing the int type data that Ithaki sends to this String... the int data will be type casted to char type data
		int gps1 = 0;
		String only_code = P_code;  
		String gps_request = only_code + "R=1003071\r" ;
		gps_modem.write(gps_request.getBytes());       //sending the bytes of P code to Ithaki
		
		
		for(;;) {        
			
			
			try {
				gps1 = gps_modem.read();                 //reading the data that Ithaki sent back
				System.out.print((char)gps1);            //printing the data just to ensure that everything works fine... the int data have been type casted to char type
				
			
			   
			
			
			if(gps1 == -1) {        //break if error
				break;
			}
			
			
		
			}
			
			catch (Exception x) {   //catch exception
				break;
			}
			
			g += String.valueOf((char)gps1);     //adding the char typecasted integers to the string variable g
		}
		
		System.out.println("\n" + g);            //print each character just to ensure that everything works fine
		
		String g1 =g.substring(27, 101);		 //substring the g string to obtain the coordinates of the 4  points that I want to save as jpgs 
		String g2 =g.substring(1167, 1241);	     // the index bounds have been calculated so as I can obtain the first 3 points with a timing difference of 15 seconds 	
		String g3 =g.substring(2307, 2381);		 // and the 4th one 30 seconds after the 3rd	
		String g4 =g.substring(4587, 4661);
		
		
		System.out.println("\n\n\n\n\n" + g1);   //printing the substrings of g string (4 points' coordinates) just to ensure that everything works fine
		System.out.println("\n\n\n\n\n" + g2);
		System.out.println("\n\n\n\n\n" + g3);		
		System.out.println("\n\n\n\n\n" + g4);
		
		String cord1_x = g1.substring(18, 27);   //separating the longitude and the latitude of each point
		String cord1_y = g1.substring(30, 40);
		
		String cord2_x = g2.substring(18, 27);
		String cord2_y = g2.substring(30, 40);
		
		String cord3_x = g3.substring(18, 27);
		String cord3_y = g3.substring(30, 40);
			
		String cord4_x = g4.substring(18, 27);
		String cord4_y = g4.substring(30, 40);
		
		
		System.out.println(cord1_x + " " + cord1_y);  //print to check that the separation has been done properly
		
		
		String int1x = cord1_x.substring(0, 4);       //Separating the decimal part and the integer part of each number...
		String dec1x = cord1_x.substring(5, 9);
		
		String int1y = cord1_y.substring(0, 5);
		String dec1y = cord1_y.substring(6, 10);
		
		String int2x = cord2_x.substring(0, 4);
		String dec2x = cord2_x.substring(5, 9);
		
		String int2y = cord2_y.substring(0, 5);
		String dec2y = cord2_y.substring(6, 10);
		
		String int3x = cord3_x.substring(0, 4);
		String dec3x = cord3_x.substring(5, 9);
		
		String int3y = cord3_y.substring(0, 5);
		String dec3y = cord3_y.substring(6, 10);		
				
		String int4x = cord4_x.substring(0, 4);
		String dec4x = cord4_x.substring(5, 9);
		
		String int4y = cord4_y.substring(0, 5);
		String dec4y = cord4_y.substring(6, 10);
		
		int x1=(int) (Integer.parseInt(dec1x) * 0.006);   //converting the string type number to integer type and multiplying it with 0.006 
		int y1=(int) (Integer.parseInt(dec1y) * 0.006);   
		
		int x2=(int) (Integer.parseInt(dec2x) * 0.006);
		int y2=(int) (Integer.parseInt(dec2y) * 0.006);
		
		int x3=(int) (Integer.parseInt(dec3x) * 0.006);
		int y3=(int) (Integer.parseInt(dec3y) * 0.006);
			
		int x4=(int) (Integer.parseInt(dec4x) * 0.006);
		int y4=(int) (Integer.parseInt(dec4y) * 0.006);
		
		String newdecx1 = String.valueOf(x1);             //saving these numbers to a new string               
		String newdecy1 = String.valueOf(y1);
		String newdecx2 = String.valueOf(x2);
		String newdecy2 = String.valueOf(y2);
		String newdecx3 = String.valueOf(x3);
		String newdecy3 = String.valueOf(y3);
		String newdecx4 = String.valueOf(x4);
		String newdecy4 = String.valueOf(y4);
		
		int1x += newdecx1;                               //adding the new string to the integer part of the longitude and latitude
		int1y += newdecy1;
		int2x += newdecx2;
		int2y += newdecy2;
		int3x += newdecx3;
		int3y += newdecy3;	
		int4x += newdecx4;
		int4y += newdecy4;
		
		System.out.println(newdecy1 + "   " + int1x);  //just print these to ensure that everything works fine
		System.out.println(newdecy2 + "   " + int2x);
		System.out.println(newdecy3 + "   " + int3x);
		System.out.println(newdecy4 + "   " + int4x);
		
		//time to create the new code with the longitude and latitude of the 4 symbols
		String newCode = only_code + "T=" + "2257" + newdecy1 + int1x + "T=" + "2257" + newdecy2 + int2x + "T=" + "2257" + newdecy3 + int3x +  "T=" + "2257" + newdecy4 + int4x + "\r" ; 
		gps_modem.write(newCode.getBytes());    //send this new code to Ithaki
		
		ArrayList<Integer> GPS_image = new ArrayList<Integer>();  //arraylist to store the data for the creation of the jpg file later
		
		for(;;) {
			try {
			int gps_image = gps_modem.read();             //Ithaki responds
			GPS_image.add(gps_image);					  //store the data to the arraylist
			
			if (gps_image == -1)  {                        //break if error
				break;
			}
		}
		catch (Exception x) {
		break;
		}
			
		}
		
		//Saving the GPS jpg image procedure 
		byte[] GPS_Image = new byte[GPS_image.size()];  //byte array for saving the bytes of the data that Ithaki sent
		
		for (int j = 0; j < GPS_image.size(); j++) {    
			
			GPS_Image[j] =  GPS_image.get(j).byteValue();  //storing the bytes
			}
		//create the jpg file
		try{    
            FileOutputStream newimage= new FileOutputStream("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\GPS_image.jpg");  //setting the path in which the gps immage will be saved  
            newimage.write(GPS_Image);   //writing the data to jpg  
            newimage.close();     //closing the writer
            System.out.println("GPS Image succesfully saved! ");  //just print a sign that everything works fine  
           }
			catch(Exception e)            //catch exception
				{ 
				System.out.println(e);
				    }
		
	
		gps_modem.close();       //close the gps modem
		
		
		 //arq part

    
            Modem arq_modem;
            
            //creating a new modem (arq_modem), setting its time and Timeout
            arq_modem = new Modem();
            arq_modem.setSpeed(8000);
            arq_modem.setTimeout(2000);
            arq_modem.open("ithaki");

            String arq_request;

            arq_request = Q_code + "\r";       //adding \r to the Q code 

            arq_modem.write(arq_request.getBytes());     //sending the code to Ithaki
            
            //Skipping welcome message
           
             
            int d;
            String delimiter ="";
            
            //Skip Welcome Message
            for (;;) {

                try {

                    d = arq_modem.read();

                    if (d == -1) {
                    	break;
                    }
                    delimiter = delimiter + (char)d;
                    if (delimiter.indexOf("\r\n\n\n") > -1){                    
                    	break;                 
                    	}

                }
                catch (Exception x) {

                    break;
                }

            }
           
            //Initializing F, C and S
              int F = 0;
              int C = 0;
              int S = 0;
              int FCS = 0;   // The fcs variable, which will take its value from the combination of F, C and S
            int xor = 0;     //initializing the integer variable xor
            int counter = 1; // counting how many tries does it take for receiving a correct package 
            ArrayList <Long> TimeTillSuccess = new ArrayList <>();       //ArrayList in which we save the time till receiving a correct package
            ArrayList <Integer> TriesTillSuccess = new ArrayList<>();     //ArrayList in which we save the tries till receiving a correct package
            long CurrentTime = System.currentTimeMillis();                //getting the current time of the system
            long StartTime = CurrentTime;								// saving this same time to another variable, as I want them both in order to make this part of the code work
            long TotalTime = 0;           //initializing the totaltime of the loops to 0
            
            int ACK_counter =0;                  //Here we set counters for ack and nack in order to use them in the "BER" calculation
            int NACK_counter = 0;
            ArrayList <Integer> ACK_NACK = new ArrayList<>();  //An array list, which I will use to save ACK_counter and NACK_counter to a text file, in order to use them in the BER calculation 
            
            do {
                //Below, we set j <= 58, because, in the received message PSTART DD-MM-YYYY HH-MM-SS PC <XXXXXXXXXXXXXXXX> FCS PSTOP, the chars are 58
             	for (int i = 1; i <= 58; i++) {
                    try {                        //try-catch method again...

                        d = arq_modem.read();

                        if (d == -1) {       //break and type "error" if error occurs

                        System.out.print("Error!");

                        break;

                        }

                         System.out.print((char) d); 
                         
                         if (i == 32) { //32 is the first char of the "XXXXX....."
                        	 xor = d;
                         }
                         
                         if (i > 32 && i < 48) {
                             xor = d ^ xor;   //this (^) is the exclusive-or ("xor") operator 
                         }
                         
                         if (i == 50) {       //Calculating F
                             F = d - 48;
                         }    

                         if (i == 51) {       //Calculating C
                             C = d - 48;
                         }

                         if (i == 52) {       //Calculating S
                             S = d - 48;
                         }

                    }
                    catch (Exception x) {     //catch the exception 

                            System.out.print("Exception");
                    break;
                    }

                
            }  //end of for loop
             	
             	long StopTime = System.currentTimeMillis();  //getting the time in the end of every loop
             	FCS = F * 100 + C * 10 + S;  // Calculating the FCS, using F, C and S multiplied with 100, 10 and 1 
             	
             	if ( xor == FCS) {                           //xor wasn't equal to FCS, so we are sending the Q code again. 
             		arq_request = Q_code + "\r";		     //add \r to the Q code
             		arq_modem.write(arq_request.getBytes());
             		long TimePassed = StopTime - StartTime;  //calculating the time that passed for a successful package 
             		TimeTillSuccess.add(TimePassed);         // saving the time that passed for each successful package
             		StartTime = System.currentTimeMillis();  //resetting the start time
             		TriesTillSuccess.add(counter);           // saving the tries needed for each successful package
             		counter = 1;							 // resetting the counter for the tries
             		ACK_counter++;							 //increasing the ack counter
             		
             		
             		
             	}
             	
             	else {
             		arq_request = R_code + "\r";    //xor wasn't equal to FCS, so we are sending the R code 
             		arq_modem.write(arq_request.getBytes()); //sending the R code to Ithaki
             		counter++;                  //this try wasn't successful, so we increase the counter that counts the tries till success
             		NACK_counter++;             //increasing the nack counter
             	}
             	
             	TotalTime = System.currentTimeMillis() - CurrentTime;
             	System.out.println(""); //going to the next line
            }
            while(TotalTime < 300000);     //loop runs for 5 mins ( 5 * 60.000 ms)
             	
            
           //Saving arq time info to a text file: 
           String long_to_string_time; 
         
           try {
           FileWriter writer_time = new FileWriter("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\ARQ_time_text.txt");    //Creating the writer and setting the time_text saving path 
     		
     		for(long time_info : TimeTillSuccess ) {
     		 
     			long_to_string_time = String.valueOf(time_info);
     			writer_time.write(long_to_string_time + System.lineSeparator()); //writing and going to the next line
     		}
     		writer_time.close(); //close the writer
     		}
     		catch(Exception e){System.out.println(e);}    
        System.out.println("Time Success...");
        
        
        
        
      //Saving tries info to a text file: 
        String long_to_string_tries; 
      
        try {
        FileWriter writer_tries = new FileWriter("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\ARQ_tries_text.txt");  //Setting the tries_text saving path 
  		
  		for(long tries_info : TriesTillSuccess ) {
  		 
  			long_to_string_tries = String.valueOf(tries_info);                 //converting the long type numbers to string type, in order to print them
  			writer_tries.write(long_to_string_tries + System.lineSeparator()); //writing and going to the next line
  		}
  		writer_tries.close();  //close the writer
  		}
  		catch(Exception e){System.out.println(e);}    
     System.out.println("Tries Success...");            //Just print that everything is fine...
            
     
     //Saving ACK and NACK to a text, in order to use them for the BER calculation
     
     String int_to_string_ACK_NACK; 
     ACK_NACK.add(ACK_counter);      //adding ACK and NACK to the arraylist
     ACK_NACK.add(NACK_counter);
     try {
     FileWriter writer_ACK_NACK = new FileWriter("C:\\Users\\George Nomikos\\Desktop\\Diktya Ergasia\\Results\\ACK_NACK_text.txt");  //Setting the ACK_NACK saving path 
		
		for(int Ack_Nack_info : ACK_NACK ) {
		 
			int_to_string_ACK_NACK = String.valueOf(Ack_Nack_info);
			 writer_ACK_NACK.write(int_to_string_ACK_NACK + System.lineSeparator()); //writing and going to the next line
		}
		 writer_ACK_NACK.close();   //close the writer
		}
		catch(Exception e){System.out.println(e);}    
  System.out.println("ACK_NACK save Success...");
    
     
     
     
     
     arq_modem.close();    //close the arq_modem
           
     
     
       
		
		
	

// NOTE : Break endless loop by catching sequence "\r\n\n\n".
// NOTE : Stop program execution when "NO CARRIER" is detected.
// NOTE : A time-out option will enhance program behavior.
// NOTE : Continue with further Java code.
// NOTE : Enjoy :)                                  - I Definitely did :)
   	
	
   }
}  //close userApplication class

