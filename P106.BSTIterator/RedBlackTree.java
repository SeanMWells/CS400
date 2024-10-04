// CS400 Spring 2024 File Header Information
// Name: Sean Wells
// Email: smwells3@wisc.edu
// Lecturer: Professor Heimerl
// Notes to Grader:
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends the BinarySearchTree class to model a Red-Black tree
 * @param <T> a generic comparable variable type
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * This class extends the Node class to represent a node in a RBT
   * @param <T> a generic comparable variable type
   */
  protected static class RBTNode<T> extends Node<T> {
    public boolean isBlack = false;
    public RBTNode(T data) { super(data); }
    public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
    public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
    public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
  }
  
  /**
   * This class aims to keep the Red-Black properties of a RedBlackTree after inserting a new node
   * @param newNode the node to be added to the RBT
   */
  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> newNode) {
    boolean check = false;
    boolean nullAunt = false;
    if(newNode.up != null) {
      if (!((RBTNode<T>) newNode.up).isBlack){
        
        //check the node opposite newNode's parent
        
        boolean right = newNode.up.isRightChild();
        if(newNode.up.up != null) { // check node's grandparent and opposite child
          if (((RBTNode<T>) newNode.up.up).isBlack) {
            if (newNode.up.up.down[0] == null || newNode.up.up.down[1] == null){
              nullAunt = true;
              } else {
            if(right && newNode.up.up.down[0] != null) {
              check = ((RBTNode<T>) newNode.up.up.down[0]).isBlack;
            } else { if (newNode.up.up.down[1] != null) {
              check = ((RBTNode<T>) newNode.up.up.down[1]).isBlack;
              }
              }   
            }
          }
        }
        if (nullAunt) {
          ((RBTNode<T>)newNode.up).isBlack = true;
          ((RBTNode<T>)newNode.up.up).isBlack = false;
          this.rotate(newNode.up, newNode.up.up);
        } else {
          if (check) { // in the top case of the diagram from slides
            this.rotate(newNode, newNode.up);
            ((RBTNode<T>) newNode.up).isBlack = false;
            this.rotate(newNode, newNode.up);
            newNode.isBlack = true;
          } else { // bottom case of the diagram
            ((RBTNode<T>) newNode.up).isBlack = true;
            if (newNode.up.up != null) {
              ((RBTNode<T>) newNode.up.up).isBlack = false;
              if (newNode.up.isRightChild() && newNode.up.up.down[0] != null) {
                ((RBTNode<T>) newNode.up.up.down[0]).isBlack = true;
              } else { if(newNode.up.up.down[1] != null) {
                ((RBTNode<T>) newNode.up.up.down[1]).isBlack = true;
                }
              }
            }
            enforceRBTreePropertiesAfterInsert((RBTNode<T>) newNode.up.up);
          }
        }
      }
    }  
  }
  
  /**
   * The overridden method to insert a new node into a RBT
   */
  @Override
  public boolean insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException();
    }
    RBTNode<T> newNode = new RBTNode<T>(data);
    this.insertHelper(newNode);
    this.enforceRBTreePropertiesAfterInsert(newNode);
    ((RBTNode<T>) this.root).isBlack = true;
    return true;
  }
  
  /**
   * Tests the top scenario from the RBTInsert slides, i.e. inserting "B" to a red "A" whose parent 
   * is a black "C" with a black "D" as its other child node. 
   */
//  @Test
  public void testTop() {
    RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    RBTNode<Integer> one = new RBTNode<Integer>(1);
    RBTNode<Integer> three = new RBTNode<Integer>(3);
    RBTNode<Integer> four = new RBTNode<Integer>(4);
    three.down[0] = one;
    three.down[1] = four;
    three.isBlack = true;
    four.isBlack = true;
    one.isBlack = false;
    four.up = three;
    one.up = three;
    tree.root = three;
    tree.size = 3;
    tree.insert(2);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(1)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(2)).isBlack);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(3)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(4)).isBlack);
    Assertions.assertEquals(2, ((RBTNode<Integer>) tree.findNode(1)).up.data);
    Assertions.assertEquals(2, ((RBTNode<Integer>) tree.findNode(3)).up.data);
    Assertions.assertEquals(3, ((RBTNode<Integer>) tree.findNode(4)).up.data);
  }
  
  /**
   * Tests the bottom scenario from the RBTInsert slides, i.e. inserting "A" to a black "B" whose 
   * parent is a red "C" with a black "D" as its other child node. Since the root node cannot be red
   * I will make "C" the only child of a Black "E" node.
   */
  //@Test
  public void testBottom() {
    RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    RBTNode<Integer> two = new RBTNode<Integer>(2);
    RBTNode<Integer> three = new RBTNode<Integer>(3);
    RBTNode<Integer> four = new RBTNode<Integer>(4);
    RBTNode<Integer> five = new RBTNode<Integer>(5);
    five.isBlack = true;
    five.down[0] = three;
    three.down[0] = two;
    three.down[1] = four;
    five.isBlack = true;
    three.isBlack = true;
    four.isBlack = false;
    two.isBlack = false;
    three.up = five;
    four.up = three;
    two.up = three;
    tree.root = five;
    tree.size = 4;
    tree.insert(1);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(1)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(2)).isBlack);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(3)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(4)).isBlack);
    Assertions.assertEquals(2, ((RBTNode<Integer>) tree.findNode(1)).up.data);
    Assertions.assertEquals(3, ((RBTNode<Integer>) tree.findNode(2)).up.data);
    Assertions.assertEquals(3, ((RBTNode<Integer>) tree.findNode(4)).up.data);
  }
  
  /**
   * This method will test a combination of the previous two methods to ensure that the red-black 
   * tree rules remain consistent after insertion at higher levels of the tree. This will test the 
   * recursion of the enforceRBTreePropertiesAfterInsert method.
   * 
   * This will be done by ensuring that the red "C" node in the bottom example becomes the "added"
   * red "B" node in the top example.   
   */
  //@Test
  public void testCombination() {
    RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    RBTNode<Integer> one = new RBTNode<Integer>(1);
    RBTNode<Integer> three = new RBTNode<Integer>(3);
    RBTNode<Integer> four = new RBTNode<Integer>(4);
    RBTNode<Integer> five = new RBTNode<Integer>(5);
    RBTNode<Integer> six = new RBTNode<Integer>(6);
    RBTNode<Integer> seven = new RBTNode<Integer>(7);
    
    one.isBlack = false;
    three.isBlack = false;
    four.isBlack = true;
    five.isBlack = false;
    six.isBlack = true;
    seven.isBlack = true;
    
    //level order of tree is [6 1 7 4 3 5]
    
    six.down[0] = one;
    six.down[1] = seven;
    one.up = six;
    seven.up = six;
    one.down[1] = four;
    four.up = one;
    four.down[0] = three;
    four.down[1] = five;
    three.up = four;
    five.up = four;
    tree.size = 6;
    tree.root = six;
    
    tree.insert(2);
    
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(1)).isBlack);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(2)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(3)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(4)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(5)).isBlack);
    Assertions.assertEquals(false, ((RBTNode<Integer>) tree.findNode(6)).isBlack);
    Assertions.assertEquals(true, ((RBTNode<Integer>) tree.findNode(7)).isBlack);
    Assertions.assertEquals(4, ((RBTNode<Integer>) tree.findNode(1)).up.data);
    Assertions.assertEquals(3, ((RBTNode<Integer>) tree.findNode(2)).up.data);
    Assertions.assertEquals(1, ((RBTNode<Integer>) tree.findNode(3)).up.data);
    Assertions.assertEquals(6, ((RBTNode<Integer>) tree.findNode(5)).up.data);
    Assertions.assertEquals(4, ((RBTNode<Integer>) tree.findNode(6)).up.data);
    Assertions.assertEquals(6, ((RBTNode<Integer>) tree.findNode(7)).up.data);
  }

}
