package db;

/**
 * Blahblahblah
 *
 * @author Replicating Rat
 * @version 3.6.7
 * @since 3.6.7
 */
public class LjDAO extends SongDAO {
	public static final String TABLE = "ljposts";
	public LjDAO(){
		super();
		coll = db.getCollection(TABLE);
	}
}
