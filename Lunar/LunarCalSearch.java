/*
  천문연구원 서버에 공공API 접속하여 음력 날짜 가져옴
*/
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.time.*;
import java.net.*;
import java.time.format.*;
import org.apache.commons.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.awt.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.json.simple.*;
import org.json.simple.parser.*;

// 참고 주소 :
// https://www.data.go.kr/iim/api/selectAPIAcountView.do
// commons-io-2.15.0.jar

public class LunarCalSearch {
    private static boolean TestMode = false;
	private static String  lunIljin  = "..";
    private static String  lunLeapmonth  = "..";
    private static int  lunMonth  = 0;
    private static int  lunMaxDay  = 0;
    private static int  lunDay  = 0;
    private static String  lunSecha  = "..";
    private static String  lunWolgeon  = "..";
    private static int  lunYear  = 0;
    private static int  solDay  = 0;
    private static String  solJd  = "..";
    private static String  solLeapyear  = "..";
    private static int  solMonth  = 0;
    private static String  solWeek  = "..";
    private static int  solYear  = 0;
	final private static String [] Yoil = new String[] {  "일", "월", "화", "수", "목", "금", "토"   };
	final private static String [] Ganji = new String[] {
		"갑자(甲子)", "을축(乙丑)", "병인(丙寅)", "정묘(丁卯)", "무진(戊辰)", "기사(己巳)", "경오(庚午)", "신미(辛未)",
		"임신(壬申)", "계유(癸酉)", "갑술(甲戌)", "을해(乙亥)", "병자(丙子)", "정축(丁丑)", "무인(戊寅)", "기묘(己卯)",
		"경진(庚辰)", "신사(辛巳)", "임오(壬午)", "계미(癸未)", "갑신(甲申)", "을유(乙酉)", "병술(丙戌)", "정해(丁亥)",
		"무자(戊子)", "기축(己丑)", "경인(庚寅)", "신묘(辛卯)", "임진(壬辰)", "계사(癸巳)", "갑오(甲午)", "을미(乙未)",
		"병신(丙申)", "정유(丁酉)", "무술(戊戌)", "기해(己亥)", "경자(庚子)", "신축(辛丑)", "임인(壬寅)", "계묘(癸卯)",
		"갑진(甲辰)", "을사(乙巳)", "병오(丙午)", "정미(丁未)", "무신(戊申)", "기유(己酉)", "경술(庚戌)", "신해(辛亥)",
		"임자(壬子)", "계축(癸丑)", "갑인(甲寅)", "을묘(乙卯)", "병진(丙辰)", "정사(丁巳)", "무오(戊午)", "기미(己未)",
	"경신(庚申)", "신유(辛酉)", "임술(壬戌)", "계해(癸亥)" };
	private static String sFormat = "[양력] %4d/%2d/%2d(%s)(%s年)  [음력] %4d/%2d/%2d(%s달)%s  %s年 %s月 %s日";
	private static String sFormat1 = "(%s年) [음력] %4d/%2d/%2d(%s달)%s年 %s月 %s日";
	//  static String sKey = "hYBE5TszUHVub0cgx%2FHsdrHZdg7uDnfCtcQi45I%2BG6QRk58leN6m1ZVdi2FWfyfwtxo3ccYYzlvc5OoVmgpJmw%3D%3D";
	static String sKey = "9232525b8c0e1d5ff6d274ba9302920248ae9df00e592a06d8f868402c42e110";
	static String sHttp = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo"; /*URL*/
	public LunarCalSearch (){};
	
	public static void main(String[] args) throws Exception {
	
		String result = "";
		LocalDateTime now =  LocalDateTime.now();
		int aYY = now.getYear() ;
		int aMM = now.getMonthValue()  ;
		switch ( args.length ) {
			case ( 3 ) :
			result = run(args[0], args[1],  args[2]) ;
			break;
			case ( 2 ) :
			result = run(args[0], args[1], "" ) ;
			break;
			case ( 1 ) :
			if  ( args[0].equals("+")) {
				aMM++;
				if ( aMM > 12 ) {
					aMM = 1;
					aYY++;
				}
				result = run(aYY+"",aMM+"" , "" ) ;
			}
			if  ( args[0].equals("-")) {
				aMM--;
				if ( aMM < 1 ) {
					aMM = 12;
					aYY--;
				}
				result = run(aYY+"",aMM+"" , "" ) ;
			}
			result = run(args[0], "" , "" ) ;
			break;
			case ( 0 ) :
			result = run( "" , "" , "" );
			break;
		}
		System.out.print(result);
	}
	
    public static String Lunar0(int YYYY,int MM,int DD , boolean today )  throws Exception {
		return ( Lunar (YYYY+"", MM+"" , DD+"" , today	));
	}
	
    public static String Lunar(String YYYY,String MM,String DD , boolean today )  throws Exception {
		String rts = null;
		String todayFlag = "..";
		int intYYYY = 0 ;
		int intMM = 0 ;
		int intDD = 0 ;
		if  (today)   {
			todayFlag = "■";
		}
		intYYYY = Integer.parseInt(YYYY);
		intMM = Integer.parseInt(MM);
		intDD = Integer.parseInt(DD);
		if (( intYYYY < 1392 ) ||  ( intYYYY > 2050 )) {
			System.err.format("[ 음력 계산 한도 (1392-2050) 초과 Error (1)] %s %s %s",YYYY, MM, DD );
			return rts;
		}
		if (( intYYYY == solYear ) && ( intMM == solMonth ) && ( solDay == intDD )) {
			/*
				rts = String.format (sFormat,
				solYear, solMonth, solDay, solWeek,solLeapyear, lunYear,lunMonth, lunDay, lunLeapmonth,
				lunSecha,lunWolgeon,lunIljin);
			*/
			rts = String.format (sFormat1,solLeapyear, lunYear,lunMonth, lunDay, lunLeapmonth,
			todayFlag + lunSecha,lunWolgeon,lunIljin);
			//  System.out.print(rts);
			return rts;
		}
		if (( intYYYY == solYear ) && ( intMM == solMonth ))  {
			int offSet = intDD - solDay;
			if (( lunDay + offSet ) <= lunMaxDay ) {
				lunIljin  = GanjiSearch ( lunIljin , offSet ) ;
                lunDay  = lunDay + offSet;
                solDay  = intDD;
                solJd  = "..";
                solWeek  = YoilSearch ( solWeek , offSet ) ;
				/*
					rts = String.format (sFormat,
					solYear, solMonth, solDay, solWeek,solLeapyear, lunYear,lunMonth, lunDay, lunLeapmonth,
					lunSecha,lunWolgeon,lunIljin);
				*/
				rts = String.format (sFormat1,solLeapyear, lunYear,lunMonth, lunDay, lunLeapmonth,
				todayFlag + lunSecha,lunWolgeon,lunIljin);
				// System.out.print(rts);
				return rts;
			}
		}
		try {
			LocalDateTime SomeDate = LocalDateTime.of(intYYYY, intMM, intDD , 0, 0, 0, 0);
			}	catch (Exception eee) {
			System.err.format("[ 날짜 Error ] %s %s %s \n",YYYY, MM, DD );
			return rts;
			//			eee.printStackTrace();
		} finally {	}
		if (MM.length() < 2) {
			MM = "0" + MM;
		}
		if (DD.length() < 2) {
			DD = "0" + DD;
		}
		if (YYYY.length() == 2) {
			YYYY = "20" + YYYY;
		}
		//      StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService/getLunCalInfo"); /*URL*/
        StringBuilder urlBuilder = new StringBuilder(sHttp);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + sKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(YYYY, "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(MM, "UTF-8")); /*월*/
		if (TestMode) {			} else {
			urlBuilder.append("&" + URLEncoder.encode("solDay","UTF-8") + "=" + URLEncoder.encode(DD, "UTF-8")); /*일*/
		}
        URL url = URI.create(urlBuilder.toString()).toURL();
        HttpURLConnection conn = null;
		DocumentBuilderFactory fac = null;
		DocumentBuilder bd = null;
		Document doc = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			fac = DocumentBuilderFactory.newInstance();
			bd = fac.newDocumentBuilder();
			doc = null;
			if (TestMode) {
				int getResponseCode = 0 ;
				BufferedReader rd;
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					} else {
					rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				}
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
					// System.out.println(line);
					rts = rts + line + "\n";
				}
				rd.close();
				return rts;
			} // TEST MODE
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				doc = bd.parse(conn.getInputStream());
				} else {
				rts = "[Error] HTTP " + conn.getResponseCode();
				System.err.println(rts);
				conn.disconnect();
				return rts;
			}
			rts = MyParse(doc, todayFlag);
			/*
				if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				doc = bd.parse(conn.getInputStream());
				}
				rts = MyParse(doc,todayFlag);
			*/
			if (rts.contains("Error")){
				rts = rts + " : " + YYYY + "/" + MM + "/" + DD;
				System.err.println(rts);
				// } else {
				// System.out.print(rts);
			}
			}	catch (Exception eee) {
			rts = "[ MyParse Error ]\n\n" ;
			System.err.println("[ MyParse Error ]\n\n" );
			eee.printStackTrace();
		} finally {	}
        conn.disconnect();
		return rts;
	}
	
	static String  MyParse(Document doc,String todayFlag)  throws Exception {
	    if (doc == null) return "[Error] doc is null";
		//  https://www.data.go.kr/data/15012679/openapi.do
		String rts = null;
		boolean bOk = false; // <successYN>Y</successYN> 획득 여부
		String ss1 = "";
		String ss2 = "";
		String nn = "";
		Node nd;
		NodeList ns = doc.getElementsByTagName("body");
		if (ns.getLength() <= 0) {
			return "[Error] API comm error(1)";
		}
		for (nd = ns.item(0).getFirstChild(); nd != null; nd = nd.getNextSibling())
		{
			nn = nd.getNodeName();
			if (nn.equals("totalCount")) // 성공 여부
			{
				int totalCount  = Integer.parseInt(nd.getTextContent());
				if ( totalCount == 0 ) {
					rts = "[Error] 날짜 오류";
					//					System.out.println("44444 rts = " + rts);
					bOk = false; // <successYN>Y</successYN> 획득 여부
					return rts;
				}
			}
		}
		ns = doc.getElementsByTagName("header");
		if (ns.getLength() <= 0) {
			return "[Error] API comm error(1)";
		}
		for (nd = ns.item(0).getFirstChild(); nd != null; nd = nd.getNextSibling())
		{
			//			System.out.println("aaaaaaaaaaaaa : " + nd.toString());
			nn = nd.getNodeName();
			if (!bOk)
			{
				//				System.out.println("============ OK  ");
				if (nn.equals("resultCode")) // 성공 여부
				{
					ss1 = nd.getTextContent();
					//					System.out.println("11111 OK : ss1 " + ss1);
					if (ss1.equals("00"))
					{
						//						System.out.println("22222 OK : ss1 " + ss1);
						bOk = true; // 검색 성공
					}
				}
				if (nn.equals("totalCount")) // 전체 검색수
				{
					int totalCount  = Integer.parseInt(nd.getTextContent());
					if ( totalCount == 0 ) {
						rts = "[Error] 날짜 오류";
						//  System.out.println("44444 rts = " + rts);
						bOk = false; // <successYN>Y</successYN> 획득 여부
					}
				}
				if (nn.equals("resultMsg")) //
				{
					// System.out.println("33333 : ss1 " + ss1);
					rts = "[Error] " + nd.getTextContent();
					// System.out.println("44444 rts = " + rts);
					bOk = false; // <successYN>Y</successYN> 획득 여부
				}
			}
			else
			{
				if (nn.equals("totalCount")) // 전체 검색수
				{
					int totalCount  = Integer.parseInt(nd.getTextContent());
					if ( totalCount == 0 ) {
						rts = "[Error] 날짜 오류";
						// System.out.println("44444 rts = " + rts);
						bOk = false; // <successYN>Y</successYN> 획득 여부
					}
				}
			}
		}
		if (bOk)
		{
			ns = doc.getElementsByTagName("item");
			for (int pp = 0; pp < ns.getLength(); pp++){
				//					System.out.println(" pp = " + pp);
				for (nd = ns.item(pp).getFirstChild(); nd != null; nd = nd.getNextSibling())
				{
					nn = nd.getNodeName();
					//				System.out.println(" >>>>>>>>> nn = " + nn);
					//				System.out.println(" >>>>>>>>> nd = " + nd.getTextContent());
					if (nn.equals("lunIljin")) {
						lunIljin = nd.getTextContent();
					}
					else if (nn.equals("lunLeapmonth")) {
						lunLeapmonth = nd.getTextContent();
					}
					else if (nn.equals("lunMonth")) {
						lunMonth = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("lunNday")) {          //   lunMaxDay
						lunMaxDay = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("lunDay")) {
						lunDay = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("lunSecha")) {
						lunSecha = nd.getTextContent();
					}
					else if (nn.equals("lunWolgeon")) {
						lunWolgeon = nd.getTextContent();
					}
					else if (nn.equals("lunYear")) {
						lunYear = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("solDay")) {
						solDay = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("solJd"))		{
						solJd = nd.getTextContent();
					}
					else if (nn.equals("solLeapyear")) {
						solLeapyear = nd.getTextContent();
					}
					else if (nn.equals("solMonth")) {
						solMonth = Integer.parseInt(nd.getTextContent());
					}
					else if (nn.equals("solWeek")) {
						solWeek = nd.getTextContent();
					}
					else if (nn.equals("solYear")) {
						solYear = Integer.parseInt(nd.getTextContent());
					}
					//	if (nn.equals("zipNo") || // 우편번호
					//		nn.equals("lnmAdres") || // 도로명 주소
					//		nn.equals("rnAdres")) // 지번 주소
					//	{
					//   v.add(nd.getTextContent());
					//	}
				}
			}
		}
		if ( rts == null ) {
			/*
				rts = String.format (sFormat,
				solYear, solMonth, solDay, solWeek,solLeapyear, lunYear,lunMonth, lunDay,  lunLeapmonth,
				lunSecha,lunWolgeon,lunIljin);
			*/
			rts = String.format (sFormat1,	solLeapyear, lunYear,lunMonth, lunDay, lunLeapmonth,
			todayFlag + lunSecha,lunWolgeon,lunIljin);
		}
		return rts;
	}
	
	public static String YoilSearch ( String sss , int offSet ) {
		int kkk = 0 ;
		for ( kkk=0;kkk<7;kkk++ ) {
			if ( sss.compareToIgnoreCase(Yoil[kkk])==0 ) {
				break;
			}
		}
		int newkkk = kkk + offSet ;
		if  ( newkkk >= 7 ) {
			newkkk = newkkk - 7;
		}
		return ( Yoil [newkkk] );
	}
	
	public static String GanjiSearch ( String sss , int offSet ) {
		int kkk = 0 ;
		for ( kkk=0;kkk<60;kkk++ ) {
			if ( sss.compareToIgnoreCase(Ganji[kkk])==0 ) {
				break;
			}
		}
		int newkkk = kkk + offSet ;
		if  ( newkkk >= 60 ) {
			newkkk = newkkk - 60;
		}
		return ( Ganji [newkkk] );
	}
	// ************************************
	public static String run(String argYY, String argMM, String argDD ) throws Exception {
	// ************************************
		String result = "\n";
		boolean today = false;
		String  todayFlag = "__";
		LocalDateTime now =  LocalDateTime.now();
		int nowYY = now.getYear() ;
		int nowMM = now.getMonthValue()  ;
		int nowDD = now.getDayOfMonth()  ;
		if  (( argYY == "" ) && ( argMM == "" ) && ( argDD == "" )) {
			argYY = now.format(DateTimeFormatter.ofPattern("yyyy"));
			argMM = now.format(DateTimeFormatter.ofPattern("MM"));
		}
		if  ( (argYY != "") &&  (argMM != "") &&  (argDD != "") ) {
			Lunar(argYY, argMM, argDD , today );
			return "" ;
		}
		if  ( argYY == "" ) {
			return "" ;
		}
		String myFormatter = "DDD) yyyy/MM/dd (E) , ww주";
		String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		// System.out.println(nowStr);
		int thisYY = 0;
		int thisMM = 0;
		int thisDD = 0;
		try {
			if  ( (argYY != "") &&  (argMM != "") &&  (argDD == "") ) {
				thisYY = Integer.parseInt(argYY);
				thisMM = Integer.parseInt(argMM);
				thisDD = 1;
			}
			if  ( (argYY != "") &&  (argMM == "") &&  (argDD == "") ) {
				thisYY = Integer.parseInt(argYY);
				thisMM = 1;
				thisDD = 1;
			}
			LocalDateTime SomeDate = LocalDateTime.of(thisYY, thisMM, thisDD , 0, 0, 0, 0);
			}	catch (Exception eee) {
			System.err.format("[ 날짜 Error ] \n" );
			return "";
			//			eee.printStackTrace();
		} finally {	}
		// System.out.println("This yy : " + thisYY );
		// System.out.println("This MM : " + thisMM );
		// System.out.println("This DD : " + thisDD );
        LocalDateTime firstDate = LocalDateTime.of(thisYY, thisMM, 1 , 0, 0, 0, 0);
		int cYY = 0;
		int cMM = 0;
		int cDD = 0;
		StringDump  ddd = new StringDump();
		ddd.DumpLines = new String[366];
		for (int kkk=0;kkk<=999;kkk++){
			LocalDateTime nextDay = firstDate.plusDays(kkk);
			int nYY = nextDay.getYear() ;
			int nMM = nextDay.getMonthValue()  ;
			int nDD = nextDay.getDayOfMonth()  ;
			int dayOfWeek = nextDay.getDayOfWeek().getValue();
			if (( nYY == nowYY ) && ( nMM == nowMM ) && ( nDD == nowDD ) ) {
				today = true;
				todayFlag = "■■";
				} else {
				today = false;
				todayFlag = "    ";
			}
			if ( cYY != 0 ) {
				if ( cYY != nYY ) {
					break;
				}
			}
			if  ( (argYY != "") &&  (argMM != "") &&  (argDD == "") ) {
				if ( cMM != 0 ) {
					if ( cMM != nMM ) {
						break;
					}
				}
			}
			String PrintLine = "" ;
			String nextDaySSS = nextDay.format(DateTimeFormatter.ofPattern(myFormatter));
			if ( dayOfWeek == 7 ) {
				PrintLine = "\n" + todayFlag + nextDaySSS ;
				// System.out.print(PrintLine);
				} else {
				PrintLine = todayFlag + nextDaySSS ;
				// System.out.print(PrintLine);
			}
			if  (( (argYY != "") &&  (argMM != "") &&  (argDD == "")  ) && ( TestMode )) {
				if ( nDD == 1 ) {
					PrintLine = PrintLine + Lunar0(nYY, nMM, nDD , today);
				}
				} else {
				PrintLine = PrintLine + Lunar0(nYY, nMM, nDD , today);
			}
			PrintLine = PrintLine + " " + todayFlag ;
			ddd.DumpLines[kkk] = PrintLine;
			ddd.totalTableLines = kkk + 1;
			System.out.println( " " + todayFlag);
			result = result + "\n" + PrintLine +  " " + todayFlag ;
			cYY = nYY;
			cMM = nMM;
			cDD = nDD;
		} // for
		ddd.outFileName = "calendar_" + argYY + argMM ;
		ddd.StringDumpWrite(true);
		return result;
	}
}  // class  LunarCalSearch
/*
	>>>>>>>>> nn = lunIljin
	>>>>>>>>> nd = 을축(乙丑)
	>>>>>>>>> nn = lunLeapmonth
	>>>>>>>>> nd = 평
	>>>>>>>>> nn = lunMonth
	>>>>>>>>> nd = 11
	>>>>>>>>> nn = lunNday  // lunMaxDay
	>>>>>>>>> nd = 29
	>>>>>>>>> nn = lunSecha
	>>>>>>>>> nd = 계묘(癸卯)
	>>>>>>>>> nn = lunWolgeon
	>>>>>>>>> nd = 갑자(甲子)
	>>>>>>>>> nn = lunYear
	>>>>>>>>> nd = 2023
	>>>>>>>>> nn = solDay
	>>>>>>>>> nd = 02
	>>>>>>>>> nn = solJd
	>>>>>>>>> nd = 2460312
	>>>>>>>>> nn = solLeapyear
	>>>>>>>>> nd = 윤
	>>>>>>>>> nn = solMonth
	>>>>>>>>> nd = 01
	>>>>>>>>> nn = solWeek
	>>>>>>>>> nd = 화
	>>>>>>>>> nn = solYear
	>>>>>>>>> nd = 2024
*/