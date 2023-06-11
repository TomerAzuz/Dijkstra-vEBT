# Dijkstra-vEBT
An optimized variant of Dijkstra's algorithm using van Emde Boas Tree

# Java Implementation of Dijkstra's Algorithm with Different Priority Queues

This repository contains a Java implementation of Dijkstra's algorithm, a popular graph traversal algorithm for finding the shortest path between nodes in a graph. The implementation provides support for different priority queues, including an unsorted array, a min heap, a Fibonacci heap, and a van Emde Boas tree. It also includes a visualization of the algorithm using the Processing library.

## Contents

- [Overview](#overview)
- [Installation](#installation)
- [Usage](#usage)
- [Priority Queue Options](#priority-queue-options)
- [Visualization](#visualization)

## Overview

Dijkstra's algorithm is a single-source shortest path algorithm that operates on weighted directed graphs. It computes the shortest path between a source node and all other nodes in the graph. The algorithm maintains a priority queue of vertices, where the priority is determined by the current distance from the source. The Java implementation provided here supports different priority queue data structures.

## Installation

To use this Java implementation of Dijkstra's algorithm, follow these steps:

1. Ensure you have Java Development Kit (JDK) installed on your system.
2. Clone this repository or download the source code files.

## Usage

To use the Dijkstra's algorithm implementation, follow these steps:

1. Create a new instance of the Graph class, specifying the number of vertices and the edge probability.
2. Access the vertices from the graph using the getVertices method and assign the source and destination vertices.
3. Create a new instance of the Dijkstra class, passing the graph, source vertex, destination vertex, and the type of priority queue to be used. Choose one of the following options for the priority queue type:
0: Unsorted array
1: Min-heap
2: Fibonacci heap
3: RS-vEB
4. Print the shortest path from the source to the destination using the printPath method, specifying the destination vertex and the maximum path length.
The following code snippet demonstrates the usage:

```java
    Graph g = new Graph(100, 0.2f);
    Vertex src = g.getVertices()[0];
    Vertex dest = g.getVertices()[99];
    Dijkstra d = new Dijkstra(g, src, dest, 3);
    d.printPath(dest, 0);
```
## Priority Queue Options
This implementation of Dijkstra's algorithm provides four different priority queue options for vertex selection:

Unsorted Array: The priority queue is implemented using an unsorted array, resulting in a time complexity of O(n) for each extraction of the minimum element.<br>
Min Heap: The priority queue is implemented using a min heap, offering a time complexity of O(log n) for each extraction of the minimum element.<br>
Fibonacci Heap: The priority queue is implemented using a Fibonacci heap, providing an amortized time complexity of O(1) for each extraction of the minimum element.<br>
van Emde Boas Tree: The priority queue is implemented using a van Emde Boas tree, which achieves a time complexity of O(log log C) for each extraction of the minimum element, where C is the maximum weight in the graph. <br>
You can select the desired priority queue by modifying the code in the Graph class constructor.

<p align="center">
<a href="https://freeimage.host/i/H6HcuGp"><img src="https://iili.io/H6HcuGp.md.jpg" alt="H6HcuGp.md.jpg" border="0"></a>
</p>

## Benchmarks
The following measurements are the average running time of 30 runs, performed on
30 random source and destination vertices in randomized connected directed graphs. <br>
<p align="center">
  <a href="https://freeimage.host/he">
    <img src="https://iili.io/H6Hl1J1.jpg" alt="H6Hl1J1.jpg" border="0" style="width: 600px;" />
  </a>
</p>

<p align="center">
  <a href="https://freeimage.host/he">
    <img src="https://iili.io/H6Hlc0B.jpg" alt="H6Hlc0B.jpg" border="0" style="width: 600px;" />
  </a>
</p>

<p align="center">
  <a href="https://freeimage.host/he">
    <img src="https://iili.io/H6HllUP.jpg" alt="H6HllUP.jpg" border="0" style="width: 600px;" />
  </a>
</p>


## Visualization

<p align="center">
  <a href="https://gifyu.com/image/SuBJJ">
    <img src="https://s12.gifyu.com/images/SuBJJ.gif" alt="SuBJJ.gif" />
  </a>
</p>


This project includes a visualization of Dijkstra's algorithm using the Java Processing library. The visualization is provided as a JAR file. To run the visualization, follow these steps: 

1. Make sure you have Java installed on your system.
2. Run the JAR file:
``` java -jar dijkstra-visualization.jar ```
The visualization will open in a new window.

Use the mouse to interact with the visualization:

1. To generate a new graph, select the number of nodes and density and click on 'generate graph'.
2. Click on a vertex to set it as the source vertex.
3. Click on another vertex to set it as the destination vertex.
4. Click on the find shortest path button to run the visualization.
