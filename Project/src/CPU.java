

/*
CPU class is used to simulate the working of CPU
CPU class consist of method cpu(X,Y,).
cpu(X,Y)
X is the initial value of the program counter (PC)
Y is the trace switch (0 = trace off and 1=trace on)
CPU class consist of method writeFile() to write the final result to output file.

*/




import java.util.*;
import java.io.*;
import java.math.*;

public class CPU 
{
	static Queue<PCB> Blocked = new LinkedList<PCB>(); 
	static Queue<PCB> Running = new LinkedList<PCB>();
	static Queue<PCB> Ready = new LinkedList<PCB>();
	MEMORY_MANAGER m = new MEMORY_MANAGER(); /*object of MEMORY subsystem*/
	LOADER load = new LOADER();/*object of LOADER subsystem*/
	SYSTEM sys = new SYSTEM();/*object of SYSTEM class for performing IO operation*/
	public static StringBuffer strbuf = new StringBuffer();/*BufferMemory Used by CPU*/
	static int effective_address=0;/*variables to hold effective address*/
	static int eff_add=0;
	public static int[] register =new int[9];/*9 CPU registers*/
	static File f;
	public static int clock_cycle =0;
	public static int runtime=0;
	public static int io_time=0;
	public static int writeadd,readadd;
	public static String temp,int_binary,bin,op_code,bit_egl,abcdefg,inst,adrr,tmp;
	public static int a,b,c,countab;
	public static int flagg,frag=0;
	public static int Job_identification_number=0;
	public static ArrayList<Integer> JobsInfinite =new ArrayList<Integer>();
	//static int effective_address=0;
	 public static int msb ,indirect_bit,index_bit,A,AA,Inst,eff_A,valueInAddress,program_counter;
	//int indirect_bit=0;
	 //int index_bit=0;
	 public static String converted="";
	//int A=0;
	public static String add="";
	
	
public static PCB cpu(PCB final1, int programCounter, int trace_switch, int inputdataAdd,
		int inputdataLastAdd, int writedataAdd, int writedataLastAdd){
		PCB block= final1;
		//trace_switch=tc;
		program_counter =programCounter;
		
	    resetclocks();
		writeadd=writedataAdd;
		readadd=inputdataAdd;
		flagg=0; 
for( ; ; )
{  
	
	//System.out.println("programcounter   before =="+ program_counter);
MEMORY_MANAGER.memoryread("READ",program_counter,strbuf);/* read the first instruction from memory*/
program_counter++;
//System.out.println("programcounter after =="+ program_counter);

/*if (strbuf.length()==0 || program_counter==512){
	new ERROR_HANDLER().displayError(110);
	break;
}*/
if(clock_cycle==35){
	Ready.add(block);
	clock_cycle=clock_cycle+10;
}    
if(clock_cycle>200){
	//new ERROR_HANDLER().displayWarning(210);
	block.errorMessage="[Warning : 110] PROGRAM TAKES TO LONG";
	SYSTEM.display(block);
	return block;
}
if(clock_cycle>210){
	
	//new ERROR_HANDLER().displayError(114);
	block.errorMessage="[E:114] ERROR: SUSPECTED INFINITE JOB";
	JobsInfinite.add(block.JobId);
	
	countab++;
	SYSTEM.display(block);
	return block;
	
}

temp=strbuf.toString();
//System.out.println("temp initial="+temp);
a= Integer.parseInt(temp,16);
//System.out.println("a="+a);

int_binary=Integer.toBinaryString(a);/*converting instruction into binary*/
if (int_binary.length()<32)/*padding 0 to if needed*/
{
	bin="";
	b = int_binary.length();
	b = 12-b;
	for(int i=0;i<b;i++)
	{
		bin=bin.concat("0");
		
	}
	bin = bin.concat(int_binary);
	temp= bin;
	
}




String reg4 = String.format("%03x",register[4]);
/*System.out.println("register 4="+ reg4);*/

String reg5 =  String.format("%03x",register[5]);
/*System.out.println("register 5="+ reg5);*/

if(trace_switch!=0 && trace_switch!=1){
	block.errorMessage=	"[W:111] INVALID TRACE FLAG";
}

if(trace_switch==1)/*if trace switch =1 ; print to trace_file*/
{
	try{
		File filetrace = new File("trace_file"+block.JobId+".txt");
		FileWriter filewrite = new FileWriter(filetrace,true);
		BufferedWriter br= new BufferedWriter(filewrite);
		PrintWriter pww = new PrintWriter(br);
		
		if(flagg==0){
		pww.write("JOB ID = "+block.JobId+"\n");
		pww.write("Instruction(BIN) \t PC(DEC) \t Accumalator(HEX) \t Index Register(HEX) \t Effective Address(HEX)"+"\n");
		flagg=1;
		}
//pww.write("\n"+temp + "\t\t\t " + (program_counter-1) + "\t\t\t" + reg55+ "\t\t\t\t\t\t" + reg44 +"\t\t\t\t\t\t  " + effective_address+"\n");
		pww.write("\n"+temp + "\t\t\t" + (program_counter-1) + "\t\t\t" + reg5 + "\t\t\t\t\t\t" + reg4 + "\t\t\t\t\t\t  " 
		+effective_address+"\n");
		pww.close();
		
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
}

op_code = temp.substring(1,4);/* extracting op-code bit*/
 

/*extracting the R bit in the Type instruction  */
int R= Integer.parseInt(Character.toString(temp.charAt(4))); 


/*extracting the 0 bit in the Type instruction  */
int zerobit = Integer.parseInt(Character.toString(temp.charAt(0))); 

add = temp.substring(6,12);




if(op_code.equals("000") || op_code.equals("001")|| op_code.equals("010") ||op_code.equals("011")||op_code.equals("100")||op_code.equals("101"))
 {	
	
 msb = Integer.parseInt(Character.toString(temp.charAt(6)));
 /*System.out.println("msb="+msb);*/
indirect_bit = Integer.parseInt(Character.toString(temp.charAt(0)));
/*System.out.println("indirect bit ="+indirect_bit);*/
 index_bit = Integer.parseInt(Character.toString(temp.charAt(5)));
 /*System.out.println("index bit="+index_bit );*/
 
if (indirect_bit==1 && index_bit==1){
	
				
				
				effective_address= indirect_indexing(add);
			}

			else if(indirect_bit==1 && index_bit!=1)
			{
				
				effective_address= indirection(add);
			}
			else if(indirect_bit!=1 && index_bit==1){
				
				effective_address= indexing(add);
			}
			
			else{
				effective_address= direct(add);
				
			}
			
			
			if (op_code.equals("000")){ /*AND*/
++clock_cycle;
MEMORY_MANAGER.memoryread("READ",effective_address,strbuf);
	String s= strbuf.toString();
	valueInAddress=Integer.parseInt(s,16);
	if(R==1){
	register[4]=register[4] & valueInAddress;
	}
	else{
	register[5]=register[5] & valueInAddress;
	}
}

if (op_code.equals("001")){/* ADD*/
++clock_cycle;
MEMORY_MANAGER.memoryread("READ",effective_address,strbuf);
	String s= strbuf.toString();
	
	valueInAddress=Integer.parseInt(s,16);
	if(valueInAddress==4095){
		valueInAddress = -1;
		
	}
	
	if(R==1){
	register[4]=register[4] + valueInAddress;
	}
	else{
	register[5]=register[5] + valueInAddress;
	}
}

if (op_code.equals("010")){ //store
++clock_cycle;
strbuf.setLength(0);	
if(R==0){
strbuf=strbuf.append(reg5);

MEMORY_MANAGER.memory("WRIT",effective_address,strbuf);
//MEMORY_MANAGER.dump();
}
else{
strbuf=strbuf.append(reg4); 
//System.out.println(strbuf+   " strbuf for reg 4");
//MEMORY_MANAGER.memory("WRIT",effective_address,strbuf);
}
//MEMORY_MANAGER.dump();
//String test= MEMORY_MANAGER.MEM[effective_address];
		
	}
 
	if (op_code.equals("011")){//LOAD
++clock_cycle;
MEMORY_MANAGER.memoryread("READ",effective_address,strbuf);
	if(R==0)
	{
	  register[5]=Integer.parseInt((strbuf.toString()),16);
	}
	else{
	register[4]=Integer.parseInt((strbuf.toString()),16);
	}
}

if (op_code.equals("100")){//JMP
	++clock_cycle;
	
	program_counter=effective_address;
	
}

if (op_code.equals("101")){//JPL
++clock_cycle;

if(R==0){
	//m.memoryread("READ",program_counter,strbuf);
	register[5]= program_counter;/*Integer.parseInt((strbuf.toString()),16)*/;
}
else{
register[4]=program_counter;/*Integer.parseInt((strbuf.toString()),16);*/
			}
			++clock_cycle;
			program_counter=effective_address;
			
		}
		
	
 
			 
		}		
		    
 switch( op_code)
	{
		  
 /*Type II instruction*/
case "110":

int r= Integer.parseInt(Character.toString(temp.charAt(5)));
int w= Integer.parseInt(Character.toString(temp.charAt(6)));
int h= Integer.parseInt(Character.toString(temp.charAt(7)));
    
   
   if ((r==w) && (r==h) && (w==h))
	 {
	   block.errorMessage="[E:126] TYPE II INSTRUCTION MORE THAN 1 OF 3 BITS SET ";
   SYSTEM.display(block);
   MEMORY_MANAGER.dump();
   return block;
 }
 else if(r==1 && w!=1 && h!=1) /*read bit is set*/
	 
 {  
	 String data = MEMORY_MANAGER.MEM[readadd];
	 readadd++;
	
    if(/*(strbuf.toString())*/data.matches("\\p{XDigit}+") && data.length()!=0)
{
    if(R==0)
	{
		register[5]= Integer.parseInt(/*strbuf.toString()*/data,16);// 16 shows its in HEX 
		
		clock_cycle+=10;
		io_time+=10;
		
		break;
	}
	else{
		/*if(strbuf.equals(reg5)){
			writeFile((Integer.toString(register[4]*register[4])));
		}*/
		//System.out.println("4");
		register[4]= Integer.parseInt(data,16);
		
		clock_cycle+=10;
		io_time+=10;
		break;
	}
    
}

else
{
	block.errorMessage="[E:111] ILLEGAL INPUT";
    	SYSTEM.display(block);
		   MEMORY_MANAGER.dump();
		   return block;
	}
 }
				 
			

else if(w==1)  /* write bit is set*/
{
	//writeFile("Output in HEX\n");
if(R==0)
	{
		MEMORY_MANAGER.MEM[writeadd]= reg5;
		//writeFile(reg5);
		clock_cycle+=10;
		io_time+=10;
		writeadd++;
		break;
	}
	else{
		MEMORY_MANAGER.MEM[writeadd]= reg4;
		clock_cycle+=10;
		io_time+=10;
		//writeFile(reg4);
				writeadd++;
				break;
			}
			
    }	
	
  else 
  {  /*halt bit is set*/
  
++clock_cycle;
runtime= clock_cycle+io_time;
block.Run_Time=runtime;
block.execution_time=clock_cycle;
block.Io_time= io_time;
block.leavetime =clock_cycle+1;
block.Cpu_time=runtime-io_time;
SYSTEM.display(block);
return block;

//tmp=Integer.toHexString(clock_cycle);
//writeFile("Job Identification Number:"+ Job_identification_number);
//writeFile("Number of Exception zero \n");

//System.exit(0);  //0 to indicate JOB terminated
/*return toCpu;*/

	  
  }
 /* break;*/
  
   
   /*Type III and Type IV register*/
case "111":
		
if(zerobit==1) /*Type IV register*/
{    
		
      bit_egl= temp.substring(5,8);/*extracting =,<,> bits*/
  //System.out.println(bit_egl +"= equal ,greater,less");

switch(bit_egl)
{
	case"000":  /*NSK*/
		clock_cycle++;
		break;
	 
	case"001":  /*GTR*/
		if(R==0){
			if(register[5]>0)
		     { clock_cycle++;
		       program_counter++;
		       break;
		     }
		    else{
			  clock_cycle++;
			  break;
		      }
		}
	else{
		if(register[4]>0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	
	
case "010":/*LSS*/
	if(R==0){
		if(register[5]<0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	else{
		if(register[4]<0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	
case"011":/*NEQ*/
	
	if(R==0)
	{
	if(register[5]>0 || register[5]<0)
	{ clock_cycle++;
	  program_counter++;
	  break;
	}
	else{
		clock_cycle++;
		break;
	}
	}
	else{
		if(register[4]>0 || register[4]<0)
		{ clock_cycle++;
		  program_counter++;
		  break;
		}
		else{
			clock_cycle++;
			break;
		}
	}
	
	
case"100":/*EQL*/
	if(R==0){
		
	if(register[5]==0)
		
	{ 
		
		clock_cycle++;
	  program_counter++;
	  break;
	}
	else{
		clock_cycle++;
		break;
	}
	}
	else{
		if(register[4]==0)
			
		{ clock_cycle++;
		  program_counter++;
		  break;
		}
		else{
			clock_cycle++;
			break;
		}
		
	}
	

case"101":/*GRE*/
	if(R==0){
		if(register[5]>=0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	else{
		if(register[4]>=0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	
	

case"110":/* LSE*/
	if(R==0){
		if(register[5]<=0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	else{
		if(register[4]<=0)
	     { clock_cycle++;
	       program_counter++;
	       break;
	     }
	    else{
		  clock_cycle++;
		  break;
	      }
	}
	
	

case"111": /*USK*/
			clock_cycle++;
			program_counter++;
			break;
			
	  default: break;
	}
	break;

}

/*Type III register*/
else{
	abcdefg = temp.substring(5,12); /*extracting abcdefg bits in Type III*/ 
//System.out.println("abcdefg="+ abcdefg);
 try{
  switch(abcdefg){  
   
   
	case "1111111":  /*Error in bits*/ 
	//clock_cycle++;
    
	MEMORY_MANAGER.dump();
	block.errorMessage="[E 112:]TYPE III WRONG COMBINATION OF ACTION ";

		  break;
	
case "1000000":/*CLR*/
	clock_cycle++;
	if(R==1)
	{
		register[4]=0;
	}
	else{
		register[5]=0;
	}
	break;

case "0100000":/*INC*/
	clock_cycle++;
	if(R==1)
	{
		register[4]=register[4]+1;
	}
	else{
		register[5]+=1;
	}
	break;

case "0010000":/*COM*/
	clock_cycle++;
	if(R==1)
	{    
		int z= Integer.parseInt(reg4,16);
	    inst=Integer.toBinaryString(z);
	    char c ='1';
	    String afterComplement="";
	    char []swapZ=inst.toCharArray();
	    for(int i=0;i<inst.length();i++){
	    	if(inst.charAt(i)==c){
	    		swapZ[i]='0';
	    		
	    	}
	    	else{
	    		swapZ[i]='1';
	    	}
	    	afterComplement+=swapZ[i]; 
	    }
		register[4]= Integer.parseInt(afterComplement, 2);
		break;
		
	}
	else
	{   
		int z= Integer.parseInt(reg5,16);
	    inst=Integer.toBinaryString(z);
	    char c ='1';
	    String afterComplement="";
	    char []swapZ=inst.toCharArray();
	    for(int i=0;i<inst.length();i++){
	    	if(inst.charAt(i)==c){
	    		swapZ[i]='0';
	    		
	    	}
	    	else{
	    		swapZ[i]='1';
	    	}
	    	afterComplement+=swapZ[i]; 
	    }
		register[5]= Integer.parseInt(afterComplement, 2);
		break;
		
	}
case"0000100": /*Rotate left by 1 bit */
    clock_cycle++;
	if(R==1)
	{
		int z = register[4];
		register[4]= z << 1;
		//register[4]= z;
		break;
		
	}
	else{
		
		int z = register[5];
		register[5]= z << 1;
		 
		break;
	}
	
	
case"0000101":/*Rotate left by 2 bit */
	clock_cycle++;
	if(R==1)
	{
		int z = register[4];
		register[4]= z << 2;
		break;
		
	}
	else{
		int z = register[5];
		register[5]= z << 2;
		break;
	}
		
case"0000010":/*Rotate right by 1 bit */
	clock_cycle++;
	if(R==1)
	{   int z = register[4];
	    register[4]= z >> 1;
		break;
		
	}
	else{
		int z = register[5];
		register[5]= z >> 1;
		break;
	}
	
case"0000011":/*Rotate left by 2 bit */
	clock_cycle++;
	if(R==1)
	{
		int z = register[4];
		register[4]= z >> 2;
		break;
		
				}
				else{
					int z = register[5];
					register[5]= z >> 1;
					break;
				}
				
			case"0001000": /*shift  by 6 bit */
				clock_cycle++;
				if(R==1)
				{
					int z = register[4];
					register[4]= z >> 6;
					break;
					
				}
				else{
					int z = register[5];
					register[5]= z >> 6;
					break;
				}
				
		       
			default:
				   break;
				
		    }
		  
		 }
		catch(Exception e){
			MEMORY_MANAGER.dump();
			new ERROR_HANDLER().displayError(127);
		}
		
		break;	
		}
		

/*default:
   
    new ERROR_HANDLER().displayError(121);	
    System.exit(0);
*/
				
		}
 
 
 String reg44 = String.format("%03x",register[4]);
	
	
	String reg55 = String.format("%03x",register[5]);
	
	
	if(trace_switch>=1)  // check for trace switch 
	{
		try
		{
File ff = new File("trace_file.txt");
FileWriter fww = new FileWriter(ff,true);
BufferedWriter brr = new BufferedWriter(fww);
PrintWriter pww = new PrintWriter(brr);
	
pww.write("\n"+temp + "\t\t\t" + (program_counter-1) + "\t\t\t" + reg55+ "\t\t\t\t\t\t" + reg44 +"\t\t\t\t\t\t  " + effective_address+"\n");	
			 pww.close();
		 
		}catch(Exception e){}
	}

}


}
	
	
		
		
		public static void resetclocks() {
		// TODO Auto-generated method stub
			 clock_cycle =0;
			 runtime=0;
			 io_time=0;
	}




public static String complement2(String s)
{
	 char c ='1';
	 int z=Integer.parseInt(s,2);
	 //String s= Integer.toBinaryString(k);
	 String after2Complement="";
	 char []swapZ=s.toCharArray();
	 for(int i=0;i<s.length();i++)
	 {
	
		 if(s.charAt(i)==c){
			 swapZ[i]='0';
				
			}
			else{
				swapZ[i]='1';
			}
			after2Complement+=swapZ[i]; 
			
		}
	 	  //System.out.println(after2Complement);
	 	  
		 return after2Complement;
		
	}

public static int indirect_indexing(String address)
{
	Inst = msbCheck(msb);
	A = Inst+program_counter;
	MEMORY_MANAGER.memoryread("READ",A,strbuf);
	String s= strbuf.toString();
	
	valueInAddress=Integer.parseInt(s,16);
	eff_A = valueInAddress+register[4];
	return eff_A;
}


public static int indirection(String address)
{
	Inst = msbCheck(msb);
	A = Inst+program_counter;
	MEMORY_MANAGER.memoryread("READ",A,strbuf);
	String s= strbuf.toString();
	
	valueInAddress=Integer.parseInt(s,16);
	eff_A =valueInAddress;
	return eff_A;
}

public static int indexing(String address)
{
	Inst = msbCheck(msb);
	A = Inst+program_counter;
	
	eff_A =A+register[4];
	return eff_A;
}



 public static int direct(String address) {
	Inst = msbCheck(msb);
	A = Inst+program_counter;
	eff_A =A;
	return eff_A;
}





 public static  int msbCheck(int msb)
 {
	if (msb==0)
 {
	//A = address;
	Inst=Integer.parseInt(add,2);
 }
else {
	
	
    converted= complement2(add);
    //System.out.println(converted +"con");
    //int k =Integer.parseInt(converted,2);
    int k =Integer.parseInt(converted,2);
      k=(k+1) * -1;
      
      Inst =k;
      //System.out.println(Inst);
		    
		    
	}
			return Inst; 
	}	
 
 
		
        
public static void writeFile(String str)
	{
		try
		{
			FileWriter f = new FileWriter("progress_file",true);
		// create output file which is virtual operators console with append mode
		BufferedWriter bw = new BufferedWriter(f);
		PrintWriter pw = new PrintWriter(bw);
		pw.write(str + "\n");
			pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
}



	
		
	
