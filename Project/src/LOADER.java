/* The LOADER class will be responsible for loading each user program into the  main Memory.
   The Loader has a buffer used for block transfer between Loader and the Memory.
   
  LOADER class simulates working of loader.
  The loader method initializes program counter and trace switch 

 Functions:
  HEXBIN - converts 3 HEX digits to 12 bit Binary value 
  BINHEX- converts 12 bit Binary value to 3 HEX digits
  readInput- that reads the input file and stores the address in appropriate memory location
  CheckLoaderFormat -is a method that checks the loader format before it store in the memory
  StoreInMemory -IT will  STIMULATE MEMORY_MANAGER TO TORE LOADER FORMAT BASED ON BLOCK 
  SIZE OF MMEORY AND SIZE OF PROGRAM */



import java.util.*;
import java.io.*;
import java.math.BigInteger;


public class LOADER {
	
	public static ArrayList<String> loaddata=new ArrayList<String>();
	public static int startAddress; 
	 public static int traceSwitch;
	 public static int base; 
	 public static int bounds;
	 public static int load_time;
	 public static int available1 =32 ,i=0;
	 public static int available2 =32,j=31;
	 public static int available3 =64,p=63;
	 public static int available4 =64,l=127;
	 public static int available5 =64,q=191;
	 public static int available6 =128,n=255;
	 public static int available7 =128,o=383;
	 public static int ABNORMAL=45;
	 public static int jobsabterm=5;
	 MEMORY_MANAGER m = new MEMORY_MANAGER ();
	 static Queue<PCB> Blocked = new LinkedList<PCB>(); 
		static Queue<PCB> Running = new LinkedList<PCB>();
		static Queue<PCB> Ready = new LinkedList<PCB>();
	
	public static void loader(int start_add, int ts, int  memory_start,int memory_ends)
	 
	 {
		 startAddress = start_add;
		 traceSwitch = ts;
		 base=memory_start;
		 bounds=memory_ends;
	 }
	 
	
	public static PCB readInput(PCB block) {
	loaddata.clear();
	load_time++;
	block.Load_Time=load_time;
	 
   int listsize = block.loaderAdd.size();
	int length = Integer.parseInt(block.length,16);
	
   int dataarraysize=block.data.size();

   loaddata.addAll(block.loaderAdd);
   loaddata.addAll(block.data);

	
	int datalines =Integer.parseInt(block.numberOfDataLines,16);
	int startADD= Integer.parseInt(block.startAdd,16);
    int tc = Integer.parseInt(block.traceswitch);
	 int output=Integer.parseInt(block.numberOfOutputLines,16);
	 int total= output+length+datalines/*block.data.size() */;
	 //System.out.println(total+"total");
	 block.totalnumber=Integer.toString(total);
  if (output!=0)
  {
	  for(int k =0;k<output;k++){
		  loaddata.add("000");
  }
  //System.out.println(loaddata);
  }
 PCB loadercheck=Checkloaderformat(block,loaddata,output,datalines,total,length,dataarraysize);
 if (loadercheck.errorMessage.equals("")){
	  
PCB inMemory= storeInMemory(loadercheck,loaddata,output,datalines,total,length,dataarraysize);
	  
	
	  
	  return inMemory;
  }

 
  else{
			  return loadercheck;
		  }
		 
		
	} // end of function 


public static PCB storeInMemory(PCB bb,ArrayList<String> loaddata2, int output,
	int datalines, int total, int length, int dataarraysize) {
 //int haltflag=0;
PCB block=bb;
if (total<=32 && available1>=total){
	//System.out.println("entered 1 ");
	   block.partitioner =1;
	   int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
       int base=0;
       int bound=31;
    	int i=0;
		      
for(int z=0;z<loaddata2.size();z++){

//System.out.println("contents of array List "+loaddata2.get(z));
String k=loaddata2.get(z);
int n2=0;
int n1=1;
String str="";	
if(flag==0){
	base=i;
	block.base=i;
	flag=1;
}

while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
	boolean valid = str.matches("\\p{XDigit}+");
	if(valid && i<=31)
	{  
		//System.out.println(str);
		 if(str.equals("610")){
			 block.datalinesstartAdd=i+1;
			  haltflag = 1;
			MEMORY_MANAGER.MEM[i]= str;
    		i++;
		 	/*base=i+1;*/
		 }
		 else if(haltflag==1 && dataarraysize!=0 && leng>length){
			 dataarraysize--;
			 //System.out.println(dataarraysize);
			 MEMORY_MANAGER.MEM[i]= str;
	    	   
	    	  if(inflag==0){
	    		  block.inputdataAdd=i;
	    		  inflag=1;
	    	  }
	    	  if(dataarraysize==0 && lastflag==0){
	    		  block.inputdataLastAdd=i; 
	    		  lastflag=1;
	    	  }
	  i++;
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[i]= str;
	  
	  
	  if(inflag1==0){
		  block.writedataAdd=i;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=i; 
		  lastflag1=1;
	  }
	  i++;
	  }
 else{
	 MEMORY_MANAGER.MEM[i]= str;
		i++;
 }
 
 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=i;
    	 block.bound=i;
    	flag=0;
    }
	
	
	
	//available1=base-bound;
	
}
else if(total<=32 && available2>=total){
	block.partitioner =2;
       int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
       //int i = 63;
   //int start = available3 +63;
   int base=31;
   int bound=63;
	int j=31;

	for(int z=0;z<loaddata2.size();z++){
		 
    String k=loaddata2.get(z);
    int n2=0;
    int n1=1;
	String str="";	
if(flag==0){
	base=j;
	block.base=j;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
boolean valid = str.matches("\\p{XDigit}+");
if(valid && j<=63)
{
	 if(str.equals("610")){
	 block.datalinesstartAdd=j+1;
	  haltflag = 1;
	MEMORY_MANAGER.MEM[j]= str;
	j++;
 	/*base=i+1;*/
 }
 else if(haltflag==1 && dataarraysize!=0 && leng>length){
	 dataarraysize--;
	 MEMORY_MANAGER.MEM[j]= str;
	 
	  if(inflag==0){
		  block.inputdataAdd=j;
		  inflag=1;
	  }
	  if(dataarraysize==0 && lastflag==0){
		  block.inputdataLastAdd=j; 
		  lastflag=1;
	  }
	  j++; 
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[j]= str;
	 
	  
	  if(inflag1==0){
		  block.writedataAdd=j;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=j; 
		  lastflag1=1;
	  }
	  j++;
	  
	  }
 
 else{
	 MEMORY_MANAGER.MEM[j]= str;
		j++;
 }
 
						 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=j;
    	 block.bound=j;
    	flag=0;
    }
	
	
	
	//available2=base-bound;
}
else if (total<=64 && available3>=total){
   block.partitioner =3;
   int leng=0;
   int flag=0;
   int inflag=0;
   int lastflag=0;
   int inflag1=0;
   int lastflag1=0;
   int haltflag=0;
   int p=63;
   
   int base=63;
   int bounds=127;
	

	for(int z=0;z<loaddata2.size();z++){
		 
    String k=loaddata2.get(z);
    int n2=0;
    int n1=1;
	String str="";	
if(flag==0){
	base=p;
	block.base=p;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
boolean valid = str.matches("\\p{XDigit}+");
if(valid && p<=127)
{
	 if(str.equals("610")){
 block.datalinesstartAdd=p+1;
  haltflag = 1;
MEMORY_MANAGER.MEM[p]= str;
p++;
/*base=i+1;*/
 }
 else if(haltflag==1 && dataarraysize!=0 && leng>length){
	 dataarraysize--;
	 MEMORY_MANAGER.MEM[p]= str;
	  
	  if(inflag==0){
		  block.inputdataAdd=p;
		  inflag=1;
	  }
	  if(dataarraysize==0 && lastflag==0){
		  block.inputdataLastAdd=p; 
		  lastflag=1;
	  }
	  p++; 
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[p]= str;
	 
	  
	  if(inflag1==0){
		  block.writedataAdd=p;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=p; 
		  lastflag1=1;
	  }
	  p++;
	  }
 else{
	 MEMORY_MANAGER.MEM[p]= str;
		p++;
 }
 
 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bounds=p;
    	 block.bound=p;
    	flag=0;
    }
	
 for(int a=63;a<=128;a++){
	//System.out.println(a + "= a =" + MEMORY_MANAGER.MEM[a]);
}


//available3= base-bounds;
	
}
	

else if(total<=64 && available4>=total){
	block.partitioner =4;
       int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
       int l=127;
       int base=127;
       int bound=191;
    	
    
    	for(int z=0;z<loaddata2.size();z++){
			 
	    String k=loaddata2.get(z);
	    int n2=0;
	    int n1=1;
    	String str="";	
if(flag==0){
	base=l;
	block.base=l;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
boolean valid = str.matches("\\p{XDigit}+");
if(valid && l<=191)
{
	 if(str.equals("610")){
 block.datalinesstartAdd=l+1;
  haltflag = 1;
MEMORY_MANAGER.MEM[l]= str;
l++;
/*base=i+1;*/
 }
 else if(haltflag==1 && dataarraysize!=0 && leng>length){
	 dataarraysize--;
	 MEMORY_MANAGER.MEM[l]= str;
	  
	  if(inflag==0){
		  block.inputdataAdd=l;
		  inflag=1;
	  }
	  if(dataarraysize==0 && lastflag==0){
		  block.inputdataLastAdd=l; 
		  lastflag=1;
	  }
	  l++; 
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[l]= str;
	 
	  
	  if(inflag1==0){
		  block.writedataAdd=l;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=l; 
		  lastflag1=1;
	  }
	  l++;
	  
	  }
 else{
	 MEMORY_MANAGER.MEM[l]= str;
		l++;
 }
 
 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=l;
    	 block.bound=l;
    	flag=0;
    }
	
	
	
	//available4=base-bound;
	
}
else if(total<=64 && available5>=total){
	block.partitioner =5;
       int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
       //int i = 63;
   //int start = available3 +63;
   int base=191;
   int bound=255;
	int q=191;

	for(int z=0;z<loaddata2.size();z++){
		 
    String k=loaddata2.get(z);
    int n2=0;
    int n1=1;
	String str="";	
if(flag==0){
	base=q;
	block.base=q;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
boolean valid = str.matches("\\p{XDigit}+");
if(valid && q<=255)
{
	 if(str.equals("610")){
 block.datalinesstartAdd=q+1;
  haltflag = 1;
MEMORY_MANAGER.MEM[q]= str;
q++;
/*base=i+1;*/
 }
 else if(haltflag==1 && dataarraysize!=0 && leng>length){
	 dataarraysize--;
	 MEMORY_MANAGER.MEM[q]= str;
	  
	  if(inflag==0){
		  block.inputdataAdd=q;
		  inflag=1;
	  }
	  if(dataarraysize==0 && lastflag==0){
		  block.inputdataLastAdd=q; 
		  lastflag=1;
	  }
	  q++; 
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[q]= str;
	  
	  
	  if(inflag1==0){
		  block.writedataAdd=q;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=q; 
		  lastflag1=1;
	  }
	  q++;
	  
	  }
 else{
	 MEMORY_MANAGER.MEM[q]= str;
		q++;
 }
 
 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=q;
    	 block.bound=q;
    	flag=0;
    }
	
	
	
	//available5=base-bound;
}
else if(total<=128 && available6>=total){
	block.partitioner =6;
       int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
       int n =255;
       int base=255;
       int bound=383;
    	
    
    	for(int z=0;z<loaddata2.size();z++){
			 
	    String k=loaddata2.get(z);
	    int n2=0;
	    int n1=1;
    	String str="";	
if(flag==0){
	base=n;
	block.base=n;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
boolean valid = str.matches("\\p{XDigit}+");
if(valid && n<=383)
{
	 if(str.equals("610")){
 block.datalinesstartAdd=n+1;
  haltflag = 1;
MEMORY_MANAGER.MEM[n]= str;
n++;
/*base=i+1;*/
 }
 else if(haltflag==1 && dataarraysize!=0 && leng>length){
	 dataarraysize--;
	 MEMORY_MANAGER.MEM[n]= str;
	  
	  if(inflag==0){
		  block.inputdataAdd=n;
		  inflag=1;
	  }
	  if(dataarraysize==0 && lastflag==0){
		  block.inputdataLastAdd=n; 
		  lastflag=1;
	  }
	  n++; 
 }
 else if(dataarraysize==0  && lastflag==1 && output!=0){
	 output--;
	 MEMORY_MANAGER.MEM[n]= str;
	  
	  
	  if(inflag1==0){
		  block.writedataAdd=n;
		  inflag1=1;
	  }
	  if(dataarraysize==0 && lastflag1==0){
		  block.writedataLastAdd=n; 
		  lastflag1=1;
	  }
	  n++;
	  }
 else{
	 MEMORY_MANAGER.MEM[n]= str;
		n++;
 }
 
 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=n;
    	 block.bound=n;
    	flag=0;
    }
	
	
	
	//available6=base-bound;
}
else if(total<=128 && available7>=total){
	block.partitioner =7;
       int leng=0;
       int flag=0;
       int inflag=0;
       int lastflag=0;
       int inflag1=0;
       int lastflag1=0;
       int haltflag=0;
      int o=383;
       int base=383;
       int bound=511;
    	
    
    	for(int z=0;z<loaddata2.size();z++){
			 
	    String k=loaddata2.get(z);
	    int n2=0;
int n1=1;
String str="";	
if(flag==0){
	base=o;
	block.base=o;
	flag=1;
}
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{ 
		
		leng++;
	//System.out.println(n1 + "=n1");
	boolean valid = str.matches("\\p{XDigit}+");
	if(valid && o<=511)
	{
		 if(str.equals("610")){
			 block.datalinesstartAdd=o+1;
			  haltflag = 1;
			MEMORY_MANAGER.MEM[o]= str;
    		o++;
		 	/*base=i+1;*/
		 }
		 else if(haltflag==1 && dataarraysize!=0 && leng>length){
			 dataarraysize--;
			 MEMORY_MANAGER.MEM[o]= str;
	    	  
	    	  if(inflag==0){
	    		  block.inputdataAdd=o;
	    		  inflag=1;
	    	  }
	    	  if(dataarraysize==0 && lastflag==0){
	    		  block.inputdataLastAdd=o; 
	    		  lastflag=1;
	    	  }
	    	  o++; 
		 }
		 else if(dataarraysize==0  && lastflag==1 && output!=0){
			 output--;
			 MEMORY_MANAGER.MEM[o]= str;
	    	  
	    	  
	    	  if(inflag1==0){
	    		  block.writedataAdd=o;
	    		  inflag1=1;
	    	  }
	    	  if(dataarraysize==0 && lastflag1==0){
	    		  block.writedataLastAdd=o; 
	    		  lastflag1=1;
	    	  }
	    	  o++;
	    	  
	    	  }
		 else{
			 MEMORY_MANAGER.MEM[o]= str;
	    		o++;
		 }
		 
		 str="";		
		}
		
	}
		
	++n2;
	++n1;
	}
}
	
	 if(flag==1){
		 bound=o;
    	 block.bound=o;
    	flag=0;
    }
	
	
	
	//available7=base-bound;
}
else{
	block.errorMessage="[E:144]: PROGRAM TOO LONG>128";
    }
  
  
  return block;
}


public static PCB Checkloaderformat(PCB b,ArrayList<String> loaddata2,
		int output, int datalines, int total, int len, int dataarraysize) 
{
	PCB block=b;
	int writeop=0; 
	int readop=0;
	int read=0;
	int count=0;
int haltposition=0;
int haltflag=0;
int datainprog=0;
int datain=0;
int outputspace=0;
int leng=0;
int out=output;
int datall=datalines;
int dataarray=dataarraysize;
for(int z=0;z<loaddata2.size();z++){
	 
    String k=loaddata2.get(z);
    int n2=0;
    int n1=1;
	String str="";	
while(n2<k.length()){
	str=str+k.charAt(n2);
	if(n1%3==0)
	{  leng++;
	 
	boolean valid = str.matches("\\p{XDigit}+");
if(valid && leng<=total)
	
{ 
	count++;
	
	if(str.equals("620") || str.equals("6A0")){ // write operation 
	writeop++;
	
	
}
else if(str.equals("640") || str.equals("6C0")){ // read operations 
	readop++;
	read=readop;
	
}

else if(str.equals("610")){
	haltposition =count;
	haltflag=1;
}

else if(haltflag==1 && !str.equals("") && leng<=len /*(len-leng<=datall)*/ /*&& datall!=0*/){
	 
	datainprog++;   // datainprog lines eg:NUM 
	
	
}
else if(haltflag==1 && leng>len && read!=0 && dataarray!=0){  // eg: 003 input 
	datain++;
	//read--;
	dataarray--;
	
}
else {
	
	if(str.equals("000")&& out!=0){
				
				out--;
			outputspace++;
			
			}
		
		}
		 
		str="";		
}
	
	else{
		block.errorMessage="[E:143]:UNRECOGNIZABLE CHARACTER ENCOUNTERED WHILE LOADING";
		}
		
	}
	++n2;
	++n1;
	}
	}



 if(datalines!=dataarraysize || dataarraysize<datalines){
	block.errorMessage="[E:140]: MISSING DATA ITEMS ";
/*return block;*/
	
}
else if(output!=outputspace){
	block.errorMessage="[E:141]: INSUFFICIENT OUTPUT SPACE";
/*return block;*/
}

else if (datalines<dataarraysize){
	block.errorMessage="[E:142]: EXTRA DATA USED";
		 
		
	}
	else{}
	
	 
	 return block;

	

}


 public static String BINHEX(String string) 
	{
        
		String string2 = Integer.toHexString(Integer.parseInt(string, 2));
        
		return string2;
    
	}
	
	/*TO convert hexadecimal to binary*/

public static String HEXBIN(String string) 
{
    
	String string2 = new BigInteger(string, 16).toString(2);
    
	Integer n = string2.length();
    
	if (n < 8) 
	{
         for (int i = 0; i < 8 - n; ++i) 
		{
           string2 = "0" + string2;
        }
    }
    
	return string2;
}

 
/*to compute 2's complement*/	 
public String complement2(String convertingTo2Complement)
{
	 char c ='1';
 int k =Integer.parseInt(convertingTo2Complement,16);
 String s= Integer.toBinaryString(k);
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
	 return after2Complement;
	
}

//DEC TO HEX 
public static String DECHEX(int n) 
{
    
	String string = "0123456789ABCDEF";

if (n == 0) 
{
    
	return "0";

}

String string2 = "";

while (n > 0) 
{
    
	int n2 = n % 16;
    
	string2 = "" + string.charAt(n2) + string2;
        
		n /= 16;
    
	}
    
	return string2;

}




//Method to convert Hexadecimal to decimal
public static int HEXDEC(String string) 
{
    
	String string2 = "0123456789ABCDEF";
		        
	string = string.toUpperCase();
  
	int n = 0;
    

	for (int i = 0; i < string.length(); ++i) 
	{
        
		char c = string.charAt(i);
        
		int n2 = string2.indexOf(c);
        
		n = 16 * n + n2;
    
	}
    
	return n;

}

	
	
}// end of class 



