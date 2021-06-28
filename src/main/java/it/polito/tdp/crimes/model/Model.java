package it.polito.tdp.crimes.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private Set<String> raggiungibili;
	private List<String> percorsoBest;
	private int pesoMin;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getOffenseList(){
		List<String> result = this.dao.getOffenseCategories();
		Collections.sort(result);
		return result;
	}
	
	public List<Integer> getYears(){
		List<Integer> result = this.dao.getYears();
		Collections.sort(result);
		return result;
	}
	
	public void creaGrafo(String category, int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(grafo, this.dao.getVertici(category, anno));
		
		//archi
		for(Adiacenza a : this.dao.getEdges(category, anno)) {
			Graphs.addEdge(grafo, a.getT1(), a.getT2(), a.getPeso());
		}
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getBestEdges() {
		List<Adiacenza> tipi = new LinkedList<>();
		int pesoMax = 0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			int peso = (int)this.grafo.getEdgeWeight(e);
			
			if(peso>pesoMax) {
				tipi.clear();
				pesoMax = peso;
				tipi.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), peso));
			}else {
				if(peso==pesoMax) {
					tipi.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), peso));
				}
			}
		}
		
		String result = "";
		for(Adiacenza a : tipi) {
			result += a.getT1()+" VS "+a.getT2()+" ("+a.getPeso()+")\n";
		}
		return tipi;
	}
	
	public String getPercorso(Adiacenza a) {
		ConnectivityInspector<String, DefaultWeightedEdge> ci = new ConnectivityInspector<String, DefaultWeightedEdge>(grafo);
		this.raggiungibili = ci.connectedSetOf(a.getT1());
		this.raggiungibili.remove(a.getT1());
		this.percorsoBest = null;
		this.pesoMin = Integer.MAX_VALUE;
		List<String> parziale = new LinkedList<>();
		parziale.add(a.getT1());
		this.cerca(parziale, 0, a.getT2());
		String result = "";
		
		if(this.percorsoBest==null) {
			return "Non è stato trovato un cammino aciclico da "+a.getT1()+" a "+a.getT2()+" che passasse per tutti i vertici\n";
		}
		for(String s : this.percorsoBest) {
			result += s+"\n";
		}
		result += "Peso totale: "+this.pesoMin+"\n";
		return result;
		
	}
	
	public void cerca(List<String> parziale, int peso, String last) {
		
		//caso terminale
		if(parziale.get(parziale.size()-1).equals(last) && parziale.size()==this.raggiungibili.size()) {
			
			if(peso<this.pesoMin) {
				pesoMin = peso;
				this.percorsoBest = new LinkedList<>(parziale);
			}
			return;
		}
		
		//genero sottoproblemi
		for(String s : this.raggiungibili) {
			
			//aggiungo solo se è un successore dell'ultimo inserito
			//e se non è già nel cammino parziale
			String ultimo = parziale.get(parziale.size()-1);
			
			if((grafo.getEdge(s, ultimo)!=null || grafo.getEdge(ultimo, s)!=null) && !parziale.contains(s)) {
				int pesoTemp = 0;
				if(grafo.getEdge(s, ultimo)!=null) {
					pesoTemp = (int)grafo.getEdgeWeight(grafo.getEdge(s, ultimo));
				}
				
				if(grafo.getEdge(ultimo, s)!=null) {
					pesoTemp = (int)grafo.getEdgeWeight(grafo.getEdge(ultimo, s));
				}
				
				parziale.add(s);
				this.cerca(parziale, peso+pesoTemp, last);
				parziale.remove(s);
				
			}
		}
	}
	
}
