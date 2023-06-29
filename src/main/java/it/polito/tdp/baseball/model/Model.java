package it.polito.tdp.baseball.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.baseball.db.BaseballDAO;

public class Model {
	
	BaseballDAO baseballDao;
	SimpleGraph<People, DefaultEdge> grafo;
	Map<String, People> playerIdMap;
	int massimoNumeroDiArchi;
	
	public Model() {
		
		baseballDao = new BaseballDAO();
		grafo = new SimpleGraph<>(DefaultEdge.class);
		playerIdMap = new HashMap<>();
	}
	
	public List<Integer> listaAnni(){
		return baseballDao.listaAnni();
	}
	
	public void creaGrafo(int annoSelezionato, double salarioSelezionato) {
		
		List<People> listaVertici = new LinkedList<>(baseballDao.listaGiocatoriConSalarioMinimoInQuellAnno(annoSelezionato, salarioSelezionato));
		Graphs.addAllVertices(grafo, listaVertici);
		for(People p : listaVertici) {
			playerIdMap.put(p.getPlayerID(), p);
		}
		
		
		List<Coppia>listaCoppie = new LinkedList<>(baseballDao.listaCoppie(annoSelezionato, salarioSelezionato));
		for(Coppia c : listaCoppie) {
			Graphs.addEdgeWithVertices(grafo, playerIdMap.get(c.getPlayerId1()), playerIdMap.get(c.getPlayerId2()));
		}
	
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public People giocatoreMassimo() {
		People giocatoreMassimo = null;
		massimoNumeroDiArchi = 0;
		
		for (People p : this.grafo.vertexSet()) {
			List<DefaultEdge> archiDelGiocatore = new LinkedList<>(this.grafo.edgesOf(p));
			if (archiDelGiocatore.size() > massimoNumeroDiArchi) {
				massimoNumeroDiArchi = archiDelGiocatore.size();
				giocatoreMassimo = p;
			}
		}
		return giocatoreMassimo;
	}
	
	public int numeroMassimoArchi() {
		return massimoNumeroDiArchi;
	}

	public int numeroComponentiConnesse() {
		ConnectivityInspector<People, DefaultEdge> componenetiConnesse = new ConnectivityInspector<People, DefaultEdge>(grafo);
		return componenetiConnesse.connectedSets().size();
	}
	
}