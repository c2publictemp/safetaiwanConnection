package safetaiwan.micromata.jak.UI;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import safetaiwan.micromata.jak.Common.CommonTools;
import safetaiwan.micromata.jak.Receive.KMLReceiveFromNet;

public class underWaterAreaFX extends Application {
	private Stage stage;
	private final double MINIMUM_WINDOW_WIDTH = 750;
	private final double MINIMUM_WINDOW_HEIGHT = 650;
	public static void main(String[] args) {

		Application.launch(underWaterAreaFX.class, (java.lang.String[]) null);
	}
	public void start(Stage primaryStage) {
		try {
			this.stage = primaryStage;
			this.stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
			this.stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
			this.stage.setTitle("SafeTaiwan-UnderWater");
			gotoLogin();
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(underWaterAreaFX.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private void gotoLogin() {
		try {
			 String path = CommonTools.appLocation() + "/resources/exampledata/underwater.kml";
			 String URL = "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
			KMLReceiveFromNet.KMLReceive(URL, path);
			underWaterAreaController uWAC = (underWaterAreaController) replaceSceneContent("underWaterArea.fxml");
			uWAC.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(underWaterAreaFX.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private Node replaceSceneContent(String fxml) throws Exception {
		FXMLLoader loader = new FXMLLoader();

		// InputStream in = new FileInputStream("D:\\WorkSpaceAll\\PercyWorkSpace\\safetaiwanConnection2\\safetaiwanConnection\\src\\safetaiwan\\micromata\\jak\\UI\\underWaterArea.fxml");
		InputStream in = underWaterAreaFX.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(underWaterAreaFX.class.getResource(fxml));
		VBox page;
		try {
			page = (VBox) loader.load(in);
		} finally {
			in.close();
		}

		// Store the stage width and height in case the user has resized the window
		double stageWidth = stage.getWidth();
		if (!Double.isNaN(stageWidth)) {
			stageWidth -= (stage.getWidth() - stage.getScene().getWidth());
		}

		double stageHeight = stage.getHeight();
		if (!Double.isNaN(stageHeight)) {
			stageHeight -= (stage.getHeight() - stage.getScene().getHeight());
		}

		Scene scene = new Scene(page);
		if (!Double.isNaN(stageWidth)) {
			page.setPrefWidth(stageWidth);
		}
		if (!Double.isNaN(stageHeight)) {
			page.setPrefHeight(stageHeight);
		}

		stage.setScene(scene);
		stage.sizeToScene();
		return (Node) loader.getController();
	}
}
