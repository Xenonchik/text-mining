package db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class MongoTest {

	public static void main(String[] args) throws Exception {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("testmining");
		DBCollection coll = db.getCollection("songs");

		String text = (String) coll.findOne().get("text");
		System.out.println(text);

	}
}
