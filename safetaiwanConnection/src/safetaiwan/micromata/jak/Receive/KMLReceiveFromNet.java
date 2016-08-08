package safetaiwan.micromata.jak.Receive;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import safetaiwan.micromata.jak.Common.CommonTools;
import safetaiwan.micromata.jak.Common.XMLParserTools;

public class KMLReceiveFromNet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KMLReceive();
	}
	public static void KMLReceive() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String url = "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
		DocumentBuilder db;
		Document doc;
		String a = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(url);
			doc.normalize();
			a = XMLParserTools.formatXML(url);
			System.out.println(a);
			List<String> lines = Arrays.asList(a);
			String s = CommonTools.appLocation();
			String path = s + "/resources/exampledata/the-file-name.kml";
			Path file = Paths.get(path);
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String url = "http://opendata.cwb.gov.tw/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-A56720DC-7A3B-4DF6-970A-326C62193C78";

		// KML kml =
		System.out.println(a);
	}
	
}
