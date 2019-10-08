/*NAME:MEGHANA KIRAN 
   PROJECT: OS Phase II */

/*
     SCHEDULER CLASS  
     
     IT CONTAINS READFILE METHOD THAT READS THE INPUT FILE AND CREATES PCB FOR  JOBS AND 
     STORES EACH JOB AND ITS PCB OBJ IN HASH MAP
     */

import java.util.*;
import java.io.*;

public class SCHEDULER {
	
  public static String line,k;
  public static ArrayList<String> temp=new ArrayList<String>();
  public static ArrayList<String> returnarraylist =new ArrayList<String>();	
 
  public static int JOBID=1;
  public static int jflag=0;
  public static int jobflag=0;
  public static int lengthflag=0;
  public static int dataflag=0;
  public static int endflag=0;
  public static int starttcflag=0;
  public static int linespace=0;
  public static int loaderflag=0,datavalueflag=0;
  //public static String numberOfDataLines,numberOfOutputLines,length,totalnumber;
  static HashMap<Integer,PCB> pcb = new HashMap<Integer,PCB>();
  
//used to read a file
public static ArrayList readfile(String filesent){
		
	//System.out.println("entered the function readfile");
try{
FileReader fileReader = new FileReader(filesent);
BufferedReader bufferedReader = new BufferedReader(fileReader);
			
 while((line= bufferedReader.readLine())!=null)
 {   
	 returnarraylist.add(line/*arr[x]*/);
	 		
}
	 
}
	catch(Exception e)
		 {
			  e.printStackTrace();
		 }
		 finally 
		 {
			 
		 }
	return returnarraylist;
}  
// closing for readfile method 

 public static HashMap<Integer, PCB> checkloaderFormat(ArrayList<String> temp){


    ArrayList<String> check= new ArrayList<String>();
    check=temp;
    PCB block = new PCB();
  for(int i=0;i<check.size();i++){


	  String k=check.get(i);
	  
	  if(k.contains("** JOB")){
  if(check.get(i).matches("\\*\\*\\s[J][O][B]\\s\\p{XDigit}{1,3}\\s\\p{XDigit}{1,3}")){
	  String s[]=k.split(" ");
	  
	  
	  block.JobId=JOBID;
	 
	  //System.out.println("JOBID="+JOBID);
	  jobflag=1;
	  //System.out.println("jobflag="+jobflag);

	  jflag=1;
	  block.numberOfDataLines =s[2];
      block.numberOfOutputLines =s[3];
      //block.totalnumber = Integer.toString((Integer.parseInt(block.numberOfDataLines)+ Integer.parseInt(block.numberOfOutputLines)));
   
      //=(Integer.parseInt(numberOfDataLines)+ Integer.parseInt(numberOfOutputLines))
  }
	 
  }
	  
  else if(i>=1 && check.get(i-1).matches("\\*\\*\\s[J][O][B]\\s\\p{XDigit}{1,3}\\s\\p{XDigit}{1,3}") && !check.get(i).matches("\\*\\*\\s[E][N][D]"))
  {
  if(check.get(i).matches("\\p{XDigit}+") && jflag==1 ){
  block.length=k;
  jflag=0;
  lengthflag=1;
  //System.out.println("lengthflag="+lengthflag);
		  
	  }
  }
	  
  else if(k.matches("\\p{XDigit}{3,12}") && !check.get(i+1).matches("\\*\\*\\s[D][A][T][A]") && starttcflag!=1 && dataflag!=1){

  if((k.length())%3==0 && dataflag==0 && starttcflag==0){
		block.loaderAdd.add(k);
		loaderflag=1;  
		//System.out.println("loaderflag="+loaderflag);
	
	
  }
  else{
	  block.errorMessage="[E:122] INVALID LOADER FORMAT:MORE THAN 12 BITS IN A LINE";
	  }
  }
	  
else if( k.matches("\\p{XDigit}{3}\\s[0|1]") /*&& check.get(i+1).matches("\\*\\*\\s[D][A][T][A]")*/){
  String p[]=k.split(" ");
  block.startAdd = p[0];
  block.traceswitch = p[1];
  starttcflag=1;
  //System.out.println("starttcflag="+starttcflag);
  }
	  
	  
else if(k.contains("** DATA")){
	  dataflag=1;
	  //System.out.println("dataflag="+dataflag);
		  
	  }
else if(dataflag==1 /*&& !"0".equals(block.numberOfDataLines) */&& !k.equals("** END") && !k.equals("") && endflag!=1)
{
	 /* if(k.matches("\\p{XDigit}")){*/
		 block.data.add(k);
		 datavalueflag=1;
		// System.out.println("datavalueflag="+datavalueflag);
	  /*}*/
  }
		  
else if(dataflag!=1 && starttcflag==1 && !k.equals("** END") && endflag!=1){
/*if(k.matches("\\p{XDigit}")){*/
	block.data.add(k);
    datavalueflag=1;
    //System.out.println("datavalueflag="+datavalueflag);  
  /*}*/
  }
		  
  
	  
  else if(k.contains("** END")){
	  endflag=1;
	 // System.out.println("endflag="+endflag);
		 
	  }
	  
  else if(k.contains("") && endflag==1 ){
 /* if(check.get(i+1).matches("\\*\\*\\s[J][O][B]\\s\\p{XDigit}{1,3}\\s\\p{XDigit}{1,3}")){*/
		  linespace=1;
		  //System.out.println("linespace="+linespace);
   if(jobflag==1 && lengthflag==1 && starttcflag==1 && dataflag==1 
		   && endflag==1 && linespace==1 && loaderflag==1 && datavalueflag==1)
	{
  
           
   
   pcb.put(JOBID,block); 
   JOBID++;
   PCB block1 = new PCB();
   block = block1;
   reset();
   				  
						  
  }
else if(loaderflag!=1 && jobflag==1 && lengthflag==1 && dataflag==1 && endflag==1){
	 block.errorMessage="[E:128]LOADER FORMAT MISSING";
	 pcb.put(JOBID,block); 
	 JOBID++;
	 PCB block1 = new PCB();
       block = block1;
       reset();
	
}

else if(jobflag!=1){
	  // **JOB MISSIING 
	 block.JobId=JOBID;
	block.errorMessage="[E:132] **JOB FOR PROOGRAM MISSING ";
	pcb.put(JOBID,block); 
	JOBID++;
	PCB block1 = new PCB();
       block = block1;
	reset();
  }
else if(dataflag!=1 && starttcflag==1  && datavalueflag==1){
	 block.errorMessage="[E:131] **DATA FOR PROOGRAM MISSING ";
	pcb.put(JOBID,block);  
	JOBID++;
       PCB block1 = new PCB();
       block = block1;
	reset();
}
else{
	//NULL JOB 
    block.errorMessage="[E:130] NULL JOB";
	pcb.put(JOBID,block);  
	JOBID++;
	PCB block1 = new PCB();
       block = block1;
	reset();
	
    }
  }
	  
  else{

  if((jobflag!=1 || lengthflag!=1 || starttcflag!=1 || dataflag!=1 || 
    		   endflag!=1 || linespace!=1 || loaderflag!=1)){
   if((!check.get(i).matches("\\*\\*\\s[J][O][B]\\s\\p{XDigit}{1,3}\\s\\p{XDigit}{1,3}")&& jobflag!=1)){
   block.JobId=JOBID;
 // block.JobId++;
  block.errorMessage="[E:132] **JOB FOR PROOGRAM MISSING ";
   
	   }
   else if(dataflag!=1 /*&& !"0".equals(numberOfDataLines)*/ /*&& k.matches("\\p{XDigit}"*/){
   //missing **DATA
   block.errorMessage="[E:131] **DATA FOR PROOGRAM MISSING ";
   }
   
   else if(endflag!=1 ){
	   //missing END
   block.errorMessage="[E:133] **END FOR PROOGRAM MISSING ";
   pcb.put(JOBID,block); 
   JOBID++;
   PCB block1 = new PCB();
   block = block1;
   /*JOBID++;*/
		   reset();
	    	   }
	       }
			  
			  
		  }
  } 
	  
    
	return pcb;
	  
	
	  }   
		  


public static void reset(){
	   jobflag=0;
	   lengthflag=0;
	   starttcflag=0;
	   dataflag=0;
	   endflag=0;
	   linespace=0;
	   loaderflag=0;
	   datavalueflag=0;
	   
	
}

} //closing for class 
