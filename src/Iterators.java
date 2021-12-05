import api.EdgeData;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class Iterators {

    public static class NodeIterator implements Iterator<NodeData> {

        private MyDWG graph;
        private int currentPos;

        public NodeIterator(MyDWG g) {
            this.graph = g;
            currentPos = 0;
        }

        @Override
        public boolean hasNext() {
            return currentPos < (this.graph.nodeSize() - 1);
        }

        @Override
        public NodeData next() {
            return this.graph.getNode(currentPos++);
        }

        @Override
        public void remove() {
            remove(currentPos--);
        }

        public void remove(int key) {
            this.graph.removeNode(key);
        }

        @Override
        public void forEachRemaining(Consumer action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    public static class EdgeIterator implements Iterator<EdgeData> {

        MyDWG graph;
        int currentSrcPos;
        int currentDstPos;
        HashMap<Integer, HashMap<Integer, EdgeData>> edges;

        public EdgeIterator(MyDWG g) {
            this.graph = g;
            this.edges = g.getEdgeList();
            currentSrcPos = 0;
            currentDstPos = 0;
        }

        @Override
        public boolean hasNext() {
            return currentDstPos < (edges.get(currentSrcPos).size() - 1);
        }

        @Override
        public EdgeData next() {
            EdgeData ans = null;
            if (currentSrcPos < this.graph.nodeSize()){
                ans = edges.get(currentSrcPos).get(currentDstPos++);
                if (!edges.get(currentSrcPos).containsKey(currentDstPos + 1)) {
                    currentSrcPos++;
                    currentDstPos = 0;
                }
            }
            return ans;
        }

        @Override
        public void remove() {
            remove(edges.get(currentSrcPos).get(currentDstPos));
        }

        public void remove(EdgeData e) {
            this.graph.removeEdge(e.getSrc(),e.getDest());
        }

        @Override
        public void forEachRemaining(Consumer action) {
            Iterator.super.forEachRemaining(action);
        }
    }


    public static class SpesificEdgeIterator implements Iterator<EdgeData> {

        MyDWG graph;
        int currentPos;
        int NodeKey;

        public SpesificEdgeIterator(MyDWG g, int nodeKey) {
            this.graph = g;
            this.currentPos = 0;
            this.NodeKey = nodeKey;
        }

        @Override
        public boolean hasNext() {
            return currentPos < (this.graph.edgeSize() - 1);
        }

        @Override
        public EdgeData next() {
            return this.graph.getEdge(NodeKey, currentPos++);
        }

        @Override
        public void remove() {
            remove(this.graph.getEdge(NodeKey, currentPos));
        }

        public void remove(EdgeData e) {
            this.graph.removeEdge(e.getSrc(), e.getDest());
        }

        @Override
        public void forEachRemaining(Consumer action) {
            Iterator.super.forEachRemaining(action);
        }
    }
}