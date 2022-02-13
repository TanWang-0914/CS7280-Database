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
    root = initNode();
    nodes[root].children[0] = createLeaf();
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
   *    - If -1 is returned, the value is inserted and increase cntValues.
   *    - If -2 is returned, the value already exists.
   */
  public void Insert(int value) {
    if(nodeInsert(value, root) == -1) cntValues++;
  }

  /*
   * Display(int node)
   *    print out the indexing tree structure under specified node.
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
    // If current node is leaf node, check if node.values has given values.
    if (isLeaf(currNode)){
      // If value exist, return true
      for(int i = 0; i < currNode.size; i++){
        if (currNode.values[i] == value) return true;
      }
      // If value does not exist return false.
      return false;
    }else{
      // If current node is not leaf node, check which child node should the value exist at.
      // Then return nodeLookup result of child node.
      for(int i = 0; i < currNode.size; i++){
        if(currNode.values[i] > value){
          return nodeLookup(value, currNode.children[i]);
        }
      }
      return nodeLookup(value, currNode.children[currNode.size]);
    }
  }

  /*
   * nodeInsert(int value, int pointer)
   *    - -2 if the value already exists in the specified node
   *    - -1 if the value is inserted into the node or
   *            something else if the parent node has to be restructured
   */
  private int nodeInsert(int value, int pointer) {
    Node currNode = nodes[pointer];
    // When current node is leaf
    if (isLeaf(currNode)){
      // If this node already exist, return -2.
      for(int i = 0; i < currNode.size; i++){
        if (currNode.values[i] == value) return -2;
      }
      // If this node does not exist and current leaf has space, then add new value to this leaf.
      if (currNode.size < NODESIZE){
        int i = currNode.size-1;
        // Loop and move all greater value to the right.
        while(i >= 0 && currNode.values[i] > value) {
          currNode.values[i+1] = currNode.values[i];
          i--;
        }
        // Add new value and return -1.
        currNode.values[i+1] = value;
        currNode.size++;
        return -1;
      } else {
        // If current leaf does not have space, we need to split this leaf node into two leaf node.
        // And return pointer to the new leaf node.
        int newChildIndex = createLeaf();
        Node newChildNode = nodes[newChildIndex];
        // Sort all values
        int[] allValues = new int[NODESIZE+1];
        System.arraycopy(currNode.values, 0, allValues, 0, NODESIZE);
        allValues[NODESIZE] = value;
        Arrays.sort(allValues);
        // Reset current node and size.
        currNode.values = new int[NODESIZE];
        currNode.size = 0;
        // Distribute values in current node and new node.
        int oldLeafStartIndex = 0;
        int newLeafStartIndex = 0;
        for(int i = 0; i < allValues.length; i++){
          if (i <= NODESIZE/2) {
            currNode.values[oldLeafStartIndex++] = allValues[i];
            currNode.size++;
          }else{
            newChildNode.values[newLeafStartIndex++] = allValues[i];
            newChildNode.size++;
          }
        }
        return newChildIndex;
      }
    }else{
      // When current node is not leaf node.
      // Find the child node to insert new value.
      int childNodeIndex = 0;
      for(; childNodeIndex < currNode.size; childNodeIndex++){
        if(currNode.values[childNodeIndex] > value){
          break;
        }
      }
      // If new value is greater than all values in current node, insert it into the last child node.
      // Add new leaf node if last child does not exist.
      if(childNodeIndex == currNode.size+1) {
        if (childNodeIndex>0) currNode.children[childNodeIndex] = createLeaf();
      }
      int childNodeGlobalIndex = currNode.children[childNodeIndex];
      // Insert new value into child node.
      int insertResult = nodeInsert(value, childNodeGlobalIndex);

      // if node already exist or does not create new child
      if (insertResult == -2) return -2;
      if (insertResult == -1) return -1;

      // Else, a new node has pop up, we need to combine the new node with current node.
      // If currNode still has space, add new node value to current node.
      int newValue = nodes[insertResult].values[0];
      if (currNode.size < NODESIZE){
        int i = currNode.size-1;
        // Shift all greater values and pointers to the right.
        while(i >= 0 && currNode.values[i] > newValue) {
          currNode.values[i + 1] = currNode.values[i];
          currNode.children[i + 2] = currNode.children[i+1];
          i--;
        }
        // Add new value and new child node.
        currNode.values[i+1] = newValue;
        currNode.children[i+2] = insertResult;
        currNode.size++;
        return -1;
      }else{
        // If currNode does not have space. Split current node into two nodes.
        // And return the pointer of second node.
        int insertNodeIndex = insertResult;
        int newNodeIndex = initNode();
        Node newNode = nodes[newNodeIndex];
        // Sort all values including new node value.
        int[] copyCurrNodeValues = Arrays.copyOf(currNode.values, NODESIZE);
        int[] copyCurrNodeChildren = Arrays.copyOf(currNode.children, NODESIZE+1);
        // Reset current node.
        currNode.values = new int[NODESIZE];
        currNode.children = new int[NODESIZE+1];
        currNode.size = 0;

        int[] allValues = new int[NODESIZE+1];
        int[] allPointers = new int[NODESIZE+2];

        int sortIndex = NODESIZE-1;
        // Shift all greater values and pointers to the right.
        while(sortIndex >= 0 && copyCurrNodeValues[sortIndex] > newValue){
          allValues[sortIndex+1] = copyCurrNodeValues[sortIndex];
          allPointers[sortIndex+2] = copyCurrNodeChildren[sortIndex+1];
          sortIndex--;
        }
        // Insert new node value and pointer.
        allValues[sortIndex+1] = newValue;
        allPointers[sortIndex+2] = insertNodeIndex;
        allPointers[sortIndex+1] = copyCurrNodeChildren[sortIndex+1];
        // Copy all rest values and pointers to allValues and allPointers.
        while( sortIndex >= 0){
          allValues[sortIndex] = copyCurrNodeValues[sortIndex];
          allPointers[sortIndex] = copyCurrNodeChildren[sortIndex];
          sortIndex--;
        }

        // Distribute values and pointers to two nodes.
        int oldNodeStartIndex = 0;
        int newNodeStartIndex = 0;
        for(int i = 0; i < allValues.length; i++){
          if (i <= NODESIZE/2) {
            currNode.values[oldNodeStartIndex] = allValues[i];
            currNode.children[oldNodeStartIndex] = allPointers[i];//
            if(i == NODESIZE/2) currNode.children[oldNodeStartIndex+1] = allPointers[i+1];
            oldNodeStartIndex++;
            currNode.size++;
          }else{
            newNode.values[newNodeStartIndex] = allValues[i];
            newNode.children[newNodeStartIndex] = allPointers[i];
            newNodeStartIndex++;
            newNode.size++;
          }
        }
        newNode.children[newNode.size] = allPointers[allPointers.length-1];
        // If current node is not root, we pop the new node up a level to combine it with parent node.
        if (pointer != root) return newNodeIndex;
        // If current node is root, we create a new root node and add current node and new node to new root.
        else {
          int newRootIndex = initNode();
          Node newRoot = nodes[newRootIndex];
          newRoot.values[0] = newNode.values[0];
          newRoot.children[0] = pointer;
          newRoot.children[1] = newNodeIndex;
          newRoot.size++;
          root = newRootIndex;
        }
        return -1;
      }
    }
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
