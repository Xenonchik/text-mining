import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import crawl.Controller;
import db.LjDAO;
import db.SongDAO;
import db.TextAnalyser;
import ncrawl.Crawler;
import ncrawl.LjCrawler;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class TMApp2 {
	public static void main(String[] args) throws Exception {
		//totalWordsCount();
		clustWithCarrot();
	}

	public static void newCrawl(String url) {
		Crawler crawler = new LjCrawler();
		crawler.getUrls().offer(url);
		crawler.getUrlsPatterns().add(Pattern.compile("^http\\:\\/\\/feministki\\.livejournal\\.com\\/\\d+\\.html$"));
		crawler.getUrlsPatterns().add(Pattern.compile("http\\:\\/\\/feministki\\.livejournal\\.com\\/\\?skip\\=\\d+"));
		crawler.getUrlsPatterns().add(Pattern.compile("http\\:\\/\\/feministki\\.livejournal\\.com\\/\\/\\d\\d\\d\\d\\/\\d\\d\\/\\d\\d"));
		crawler.crawl();
	}

    public static void clustWithCarrot() {

        List<Document> documents;

        final org.carrot2.core.Controller controller =
                ControllerFactory.createSimple();//<co id="crt2.controller.creation"/>
        documents = new ArrayList<Document>();

        DBCursor cursor = new LjDAO().getAll();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            String name = (String) item.get("title");
            String text = (String) item.get("text");
            Document doc = new Document(name, text,
                    "file://foo_" + name + ".txt");
            documents.add(doc);
        }

        final ProcessingResult result = controller.process(documents,
                "red fox",
                LingoClusteringAlgorithm.class);
        displayResults(result);
    }



    private static void displayResults(ProcessingResult result) {
        Collection<Cluster> clusters = result.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Cluster: " + cluster.getLabel());
            for (Document document : cluster.getDocuments()) {
                System.out.println("\t" + document.getTitle());
            }
        }

    }

    public static void totalWordsCount() throws Exception {

        TextAnalyser analyser = new TextAnalyser();
        Map<String, Integer> wordsCount = analyser.getWordsCount();

        Scanner s = new Scanner( Paths.get(TMApp.class.getResource("stopwords-ru.txt").toURI()).toFile() );
        while (s.hasNext()){
            String word = s.next();
            if(wordsCount.containsKey(word)) {
                wordsCount.remove(word);
            }
        }
        s.close();

        TextAnalyser.ValueComparator bvc =  new TextAnalyser.ValueComparator(wordsCount);
        TreeMap<String,Integer> sorted_map = new TreeMap<>(bvc);
        sorted_map.putAll(wordsCount);

        int count = 1;
        for(Entry<String, Integer> entry : sorted_map.entrySet()) {
            System.out.println(count + " " + entry.getKey() + ": " + entry.getValue());
            count++;
			if(count > 100) break;
        }
    }

}
