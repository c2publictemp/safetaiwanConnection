package safetaiwan.micromata.jak.UI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import javafx.application.Application;
import safetaiwan.micromata.jak.Common.CommonTools;
import safetaiwan.micromata.jak.parser.underWaterArea;

public class RWJavascriptFile {

	private underWaterAreaFX application;
	private static String Wpath = CommonTools.appLocation() + "/resources/exampledata/index.html";
	static String path = CommonTools.appLocation() + "/resources/exampledata/underwater.kml";
	public static void main(String[] args) {
//		new RWJavascriptFile() ;
	}
	RWJavascriptFile(HashMap areaOverArray,String color) {
		String one = "<html>  <head>  <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">" + "<meta charset=\"utf-8\">   <title>Simple Polygon</title>   <style>       html, body {"
				+ "height: 100%;  margin: 0;  padding: 0;       } " + " #map {   height: 100%;   }  </style>   </head>   <body> <div id=\"map\"></div> <script> ";
		
		String two = "function initMap() { 	var myLatLng = { lat : 23.973875, lng : 120.982024 "+
			 "	};  var map = new google.maps.Map(document.getElementById('map'), {  zoom: 7, "+
             " center: myLatLng, mapTypeId: google.maps.MapTypeId.ROADMAP  }); " ;
		String three = content()+content2( areaOverArray, color)+content3();
		String four = "  }  </script> <script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyDokwH5r_98ZW0izFtwtICJ1iXcRlVf97I&signed_in=true&callback=initMap\" " + " async defer> </script>  </body> </html> ";

		String sum = one + two + three+four;
		WriteFile(sum, Wpath, false);
	}

	public static String content() {
		String var = "";

		underWaterArea uWA = new underWaterArea(path);

		for (int i = 0; i < uWA.placeMarkTotalNum(0); i++) {
//		for (int i = 0; i < 1; i++) {
			// for(int i = 0 ; i < 3;i++){
			String selectKey = "0" + "/" + i + "/" + "0" + "/" + "out";
			List<Coordinate> c = uWA.getKMLCoordinates(0, i, 0).get(selectKey);
			var = var + "var tcoords" + i + "=[";
			String varContent = "";
			String sumvarContent = "";
			for (int j = 0; j < c.size(); j++) {
				varContent = "{lat:" + c.get(j).getLatitude() + "," + "lng:" + c.get(j).getLongitude() + "}";
				if (j != c.size() - 1) {
					varContent = varContent + ",";
				}
				sumvarContent = sumvarContent + varContent;
			}
			var = var + sumvarContent + "]; " + "\n";
		}

		return var;

	}
	public String content2(HashMap areaOverArray,String color) {
		String var = "";
		underWaterArea uWA = new underWaterArea(path);
		HashMap colorDecoder = new HashMap();
		colorDecoder.put("red", "'#FF0000'");
		colorDecoder.put("yellow", "'#FFFF00'");
		colorDecoder.put("green", "'#ADFF2F'");
		colorDecoder.put("default","'#0000FF'");
		String strokeColor = "'#FF0000'";
		String fillColor = "'#FF0000'";
		for (int i = 0; i < uWA.placeMarkTotalNum(0); i++) {
//		for (int i = 0; i < 1; i++) {
			if(areaOverArray.get(i)!=null){
				var = var + " var bermudaTriangle" + i + 
						"= new google.maps.Polygon({ paths : tcoords" + i + 
						"," + " strokeColor: " + colorDecoder.get(color) + "," + 
						" strokeOpacity: 0.8," + " strokeWeight: 2," + 
						" fillColor:" + colorDecoder.get(color) + ","
						+ " fillOpacity: 0.35 }); ";
			}else {
				var = var + " var bermudaTriangle" + i + 
						"= new google.maps.Polygon({ paths : tcoords" + i + 
						"," + " strokeColor: " + colorDecoder.get("default") + "," + 
						" strokeOpacity: 0.8," + " strokeWeight: 2," + 
						" fillColor:" + colorDecoder.get("default") + ","
						+ " fillOpacity: 0.35 }); ";
			}
			
		}

		return var;
	}
	public String content3(){
		String var = "";
		underWaterArea uWA = new underWaterArea(path);
		
		for (int i = 0; i < uWA.placeMarkTotalNum(0); i++) {
//		for (int i = 0; i < 1; i++) {
		 var = var+ "bermudaTriangle"+i+".setMap(map) ; ";
		}
		return var;
		
	}
	
	public String content4(){
		
		
		return null;
		
	}
	
	
	public static void WriteFile(String str, String path, boolean append)

	{ // 寫檔

		try

		{

			File file = new File(path);// 建立檔案，準備寫檔

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "utf8"));// 設定為BIG5格式

			// 參數append代表要不要繼續許入該檔案中

			writer.write(str); // 寫入該字串

			writer.newLine(); // 寫入換行

			writer.close();

		} catch (IOException e)

		{

			e.printStackTrace();

			System.out.println(path + "寫檔錯誤!!");

		}

	}
}
