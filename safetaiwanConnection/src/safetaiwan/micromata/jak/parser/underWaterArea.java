package safetaiwan.micromata.jak.parser;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.magiclen.magiclocationchecker.GeographyChecker;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.ListItemType;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.Style;
import safetaiwan.micromata.jak.Common.CommonTools;

public class underWaterArea {
	Kml kml = null;
	public String path = "";
	public underWaterArea(String path) {
		this.path = path;
		this.kml = Kml.unmarshal(new File(path));
	}
	public static void main(String[] args) {
		// test1
		// String url =
		// "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
		// internetGetKmlFile(url);
		// test2
//		 GeographyChecker ntust = new GeographyChecker(false,
//		 GeographyChecker.Vertex.vertex("25.011390, 121.541024"),
//		 GeographyChecker.Vertex.vertex("25.012858, 121.543207"),
//		 GeographyChecker.Vertex.vertex("25.011823, 121.544151"),
//		 GeographyChecker.Vertex.vertex("25.012542, 121.545020"),
//		 GeographyChecker.Vertex.vertex("25.015576, 121.542660"),
//		 GeographyChecker.Vertex.vertex("25.013019, 121.539672"),
//		 GeographyChecker.Vertex.vertex("25.011400, 121.540943"));
//		 System.out.println(ntust.computeArea());

		
		// test3
		String s = CommonTools.appLocation();
		String path = s + "/resources/exampledata/GWREGION.kml";
		String path2 = s + "/resources/exampledata/worldBorders.kml";
		// System.out.println(getKMLFolderName(path1)[0]);
		// System.out.println(getKMLFolderName(path2)[0]);
		// sysPrint1D(getKMLPlaceMarkName(path1, "gwregion"));
		// System.out.println();
		// System.out.println(getKMLPlaceMarkDescription(path1, "gwregion","澎湖地區", "ID_00001"));
		 underWaterArea uwa = new underWaterArea(path);
		 HashMap<String, java.util.List<Coordinate>> r = uwa.getKMLCoordinates("gwregion","澎湖地區", "ID_00001", 0);
		 java.util.List<Coordinate> c = r.get("0/1/0/out");
		 printGeoArea(c);
//		 System.out.println(c.get(0).getLongitude()+" "+c.get(0).getLatitude()+" "+c.get(0).getAltitude());

	}
	
	public static void printGeoArea(java.util.List<Coordinate> c){
		GeographyChecker.Vertex[] v= new GeographyChecker.Vertex[c.size()];
		for(int i=0;i<c.size();i++){
			System.out.println(c.get(i).getLongitude()+" "+c.get(i).getLatitude());
			v[i] = new GeographyChecker.Vertex(c.get(i).getLongitude(),c.get(i).getLatitude());
		}
		GeographyChecker ntust = new GeographyChecker(v);
		System.out.println(ntust.computeArea());
	}
	


	public  void createKMLForColorStyle(int placeMarkNum, String colorType) {
		de.micromata.opengis.kml.v_2_2_0.Document doc = (de.micromata.opengis.kml.v_2_2_0.Document) kml.getFeature().withName("TaiwanUnderWater");

		// Folder rootFolder =
		// doc.createAndAddFolder().withName("Taiwan").withStyleUrl("radioFolder");
		// set radiofolder: only one folder can be activated
		Style radioStyle = doc.createAndAddStyle().withId("radioFolder");
		Style redPolygonStyle = doc.createAndAddStyle().withId("redPolygon");
		Style yellowPolygonStyle = doc.createAndAddStyle().withId("yellowPolygon");
		Style greenPolygonStyle = doc.createAndAddStyle().withId("greenPolygon");
		radioStyle.createAndSetListStyle().withListItemType(ListItemType.RADIO_FOLDER);
		redPolygonStyle.createAndSetPolyStyle().withColor("FFFF0000");
		yellowPolygonStyle.createAndSetPolyStyle().withColor("FFFFFF00");
		greenPolygonStyle.createAndSetPolyStyle().withColor("FF008000");
		// put the original folder into the rootFolder and use for 3D shapes
		Folder rootFolder = (Folder) doc.getFeature().get(0).withName("Taiwan");
		Placemark pmList = (Placemark) rootFolder.getFeature();

		doc.getFeature().remove(0);
		doc.addToFeature(rootFolder);
		// rootFolder.addToFeature(oldFolderTMP);
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
				System.out.println(pmlist.size());
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
				System.out.println(pmlist.size());
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
