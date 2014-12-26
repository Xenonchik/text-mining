package db;

import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import com.mongodb.*;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class TextAnalyser {

    static public Map<String, Integer> countWordsInText(String text) {
        Map<String, Integer> wordsCount = new LinkedHashMap<>();
        Scanner scan = new Scanner(text);
        while (scan.hasNext()) {
            String word = scan.next().toLowerCase();
            word = word.replace(".", "").replace(",", "").replace("!", "").replace("?", "");
            if (wordsCount.containsKey(word)) {
                wordsCount.put(word, wordsCount.get(word) + 1);
            } else wordsCount.put(word, 1);
        }
        return wordsCount;
    }


	public Map<String, Integer> getWordsCount() {
		Map<String, Integer> totalWordsCount = new LinkedHashMap<>();

        SongDAO dao = new SongDAO();
		DBCursor cursor = dao.getWordsCounts();

		while (cursor.hasNext()) {
			Map<String, Integer> wordsCount = (Map<String, Integer>) cursor.next().get("wordsCount");
			for(Entry<String, Integer> entry : wordsCount.entrySet()) {
				if (totalWordsCount.containsKey(entry.getKey())) {
					totalWordsCount.put(entry.getKey(), totalWordsCount.get(entry.getKey()) + entry.getValue());
				} else totalWordsCount.put(entry.getKey(), entry.getValue());
			}
		}

		return totalWordsCount;
	}


    public static class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;
        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
