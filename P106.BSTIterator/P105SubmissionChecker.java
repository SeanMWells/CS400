import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
* This class extends the IterableRedBlackTree class to run submission checks on it.
*/
public class P105SubmissionChecker extends IterableRedBlackTree {

	/**
	* Inserts a small number of keys, including some duplicates into an empty
	* tree and checks if both size() returns the correct number and an iterator
	* includes all duplicates.
	*/
	@Test
	public void submissionCheckerDuplicateKeys() {
		IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
		tree.insert(50);
		tree.insert(50);
		tree.insert(100);
		tree.insert(100);
		tree.insert(150);
		tree.insert(150);
		
		Assertions.assertEquals(6, tree.size());
		
		int count = 0;
		Iterator<Integer> iter = tree.iterator();
		for (Integer key : tree) {
			int expected = ((count++/2)+1)*50;
			Assertions.assertEquals(expected, iter.next());
		}
	}

}
