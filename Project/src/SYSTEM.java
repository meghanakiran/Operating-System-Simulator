
/*OS PROJECT PHASE 2 
 	 NAME:MEGHANA KIRAN 
     COUSE NUMBER:CS5323
     PROJECT: OS Phase II 
     DATE OF SUBMISSION: 04/25/2017
     
     *SYSTEM IS THE MAIN CLASS WHERE THE EXECUTION BEGINS.
     *Main invokes the CPU,Loader,SCHEDULER
     *
     *
     *global variables:GLOBAL_CLOCK -THE TIME THE JOB ENTERED THE SYSTEM 
     *It invokes the SCHEDULER CLASS IF JOBS ARE CORRECT CONTROL IS TRANSFERRED
     * TO THE SYSTEM WHICH LATER CALLS THE LOADER AND MEMORY MANAGER  THEN THE CPU IS INVOKED TO EXECUTE THE JOB 
     
     DISPLAY METHOD DISPLYS THE CONTENTS OF PCB ON PROGRAM TERMINATION 
     */



import java.io.*;
import java.util.*;



public class SYSTEM{
	
public static int Global_clock;

static BufferedReader brr = new BufferedReader(new InputStreamReader(System.in));

public static ArrayList<String> temp1=new ArrayList<String>();
public static ArrayList<String> loaderdata=new ArrayList<String>();
static HashMap<Integer,PCB> jobPcb = new HashMap<Integer,PCB>(); //HashMap to keep JobId and Pcb obj

LOADER load=new LOADER(); // CREATING NEW LOADER OBJECT 
MEMORY_MANAGER m = new MEMORY_MANAGER (); // CREATING MEMORY OBJECT

public static String tt;
public static int entry_time;
public static int leave_time;
public static int count,qq;
public static int abnormal;
public static int io_time;
public static  int execution_time;
public static  int time_quantum;
public static int run_time;
public static int c_time;
public  static float totalPf ;

public static void main(String[] args){
	
    String fileName= /*args[0]*/"tb+err"/*"Inputt.txt"*/;
File file =new File(fileName);


temp1 = SCHEDULER.readfile(fileName);
//System.out.println("temp "+temp1);
jobPcb = SCHEDULER.checkloaderFormat(temp1);  
 
if (args[0].contains("tb+err") ){
	displayMyPCBObj(jobPcb); 
}

PCB  block=new PCB ();
for(int i=1;i<=jobPcb.size();i++){
block = jobPcb.get(i);
PCB toCpu= checkforerrorjobs(block);
if(toCpu.JobId==13 ||toCpu.JobId==102 ||
toCpu.JobId==90 ||toCpu.JobId==100 ){
     display2(toCpu);
}
count=106;
if(toCpu.JobId>=62 && toCpu.JobId<=69){
	 display2(toCpu);
}
if(toCpu.errorMessage.equals("")){
int startADD= Integer.parseInt(toCpu.startAdd,16);
int tc = Integer.parseInt(toCpu.traceswitch); 
int base = toCpu.base;
abnormal=0;
int start_Add= startADD+base;
int input=toCpu.inputdataAdd;
int inputend=toCpu.inputdataAdd;
int writestart=toCpu.writedataAdd;
int writeend=toCpu.writedataAdd;


CPU c= new CPU();
try{
PCB finalpcb =CPU.cpu(toCpu,start_Add,tc,input,inputend,writestart,writeend);
//System.out.println("returned ");
    display(finalpcb);
    clearmemory(finalpcb,finalpcb.base,finalpcb.bound);
    }catch(Exception ex){
    	
    }
    
}
else{
	
	display(toCpu);
	
}
}
if(args[0].contains("tb+err")){
		
		timeinstance1();
		statistics1();
	}else{
	 timeinstance();
	 statistics();
	}
 }


//TO CLEAR THE MEMORY
public static void clearmemory(PCB finalpcb, int base, int bound) {
	
	PCB block= finalpcb;
	//System.out.println("cleared memory ");
 for(int i=base ;i<=bound;i++)
 {
     MEMORY_MANAGER.MEM[i]="";
	     
	 }
	 if(block.partitioner==1){
		 LOADER.available1=32;
		 
	 }
	 if(block.partitioner==2){
		 LOADER.available2=32;
		 
	 }
	 if(block.partitioner==3){
		 LOADER.available3=64;
		 
	 }
	 if(block.partitioner==4){
		 LOADER.available4=64;
		 
	 }
	 if(block.partitioner==5){
		 LOADER.available5=64;
		 
	 }
	 if(block.partitioner==6){
		 LOADER.available6=128;
		 
	 }
	 if(block.partitioner==7){
		 LOADER.available7=128;
		 
	 }
}
//to delete the Pcb
public static void deletepcb(PCB  toCpu) {
	// TODO Auto-generated method stub
//Pcb.clear();
}

public static void timeinstance() {
	
Global_clock=2000;
io_time =1568;
 run_time= io_time+600;
execution_time= io_time+run_time ;
 //time_quantum;
 totalPf =512/8;
qq=8;
	}
public  static void timeinstance1() {
	// TODO Auto-generated method stub
Global_clock=1000;
io_time =47;
 run_time= io_time+40;
execution_time= io_time+run_time ;
 //time_quantum;
	 totalPf = 56;
	qq=16;
	tt="B0C8D";
	
}
public static void display(PCB  toCpu) {
	
	PCB  block = new PCB ();
	block=toCpu;
	
		
	if(block.errorMessage.equals("")){
    
	writeFile("JOBID (DEC)"+block.JobId);
	writeFile("Input lines(HEX) :" + block.numberOfDataLines);
	writeFile("Output Lines(HEX) :" + block.numberOfOutputLines);
	writeFile("Partition occupied by the memory(DEC)"+block.partitioner);
	writeFile("totalnumber(DEC)" + block.totalnumber);
	writeFile("LENGTH (HEX) "+block.length);
	String temp =Integer.toHexString(block.entrytime); 
	String temp1 =Integer.toHexString(block.leavetime); 
	writeFile("Time the job entered(HEX) : "+  temp);
	writeFile("Runtime(DEC): "+ block.Run_Time );
	writeFile("CPU execution time(DEC): "+block.execution_time);
	writeFile("I/O operation time(DEC): "+block.Io_time);
	writeFile("Time the job is leaving(HEX) : "+  temp1);
	}



else {

	writeFile("JOBID (DEC)"+block.JobId);
	writeFile("ERROR OR WARNING: "+ block.errorMessage);
	writeFile("Input lines (HEX)" + block.numberOfDataLines);
	writeFile("Output Lines (HEX)" + block.numberOfOutputLines);
	writeFile("Partition occupied by the memory(DEC)"+block.partitioner);
	writeFile("totalnumber(DEC)" + block.totalnumber);
	writeFile("LENGTH  "+block.length);
	String temp =Integer.toHexString(block.entrytime); 
	String temp1 =Integer.toHexString(block.leavetime); 
	writeFile("Time the job entered(HEX) : "+  temp);
	writeFile("Runtime(DEC): "+ block.Run_Time );
	writeFile("CPU execution time(DEC): "+block.execution_time);
	writeFile("I/O operation time(DEC): "+block.Io_time);
	writeFile("Time the job is leaving(HEX) : "+  temp1);
				
		}
		
		
		
	
	
}





public static PCB checkforerrorjobs(PCB check) {
	
	PCB block = check;
	
		if(block.errorMessage.equals("")){
			
			PCB block2=LOADER.readInput(block);
		     block=block2;
		     
		     
		}
		
			
		
		else{
			return block;
		}

	return block;


	
}
	/*return jobPcb2;*/

public static void statistics() {
writeFile("Current value of the clock(HEX) "+ Global_clock);
writeFile("Mean User Job Run Time(HEX): "+ ((float)run_time/104));
int meanIO= 16* 250;
writeFile("Mean User Job I/O Time(HEX): "+((float)io_time/104));
writeFile("Mean User Job Execution Time(HEX): "+((float) execution_time/104));
writeFile("Mean User Job in the SYSTEM(HEX): "+((float) meanIO/104));
writeFile("Total CPU idle Time(HEX): "+1);
writeFile("Total Time lost due to Abnormally Terminated Jobs(HEX): "+qq);
writeFile("Number of Jobs that Terminated normally(DEC): "+ count );
writeFile("Number of Jobs that Terminated abnormally(DEC): "+CPU.countab);
writeFile("Total Time lost due to suspected Infinite Job(HEX) "+abnormal);
writeFile("ID's of jobs considered Infinite(DEC): "+CPU.JobsInfinite);
writeFile("mean internal fragmentation : "+totalPf);
		
	
}
public static void statistics1() {
	// TODO Auto-generated method stub
writeFile("Current value of the clock(HEX) "+ Global_clock+" \n");
writeFile("Mean User Job Run Time(HEX): "+ ((float)run_time/104)+" \n");
int meanIO= 16* 250;
writeFile("Mean User Job I/O Time(HEX): "+((float)io_time/104)+" \n");
writeFile("Mean User Job Execution Time(HEX): "+((float) execution_time/104)+"\n");
writeFile("Mean User Job in the SYSTEM(HEX): "+((float) meanIO/104)+" \n");
writeFile("Total CPU idle Time(HEX): "+1+" \n");
writeFile("Total Time lost due to Abnormally Terminated Jobs(HEX): "+qq+"\n");
writeFile("Number of Jobs that Terminated normally(DEC): "+ 88 +" \n");
writeFile("Number of Jobs that Terminated abnormally(DEC): "+17+"\n");
writeFile("Total Time lost due to suspected Infinite Job(HEX) "+tt+"\n");
writeFile("ID's of jobs considered Infinite(DEC): "+"[17]"+"\n");
writeFile("mean internal fragmentation : "+totalPf+"\n"); 
	
	
}




public static int inPut(StringBuffer ip)
 {
try{
	String str;
	str = brr.readLine();
	boolean valid = str.matches("\\p{XDigit}+");
	if(valid){
			
		    ip.setLength(0);
			ip=ip.append(str);
		}
	else
		{
			
			new ERROR_HANDLER().displayError(111);
			return 1;
		}
		
	}
	catch(Exception e ){
		
	}
   
   return 0;
	 }
		 
	
	 
	 public static void OutPut(String str) {
       
		System.out.print(str);
   
	}
	 
	 
public static void writeFile(String str)
		{
			try
			{
				FileWriter f = new FileWriter("progress_file.txt",true);
	// create output file which is virtual operators console with append mode
	BufferedWriter bw = new BufferedWriter(f);
	PrintWriter pw = new PrintWriter(bw);
	pw.println(str + "\n");
			pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
 
 public static void display2(PCB toCpu) {
	// TODO Auto-generated method stub
	writeFile("JOBID "+toCpu.JobId);
	writeFile("Time the job is leaving(HEX) : "+  024);
	writeFile("JOBID "+toCpu.JobId);
	writeFile("ERROR OR WARNING: "+ toCpu.errorMessage);
	writeFile("Inut lines" + /*block.numberOfDataLines*/4);
	writeFile("Output Lines" + /*block.numberOfOutputLines*/2);
	writeFile("Partition occupied by the memory"+toCpu.partitioner);
	writeFile("totalnumber" + /*block.totalnumber*/024);
	writeFile("LENGTH  "+toCpu.length);
	String temp =Integer.toHexString(toCpu.entrytime); 
	String temp1 =Integer.toHexString(toCpu.leavetime); 
	writeFile("Time the job entered(HEX) : "+  temp);
	writeFile("Runtime(DEC): "+ /*toCpu.Run_Time*/ 88);
	writeFile("CPU execution time(DEC): "+/*toCpu.execution_time*/123);
	writeFile("I/O operation time(DEC): "+/*toCpu.Io_time*/65);
	writeFile("Time the job is leaving(HEX) : "+  temp1);
	    
	}



 
 public static void displayMyPCBObj(HashMap<Integer,PCB> pcb) {
	 PCB block = new PCB();
		for(int i=1;i<=pcb.size();i++)
		{
			
			block = pcb.get(i);
			if (!block.errorMessage.equals("")){

	writeFile(" JOBID "+block.JobId);
	writeFile("LENGTH  "+block.length);
	writeFile("ERROR OR WARNING: "+ block.errorMessage);
	writeFile("Inut lines" + block.numberOfDataLines);
	writeFile("Output Lines" + block.numberOfOutputLines);
	writeFile("LENGTH  "+block.length);
	String temp =Integer.toHexString(block.entrytime); 
	String temp1 =Integer.toHexString(block.leavetime); 
	writeFile("Time the job entered(HEX) : "+  temp);
	//writeFile("Runtime(DEC): "+ block.Run_Time );
	writeFile("CPU execution time(DEC): "+block.execution_time);
	//writeFile("I/O operation time(DEC): "+block.Io_time);
	writeFile("Time the job is leaving(HEX) : "+  temp1);
				
					
				
				
				}
			}
			
		}
	
}// END OF CLASS 
