package net.coderodde.graph;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class implements an undirected graph.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jan 10, 2016)
 */
public class UndirectedGraph extends AbstractGraph {

    private final Map<Integer, Map<Integer, Double>> map = 
            new LinkedHashMap<>();
    
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public int getNumberOfEdges() {
        return edges;
    }
    
    @Override
    public boolean addNode(int nodeId) {
        if (map.containsKey(nodeId)) {
            return false;
        }
        
        map.put(nodeId, new LinkedHashMap<Integer, Double>());
        return true;
    }

    @Override
    public boolean hasNode(int nodeId) {
        return map.containsKey(nodeId);
    }

    @Override
    public boolean removeNode(int nodeId) {
        if (!map.containsKey(nodeId)) {
            return false;
        }

        Map<Integer, Double> neighbors = map.get(nodeId);
        
        for (Integer neighborId : neighbors.keySet()) {
            map.get(neighborId).remove(nodeId);
        }
        
        map.remove(nodeId);
        return true;
    }

    @Override
    public boolean addEdge(int tailNodeId, int headNodeId, double weight) {
        addNode(tailNodeId);
        addNode(headNodeId);
        
        if (!map.get(tailNodeId).containsKey(headNodeId)) {
            map.get(tailNodeId).put(headNodeId, weight);
            map.get(headNodeId).put(tailNodeId, weight);
            ++edges;
            return true;
        } else {
            double oldWeight = map.get(tailNodeId).get(headNodeId);
            map.get(tailNodeId).put(headNodeId, weight);
            map.get(headNodeId).put(tailNodeId, weight);
            return oldWeight != weight;
        }
    }

    @Override
    public boolean hasEdge(int tailNodeId, int headNodeId) {
        if (!map.containsKey(tailNodeId)) {
            return false;
        }
        
        return map.get(tailNodeId).containsKey(headNodeId);
    }
    
    @Override
    public double getEdgeWeight(int tailNodeId, int headNodeId) {
        if (!hasEdge(tailNodeId, headNodeId)) {
            return Double.NaN;
        } 
        
        return map.get(tailNodeId).get(headNodeId);
    }

    @Override
    public boolean removeEdge(int tailNodeId, int headNodeId) {
        if (!map.containsKey(tailNodeId)) {
            return false;
        }
        
        if (!map.get(tailNodeId).containsKey(headNodeId)) {
            return false;
        }
        
        map.get(tailNodeId).remove(headNodeId);
        map.get(headNodeId).remove(tailNodeId);
        --edges;
        return true;
    }

    @Override
    public Set<Integer> getChildrenOf(int nodeId) {
        if (!map.containsKey(nodeId)) {
            return Collections.<Integer>emptySet();
        }
        
        return Collections.<Integer>unmodifiableSet(map.get(nodeId).keySet());
    }

    @Override
    public Set<Integer> getParentsOf(int nodeId) {
        if (!map.containsKey(nodeId)) {
            return Collections.<Integer>emptySet();
        }
        
        return Collections.<Integer>unmodifiableSet(map.get(nodeId).keySet());    
    }

    @Override
    public Set<Integer> getAllNodes() {
        return Collections.<Integer>unmodifiableSet(map.keySet());
    }

    @Override
    public void clear() {
        map.clear();
        edges = 0;
    }
}