package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private Map<Integer, Actor> idMap;
	private ImdbDAO dao;
	private Graph<Actor,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		dao.listAllActors(idMap);
	}
	public List<String> getGeneri(){
		return dao.getGeneri();
	}
	
	public void creaGrafo(String genere) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, genere));
		
		for(Adiacenza a : dao.getArchi(idMap, genere)) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public int getNArchi() {
		if(this.grafo!=null)
			return this.grafo.edgeSet().size();
		return -1;
	}
	
	public int getNVertici() {
		if(this.grafo!=null)
			return this.grafo.vertexSet().size();
		return -1;
	}
	public List<Actor> getBoxAttori(){
		if(this.grafo!=null) {
		Set<Actor> p = new HashSet<>(this.grafo.vertexSet());
		List<Actor> result = new ArrayList<>(p);
		Collections.sort(result);
		return result;
		}
		return null;
	}
	
	public List<Actor> getAttoriSimili(Actor a){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor,DefaultWeightedEdge>(this.grafo);
		List<Actor> result = new LinkedList<Actor>(ci.connectedSetOf(a));
		result.remove(a);
		Collections.sort(result);
		return result;
	}
		
		
	
}
