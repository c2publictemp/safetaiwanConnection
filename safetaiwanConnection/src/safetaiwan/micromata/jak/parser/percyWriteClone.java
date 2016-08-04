package safetaiwan.micromata.jak.parser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class percyWriteClone {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println(s);
		String path = s + "/resources/exampledata/worldBorders.kml";
		Kml kml = Kml.unmarshal(new File(path));
		Document document = (Document) kml.getFeature();
		Folder folder = (Folder) document.getFeature().get(0);
		Placemark pm = (Placemark) folder.getFeature().get(17);

		Placemark placemark = pm.clone();// Utils.clonePlacemark(pm);

		MultiGeometry multiGeometry = new MultiGeometry();
		MultiGeometry mg = ((MultiGeometry) pm.getGeometry());
		for (int i = 0; i < mg.getGeometry().size(); i++) {

			Polygon p = (Polygon) mg.getGeometry().get(i);
			Polygon polygon = new Polygon();
			polygon.withAltitudeMode(p.getAltitudeMode()).withExtrude(p.isExtrude());
			Boundary outerBoundaryIs = new Boundary();
			List<Coordinate> coordinates = outerBoundaryIs.createAndSetLinearRing().createAndSetCoordinates();

			// set the altitude of all vertices (height of the polygon)
			for (int j = 0; j < p.getOuterBoundaryIs().getLinearRing().getCoordinates().size(); j++) {
				Coordinate c = p.getOuterBoundaryIs().getLinearRing().getCoordinates().get(j);
				coordinates.add(j, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
				double longitude = c.getLongitude();
				double latitude = c.getLatitude();
				double altitude = c.getAltitude();
				System.out.println("p" + longitude + " " + latitude + " " + altitude);
				c.setLongitude(0);
				c.setLatitude(0);
				c.setAltitude(0);
			}
			if (!p.getInnerBoundaryIs().isEmpty()) {
				for (int j = 0; j < p.getInnerBoundaryIs().size(); j++) {
					Boundary innerBoundary = new Boundary();
					List<Coordinate> coordinatesInner = innerBoundary.createAndSetLinearRing().createAndSetCoordinates();
					for (int k = 0; k < p.getInnerBoundaryIs().get(j).getLinearRing().getCoordinates().size(); k++) {
						Coordinate c = p.getInnerBoundaryIs().get(j).getLinearRing().getCoordinates().get(k);
						coordinatesInner.add(k, new Coordinate(c.getLongitude(), c.getLatitude(), c.getAltitude()));
						double longitude = c.getLongitude();
						double latitude = c.getLatitude();
						double altitude = c.getAltitude();
						System.out.println("!p" + longitude + " " + latitude + " " + altitude);
						c.setLongitude(0);
						c.setLatitude(0);
						c.setAltitude(0);
					}
					polygon.addToInnerBoundaryIs(innerBoundary);
				}
			}
			polygon.setOuterBoundaryIs(outerBoundaryIs);
			multiGeometry.addToGeometry(polygon);
		}

		placemark.setGeometry(multiGeometry);
	}
	public static void parserhtml() {
		String html = "<html><head><title>First parse</title></head>" + "<body><p>Parsed HTML into a doc.</p></body></html>";
		org.jsoup.nodes.Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		Element head = doc.select("head").first();
	}

}
