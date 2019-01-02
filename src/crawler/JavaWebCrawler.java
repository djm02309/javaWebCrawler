package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class JavaWebCrawler {
	private static Element getContract(String[] addressArray, int pagenumber) throws IOException {
		for (int i = 0; i < addressArray.length; i++) {
			String fileName = addressArray[i].substring(addressArray[i].lastIndexOf("/") + 1, addressArray[i].lastIndexOf("#"));
			
			HttpPost http = new HttpPost("https://etherscan.io" + addressArray[i]);
		    HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpResponse response = httpClient.execute(http);
		    HttpEntity entity = response.getEntity();
		    ContentType contentType = ContentType.getOrDefault(entity);
	        Charset charset = contentType.getCharset();
		    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
		    StringBuffer sb = new StringBuffer();
		    String line = "";
		    while((line=br.readLine()) != null){
		    	sb.append(line+"\n");
		    }
//		    System.out.println(sb.toString());
		    Document doc = Jsoup.parse(sb.toString());
			
			
//			Document doc = Jsoup.connect("https://etherscan.io" + addressArray[i]).get();
			
			Element pre = doc.select(".js-sourcecopyarea").first();
			String sourceCode = pre.text().toString();
			String path = "C:\\Users\\user\\Desktop\\scCode\\page" + pagenumber ;
			File file = new File(path); // 경로
																													// 설정하기
			if (!file.exists()) {
				file.mkdirs();
			}
			FileWriter writer = null;

			try {
				// 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
				writer = new FileWriter(file + "\\"+fileName + ".txt", false);
				writer.write(sourceCode);
				writer.flush();

				System.out.println(fileName+"  DONE");
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			finally {
				try {
					if (writer != null)
						writer.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	public static String getCurrentData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpPost http = new HttpPost("https://etherscan.io/contractsVerified");
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    HttpResponse response = httpClient.execute(http);
	    HttpEntity entity = response.getEntity();
	    ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
	    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
	    StringBuffer sb = new StringBuffer();
	    String line = "";
	    while((line=br.readLine()) != null){
	    	sb.append(line+"\n");
	    }
//	    System.out.println(sb.toString());
	    Document doc = Jsoup.parse(sb.toString());
	    
//		Document doc2 = Jsoup.connect("https://etherscan.io/contractsVerified").timeout(30000).get();
//		Elements page = doc2.select("a");
		Elements page = doc.select("a");
		Element aggg = page.get(53);
		String lastPage = aggg.attr("href");

		String length = lastPage.substring(lastPage.lastIndexOf("/") + 1);
//		for (int pagenumber = 1; pagenumber <= 2; pagenumber++) {
		for (int pagenumber = 356; pagenumber <= Integer.parseInt(length); pagenumber++) {
			
			HttpPost http2 = new HttpPost("https://etherscan.io/contractsVerified/"+ pagenumber);
		    HttpClient httpClient2 = HttpClientBuilder.create().build();
		    HttpResponse response2 = httpClient2.execute(http2);
		    HttpEntity entity2 = response2.getEntity();
		    ContentType contentType2 = ContentType.getOrDefault(entity2);
	        Charset charset2 = contentType2.getCharset();
		    BufferedReader br2 = new BufferedReader(new InputStreamReader(entity2.getContent(), charset2));
		    StringBuffer sb2 = new StringBuffer();
		    String line2 = "";
		    while((line2=br2.readLine()) != null){
		    	sb2.append(line2+"\n");
		    }
//		    System.out.println(sb.toString());
		    Document doc2 = Jsoup.parse(sb2.toString());
			
			
//			Document pages = Jsoup.connect("https://etherscan.io/contractsVerified/"+ pagenumber).get();
//			Elements addressOfContracts = pages.select("table").select("a").select(".address-tag");
			Elements addressOfContracts = doc2.select("table").select("a").select(".address-tag");
			String[] addressArray = new String[addressOfContracts.size()];

//			<1번 방법>
//			int i = 0;
//			for (Element addressIndex : addressOfContracts) {
//				addressArray[i] = addressIndex.attr("href");
//				i++;
//			}
//			<2번방법>
			for(int i = 0; i < addressOfContracts.size(); i++) {
				addressArray[i] = addressOfContracts.get(i).attr("href");
			}
			getContract(addressArray, pagenumber); 
			System.out.println(" End "+ pagenumber);
		}

	}
}
