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

// import org.json.simple.*;
// import org.json.simple.parser.*;

public  class StringDump {
	public  static String encode = "";
	public  static int totalTableLines = 0 ;
	public  static String outFileName = "" ;
	public  static String outFileExt = "txt" ;
	public  static String[] DumpLines = null ;
	private  static String outFileNameReal = "" ;
	private  static int totalWriteLines = 0 ;

	private static	char ch = (char) 34; // 따옴표
	private static	char cr = (char) 13;
	private static	char lf = (char) 10;

	public  StringDump (){
		DumpLines = new String[]{};
	};

	public static String getStringDumpWriteFileName(){
		return outFileNameReal;
	}
	public static String StringDumpWrite(){
		try {
		return StringDumpWrite(false);
		} catch (Exception eee) {	} finally { };
		return "[Error]";
	}
	public static String StringDumpWrite(boolean browser)throws Exception {
		if (browser) {
			return StringDumpWrite (1);
		} else {
			return StringDumpWrite (0);
		}
	}
	public static String StringDumpWrite(int action)throws Exception {
		if (( DumpLines == null ) || ( totalTableLines==0 )) {
			return "[Error][StringDumpWrite] 데이타가 없습니다";
		}
		if (( outFileName == null ) || ( outFileName=="" )) {
			return "[Error][StringDumpWrite] 파일 이름이 누락되었습니다 ";
		}  else {
			outFileName = outFileName.replaceAll(" ","_");
			outFileName = outFileName.replaceAll(ch+"","@");
		}

		String rts = "";
//		FileWriter fw = null;
		FileOutputStream fos = null;
        OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		String fmt2 = "(yyyyMMdd_HHmmss)";
		SimpleDateFormat sdf2 = new SimpleDateFormat(fmt2);
		long  startTime = System.currentTimeMillis() ;
//		long  currentTimeMillis = System.currentTimeMillis() ;
		String startTimesss = sdf2.format(startTime) ;
		outFileNameReal = outFileName + startTimesss + "." + outFileExt;
		try {
			fos = new FileOutputStream(outFileNameReal,false);  // new file
			if ( encode == "" ) {
			osw = new OutputStreamWriter(fos,new Environment().myINIget("EncodeForWrite")); 
			} else {
			osw = new OutputStreamWriter(fos,encode); 
			}
			
			bw = new BufferedWriter(osw);
			for( int kkk=0; kkk<totalTableLines; kkk++ ) {
				if ( ( DumpLines[kkk] == null ) || ( DumpLines[kkk] == "" ) ) {
				} else {
					bw.write(DumpLines[kkk]);
					bw.newLine();
					totalWriteLines++ ;
				}
			}
			bw.close();
			osw.close();
			fos.close();
			rts = "(" + totalWriteLines + ")건, 정상 write 완료, 파일명 = " + outFileNameReal;
		} catch (Exception eee) {
			rts = "[Error][StringDumpWrite]" + "(" + totalWriteLines + ")건, write 하면서 Error 발생 중단, 파일명 = "
			+ outFileNameReal + "\n\n" + eee.toString() + "\n" ;
			// eee.printStackTrace();
		} finally {
			try {
			bw.close();
			osw.close();
			fos.close(); } catch (Exception eee) {};
		}
		String fname = new File(outFileNameReal).getAbsolutePath().replaceAll(Matcher.quoteReplacement(File.separator), "/");
		if ((action==0)||(action==9)) {
			String[] cmdArray = { new Environment().myINIget("TextReader"),fname};	
			try{
				if  (( outFileExt.compareToIgnoreCase("htm") == 0) ||
					( outFileExt.compareToIgnoreCase("html") == 0) ) {
					 cmdArray[0] = new Environment().myINIget("HTMLReader");
				}
				Runtime.getRuntime().exec(cmdArray);
			} catch ( Exception eee ) { 
				System.out.println("ini 파일에서 지정한 TextReader [" + cmdArray[0] + "]가 존재하지 않습니다");
				System.exit(99);				
			}
		}
		if (action==1) {
			try {
				if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI("file:///" + fname));
//				Desktop.getDesktop().open(new File(outFileNameReal));
//				Desktop.getDesktop().edit(new File(outFileNameReal));
//				Desktop.getDesktop().print(new File(outFileNameReal));
				}
			} catch ( Exception eee ) { eee.printStackTrace()  ;	}
		}
		if (action==2) {
			try {
				if (Desktop.isDesktopSupported()) {
//				Desktop.getDesktop().browse(new URI("file:///" + fname));
				Desktop.getDesktop().open(new File(outFileNameReal));
//				Desktop.getDesktop().edit(new File(outFileNameReal));
//				Desktop.getDesktop().print(new File(outFileNameReal));
				}
			} catch ( Exception eee ) { eee.printStackTrace()  ;	}
		}
		if (action==3) {
			try {
				if (Desktop.isDesktopSupported()) {
//				Desktop.getDesktop().browse(new URI("file:///" + fname));
//				Desktop.getDesktop().open(new File(outFileNameReal));
				Desktop.getDesktop().edit(new File(outFileNameReal));
//				Desktop.getDesktop().print(new File(outFileNameReal));
				}
			} catch ( Exception eee ) { eee.printStackTrace()  ;	}
		}
		if (action==4) {
			try {
				if (Desktop.isDesktopSupported()) {
//				Desktop.getDesktop().browse(new URI("file:///" + fname));
//				Desktop.getDesktop().open(new File(outFileNameReal));
//				Desktop.getDesktop().edit(new File(outFileNameReal));
				Desktop.getDesktop().print(new File(outFileNameReal));
				}
			} catch ( Exception eee ) { eee.printStackTrace()  ;	}
		}
		return rts;
	};
}  // StringDump

