import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
* This class extends the RedBlackTree class to run submission check on it.
*/
public class P104SubmissionChecker extends RedBlackTree {

    /**
    * Submission check that checks if the root node of a newly created tree is null.
    * @return true is check passes, false if it fails
    */
    @Test
    public void submissionCheckerSmallTree() {
        RedBlackTree<String> tree = new RedBlackTree<>();
        tree.insert("a");
        tree.insert("b");
        tree.insert("c");
        Assertions.assertTrue(tree.toLevelOrderString().equals("[ b, a, c ]"));
    }

    /**
    * Override for the enforeRBTPropertiesAfterInsert method with required signature.
    * This will cause compilation to fail if the method does not exist. 
    */
    @Override
    public void enforceRBTreePropertiesAfterInsert(RBTNode newNode) { }

}
