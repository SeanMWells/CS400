/**
* This class extends the BinarySearchTree class to allow the submission checks to have access to the
* internal fields of the BinarySearchTree class.
*/
public class P101SubmissionChecker extends BinarySearchTree<Integer> {

    /**
    * Perform a simple rotation at the root node of the tree to check if
    * the tree contains a rotation implementation.
    * @return true if test passes, false if it fails
    */
    public boolean checkSimpleRotationAtRoot() {
        // create new tree
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        // create nodes for 2 and 4
        Node<Integer> two = new Node<Integer>(2);
        Node<Integer> four = new Node<Integer>(4);
        two.down[1] = four;
        four.up = two;
        // set 2 as root node of tree
        tree.root = two;
        // set size of tree
        tree.size = 2;
        // then rotate 4 and 2
        tree.rotate(four, two);
        // check level order of tree after rotation
        return tree.toLevelOrderString().equals("[ 4, 2 ]");
    }

    /**
    * This main method runs test and reports back to gitlab if it passed or failed.
    */
    public static void main(String[] args) {
        if (!(new P101SubmissionChecker()).checkSimpleRotationAtRoot()) {
            // stop execution and return a non 0 value which will cause gitlab to show that a test failed
            System.exit(1);
        }
        // test passes
        System.exit(0);
    }

}
