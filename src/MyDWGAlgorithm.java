
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.io.*;
import java.util.*;

public class MyDWGAlgorithm implements DirectedWeightedGraphAlgorithms {

    private DirectedWeightedGraph graph;
    int IsConnected = 1;
    int NotYetConnected = 0;
    int Infinity = Integer.MAX_VALUE;

    public MyDWGAlgorithm() {
        this.graph = new MyDWG();
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new MyDWG(this.graph);
    }

    @Override
/*  The Idea for this function is based on the BFS method for graph traversal.
    Site which explains about BFS and its algorithm: https://en.wikipedia.org/wiki/Breadth-first_search .
    Which we learned in Algorithm 1 class.
    It runs as follows :
    Step 1: Take a random node from the graph and check if this Node is connected to all the other nodes.
    Step 2: Reverse all the edges of the graph.
    Step 3: check if the random node we chose is still connected to all the other nodes.
    In class, we proved that when it happens the graph is connected.
    Meaning, there is a valid path from each node to the others.
 */
    public boolean isConnected() {
        NodeData someNode = this.getGraph().nodeIter().next();
        boolean oneWayConnected = isNodeConnected(this.graph, someNode);
        if (!oneWayConnected) {
            return false;
        }
        MyDWG reverseGraph = reverseGraph();
        return isNodeConnected(reverseGraph, someNode);
    }

    /*  Mission -> Check if a specific node is connected. Meaning, there is a valid path from it to all the others.
        Our strategy -> pick a starting node and put if in a queue. Then run in a loop until the queue is empty and
     *  Remove the head and go throw all the nodes he is connected with one by one. for each,
     *  add the current node to the queue, and change its tag to "isConneted".
     *  In the end only the tag of the nodes we have seen in the process will show "IsConnected".
     * Last, check if all  the nodes tags show "IsConnected"  */
    public boolean isNodeConnected(DirectedWeightedGraph g, NodeData specificNode) {

        if (g.nodeSize() < 0) {
            return false;
        } else if (g.nodeSize() == 0 || g.nodeSize() == 1) {
            return true;
        }
        setAllTags(g,0);
        Queue<NodeData> NodeQueue = new LinkedList<>();
        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData n = nodeIter.next();
            n.setTag(NotYetConnected);
        }
//     The next line is legal because in the beginning we confirmed that our graph nodeSize > 1.
//     In addition, it doesn't meter which node will be the starting one.
        NodeQueue.add(specificNode);
        while (!NodeQueue.isEmpty()) {
            NodeData currentNode = NodeQueue.remove();

            Iterator<EdgeData> edgeIter = g.edgeIter(currentNode.getKey());
            while (edgeIter.hasNext()) {
                EdgeData currentEdge = edgeIter.next();
                NodeData currentEdgeNode = g.getNode(currentEdge.getDest());
                NodeQueue.add(currentEdgeNode);
                currentEdgeNode.setTag(IsConnected);
            }
        }
        nodeIter = g.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currentNode = nodeIter.next();
            if (currentNode.getTag() == NotYetConnected) {
                return false;
            }
        }
        return true;
    }

    private MyDWG reverseGraph() {
        MyDWG newGrph = new MyDWG();
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData currentNode = nodeIter.next();
            newGrph.nodeList.put(currentNode.getKey(), currentNode);
        }
        Iterator<EdgeData> edgeIter = this.graph.edgeIter();
        while (edgeIter.hasNext()) {
            EdgeData currentEdge = edgeIter.next();
            HashMap<Integer,EdgeData> reversedEdge = new HashMap<>();
            reversedEdge.put(currentEdge.getSrc(),currentEdge);
            newGrph.edgeList.put(currentEdge.getDest(),reversedEdge);
        }
        return newGrph;
    }

/*  The Idea for this function is based on the Dijkstra's algorithm.
    Site which explains about Dijkstra's algorithm: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm .
    Mission -> Find the shortest path between given src and dest nodes;
    It runs as follows :
    Step 1: Build a structure which stores the distance to each node and set it all to infinity.
    Step 2: Create a priority queue and add the src node and change its distance to zero.
    Step 3: While queue is not empty, poll a node.
    Step4: Run throw all its neighbours and check,if the [distance to this current neighbour node +
           distance(this node, current neighbour)] < (current distance to current neighbour).
           If true, update the (current neighbour distance from src to it) to the new value.
    Meaning, in the end all the nodes will contain the shortest path from src to them.
    Last, return the distance to dest.
 */
    @Override
    public double shortestPathDist(int src, int dest) {              // in progress
        if (this.graph.getNode(src) == null || this.graph.getNode(dest) == null) {
            return -1;
        }
        if (src == dest){
            return 0;
        }
        HashMap<Integer,Double> dist = new HashMap<>();
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()){
            NodeData node = nodeIter.next();
            dist.put(node.getKey(),Double.MAX_VALUE);
        }
        Queue<NodeData> NodeQueue = new PriorityQueue<>();
        NodeData srcNode = this.graph.getNode(src);
        NodeQueue.add(srcNode);
        dist.put(srcNode.getKey(),0.0);

        while (!NodeQueue.isEmpty()){
            NodeData currentSrcNode = NodeQueue.poll();
            Iterator<EdgeData> edgeIter = this.graph.edgeIter(currentSrcNode.getKey());
            while (edgeIter.hasNext()) {
                EdgeData edgeBetweenCurrentNeighbours = edgeIter.next();
                NodeData dstNode = this.graph.getNode(edgeBetweenCurrentNeighbours.getDest());
                double edgeWeight = edgeBetweenCurrentNeighbours.getWeight();
                double currentSrcWeight = dist.get(currentSrcNode.getKey());
                double currentNeighbourWeight = dist.get(dstNode.getKey());
                double neighbourNewWeight = edgeWeight + currentSrcWeight;
                if (currentNeighbourWeight > neighbourNewWeight){
                    dist.put(dstNode.getKey(),neighbourNewWeight);
                    NodeQueue.add(dstNode);
                }
            }
        }
        double shortestPath = dist.get(dest);
        return (shortestPath == 0.0) ? -1 : shortestPath;
    }
/*
    The Idea for this function is also based on the Dijkstra's algorithm.
    Mission -> return the list of nodes which represent the shortest path from a given src node to the given dest.
    Implementation -> same as shortestPathDist, only now we for each node we saved a list of nodes from src to it,
                      which updates as well.
    Last, return the dest list.
 */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (this.graph.getNode(src) == null || this.graph.getNode(dest) == null) {
            return null;
        }
        List<NodeData> nodeFromSrcToDst = new ArrayList<>();
        if (src == dest){
            nodeFromSrcToDst.add(this.graph.getNode(src));
            return nodeFromSrcToDst;
        }
        HashMap<Integer,List<NodeData>> nodesList = new HashMap<>();
        HashMap<Integer,Double> dist = new HashMap<>();
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()){
            NodeData node = nodeIter.next();
            dist.put(node.getKey(),Double.MAX_VALUE);
            nodesList.put(node.getKey(),new LinkedList<>());
        }
        // Created a priority queue which gets the src node at first and all its valid paths
        Queue<NodeData> NodeQueue = new PriorityQueue<>();
        NodeData srcNode = this.graph.getNode(src);
        NodeQueue.add(srcNode);
        dist.put(srcNode.getKey(),0.0);

        while (!NodeQueue.isEmpty()){
            NodeData polledNode = NodeQueue.poll();
            // List of nodes from the polled Node containing the nodes from src to the current polled node.
            List<NodeData> currentNodeMinList = new LinkedList<>(nodesList.get(polledNode.getKey()));
            // Iterator that runs throw all the neighbours of the polled node.
            Iterator<EdgeData> edgeIter = this.graph.edgeIter(polledNode.getKey());
            while (edgeIter.hasNext()) {
                EdgeData edgeBetweenCurrentNeighbours = edgeIter.next();
                NodeData dstNode = this.graph.getNode(edgeBetweenCurrentNeighbours.getDest());
                double edgeWeight = edgeBetweenCurrentNeighbours.getWeight();
                double currentSrcWeight = dist.get(polledNode.getKey());
                double currentNeighbourWeight = dist.get(dstNode.getKey());
                double neighbourNewWeight = edgeWeight + currentSrcWeight;
                if (currentNeighbourWeight > neighbourNewWeight){
                    // If we got here it means the current min dist and Lst of nodes are about to be updated
                    dist.put(dstNode.getKey(),neighbourNewWeight);
                    NodeQueue.add(dstNode);
                    currentNodeMinList.add(dstNode);
                    nodesList.put(dstNode.getKey(),currentNodeMinList);
                }
            }
        }
        return nodesList.get(dest);
    }

    @Override
    public NodeData center() {
        // if the graph in not strongly connected -> return null
        if (!this.isConnected()) {
            return null;
        }
        double min_max_SP = Integer.MAX_VALUE;
        int chosenNode = -1;
        Iterator<NodeData> iter = this.graph.nodeIter();
        while(iter.hasNext()){
            int node = iter.next().getKey();
            // find the maximum shortest path for each node
            double max_SP = maxShortestPath(node);
            if(max_SP < min_max_SP){
                min_max_SP = max_SP;
                chosenNode = node;
            }
        }
        // return the node with the minimized maximum shortest path
        return this.graph.getNode(chosenNode);
    }

    private double maxShortestPath(int src){
        double maxS_P = 0;
        Iterator<NodeData> iter = this.graph.nodeIter();
        while(iter.hasNext()){
            NodeData N = iter.next();
            if (N.getKey() != src) {
                double S_P = this.shortestPathDist(src, N.getKey());
                if (S_P > maxS_P)
                    maxS_P = S_P;
            }
        }
        return maxS_P;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    private void setAllTags(DirectedWeightedGraph g ,int value) {
        Iterator<NodeData> nodeIter = g.nodeIter();
        while (nodeIter.hasNext()){
            NodeData currentNode = nodeIter.next();
            currentNode.setTag(value);
        }
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    public static void main(String[] args) {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        g.load("G1.json");
    }
}
