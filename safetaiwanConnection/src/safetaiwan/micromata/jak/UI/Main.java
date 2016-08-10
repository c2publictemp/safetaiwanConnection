package safetaiwan.micromata.jak.UI;

import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import safetaiwan.micromata.jak.Common.CommonTools;
import safetaiwan.micromata.jak.parser.underWaterArea;
//  w  w  w.j  a  v a 2s.  co m
public class Main extends Application {

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root, 1000, 1000);
    stage.setScene(scene);

    Group g = new Group();

	 String path = CommonTools.appLocation() + "/resources/exampledata/underwater.kml";
	 underWaterArea uWA = new underWaterArea(path);
    
    int selectIndex = 0;
   
	String selectKey = "0" + "/" + selectIndex + "/" + "0" + "/" + "out";
	List<Coordinate> c = uWA.getKMLCoordinates(0, selectIndex, 0).get(selectKey);
	Double[] d= new Double[c.size()*2];
	for(int i = 0 ; i < c.size() ; i ++){
		
		System.out.println(c.get(i).getLatitude());
//		d[2*i+1]=(c.get(i).getLatitude()-20.0)*100;
//		d[2*i]=(c.get(i).getLongitude()-121.0)*100;
		d[2*i+1]=lngToPixel(c.get(i).getLatitude(),0);
		d[2*i]=latToPixel(c.get(i).getLongitude(),0);
		
		System.out.println(d[2*i]+" "+d[2*i+1]);
	}
	
    Polyline polyline = new Polyline();
//    polyline.getPoints().addAll(new Double[]{
//        0.0, 0.0,
//        20.0, 10.0,
//        10.0, 20.0 });
    polyline.getPoints().addAll(d);
    g.getChildren().add(polyline);
    
    scene.setRoot(g);
    stage.show();
  }

  /** 
       * 经度到象数点 
       * @param lng 
       * @param zoom 
       * @return 
       */  
      public static double lngToPixel(double lng, int zoom) {  
    
          return (lng + 180) * (256L << zoom) / 360;  
    
      }  
        
      /** 
       * 纬度到象数点 
       * @param lat 
       * @param zoom 
       * @return 
       */  
      public static double latToPixel(double lat, int zoom) {  
    
          double siny = Math.sin(lat * Math.PI / 180);  
    
          double y = Math.log((1 + siny) / (1 - siny));  
    
          return (128 << zoom) * (1 - y / (2 * Math.PI));  
    
          }  

  public static void main(String[] args) {
    launch(args);
  }
}