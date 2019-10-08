


/*
OS CHECK PROGRAMS */

/*
ERROR_HANDLER  class deals with all the errors and warnings

CONSISTS OF DISPLAYERROR METHOD THAT DISPLAYS ERRORS AND DISPLAY WARNING METHOD THAT DISPLAYS WARNING
*/
import java.util.*;
import java.io.*;


public class ERROR_HANDLER{
	
	public void displayError(int errorNumber)
	{
		switch(errorNumber){
		
		case 110:
			writeFile("[E:110] ADDRESS OUT OF RANGE");
			break;
			
		case 111:
			writeFile("[E:111] ILLEGAL INPUT");
			break;
			
	     case 113:
			writeFile("[E:113] ERROR: MISSING OR BAD TRACE FLAG");
			break;
			
		case 114:
			writeFile("[E:114] ERROR: SUSPECTED INFINITE JOB");
			break;
			
		case 115:
			writeFile("[E:115] ERROR: PROGRAM SIZE TOO LARGE");
			break;
			
		case 116:
			writeFile("[E:116] ERROR:  MEMORY OVERFLOW");
			break;
			
		
		case 119:
			writeFile("[E:119] INVALID LOADER FORMAT CHARACTER");
			break;
			
		case 120:
			writeFile("[E:120] ATTEMPT TO DIVIDE BY ZERO");
			break;
			
		case 121:
			writeFile("[E:121] INVALID OPCODE");
			break;
			
		case 122:
			writeFile("[E:122] INVALID LOADER FORMAT:MORE THAN 12 BITS IN A LINE");
			break;
			
		case 123:
			writeFile("[E:123] FILE DOES NOT EXIST");
			break;
			
		
	    case 125:
			writeFile("[E:125] INVALID MEMEORY OPERATION");
			break;
			
		case 126:
			writeFile("[E:126] TYPE II INSTRUCTION MORE THAN 1 OF 3 BITS SET ");
			break;
			
		case 127:
			writeFile("[E:127] TYPE III WRONG COMBINATION OF ACTION ");
			break;
			
		case 130:
			writeFile("[E:128]LOADER FORMAT MISSING");
			break;	
			
		case 131:
			writeFile("[E:132] **JOB FOR PROOGRAM MISSING ");
			break;		
			
		case 132:
			writeFile("[E:133] **END FOR PROOGRAM MISSING ");
			break;		
			
		case 133:
			writeFile("[E:134] **DATA FOR PROOGRAM MISSING ");	
			
		case 134:
			writeFile("[E:130] NULL JOB ");	
			
		case 135:
			writeFile("[E:140]: MISSING DATA ITEMS ");
			
		case 136:
			writeFile("[E:141]: INSUFFICIENT OUTPUT SPACE");
			
		case 137:
			writeFile("[E:142]: EXTRA DATA USED");		
			
		}
		
			
	}
		
	// to display error warning 
	public void displayWarning(int warningNumber){
		
		switch(warningNumber){
		
		case 210:
			writeFilewarning("[W: 110] PROGRAM TAKES TO LONG");
			break;
		
		case 211:
			writeFilewarning("[W:111] INVALID TRACE FLAG");
			break;
			
			
		}
		
	}
	
	
	public static void writeFile(String str)
	{
		try
		{
		
			FileWriter f = new FileWriter("output.txt",true); // create output file with append mode 
			BufferedWriter bw = new BufferedWriter(f);
			PrintWriter pw = new PrintWriter(bw);
			pw.println("Abnormal termination");
			pw.println(str + "\n");
			pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static void writeFilewarning(String str)
	{
		try
		{
			FileWriter f = new FileWriter("output.txt",true); // create output file with append mode 
			BufferedWriter bw = new BufferedWriter(f);
			PrintWriter pw = new PrintWriter(bw);
			//pw.println("Abnormal termination");
			pw.println(str + "\n");
			pw.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}