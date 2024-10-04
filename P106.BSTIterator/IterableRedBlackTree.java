// CS400 Spring 2024 File Header Information// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Professor Heimerl
// Notes to Grader:

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Stack;
//import BinarySearchTree.Node;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {

    private Comparable<T> iterationStartPoint;
    
    private final Comparable<T> defaultStart = new Comparable<T>() {
      @Override
      public int compareTo(T o) {
        return -1;
      }
    };
  
    public void setIterationStartPoint(Comparable<T> startPoint) {
      if (startPoint == null) {
        iterationStartPoint = defaultStart;
      } else {
        iterationStartPoint = startPoint;
      }
    }

    public Iterator<T> iterator() { 
      return new RBTIterator<>(this.root, this.iterationStartPoint); 
      }

    private static class RBTIterator<R> implements Iterator<R> {
      
      private Comparable<R> startPoint;
      private Stack<Node<R>> stack;

      public RBTIterator(Node<R> root, Comparable<R> startPoint) {
	this.startPoint = (startPoint != null) ? startPoint : new Comparable<R>() {
          @Override
          public int compareTo(R o) {
            return -1;
          }
        };
        this.stack = new Stack<Node<R>>();
        buildStackHelper(root);
      }

      private void buildStackHelper(Node<R> node) {
        if (node == null) return;
        if (startPoint.compareTo(node.data) <= 0) {
          stack.push(node);
          buildStackHelper(node.down[0]);
        } else {
          buildStackHelper(node.down[1]);
        }
      }

      public boolean hasNext() { 
        return !stack.empty();
        }

      public R next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Node<R> node = stack.pop();
        buildStackHelper(node.down[1]);
        return node.data;
      }

    }
    
    /**
     * Performs a naive insertion into a binary search tree: adding the new node
     * in a leaf position within the tree. After this insertion, no attempt is made
     * to restructure or balance the tree.
     * @param node the new node to be inserted
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided node is null
     */
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if(newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                //if (compare == 0) {
                //    return false;
                //}
                if (compare < 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1]; 
                    }
                }
            }
        }
    }
    
   /**
    * This test method ensures that duplicate values are allowed to be inserted into the tree
    * as well as become added to the stack. This is tested by inserting multiple 'H' values into the 
    * tree at varying points and ensuring that all can be accessed through iteration. 
    */
    @Test
    public void testDupe() {
      IterableRedBlackTree<String> tree = new IterableRedBlackTree<String>();
      tree.insert("H");
      tree.insert("H");
      tree.insert("B");
      tree.insert("E");
      tree.insert("H");
      tree.insert("X");
      tree.insert("H");
      tree.setIterationStartPoint("H");
      Iterator<String> it = tree.iterator();
      ArrayList<String> result = new ArrayList<>();
      for (int i=0; i<4; i++) {
        result.add(it.next());
      }
      Assertions.assertEquals("[H, H, H, H]", result.toString());
    }
    
   /**
    * This test method ensures that the iteration procedure is not interrupted by a null starting 
    * point. This is done by initializing the iterator with a specified null start point and then
    * testing the default starting method field as well as it's comparison behavior.
    */
    @Test
    public void testNullStart() {
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      tree.insert(1);
      tree.insert(12);
      tree.insert(5);
      tree.insert(88);
      tree.insert(5);
      tree.insert(32);
      tree.insert(11);
      tree.setIterationStartPoint(null);
      Iterator<Integer> it = tree.iterator();
      ArrayList<Integer> result = new ArrayList<>();
      for (int i=0; i<tree.size; i++) {
        result.add(it.next());
      }
      Assertions.assertEquals("[1, 5, 5, 11, 12, 32, 88]", result.toString());
    }
    
   /**
    * This method tests a valid implementation of the iterable red black tree including values that
    * are non-duplicates as well as specifying a starting point.
    */
    @Test
    public void testValidTree() {
      IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<Integer>();
      tree.insert(100);
      tree.insert(78);
      tree.insert(23);
      tree.insert(32);
      tree.insert(5);
      tree.insert(88);
      tree.insert(9);
      tree.setIterationStartPoint(32);
      Iterator<Integer> it = tree.iterator();
      ArrayList<Integer> result = new ArrayList<>();
      for (int i=0; i<4; i++) {
        result.add(it.next());
      }
      Assertions.assertEquals("[32, 78, 88, 100]", result.toString());
    }

}
