import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.time.*;
import java.net.*;
import java.time.format.*;
// import org.apache.commons.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.awt.*;


public class Environment {
	private static String resource = "C:/Users/garpsu/Java25/ini/AllInOne.ini";
	private static HashMap<String, String> myINIhm = new HashMap<String, String>();
	private static HashMap<String, String> javaHM = new HashMap<String, String>();
	public static String  MyHome         =  "";
	public static String  MyCurDir       =  "";
	public static String  MyUserName     =  "";

	public Environment ()  {	}
	public static void EnvironmentInit () throws Exception {
		JavaProperties (false);
		SysInfo (false);
		myINIread (false);
	}
	public static void EnvironmentPrint () throws Exception {
		JavaProperties (true);
		SysInfo (true);
		myINIread (true);
	}
/*
	if ( new Environment().myINIread(false) < 0 ) {
		return ;
	}
*/
	public static void main  ( String[] args ) throws Exception {
			myINIread ( false );
			JavaProperties ( false );
			
			if   (args.length > 0) {
				System.out.println("My INI        : " + args[0] + " = [" + myINIget(args[0]) + "]" );
				System.out.println("Java Property : " + args[0] + " = [" + getJavaProperties(args[0]) + "]" );
				return;
			}
			
			// iterator() 메소드와 get() 메소드를 이용한 요소의 출력
			Iterator<String> keyjj = javaHM.keySet().iterator();
			int seqA = 0 ;
			while (keyjj.hasNext()) {
				String keyA = keyjj.next();
				System.out.println(String.format("%d ) %s = [%s]",++seqA, keyA, 
					(getJavaProperties(keyA) == null ? "" : getJavaProperties(keyA))         ));
			}			
			System.out.format("\n[Java properties] Total counts = %d\n\n",javaHM.size());

			// iterator() 메소드와 get() 메소드를 이용한 요소의 출력
			Iterator<String> keyss = myINIhm.keySet().iterator();
			int seqB = 0 ;
			while (keyss.hasNext()) {
				String keyB = keyss.next();
				System.out.println(String.format("%d ) %s = [%s]",++seqB, keyB, 
				( myINIget(keyB) == null ? "" : myINIget(keyB) )               ));
			}			
			System.out.format("\n[my INI] Total counts = %d\n",myINIhm.size());
	}

	public static String myINIget( String key ) {	
			if ( myINIhm.size() == 0 ) {
				myINIread ( false );
			}			
			String rts = myINIhm.get(key) ;
			if ((rts == null) || (rts.isEmpty()) || (rts.length() == 0) || (rts.equals(""))) {
				rts = " ";
				System.out.println("ini 파일에서 [" + key + "] 값을 못 찾았습니다");
				System.exit(99);				
			}	else {		
				rts = rts.replaceAll("null"," ");
			}
			return rts;
	}	

	public static int myINIread( boolean printConsole ) {		
        Properties prop = new Properties();
		int returnValue = 0;
        try {
			prop.load(new FileInputStream(resource));
			for (Enumeration en = prop.propertyNames(); en.hasMoreElements();) {
				returnValue++;
                String key = (String) en.nextElement();
                String vvv = prop.getProperty(key);
				myINIhm.put(key,vvv);
				if (printConsole) {
					System.out.println(returnValue + ") " + key + " = [" + vvv + "]");
				}
            }						
//			new FileInputStream(resource).close();
			if (printConsole) {
                System.out.println(prop); // prop에 저장된 요소들을 출력한다.
                prop.list(System.out); // prop에 저장된 요소들을 화면에 출력한다.
			}
        } catch (Exception eee) {
			returnValue = -9;
            eee.printStackTrace();
            System.out.println("[Initialize] File Not Found : "+resource);
        }
		return returnValue ;
	}
	public static String getJavaProperties( String kkk ) {
		Properties props = System.getProperties();
		
		String rts = props.getProperty(kkk);
		if ((rts == null) || (rts.isEmpty()) || (rts.length() == 0) || (rts.equals(""))) {
			rts = " ";
		}	else { 	
		    rts =  rts.replaceAll("null"," ");
		}
		return  rts;
	}
	public static int JavaProperties( boolean printConsole )  {
		// java properties
		long seq = 0;
		int returnValue = 0;
		Properties props = System.getProperties();
        try {
		for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
			returnValue++;
			String key = (String) en.nextElement();
			String value = props.getProperty(key);
			javaHM.put(key,value);
	
			if ( key.contains("user.home") ) {
				MyHome = value;
			}
			if ( key.contains("user.dir") ) {
				MyCurDir = value;
			}
			if ( key.contains("user.name") ) {
				MyUserName = value;
			}
			if ( printConsole ) {
				System.out.println(++seq + ") [" + key + "] = [" + value + "]");
			}
		}
		if ( printConsole ) {
		System.out.println("Home Dir    : " + MyHome);
		System.out.println("Current Dir : " + MyCurDir);
		System.out.println("User Name   : " + MyUserName);
		}
        } catch (Exception eee) {
            eee.printStackTrace();
        }
		return returnValue;
	}
	//  *********************
	//  System Info
	//  *********************
	public static void SysInfo(boolean printConsole) throws Exception {
		// OS
		String os = System.getProperty("os.name").toLowerCase();
		if ( !printConsole ) {
			return;
		}

		if (os.contains("win")) {
			System.out.println("Windows");
		} else if (os.contains("mac")) {
			System.out.println("Mac");
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			System.out.println("Unix");
		} else if (os.contains("linux")) {
			System.out.println("Linux");
		} else if (os.contains("sunos")) {
			System.out.println("Solaris");
		}

		System.out.println(os);

		// 메모리 및 디스크 용량
		/* Total number of processors or cores available to the JVM */
		System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
		/* Total amount of free memory available to the JVM */
		System.out.format("Free memory (bytes): %,d\n" , Runtime.getRuntime().freeMemory());
		/* This will return Long.MAX_VALUE if there is no preset limit */
		long maxMemory = Runtime.getRuntime().maxMemory();
		/* Maximum amount of memory the JVM will attempt to use */
		System.out.println("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
		/* Total memory currently available to the JVM */
		System.out.format("Total memory available to JVM (bytes): %,d\n" , Runtime.getRuntime().totalMemory());

		/* Get a list of all filesystem roots on this system */
		File[] roots = File.listRoots();
		/* For each filesystem root, print some info */
		for (File root : roots) {
			System.out.println("\nFile system root: " + root.getAbsolutePath());
			System.out.format(" Total space (bytes): %,21d\n" , root.getTotalSpace());
			System.out.format("  Free space (bytes): %,21d\n" , root.getFreeSpace());
			System.out.format("Usable space (bytes): %,21d\n" , root.getUsableSpace());
		}
		System.out.println();
		InetAddress localhost = InetAddress.getLocalHost();
		System.out.println("================");
		System.out.println(localhost);
		System.out.println("================");
		System.out.println("Host Name: "+ localhost.getHostName());  
		System.out.println("IP Address: "+ localhost.getHostAddress());  
		System.out.println("================");
	}
}

