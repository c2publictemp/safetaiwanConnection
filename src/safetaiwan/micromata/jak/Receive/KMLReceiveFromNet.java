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
		String s = CommonTools.appLocation();
		String URL = "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
		String outFilePath = s + "/resources/exampledata/underwater.kml";
		KMLReceive(URL, outFilePath);
	}
	public static void KMLReceive(String url, String outFilePath) {

		// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		if (url.equals("") || url == null) {
			url = "http://gic.wra.gov.tw/gic/API/Google/DownLoad.aspx?fname=GWREGION";
		}

		// DocumentBuilder db;
		// Document doc;
		String a = null;
		try {
			// db = dbf.newDocumentBuilder();
			// doc = db.parse(url);
			// doc.normalize();
			a = XMLParserTools.formatXML(url);
			// System.out.println(a);
			List<String> lines = Arrays.asList(a);
			if (outFilePath.equals("") || outFilePath == null) {
				String s = CommonTools.appLocation();
				outFilePath = s + "/resources/exampledata/underwater.kml";
			}
			Path file = Paths.get(outFilePath);
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String url = "http://opendata.cwb.gov.tw/opendataapi?dataid=F-C0032-001&authorizationkey=CWB-A56720DC-7A3B-4DF6-970A-326C62193C78";

		// KML kml =
		// System.out.println(a);
	}

}
