import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import crawl.Controller;
import db.TextAnalyser;
import db.SongDAO;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.ProcessingResult;

import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class TMApp {
	public static void main(String[] args) throws Exception {
        String url = "http://megalyrics.ru/songs/ariia.htm";
        //parseAndGetTotalWordsCount(url);
       clustWithCarrot();

	}

    public static void clustWithCarrot() {

        List<Document> documents;

        final org.carrot2.core.Controller controller =
                ControllerFactory.createSimple();//<co id="crt2.controller.creation"/>
        documents = new ArrayList<Document>();

        DBCursor cursor = new SongDAO().getAll();
        while (cursor.hasNext()) {
            DBObject song = cursor.next();
            String name = (String) song.get("name");
            String text = (String) song.get("text");
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

    public static void parseAndGetTotalWordsCount(String url) throws Exception {
        Controller.parse(url);
        TextAnalyser analyser = new TextAnalyser();
        Map<String, Integer> wordsCount = analyser.getWordsCount();

        Scanner s = new Scanner( Paths.get(TMApp.class.getResource("stopwords.txt").toURI()).toFile() );
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
        }
    }

}
