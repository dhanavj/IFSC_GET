import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileWriter;

class MyRunnable implements Runnable {
	private final String url;
	private final FileWriter writer;

	MyRunnable(String url,FileWriter writer){
		this.url = url;
		this.writer = writer;
	}

	@Override
	public void run() {
		try {
			String record_string = "";
			URL obj = new URL(url);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		    con.setRequestMethod("GET");
		    con.setRequestProperty("User-Agent", "Mozilla/5.0");
		    con.setRequestProperty("Accept-Charset", "ISO-8859-1"); 
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(con.getInputStream()));
		    String inputLine;
		    int flag = 0,count=0;
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
			    		temp+= (count!=1)?(inputLine)+"?":(inputLine);
			    		count++;
			    	}
		    	}
		    	if(count==2)
		    	{
		    		record_string+=(temp.substring(temp.indexOf("?")+1,temp.length())+'\t');
		    		temp="";
		    		count=0;
		    	}	
		    }
		    writer.append(record_string+'\n');
		    writer.flush();
		    System.out.println(url+"====>true");
		    con.disconnect();
		}
		catch(Exception e)
		{
			System.out.println(url+"====>false");
		}
	}	
}




public class check {	
	public static void main(String[] args) throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(15);
		String location = "RBI_Ifsc.xls";
		FileWriter writer = new FileWriter(location);
		writer.append("Bank Name");
	    writer.append('\t');
	    writer.append("Address");
	    writer.append('\t');
	    writer.append("IFSC");
	    writer.append('\n');
	    final long startTime = System.nanoTime();
		for(int i=1;i<=154734;i++)
		{
			String url = "https://www.rbi.org.in/Scripts/IFSCDetails.aspx?pkid="+i;
			Runnable worker = new MyRunnable(url,writer);
			executor.execute(worker);
			Thread.sleep(5);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
 
		}
		final long duration = System.nanoTime() - startTime;
		System.out.println(duration/1000000000);
		System.out.println("\nFinished all threads");
	    writer.close();
	}
}