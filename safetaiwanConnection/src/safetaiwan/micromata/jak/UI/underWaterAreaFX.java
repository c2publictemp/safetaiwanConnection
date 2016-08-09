package safetaiwan.micromata.jak.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;

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

			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(underWaterAreaFX.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
