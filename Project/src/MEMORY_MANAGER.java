/*
MEMORY class is used to simulate MEMORY module of the system.
MEMORY class consist of String array called "MEM" of size 512 to simulate system memory 
having address from 00 to FF. 

memory method:
memory(String X,int Y,String Z)

This method accepts three parameters X,Y,Z
X is the control Signal
Y is the mem.add.reg(MAR)
Z is the mem.buf.reg(MBR)

X will hold three of possible action "READ", "WRIT","DUMP".
Y=Memory address ( 00-FF).
Z = variable to be written or read into

This method acts as interface for CPU to access the Main Memory as CPU cannot access memory directly.
 store the loader format in memory based on the block size and program size 
 THERE are 7 blocks of size (32,32,64,64,64,128,128)
*/



import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.*;

public class MEMORY_MANAGER

{
	MEMORY_MANAGER(){}
	
	public static String MEM[] = new String [512];
	public static StringBuffer memoryread(String R, int y, StringBuffer g)
	{
		if(R.equals("READ")) //read operation 
{ 

	if (y< 0 || y>512) /*Check if memory location is valid*/
{
	new ERROR_HANDLER().displayError(110);
	
}
else
{
	g.setLength(0);
	if(MEM[y]!= null)
	{
	  g=g.append(MEM[y]);/*contents of Memory location Y written to variable Z*/
				
		return g;
			}
				
		}
		
	}
	new ERROR_HANDLER().displayError(125);
	return g;
}

public static void memory(String X, int Y, StringBuffer Z)
{
	
   if(X.equals("WRIT")) //write operation
{   /*contents of variable Z written to Memory location Y */
		MEM[Y]=Z.toString();
		
	}
   else{
	   new ERROR_HANDLER().displayError(125);
   }
}
   
   public static void dump(){ //dump

if(MEM.length==0)
{
	new ERROR_HANDLER().displayError(124);
	
}
else 
{
	int move =0;
	//writeFile(String.format("%04x",move) + " ");
move = 8;
for(int i=1;i<MEM.length;i++)
{
	if(MEM[i]!=null)
	{
		//writeFile(MEM[i] + " ");
		if(i%8==0)
		{
			//writeFile/*SYSTEM.OutPut*/("\n");
			//writeFile/*SYSTEM.OutPut*/(String.format("%04x",move) + " ");
			move=move+8;
			if(i==MEM.length)
				continue;	
		}
	}	
}
//writeFile("null \n");
		}
	}
   
   public static void writeFile(String str)
	{
		try
		{
			// create output file with append mode 
	FileWriter f = new FileWriter("progress_file.txt",true); 
	BufferedWriter bw = new BufferedWriter(f);
	PrintWriter pw = new PrintWriter(bw);
	//pw.println("Abnormal termination");
	pw.print(str);
	pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
}

