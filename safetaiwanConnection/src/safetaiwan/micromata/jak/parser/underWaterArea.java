package safetaiwan.micromata.jak.parser;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.magiclen.magiclocationchecker.GeographyChecker;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.ListItemType;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import safetaiwan.micromata.jak.Common.CommonTools;

public class underWaterArea {
	Kml kml = null;
	public String path = "";
	public underWaterArea(String path) {
		this.path = path;
		this.kml = Kml.unmarshal(new File(path));
	}
	public static void main(String[] args) throws FileNotFoundException {
		// test1
		// String url =
		// "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
		// internetGetKmlFile(url);

		// test3
		String s = CommonTools.appLocation();
		String path = s + "/resources/exampledata/GWREGION.kml";
		String path2 = s + "/resources/exampledata/worldBorders.kml";
		// String path ="src/main/resources/exampledata/worldBorders.kml";
		// System.out.println(getKMLFolderName(path1)[0]);
		// System.out.println(getKMLFolderName(path2)[0]);
		// sysPrint1D(getKMLPlaceMarkName(path1, "gwregion"));
		// System.out.println();
		underWaterArea uwa = new underWaterArea(path);
		// System.out.println(uwa.getKMLPlaceMarkDescription( "gwregion","澎湖地區", "ID_00001"));
		// underWaterArea uwa = new underWaterArea(path);
		 HashMap<String, java.util.List<Coordinate>> r = uwa.getKMLCoordinates("gwregion", "澎湖地區", "ID_00001", 0);
		 java.util.List<Coordinate> c = r.get("0/1/0/out");
		 r = uwa.getKMLCoordinates(0,1,0);
		 c = r.get("0/1/0/out");
//		 printGeoArea(c);
		// System.out.println(c.get(0).getLongitude()+" "+c.get(0).getLatitude()+" "+c.get(0).getAltitude());

		// HashMap<String, java.util.List<Coordinate>> r = uwa.getKMLCoordinates(0,1,0);
		// java.util.List<Coordinate> c = r.get("0/1/0/out");
		// printGeoArea(c);
		// System.out.println(c.get(0).getLongitude()+" "+c.get(0).getLatitude()+" "+c.get(0).getAltitude());
		//

//		uwa.createKMLForColorStyle(0, "yellow", 1000);
//		printGeoEdge(c);
	}

	public  double printGeoArea(java.util.List<Coordinate> c) {

		// System.out.println("Size"+c.size());
		GeographyChecker.Vertex[] v = new GeographyChecker.Vertex[c.size()];
		for (int i = 0; i < c.size(); i++) {
			// System.out.println(c.get(i).getLongitude() + " " + c.get(i).getLatitude());
			v[i] = new GeographyChecker.Vertex(c.get(i).getLongitude(), c.get(i).getLatitude());
		}
		GeographyChecker ntust = new GeographyChecker(v);
		System.out.println("computeArea:" + ntust.computeArea());
		return ntust.computeArea();
	}
	
	public  double printGeoEdge(java.util.List<Coordinate> c) {

		// System.out.println("Size"+c.size());
		GeographyChecker.Vertex[] v = new GeographyChecker.Vertex[c.size()];
		double sum = 0.0;
		for (int i = 0; i < c.size(); i++) {
			// System.out.println(c.get(i).getLongitude() + " " + c.get(i).getLatitude());
			v[i] = new GeographyChecker.Vertex(c.get(i).getLongitude(), c.get(i).getLatitude());
			
		}
		
		for(int i = 0 ; i < v.length;i++){
			if(i==v.length-1){
				sum = sum + GeographyChecker.Vertex.computeDistance(v[i], v[0]);
			}else{
				sum = sum + GeographyChecker.Vertex.computeDistance(v[i], v[i+1]);
			}
			
		}
//		GeographyChecker ntust = new GeographyChecker(v);
		
//		System.out.println("computeArea:" + ntust.computeArea());
		System.out.println("computeDistince:" + sum);
		return sum;
	}

	public void createKMLForColorStyle(int placeMarkNum, String colorType, double overArea) {
		// Kml worldBorders = Kml.unmarshal(new File(CommonTools.appLocation() + "/resources/exampledata/GWREGION.kml"));
		de.micromata.opengis.kml.v_2_2_0.Document doc = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature().withName("TaiwanUnderWater");

		// Folder rootFolder =
		// doc.createAndAddFolder().withName("Taiwan").withStyleUrl("radioFolder");
		// set radiofolder: only one folder can be activated
		double lineWidth = 2.0;
		Style radioStyle = doc.createAndAddStyle().withId("radioFolder");
		Style redPolygonStyle = doc.createAndAddStyle().withId("redPolygon");
		Style yellowPolygonStyle = doc.createAndAddStyle().withId("yellowPolygon");
		Style greenPolygonStyle = doc.createAndAddStyle().withId("greenPolygon");
		Style defaultPolygonStyle = doc.createAndAddStyle().withId("defaultPolygon");
		radioStyle.createAndSetListStyle().withListItemType(ListItemType.RADIO_FOLDER);
		redPolygonStyle.createAndSetPolyStyle().withColor("800000FF");
		redPolygonStyle.createAndSetLineStyle().withWidth(lineWidth);
		yellowPolygonStyle.createAndSetPolyStyle().withColor("8000FFFF");
		yellowPolygonStyle.createAndSetLineStyle().withWidth(lineWidth);
		greenPolygonStyle.createAndSetPolyStyle().withColor("80008000");
		greenPolygonStyle.createAndSetLineStyle().withWidth(lineWidth);
		defaultPolygonStyle.createAndSetPolyStyle().withColor("80FF0000");
		defaultPolygonStyle.createAndSetLineStyle().withWidth(lineWidth);
		// put the original folder into the rootFolder and use for 3D shapes
		Folder rootFolder = (Folder) doc.getFeature().get(0).withName("Taiwan");
		java.util.List<Feature> pmList = rootFolder.getFeature();

//		HashMap<Integer, Double> pmArea = new HashMap<Integer, Double>();

		for (int i = 0; i < pmList.size(); i++) {
			// cTmp = getKMLCoordinates(0, i, 0);
			HashMap<String, java.util.List<Coordinate>> cTmp = getKMLCoordinates(0, i, 0);
			double geoArea = printGeoArea(cTmp.get("0" + "/" + String.valueOf(i) + "/" + "0" + "/" + "out"));
//			pmArea.put(i, geoArea);
			// cTmp.get("0" + "/" + String.valueOf(i) + "/" + "0/" + "out");
			Placemark pm = (Placemark) pmList.get(i);
			if (colorType.equals("red")&&geoArea>=overArea) {
				pm.withStyleUrl("#redPolygon");
			} else if (colorType.equals("yellow")&&geoArea>=overArea) {
				pm.withStyleUrl("#yellowPolygon");
			} else if (colorType.equals("green")&&geoArea>=overArea) {
				pm.withStyleUrl("#greenPolygon");
			} else {
				pm.withStyleUrl("#defaultPolygon");
			}
		}

		try {
//			StringWriter sw = new StringWriter();
			kml.marshal(new File(CommonTools.appLocation()+"/resources/exampledata/writebyuser.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// key foldernum+/+placeMarknum+/+polygonNum value+/+out
	// +InnerBoundarynum+/+in
	public HashMap<String, java.util.List<Coordinate>> getKMLCoordinates(String folderName, String placeMarkName, String placeMarkID, int polygonNum) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		HashMap<String, java.util.List<Coordinate>> result = new HashMap<String, java.util.List<Coordinate>>();
		for (int i = 0; i < folderList.size(); i++) {
			Folder tmpFolder = new Folder();
			tmpFolder = (Folder) folderList.get(i);
			if (tmpFolder.getName().equals(folderName)) {
				java.util.List<Feature> pmlist = tmpFolder.getFeature();

				for (int j = 0; j < pmlist.size(); j++) {
					Placemark tmpplaceMark = new Placemark();
					tmpplaceMark = (Placemark) pmlist.get(j);
					if (tmpplaceMark.getName().contains(placeMarkName) && tmpplaceMark.getId().contains(placeMarkID)) {
						// MultiGeometry multiGeometry = new MultiGeometry();
						MultiGeometry mg = ((MultiGeometry) tmpplaceMark.getGeometry());
						// for (int k = 0; k < mg.getGeometry().size(); k++)// polygon
						// {
						Polygon p = (Polygon) mg.getGeometry().get(polygonNum);
						Polygon polygon = new Polygon();
						polygon.withAltitudeMode(p.getAltitudeMode()).withExtrude(p.isExtrude());

						// java.util.List<Coordinate> coordinates = outerBoundaryIs.createAndSetLinearRing().createAndSetCoordinates();
						java.util.List<Coordinate> coordinates = new java.util.ArrayList<Coordinate>();
						for (int l = 0; l < p.getOuterBoundaryIs().getLinearRing().getCoordinates().size(); l++) {
							Coordinate c = p.getOuterBoundaryIs().getLinearRing().getCoordinates().get(l);
							coordinates.add(l, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
						}
						String resultkey = i + "/" + j + "/" + polygonNum + "/out";
						result.put(resultkey, coordinates);

						if (!p.getInnerBoundaryIs().isEmpty()) {
							for (int m = 0; m < p.getInnerBoundaryIs().size(); m++) {// inner可以很多個

								// java.util.List<Coordinate> coordinatesInner = innerBoundary.createAndSetLinearRing().createAndSetCoordinates();
								for (int n = 0; n < p.getInnerBoundaryIs().get(m).getLinearRing().getCoordinates().size(); n++) {
									Coordinate c = p.getInnerBoundaryIs().get(m).getLinearRing().getCoordinates().get(n);
									coordinates.add(n, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
								}
								String resultkeyIn = i + "/" + j + "/" + polygonNum + "/" + m + "/in";
								result.put(resultkeyIn, coordinates);
								// polygon.addToInnerBoundaryIs(innerBoundary);
							}
						}
						// }
					}
				}
			}
		}
		return result;

	}

	public HashMap<String, java.util.List<Coordinate>> getKMLCoordinates(int folderLevel, int placeMarkLevel, int polygonNum) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		HashMap<String, java.util.List<Coordinate>> result = new HashMap<String, java.util.List<Coordinate>>();
		Folder tmpFolder = new Folder();
		tmpFolder = (Folder) folderList.get(folderLevel);
		java.util.List<Feature> pmlist = tmpFolder.getFeature();
		Placemark tmpplaceMark = new Placemark();
		tmpplaceMark = (Placemark) pmlist.get(placeMarkLevel);
		// System.out.println(tmpplaceMark.getName());
		// MultiGeometry multiGeometry = new MultiGeometry();
		MultiGeometry mg = ((MultiGeometry) tmpplaceMark.getGeometry());
		// for (int k = 0; k < mg.getGeometry().size(); k++)// polygon
		Polygon p = (Polygon) mg.getGeometry().get(polygonNum);

		Polygon polygon = new Polygon();
		polygon.withAltitudeMode(p.getAltitudeMode()).withExtrude(p.isExtrude());
		// java.util.List<Coordinate> coordinates = outerBoundaryIs.createAndSetLinearRing().createAndSetCoordinates();
		java.util.List<Coordinate> coordinates = new java.util.ArrayList<Coordinate>();
		for (int l = 0; l < p.getOuterBoundaryIs().getLinearRing().getCoordinates().size(); l++) {
			Coordinate c = p.getOuterBoundaryIs().getLinearRing().getCoordinates().get(l);

			coordinates.add(l, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
		}
		String resultkey = folderLevel + "/" + placeMarkLevel + "/" + polygonNum + "/out";

		result.put(resultkey, coordinates);

		if (!p.getInnerBoundaryIs().isEmpty()) {
			for (int m = 0; m < p.getInnerBoundaryIs().size(); m++) {// inner可以很多個
				// java.util.List<Coordinate> coordinatesInner = innerBoundary.createAndSetLinearRing().createAndSetCoordinates();
				for (int n = 0; n < p.getInnerBoundaryIs().get(m).getLinearRing().getCoordinates().size(); n++) {
					Coordinate c = p.getInnerBoundaryIs().get(m).getLinearRing().getCoordinates().get(n);
					coordinates.add(n, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
				}
				String resultkeyIn = folderLevel + "/" + placeMarkLevel + "/" + polygonNum + "/" + m + "/in";
				result.put(resultkeyIn, coordinates);
			}
		}
		return result;

	}

	public HashMap<String, String> getKMLFolderAllNumName() {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		HashMap<String, String> resultHMF = new HashMap<String, String>();
		// String[] result = new String[folderList.size()];
		for (int i = 0; i < folderList.size(); i++) {
			// System.out.println(folderList.get(i).getName());
			resultHMF.put(String.valueOf(i), new String(folderList.get(i).getName()));
		}
		return resultHMF;
	}
	public HashMap<String, String> getKMLFolderOneNumName(int levelNum) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		HashMap<String, String> resultHMF = new HashMap<String, String>();
		// String[] result = new String[folderList.size()];

		// System.out.println(folderList.get(i).getName());
		resultHMF.put(String.valueOf(levelNum), new String(folderList.get(levelNum).getName()));

		return resultHMF;
	}
	public HashMap<String, String> getKMLPlaceMarkNumName(String folderName) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		HashMap<String, String> resultHMPM = new HashMap<String, String>();
		for (int i = 0; i < folderList.size(); i++) {
			Folder tmpFolder = new Folder();
			tmpFolder = (Folder) folderList.get(i);
			if (tmpFolder.getName().equals(folderName)) {
				java.util.List<Feature> pmlist = tmpFolder.getFeature();

				for (int j = 0; j < pmlist.size(); j++) {
					resultHMPM.put(String.valueOf(j), new String(pmlist.get(j).getName()));
				}
			}

		}
		return resultHMPM;
	}

	public String getKMLPlaceMarkDescription(String folderName, String placeMarkName) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		String result = null;
		for (int i = 0; i < folderList.size(); i++) {
			Folder tmpFolder = new Folder();
			tmpFolder = (Folder) folderList.get(i);
			if (tmpFolder.getName().equals(folderName)) {
				java.util.List<Feature> pmlist = tmpFolder.getFeature();
				// System.out.println(pmlist.size());
				for (int j = 0; j < pmlist.size(); j++) {
					Placemark tmpplaceMark = new Placemark();
					tmpplaceMark = (Placemark) pmlist.get(j);
					if (tmpplaceMark.getName().contains(placeMarkName)) {
						result = new String(tmpplaceMark.getDescription());
					}
				}
			}

		}
		return result;
	}

	public String getKMLPlaceMarkDescription(String folderName, String placeMarkName, String ID) {

		de.micromata.opengis.kml.v_2_2_0.Document document = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature();
		java.util.List<Feature> folderList = document.getFeature();
		String result = null;
		for (int i = 0; i < folderList.size(); i++) {
			Folder tmpFolder = new Folder();
			tmpFolder = (Folder) folderList.get(i);
			if (tmpFolder.getName().equals(folderName)) {
				java.util.List<Feature> pmlist = tmpFolder.getFeature();
				// System.out.println(pmlist.size());
				for (int j = 0; j < pmlist.size(); j++) {
					Placemark tmpplaceMark = new Placemark();
					tmpplaceMark = (Placemark) pmlist.get(j);
					if (tmpplaceMark.getName().contains(placeMarkName) && tmpplaceMark.getId().contains(ID)) {
						return result = new String(tmpplaceMark.getDescription());
					}
				}
			}

		}
		return result;
	}

}
