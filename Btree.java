/*
 * CS7280 Special Topics in Database Management
 * Project 1: B-tree implementation.
 *
 * You need to code for the following functions in this program
 *   1. Lookup(int value) -> nodeLookup(int value, int node)
 *   2. Insert(int value) -> nodeInsert(int value, int node)
 *   3. Display(int node)
 *
 */

import java.util.Arrays;

final class Btree {

  /* Size of Node. */
  private static final int NODESIZE = 5;

  /* Node array, initialized with length = 1. i.e. root node */
  private Node[] nodes = new Node[1];

  /* Number of currently used nodes. */
  private int cntNodes;

  /* Pointer to the root node. */
  private int root;

  /* Number of currently used values. */
  private int cntValues;

  /*
   * B+ tree Constructor.
   */
  public Btree() {
    // Init tree by creating a leaf node as root
    root = createLeaf();
  }

  /*********** B tree functions for Public ******************/

  /*
   * Lookup(int value)
   *   - True if the value was found.
   */
  public boolean Lookup(int value) {
    return nodeLookup(value, root);
  }

  /*
   * Insert(int value)
   *    - Void function, Insert value if it does not exist in our tree.
   */
  public void Insert(int value) {
    // Lookup value in our tree, if exist, return
    if (Lookup(value)) return;
    // Start from rootNode, split root if no space left.
    Node rootNode = nodes[root];
    if (rootNode.size == NODESIZE){
      int oldRootIndex = root;
      // Create new root node
      int newRootIndex = initNode();
      Node newRoot = nodes[newRootIndex];
      root = newRootIndex;
      // Set new root first child as old root, second child as new split node
      newRoot.children[0] = oldRootIndex;
      nodeSplit(newRootIndex, 0, oldRootIndex);
      nodeInsert(value, newRootIndex);
    }else{
      nodeInsert(value, root);
    }
    // Insert finished, count new value.
    cntValues++;
  }

  /*
   * Display(int node)
   *    - Print out the indexing tree structure under specified node.
   */
  public void Display(int pointer) {
    System.out.println("\nPrinting node "+pointer);
    // Print out values of this Node.
    Node currNode = nodes[pointer];
    StringBuilder values = new StringBuilder();
    for (int i = 0; i < currNode.size; i++){
      values.append("      ");
      values.append("Value");
      values.append(i);
      values.append("=");
      values.append(currNode.values[i]);
    }
    System.out.println(values.toString());
    // Print out child pointers of this Node.
    if (!isLeaf(currNode)){
      StringBuilder nodes = new StringBuilder();
      for (int i = 0; i < currNode.size+1; i++){
        nodes.append("Child");
        nodes.append(i);
        nodes.append("=");
        nodes.append(currNode.children[i]);
        nodes.append("     ");
      }
      System.out.println(nodes.toString());
    }
  }

  /*
   * CntValues()
   *    - Returns the number of used values.
   */
  public int CntValues() {
    return cntValues;
  }

  /*********** B-tree functions for Internal  ******************/

  /*
   * nodeLookup(int value, int pointer)
   *    - True if the value was found in the specified node.
   *
   */
  private boolean nodeLookup(int value, int pointer) {
    Node currNode = nodes[pointer];
    // start searching from index 0
    int i = 0;
    for(; i < currNode.size; i++) {
      int currValue = currNode.values[i];
      // if current value is greater than searching value, break for loop.
      if (currValue > value) break;
      // if current value equals to searching value, return true.
      if (currValue == value) return true;
    }
    // i == currNode.size+1 or currNode.values[i]> value, either way, we should search ith child.
    // if currNode is leafNode return false, else, start searching ith child
    if (isLeaf(currNode)) return false;
    else return nodeLookup(value, currNode.children[i]);
  }

  /*
   * nodeInsert(int value, int pointer)
   *    - Void function, start at current pointer to insert value.
   */
  private void nodeInsert(int value, int pointer) {
    Node currNode = nodes[pointer];
    // Start iterating
    int i = currNode.size-1;
    // if current node is a leaf node, we insert new value.
    // Size of leaf node has been check at parent node/root, thus must have empty space.
    if (isLeaf(currNode)){
      while(i >= 0 && value < currNode.values[i]) {
        currNode.values[i + 1] = currNode.values[i];
        i--;
      }
      currNode.values[i+1] = value;
      currNode.size++;
    }else{
      // find write place to insert new value
      while(i >= 0 && value < currNode.values[i]) {
        i--;
      }
      int childNodeIndex = currNode.children[i+1];
      Node childNode = nodes[childNodeIndex];
      // If child node is full, split child node first.
      if (childNode.size == NODESIZE){
        nodeSplit(pointer, i+1, childNodeIndex);
        // Decide to insert left or right childNode
        if (value > currNode.values[i+1]) i++;
      }
      // Insert value at child node.
      nodeInsert(value, currNode.children[i+1]);
    }
  }

  /*
   * nodeSplitt(int parentPointer, int position, int childPointer)
   *    - Void function, Split childNode at given position of parentNode
   */
  private void nodeSplit(int parentPointer, int position, int childPointer){
    // Create new node or leaf as new node.
    int newNodeIndex;
    if (isLeaf(nodes[childPointer])){
      newNodeIndex = createLeaf();
    }else{
      newNodeIndex = initNode();
    }

    Node newNode = nodes[newNodeIndex];
    Node childNode = nodes[childPointer];
    Node parentNode = nodes[parentPointer];

    int halfSize = (NODESIZE+1)/2; // Half size if NODESIZE is even, bigger half of NODESIZE if NODESIZE is odd.
    // Split values of given childNode
    for (int i = 0; i < halfSize-1; i++){ // childNode.values(halfSize) to be popped up
      newNode.values[i] = childNode.values[i+halfSize];
      newNode.size++;
    }
    // Split children of given childNode
    if (!isLeaf(childNode)){
      for (int i = 0; i < halfSize; i++){
        newNode.children[i] = childNode.children[i+halfSize];
      }
    }
    // Update childNode size
    childNode.size = NODESIZE/2;

    // Shift children and values of parentNode
    for (int i = parentNode.size-1; i >= position; i--){
      parentNode.values[i+1] = parentNode.values[i];
    }
    for (int i = parentNode.size; i >= position+1; i--){
      parentNode.children[i+1] = parentNode.children[i];
    }
    // Add new node to correct position of parent node
    parentNode.values[position] = childNode.values[halfSize-1];
    parentNode.children[position+1] = newNodeIndex;
    parentNode.size++;
  }

  /*********** Functions for accessing node  ******************/

  /*
   * isLeaf(Node node)
   *    - True if the specified node is a leaf node.
   *         (Leaf node -> a missing children)
   */
  boolean isLeaf(Node node) {
    return node.children == null;
  }

  /*
   * initNode(): Initialize a new node and returns the pointer.
   *    - return node pointer
   */
  int initNode() {
    Node node = new Node();
    node.values = new int[NODESIZE];
    node.children =  new int[NODESIZE + 1];

    checkSize();
    nodes[cntNodes] = node;
    return cntNodes++;
  }

  /*
   * createLeaf(): Creates a new leaf node and returns the pointer.
   *    - return node pointer
   */
  int createLeaf() {
    Node node = new Node();
    node.values = new int[NODESIZE];

    checkSize();
    nodes[cntNodes] = node;
    return cntNodes++;
  }

  /*
   * checkSize(): Resizes the node array if necessary.
   */
  private void checkSize() {
    if(cntNodes == nodes.length) {
      Node[] tmp = new Node[cntNodes << 1];
      System.arraycopy(nodes, 0, tmp, 0, cntNodes);
      nodes = tmp;
    }
  }
}

/*
 * Node data structure.
 *   - This is the simplest structure for nodes used in B-tree
 *   - This will be used for both internal and leaf nodes.
 */
final class Node {
  /* Node Values (Leaf Values / Key Values for the children nodes).  */
  int[] values;

  /* Node Array, pointing to the children nodes.
   * This array is not initialized for leaf nodes.
   */
  int[] children;

  /* Number of entries
   * (Rule in B Trees:  d <= size <= 2 * d).
   */
  int size;
}
