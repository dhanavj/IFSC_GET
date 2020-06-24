import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.FileWriter;

public class check {
	public static void main(String[] args) throws Exception
	{
		String location = "RBI_Ifsc.xls";
		FileWriter writer = new FileWriter(location);
		writer.append("Bank Name");
	    writer.append('\t');
	    writer.append("Address");
	    writer.append('\t');
	    writer.append("IFSC");
	    writer.append('\n');
		Scanner sc = new Scanner(System.in);
		for(int i=1;i<=154734;i++)
		{
		String url = "https://www.rbi.org.in/Scripts/IFSCDetails.aspx?pkid="+i;
	    URL obj = new URL(url);
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    System.setProperty("https.proxyHost", "myProxy");
	    System.setProperty("https.proxyPort", "80");
	    con.setRequestMethod("GET");
	    con.setRequestProperty("User-Agent", "Mozilla/5.0");
	    con.setRequestProperty("Accept-Charset", "ISO-8859-1"); 
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    sc.close();
	    int flag = 0,ii=0;
	    String temp = "";
	    while ((inputLine = in.readLine()) != null) 
	    {
	    	if(inputLine.contains("pnlDetails"))
	    	{
	    		flag = 1;
	    	}
	    	else if(inputLine.contains("/div")) 
	    	{
	    		flag = 0;
	    	}
	    	if(flag==1)
	    	{
	    		inputLine = inputLine.replaceAll("\\<.*?\\>", "").trim();
	    		if(inputLine.length()!=0)
	    		{
		    		temp+= (ii!=1)?(inputLine)+"?":(inputLine);
		    		ii++;
		    	}
	    	}
	    	if(ii==2)
	    	{
	    		writer.append(temp.substring(temp.indexOf("?")+1,temp.length()));
		   	    writer.append('\t');
	    		temp="";
	    		ii=0;
	    	}	
	    }
	    writer.append('\n');
	    System.out.println(i);
		}
		writer.flush();
	    writer.close();
	}
}