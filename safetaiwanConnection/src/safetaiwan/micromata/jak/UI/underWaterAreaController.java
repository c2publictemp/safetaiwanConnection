package safetaiwan.micromata.jak.UI;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import safetaiwan.micromata.jak.Common.CommonTools;
import safetaiwan.micromata.jak.Common.FTPClientUpload;
import safetaiwan.micromata.jak.Receive.KMLReceiveFromNet;
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
	ComboBox<String> regionNameComboBox;
	@FXML
	WebView webContentView;
	@FXML
	Label cAreaLabel;
	@FXML
	Label cEdgeLabel;
	@FXML
	Button analysisButton;
	@FXML
	TextField InputOverAreaTextField = new TextField();
	
	@FXML
	ComboBox<String> OverAreaComboBoxColor;
	public String colorSelect = "red";

	private underWaterAreaFX application;
	private String path = CommonTools.appLocation() + "/resources/exampledata/underwater.kml";
	private underWaterArea uWA = new underWaterArea(path);
	
	public void setApp(underWaterAreaFX application) {
		this.application = application;
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		
		FTPClientUpload.uploadFileToFTP("60.250.226.78", "sowlu", "710522", CommonTools.appLocation()+"\\resources\\exampledata\\index_red.html", "index.html");;
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		webViewInitial();
		comboBoxRegionSelect();
		comboBoxOverAreaColorSelect();
		InputOverAreaTextField.setText("0.0");
	}


	private String[] getRegionName() {

		HashMap<String, String> HM = uWA.getKMLPlaceMarkNumName(uWA.getKMLFolderAllNumName().get("0"));// "0" because just one folder
		String[] jboxInput = new String[HM.size()];
		for (int i = 0; i < HM.size(); i++) {
			jboxInput[i] = HM.get(String.valueOf(i));
		}
		return jboxInput;
	}

	public void processButton(ActionEvent event) {
//		System.out.println("hello world");
		HashMap areaOver = uWA.createKMLForColorStyle(0, colorSelect, Double.valueOf(InputOverAreaTextField.getText()));
		new RWJavascriptFile(areaOver,colorSelect);
		FTPClientUpload.uploadFileToFTP("60.250.226.78", "sowlu", "710522", CommonTools.appLocation()+"\\resources\\exampledata\\index.html", "index.html");;
		webViewInitial();
		String[] excelCommand = new String[] { "C:\\Program Files (x86)\\Google\\Google Earth\\client\\googleearth.exe", "/s", path };
		try {
			Runtime.getRuntime().exec(excelCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void comboBoxRegionSelect() {
		regionNameComboBox.setPromptText("選擇查詢區域");
		ObservableList<String> data = FXCollections.observableArrayList(getRegionName());

		regionNameComboBox.setItems(data);
		regionNameComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				// System.out.println(ov);
				// System.out.println(t);
				// System.out.println(t1);
				int selectIndex = regionNameComboBox.getSelectionModel().getSelectedIndex();
				String selectKey = "0" + "/" + selectIndex + "/" + "0" + "/" + "out";
				List<Coordinate> c = uWA.getKMLCoordinates(0, selectIndex, 0).get(selectKey);
				double x = uWA.printGeoArea(c);
				DecimalFormat df = new DecimalFormat("#.###");
				String s = df.format(x);
				cAreaLabel.setText(s + "(km^2)");
				x = uWA.printGeoEdge(c);
				s = df.format(x);
				cEdgeLabel.setText(s + "(km)");
			}
		});
		regionNameComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				final ListCell<String> cell = new ListCell<String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item);
							setFont(this.getFont().font(this.getFont().getName(), 14.0)); // set your desired size
							setTextFill(Color.DARKGRAY);
						} else {
							setText(null);
						}
					}
				};
				return cell;
			}

		});

	}

	private void comboBoxOverAreaColorSelect() {
		OverAreaComboBoxColor.setPromptText("選擇顏色設置");
		String[] colorArray = {"red", "yellow", "green"};
		ObservableList<String> data = FXCollections.observableArrayList(colorArray);

		OverAreaComboBoxColor.setItems(data);
		OverAreaComboBoxColor.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String t, String t1) {
				// System.out.println(ov);
				// System.out.println(t);
				// System.out.println(t1);
				// int selectIndex = regionNameComboBox.getSelectionModel().getSelectedIndex();
				colorSelect = t1;
			}
		});
		OverAreaComboBoxColor.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				final ListCell<String> cell = new ListCell<String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item);
							setFont(this.getFont().font(this.getFont().getName(), 14.0)); // set your desired size
							if (item.contains("red")) {
								setTextFill(Color.RED);
							} else if (item.contains("yellow")) {
								setTextFill(Color.GOLD);
							} else if (item.contains("green")) {
								setTextFill(Color.GREEN);
							}

						} else {
							setText(null);
						}
					}
				};
				return cell;
			}

		});

	}

	private void webViewInitial() {
		WebEngine webEngine = webContentView.getEngine();
		webEngine.load("http://60.250.226.78/index.html");
//		webEngine.load("http://127.0.0.1:8080");
		// getChildren().add(webContentView);
	}
}
