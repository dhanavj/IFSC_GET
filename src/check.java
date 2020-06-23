import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class check {
	public static void main(String[] args) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter IFSC code:");
		String ifs = sc.next();
		String url = "https://ifsc-finder.com/IFSC/"+ifs;
	    URL obj = new URL(url);
	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    // optional default is GET
	    con.setRequestMethod("GET");
	    //add request header
	    con.setRequestProperty("User-Agent", "Mozilla/5.0");
	    con.setRequestProperty("Accept-Charset", "ISO-8859-1"); 
	    int responseCode = con.getResponseCode();
	    System.out.println("\nSending 'GET' request to URL : " + url);
	    System.out.println("Response Code : " + responseCode);
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    HashMap<String,String> ifsc = new HashMap<>();
	    String temp="";
	    int i=0;
	    sc.close();
	    boolean flag = false;
	    while ((inputLine = in.readLine()) != null) {
	    	if(inputLine.contains("/tbody"))
	    	{
	    		flag = false;
	    	}
	    	else if(inputLine.contains("tbody"))
	    	{
	    		flag = true;
	    	}
	    	if(flag)
	    	{
	    		inputLine = inputLine.replaceAll("\\<.*?\\>", "");
	    		if(inputLine.length()!=0 && !inputLine.contains("Click here for all the branches"))
	    		{
		    		temp+=inputLine;
		    		i++;
		    		if(i==1) temp+="?";
		    	}
	    	}
	    	if(i==2)
	    	{
	    		ifsc.put(temp.substring(0, temp.indexOf("?")),temp.substring(temp.indexOf("?")+1));
	    		temp="";
	    		i=0;
	    	}
	    }
	    in.close();
	    ifsc.remove("CITY , DISTRICT , STATE");
	    if(ifsc.size()==0)
	    {
	    	System.out.println("Wrong IFSC Code");
	    }
	    else
	    {
	    	System.out.println(ifsc.toString());
	    }
	}
}