/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;
	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.boxArco.getItems().clear();
    	String category = this.boxCategoria.getValue();
    	Integer anno = this.boxAnno.getValue();
    	if(category==null) {
    		this.txtResult.appendText("Selezionare una categoria\n");
    		return;
    	}
    	if(anno==null) {
    		this.txtResult.appendText("Selezionare un anno\n");
    		return;
    	}
    	this.model.creaGrafo(category, anno);
    	this.grafoCreato = true;
    	this.txtResult.appendText("Grafo creato!\n# vertici: "+this.model.getNumVertici()+"\n# archi: "+this.model.getNumArchi()+"\n");
    	String result = "";
		for(Adiacenza a : this.model.getBestEdges()) {
			result += a.getT1()+" VS "+a.getT2()+" ("+a.getPeso()+")\n";
		}
		this.txtResult.appendText(result);
    	this.boxArco.getItems().addAll(this.model.getBestEdges());
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	if(this.boxArco.getValue()==null) {
    		this.txtResult.appendText("Selezionare un arco\n");
    		return;
    	}
    	Adiacenza a = this.boxArco.getValue();
    	this.txtResult.appendText(this.model.getPercorso(a));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(this.model.getYears());
    	this.boxCategoria.getItems().addAll(this.model.getOffenseList());
    }
}
