
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

    /* https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 */
    @Override
    public double shortestPathDist(int src, int dest) {              // in progress
        if (this.graph.nodeSize() < 2) {
            return -1;
        }
        HashMap<Integer, Double> allPaths = new HashMap<>();
        Queue<NodeData> NodeQueue = new LinkedList<>();
     /* The next line is legal because in the beginning we confirmed that our graph nodeSize > 1.
        Started with first element of the nodeList because, there is no preference to start with a specific node */
        Iterator<NodeData> nodeIter = this.graph.nodeIter();
        while (nodeIter.hasNext()) {
            NodeData n = nodeIter.next();
            n.setTag(NotYetConnected);
            allPaths.put(n.getKey(), 0.0);
        }
//     The next line is legal because in the beginning we confirmed that our graph nodeSize > 1.
//     In addition, it doesn't meter which node will be the starting one.
        NodeQueue.add(this.graph.getNode(src));
        while (!NodeQueue.isEmpty()) {
            NodeData currentNode = NodeQueue.remove();
            boolean flag = false;

            Iterator<EdgeData> edgeIter = this.graph.edgeIter(currentNode.getKey());
            while (edgeIter.hasNext() && !flag) {
                EdgeData currentEdge = edgeIter.next();
                NodeData currentEdgeNode = this.graph.getNode(currentEdge.getDest());
                NodeQueue.add(currentEdgeNode);
                Double currentWight = allPaths.get(currentEdgeNode.getKey());
                allPaths.put(currentNode.getKey(), (currentWight + currentEdge.getWeight()));
                flag = (currentEdgeNode.getKey() == dest);
                currentEdgeNode.setTag(IsConnected);
            }
        }
        double shortestPath = Double.MAX_VALUE;
        for (int i = 0; i < allPaths.size(); i++) {
            int currentNodeTag = this.graph.getNode(i).getTag();
            if ((currentNodeTag == IsConnected) && allPaths.get(i) < shortestPath) {
                shortestPath = allPaths.get(i);
            }
        }
        return (shortestPath == 0.0) ? -1 : shortestPath;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
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

