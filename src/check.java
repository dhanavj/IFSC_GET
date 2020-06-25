import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileWriter;

class MyRunnable implements Runnable {
	private final String url;
	private final FileWriter writer;
	private final ArrayList<String> list;
	MyRunnable(String url,FileWriter writer,ArrayList<String> list) {
		this.url = url;
		this.writer = writer;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			String str = "";
			URL obj = new URL(url);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		    con.setRequestMethod("GET");
		    con.setRequestProperty("User-Agent", "Mozilla/5.0");
		    con.setRequestProperty("Accept-Charset", "ISO-8859-1"); 
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(con.getInputStream()));
		    String inputLine;
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
		    		str+=(temp.substring(temp.indexOf("?")+1,temp.length())+'\t');
		    		temp="";
		    		ii=0;
		    	}	
		    }
		    writer.append(str);
		    writer.append('\n');
		    writer.flush();
		    //System.out.println(url+"====>true");
		    con.disconnect();
		}
		catch(Exception e)
		{
			list.add(url);
			System.out.println(url+"====>false");
			//e.printStackTrace();
		}
	}	
}




public class check {
	public static void main(String[] args) throws Exception
	{
		ExecutorService executor = Executors.newFixedThreadPool(10);
		String location = "RBI_Ifsc.xls";
		FileWriter writer = new FileWriter(location);
		writer.append("Bank Name");
	    writer.append('\t');
	    writer.append("Address");
	    writer.append('\t');
	    writer.append("IFSC");
	    writer.append('\n');
	    ArrayList<String> list = new ArrayList<>();
		for(int i=1;i<=154734;i++)
		{
			String url = "https://www.rbi.org.in/Scripts/IFSCDetails.aspx?pkid="+i;
			Runnable worker = new MyRunnable(url,writer,list);
			executor.execute(worker);
			Thread.sleep(5);
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
 
		}
		System.out.println(list);
		/*int i=0;
		while(list.size()!=0)
		{
			String url = list.get(i++);
			Runnable worker = new MyRunnable(url,writer,list);
			executor.execute(worker);
			Thread.sleep(10);
		}*/
		System.out.println("\nFinished all threads");
	    writer.close();
	}
}