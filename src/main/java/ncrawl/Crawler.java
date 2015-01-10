package ncrawl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

abstract public class Crawler {

	private List<Pattern> urlsPatterns = new ArrayList<>();

	private Queue<String> urls = new LinkedBlockingQueue<>();
	private Map<String, Integer> visitedUrls = new HashMap<>();

	public void crawl(){
		String currentUrl = urls.poll();
		if(currentUrl == null) {
			return;
		}
		parsePage(currentUrl);
	}

	public void parsePage(String url) {
		if(url == null) return;
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Document doc = Jsoup.connect(url).get();

			findUrls(doc);

			doSomething(doc);



		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		parsePage(urls.poll());
	}

	protected void doSomething(final Document doc) {System.out.println(doc.baseUri());}

	private void findUrls(Document doc) {
		Elements elements = doc.select("a");
		for(Element el : elements) {
			String href = el.attr("href");
			if(urlIsOk(href)){
				urls.offer(href);
			}
		}
	}

	protected Boolean urlIsOk(String url) {
		if (visitedUrls.get(url) != null) {
			return false;
		}
		visitedUrls.put(url, 1);
		for (Pattern pattern : urlsPatterns) {
			if(pattern.matcher(url).matches()){
				return true;
			}
		}
		return false;
	}

	public List<Pattern> getUrlsPatterns() {
		return urlsPatterns;
	}

	public Queue<String> getUrls() {
		return urls;
	}
}
