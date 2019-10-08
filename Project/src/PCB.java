

/* PCB CLASS 
 * PCB OR PROCESS CONTROL BLOCK INCLUDER THE FOLLOWING INFORMATION FOR A JOB THAT ENTERS THE SYSTEM
 */

import java.util.ArrayList;

public class PCB {

	
public int JobId;  // to store the jobID of the program 
public String numberOfDataLines;  //to store data lines 
public String numberOfOutputLines; //to store outputlines
public String traceswitch; //to store trace switch
public String startAdd; // to store start address of instruction 
public String totalnumber;// total memory needed 
public String length; // length of the program
public int partitioner; // partition of the memory occupied 
public int base; // base address of the memory
public int bound;  // bound address of the memory
public int datalinesstartAdd; // data lines 
public int inputdataAdd;    //data input for reading
public int inputdataLastAdd;
public int writedataAdd;   // space for writing output 
public int writedataLastAdd;
public int PC; 
// to store the value of the next instruction which needs to be executed 
public int [] registers= new int[9]; 
// to maintain a  copy of the registers for the program
//public double cTime=0; // to calculate the cumulative time
public int Cpu_time; // cpu time 
public int entrytime; //entry time of the program
public int leavetime; // leaving time of the program
public int Load_Time;  //load time of the program
public int Io_time; // i/o time of the program
public int execution_time; // execution time 
public int time_quantum;
public int Run_Time;  // run time of the program 
public ArrayList<String> loaderAdd= new ArrayList<String>();
public ArrayList<String> data= new ArrayList<String>();  // to store the address of the program 
//public ArrayList<String> Program = new ArrayList<String>();
public String errorMessage ="";  // to store appropriate error message

	}
		



