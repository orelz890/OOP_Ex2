import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyDWGAlgorithmTest {

    Location p1 = new Location(35.19589389346247, 32.10152879327731, 0.0);
    Location p2 = new Location(35.20319591121872, 32.10318254621849, 0.0);
    Location p3 = new Location(35.20752617756255, 32.1025646605042, 0.0);
    Location p4 = new Location(35.21007339305892, 32.10107446554622, 0.0);
    Location p5 = new Location(35.21310882485876, 32.104636394957986, 0.0);

    NodeData a = new Node(0, p1);
    NodeData b = new Node(1, p2);
    NodeData c = new Node(2, p3);
    NodeData d = new Node(3, p4);
    NodeData e = new Node(4, p5);
    NodeData[] nodes = {a, b, c, d, e};

    EdgeData e1 = new Edge(0, 4, 1.3);
    EdgeData e2 = new Edge(1, 0, 1.2);
    EdgeData e3 = new Edge(1, 4, 1.8);
    EdgeData e4 = new Edge(2, 1, 1.7);
    EdgeData e5 = new Edge(3, 2, 1.5);
    EdgeData e6 = new Edge(3, 0, 1);
    EdgeData e7 = new Edge(4, 2, 4);
    EdgeData e8 = new Edge(4, 3, 1.4);
    EdgeData[] edge = {e1, e2, e3, e4, e5, e6, e7, e8};

    MyDWG empty = new MyDWG();
    MyDWGAlgorithm EmptyAlgorithm = new MyDWGAlgorithm();
    MyDWG dwg = new MyDWG();
    MyDWGAlgorithm algorithm = new MyDWGAlgorithm();
    Random random = new Random();


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        empty = new MyDWG();
        EmptyAlgorithm = new MyDWGAlgorithm();
        dwg = new MyDWG();
        algorithm = new MyDWGAlgorithm();

        dwg.addNode(a);
        dwg.addNode(b);
        dwg.addNode(c);
        dwg.addNode(d);
        dwg.addNode(e);

        dwg.connect(e1.getSrc(), e1.getDest(), e1.getWeight());
        dwg.connect(e2.getSrc(), e2.getDest(), e2.getWeight());
        dwg.connect(e3.getSrc(), e3.getDest(), e3.getWeight());
        dwg.connect(e4.getSrc(), e4.getDest(), e4.getWeight());
        dwg.connect(e5.getSrc(), e5.getDest(), e5.getWeight());
        dwg.connect(e6.getSrc(), e6.getDest(), e6.getWeight());
        dwg.connect(e7.getSrc(), e7.getDest(), e7.getWeight());
        dwg.connect(e8.getSrc(), e8.getDest(), e8.getWeight());
        algorithm.init(dwg);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void init() {
        Iterator<NodeData> nodeIter = dwg.nodeIter();
        Iterator<EdgeData> edgeIter = dwg.edgeIter();
        NodeData n1 = null;
        EdgeData e = null;
        int i = 0;
        while (i < 5) {
            if (nodeIter.hasNext()) {
                n1 = nodeIter.next();
            }
            if (edgeIter.hasNext()) {
                e = edgeIter.next();
            }
            assertEquals(n1.getKey(), nodes[i].getKey());
            assertEquals(n1.getWeight(), nodes[i].getWeight());
            assertEquals(n1.getTag(), nodes[i].getTag());
            assertEquals(n1.getLocation().x(), nodes[i].getLocation().x());
            assertEquals(n1.getLocation().y(), nodes[i].getLocation().y());
            assertEquals(n1.getLocation().z(), nodes[i].getLocation().z());

            assertTrue(dwg.edgeList.containsKey(e.getSrc()));
            assertTrue(dwg.edgeList.get(e.getSrc()).containsKey(e.getDest()));
            i++;
        }
    }

    @org.junit.jupiter.api.Test
    void getGraph() {
        DirectedWeightedGraph g = algorithm.getGraph();
        isTheSame(g, dwg);
    }

    @org.junit.jupiter.api.Test
    void copy() {
        DirectedWeightedGraph g = algorithm.copy();
        isTheSame(g, dwg);
    }

    @org.junit.jupiter.api.Test
    void isConnected() {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        g.load("data/CompleteG.json");
        assertTrue(g.isConnected());
        g.load("data/MyG.json");
        assertTrue(g.isConnected());
        g.load("data/G1.json");
        assertTrue(g.isConnected());
        g.load("data/G2.json");
        assertTrue(g.isConnected());
        g.load("data/G3.json");
        assertTrue(g.isConnected());
        // 1 is not connected
        g.load("data/NotConnectedG.json");
        assertFalse(g.isConnected());
        // 47 is not connected
        g.load("data/NotConnectedG2.json");
        assertFalse(g.isConnected());
    }

    @org.junit.jupiter.api.Test
    void isNodeConnected() {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        DirectedWeightedGraph graph = g.getGraph();
        g.load("data/CompleteG.json");
        assertTrue(g.isNodeConnected(graph, graph.getNode(0)));
        g.load("data/MyG.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(1)));
        g.load("data/G1.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(2)));
        g.load("data/G2.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(3)));
        g.load("data/G3.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(4)));
        // 1 is connected but no one can get to him
        g.load("data/NotConnectedG.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(1)));
        assertFalse(g.isNodeConnected(graph, graph.getNode(2)));
        // 47 is connected but no one can get to him
        g.load("data/NotConnectedG2.json");
        graph = g.getGraph();
        assertTrue(g.isNodeConnected(graph, graph.getNode(47)));
        assertFalse(g.isNodeConnected(graph, graph.getNode(45)));
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        DirectedWeightedGraph graph = g.getGraph();
        g.load("data/CompleteG.json");
        graph = g.getGraph();
        // Direct connection
        assertEquals(1, g.shortestPathDist(0, 2));
        assertEquals(1.5, g.shortestPathDist(3, 0));
        assertEquals(4, g.shortestPathDist(2, 4));
        // Not a Direct connection: 2->0->3
        assertEquals(5, g.shortestPathDist(2, 3));
        // 1->2->0
        assertEquals(3, g.shortestPathDist(1, 0));
        g.load("data/NotConnectedG.json");
        graph = g.getGraph();
        // Direct connection
        assertEquals(1, g.shortestPathDist(3, 0));
        // Not a Direct connection:
        // 2->0->4
        assertEquals(3, g.shortestPathDist(2, 4));
        // 2->0->4->3
        assertEquals(4.4, g.shortestPathDist(2, 3));
        // 0->4->3->2
        assertEquals(4.2, g.shortestPathDist(0, 2));
        // Path not exist
        assertEquals(-1, g.shortestPathDist(2, 1));
        // Input node do not exist
        assertEquals(-1, g.shortestPathDist(2, -1));

    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        g.load("data/NotConnectedG.json");
        DirectedWeightedGraph graph = g.getGraph();
        graph = g.getGraph();
        List<NodeData> ans = new LinkedList<>();
        // Direct connection
        ans.add(graph.getNode(3));
        ans.add(graph.getNode(0));
        assertEquals(ans, g.shortestPath(3, 0));
        // Not a Direct connection:
        // 2->0->4
        ans = new LinkedList<>();
        ans.add(graph.getNode(2));
        ans.add(graph.getNode(0));
        ans.add(graph.getNode(4));
        assertEquals(ans, g.shortestPath(2, 4));
        // 2->0->4->3
        ans = new LinkedList<>();
        ans.add(graph.getNode(2));
        ans.add(graph.getNode(0));
        ans.add(graph.getNode(4));
        ans.add(graph.getNode(3));
        assertEquals(ans, g.shortestPath(2, 3));
        // 0->4->3->2
        ans = new LinkedList<>();
        ans.add(graph.getNode(0));
        ans.add(graph.getNode(4));
        ans.add(graph.getNode(3));
        ans.add(graph.getNode(2));
        assertEquals(ans, g.shortestPath(0, 2));
        // Path not exist
        assertNull(g.shortestPath(2, 1));
        // Input node do not exist
        assertNull(g.shortestPath(2, -1));
    }

    @org.junit.jupiter.api.Test
    void center() {
        MyDWGAlgorithm g = new MyDWGAlgorithm();
        g.load("data/G1.json");
        DirectedWeightedGraph graph = g.getGraph();
        assertEquals(graph.getNode(8), g.center());

        g.load("data/G2.json");
        graph = g.getGraph();
        assertEquals(graph.getNode(0), g.center());

        g.load("data/G3.json");
        graph = g.getGraph();
        assertEquals(graph.getNode(40), g.center());

        g.load("data/MyG.json");
        graph = g.getGraph();
        assertEquals(graph.getNode(3), g.center());

    }

    @org.junit.jupiter.api.Test
    void tsp() {

    }

    @org.junit.jupiter.api.Test
    void save() {
        try {
            assertTrue(algorithm.save("file"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void load() {
        algorithm.save("file");
        DirectedWeightedGraphAlgorithms g1 = new MyDWGAlgorithm();
        g1.load("file");

        DirectedWeightedGraph graph1 = g1.getGraph();
        assertTrue(isTheSame(dwg, graph1), "load return different graph");

    }

    public boolean isTheSame(DirectedWeightedGraph g1, DirectedWeightedGraph g2) {
        if (g1.nodeSize() != g2.nodeSize() || g1.edgeSize() != g2.edgeSize()) {
            return false;
        }
        Iterator<NodeData> nodeIter1 = g1.nodeIter();
        Iterator<NodeData> nodeIter2 = g2.nodeIter();
        NodeData n1 = null;
        NodeData n2 = null;
        int i = 0;
        while (i < g1.nodeSize()) {
            if (nodeIter1.hasNext()) {
                n1 = nodeIter1.next();
            }
            if (nodeIter2.hasNext()) {
                n2 = nodeIter2.next();
            }
            assertEquals(n1.getKey(), n2.getKey());
            assertEquals(n1.getWeight(), n2.getWeight());
            assertEquals(n1.getLocation().x(), n2.getLocation().x());
            assertEquals(n1.getLocation().y(), n2.getLocation().y());
            assertEquals(n1.getLocation().z(), n2.getLocation().z());
            i++;
        }
        Iterator<EdgeData> edgeIter1 = g1.edgeIter();
        Iterator<EdgeData> edgeIter2 = g2.edgeIter();
        EdgeData e1 = null;
        EdgeData e2 = null;
        i = 0;
        while (i < g1.edgeSize()) {
            if (edgeIter1.hasNext()) {
                e1 = edgeIter1.next();
            }
            if (edgeIter2.hasNext()) {
                e2 = edgeIter2.next();
            }
            assertEquals(e1.getSrc(), e2.getSrc());
            assertEquals(e1.getDest(), e2.getDest());
            assertEquals(e1.getWeight(), e2.getWeight());
            assertEquals(e1.getTag(), e2.getTag());
            i++;
        }
        return true;
    }

    @org.junit.jupiter.api.Test
    void runTimeMillionNodes() {
        long start = new Date().getTime();
        int size =6;
        for (int i = 0; i < size; i++) {
            Node temp = new Node(i);
            empty.addNode(temp);
        }

        double w = 0.1;
        List<NodeData> cities = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            w += 0.2;
            for (int j = i + 1; j < size && j < i + 21; j++) {
                w += 0.1;
                empty.connect(i, j, w);
            }
            if (i == 0) {
                empty.connect(0, i+1, w);
            } else if (i%20 == 0){
                empty.connect(i, i - 20, w);
            }
            if (i % 1000 == 0) {
                cities.add(empty.getNode(i));
            }
        }
        EmptyAlgorithm.init(empty);
        Iterator<NodeData> nodeIter = EmptyAlgorithm.getGraph().nodeIter();
        while (nodeIter.hasNext()) {
            NodeData node = nodeIter.next();
            Iterator<EdgeData> edge = EmptyAlgorithm.getGraph().edgeIter(node.getKey());
            while (edge.hasNext()){
                System.out.println(edge.next());
            }
        }
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        assertTrue(dt < 5);
        System.out.println("runTimeTwoMillionNodes: " + dt);

        EmptyAlgorithm.shortestPathDist(0, 8862);
        end = new Date().getTime();
        dt = (end - start) / 1000.0;
        System.out.println("runTimeTwoMillionNodes: " + dt);
        assertTrue(dt < 10);

        EmptyAlgorithm.shortestPath(randomInt(0,10000), randomInt(235,436345));
        end = new Date().getTime();
        dt = (end - start) / 1000.0;
        System.out.println("runTimeTwoMillionNodes: " + dt);
        assertTrue(dt < 15);

        EmptyAlgorithm.isConnected();
        end = new Date().getTime();
        dt = (end - start) / 1000.0;
        System.out.println("runTimeTwoMillionNodes: " + dt);
        assertTrue(dt < 20);

//        EmptyAlgorithm.center();
//        end = new Date().getTime();
//        dt = (end - start) / 1000.0;
//        System.out.println("runTimeTwoMillionNodes: " + dt);
//        assertTrue(dt < 6);

        EmptyAlgorithm.tsp(cities);
        end = new Date().getTime();
        dt = (end - start) / 1000.0;
        System.out.println("runTimeTwoMillionNodes: " + dt);
//        assertTrue(dt < 10);

    }

    public int randomInt(int min, int max) {
        return random.ints(1, min, max).findFirst().getAsInt();
    }

}