package safetaiwan.micromata.jak.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import safetaiwan.micromata.jak.parser.underWaterArea;

/**
 * Login Controller.
 */
public class underWaterAreaController extends AnchorPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML
    ComboBox regionNameComboBox;
    @FXML
    WebView webContentView;
    @FXML
    Label cAreaLabel;
    @FXML
    Label cEdgeLabel;
    @FXML
    Button analysisButton;
    @FXML
    TextField InputOverAreaTextField;
    @FXML
    ComboBox OverAreaComboBox;

    private underWaterAreaFX application;
    
    
    public void setApp(underWaterAreaFX application){
        this.application = application;
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    	WebEngine webEngine = webContentView.getEngine();
    	 webEngine.load("http://60.250.226.78/index.html");
//    	 getChildren().add(webContentView);
    }
    
    

}
