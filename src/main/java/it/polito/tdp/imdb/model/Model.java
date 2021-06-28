package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

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
}
