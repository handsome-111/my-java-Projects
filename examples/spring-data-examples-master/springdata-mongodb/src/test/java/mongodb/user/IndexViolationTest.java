package mongodb.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import mongodb.config.LocalhostMongoConfiguration;
import mongodb.user.User;
import mongodb.user.UserRepository;

/**
 * This test shows that violation on indexes are ignored.
 * <p/>
 * To run this test, a local mongod instance is required using the standard port.
 * 
 * @author Tobias Trelle
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LocalhostMongoConfiguration.class)
public class IndexViolationTest {

	private static final String COLLECTION_NAME = "user";
	
	 @Autowired UserRepository repo;
	  
	 @Autowired MongoOperations template;
	
	 /** 
	  * Create a target collection.
	  */
	 @Before
	 public void setUp() {
		 template.dropCollection(COLLECTION_NAME);
		 template.createCollection(COLLECTION_NAME);
		 template.indexOps(COLLECTION_NAME).ensureIndex(new Index().on("fullName", Direction.ASC).unique());
	 }
	 
	 @Test
	 public void does_not_detect_index_violation_on_id() {
		 // given
		 repo.save( new User("0", "User 0") ); // 1st param = _id field, 2nd = unique secondary index
		 
		 // when
		 repo.save( new User("0", "User 1") );
		 
		 // then
	 }
}
