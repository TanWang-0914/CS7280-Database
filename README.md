# CS7280-Database
Project-1  
Name: Tan Wang  
NUID: 001063115  

Write up:  
Including new implementation of methods
Added new method nodeSplit to split a child node at given position of parentNode.
- Lookup(int value): find the specified value. If the value exists, returning value is True.
- Insert(int value): insert the specified value while keep the tree balanced.
- Display(int node): print out the indexing tree structure under specified node. For simplification, I will only print out the existing values and pointers in given node.
- nodeSplit(int parentPointer, int position, int childPointer): Void function, Split childNode at given position of parentNode.