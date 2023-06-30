package it.polito.tdp.baseball;


// Pulisci il grafo ogni volta che fai clic sul pulsante "Crea grafo"

import java.net.URL;
import java.util.ResourceBundle;


import it.polito.tdp.baseball.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*      



--- FXML --- 

this.model.creaGrafo(annoSelezionato);

txtResult.appendText("numero vertici: " + this.model.numeroVertici() + "\n");
txtResult.appendText("numero archi: " + this.model.numeroArchi() + "\n");

txtMinuti.clear();
cmbMese.getItems().clear();

public int numeroVertici() {
return this.grafo.vertexSet().size();
}

public int numeroArchi() {
	return this.grafo.edgeSet().size();
}




--- BASEBALL --- 

BaseballDAO baseballDao;
SimpleGraph<People, DefaultEdge> grafo;
Map<String, People> playerIdMap;

public Model() {

	baseballDao = new BaseballDAO();
}

public void creaGrafo(){
grafo = new SimpleGraph<>(DefaultEdge.class);
playerIdMap = new HashMap<>();
}






--- CONTROLLI ---

if (grafo == null) {
	throw new RuntimeException("Grafo non esistente");
}



--- MASSIMO ---

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

public Movie movieDiGradoMassimo() {
sommaMassima = 0;
Movie massimo = null;
for (Movie m : this.grafo.vertexSet()) {  
	List<Movie> listaVicini = new LinkedList<>(Graphs.neighborListOf(grafo, m));
	int somma = 0; //inizializzo una somma per ogni vertice
		for (Movie mm : listaVicini) { 
			for(CoppiaPeso cp : archi) { 
				if (cp.getMovie1().equals(m) && cp.getMovie2().equals(mm) ||
						cp.getMovie1().equals(mm) && cp.getMovie2().equals(m)) {
					somma += cp.getPeso();
		}
	}
}
if (somma > sommaMassima) { 
	sommaMassima = somma;
	massimo = m;
	}
}
return massimo;
}





--- COMPONENTE CONNESSA ---

public DimensioneConnessaSommaArchi dimensioneComponeneteConnessa(Retailers venditore) {
int sommaArchi= 0;
ConnectivityInspector<Retailers, DefaultWeightedEdge> cc = new ConnectivityInspector<>(grafo);
Set<Retailers> connessi = cc.connectedSetOf(venditore);
for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
	if (connessi.contains(this.grafo.getEdgeSource(d)) && connessi.contains(this.grafo.getEdgeTarget(d))) {
		sommaArchi+= this.grafo.getEdgeWeight(d);
	}
}
DimensioneConnessaSommaArchi dcsa = new DimensioneConnessaSommaArchi(connessi.size(), sommaArchi);
return dcsa;
}

}




--- ADIACENTI ---

public List<Vicini> listaAdiacenti(Distretto distretto){
List<Vicini> listaVicini = new LinkedList<>();
List<Distretto> listaAdiacenti = new LinkedList<>(Graphs.neighborListOf(grafo, distretto));
for (Distretto d : listaAdiacenti) {
	listaVicini.add(new Vicini(d.getDistrettoId(), grafo.getEdgeWeight(grafo.getEdge(distretto, d)) ));
}
return listaVicini;
}

public List<CoppiaPeso> getAdiacenti(Director registaSelezionato, int annoSelezionato){
List<CoppiaPeso> listaAdiacenti = new LinkedList<>();
for (CoppiaPeso c : imdbDAO.listaCoppieConPeso(annoSelezionato)) {
		if(c.getDirectorId1() == registaSelezionato.getId()|| c.getDirectorId2() == registaSelezionato.getId()) {
			listaAdiacenti.add(c);
		}
}
return listaAdiacenti;
}





--- ARCHI ENTRANTI E USCENTI ---

public double deltaEfficienzaDiUnGiocatore(Player giocatore) {
double differenza = 0.0;
double sommaEntranti = 0.0;
double sommaUscenti = 0.0;

List<DefaultWeightedEdge>archiEntranti = new LinkedList<>(this.grafo.incomingEdgesOf(giocatore));
List<DefaultWeightedEdge>archiUscenti = new LinkedList<>(this.grafo.outgoingEdgesOf(giocatore));

-- List<DefaultWeightedEdge> archiDiUnVertice = new LinkedList<>(this.grafo.edgesOf(albumSelezionato)); -- SE GLI ARCHI NON SONO DIREZIONATI

for (DefaultWeightedEdge d : archiEntranti) {
	sommaEntranti += grafo.getEdgeWeight(d);
}
for (DefaultWeightedEdge de : archiUscenti) {
	sommaUscenti += grafo.getEdgeWeight(de);
}
differenza = sommaUscenti - sommaEntranti;
return differenza;
}






--- PESO TRA DUE VERTICI ---

public Double pesoArcoTraDueGeni(Genes gene1, Genes gene2) {
Double peso = 0.0;
for (DefaultWeightedEdge d : this.grafo.edgeSet()) {
	if (this.grafo.getEdgeSource(d).equals(gene1) && this.grafo.getEdgeTarget(d).equals(gene2)) {
		peso = this.grafo.getEdgeWeight(d);
	}
}
return peso;
}

*/

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnesse;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnDreamTeam;

    @FXML
    private Button btnGradoMassimo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtYear;

    
    
    @FXML
    void doCalcolaConnesse(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	txtResult.appendText("numero componenti connesse: " + this.model.numeroComponentiConnesse());
    	
    }

    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.btnGradoMassimo.setDisable(false);
    	this.btnConnesse.setDisable(false);
    	
    	txtResult.clear();
    	
    	if (txtYear.getText() == "") {
    		txtResult.setText("inserisci un anno");{
    			return;
    		}
    	}
    	
    	if (txtSalary.getText() == "") {
    		txtResult.setText("inserisci un salario");{
    			return;
    		}
    	}
    	
    	txtResult.clear();
    	try {
    		int annoInserito = Integer.parseInt(txtYear.getText());
    		double salarioInserito = Double.parseDouble(txtSalary.getText()) * 1000000;
    		if (model.listaAnni().contains(annoInserito)) {
    			this.model.creaGrafo(annoInserito, salarioInserito);
    			txtResult.appendText("numero vertici: " + this.model.numeroVertici() + "\n");
    			txtResult.appendText("numero archi: " + this.model.numeroArchi() + "\n");
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("il valore inserito non è un numero");
    	}
    	
    }

    
    @FXML
    void doDreamTeam(ActionEvent event) {

    }

    
    @FXML
    void doGradoMassimo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	
    	
    	txtResult.appendText("giocatore con grado massimo : " + this.model.giocatoreMassimo().toString() 
    			+ " " +this.model.numeroMassimoArchi());

    }

    
    @FXML
    void initialize() {
        assert btnConnesse != null : "fx:id=\"btnConnesse\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGradoMassimo != null : "fx:id=\"btnGradoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
