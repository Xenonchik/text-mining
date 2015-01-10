package ncrawl;

import com.mongodb.BasicDBObject;
import db.LjDAO;
import db.TextAnalyser;
import org.jsoup.nodes.Document;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class LjCrawler extends Crawler {
	LjDAO itemDAO = new LjDAO();
	@Override
	protected void doSomething(final Document doc) {
		String url = doc.baseUri();
		System.out.println(url);
		if(getUrlsPatterns().get(0).matcher(url).find()) {
			if(doc.select("div#content h2").first() == null) return;
			String date = doc.select("div#content h2").first().text();
			String title = doc.select("div.H3Holder h3 span em").first() == null ? "" : doc.select("div.H3Holder h3 span em").first().text();
			String text = doc.select("div.H3Holder").text();
			if (text != null && text != "") {
				// save to db

				BasicDBObject item = new BasicDBObject("date", date).append("title", title).append("url", url)
						.append("text", text).append("wordsCount", TextAnalyser.countWordsInText(text));

				itemDAO.save(item);
			}
		}
	}
}