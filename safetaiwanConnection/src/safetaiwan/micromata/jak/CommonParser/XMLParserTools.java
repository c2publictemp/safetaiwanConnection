package safetaiwan.micromata.jak.CommonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

public class XMLParserTools {
	public static void internetGetKmlFile(String url) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;

		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(url);
			doc.normalize();
			parseElement(doc.getDocumentElement());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void parseElement(Element element) {
		// ��o�����W�r
		String tagName = element.getNodeName();

		System.out.print("<" + tagName);

		// ��o��e�������Ҧ��ݩ�Map��H�C
		// element�������Ҧ��ݩʩҺc����NamedNodeMap��H�A�ݭn���i��P�_�C
		NamedNodeMap map = element.getAttributes();

		// �p�G�Ӥ����s�b�ݩ�
		if (null != map) {
			for (int i = 0; i < map.getLength(); i++) {
				// ��o�Ӥ������C�@���ݩ�
				Attr attr = (Attr) map.item(i);

				// ?�ʦW�M?�ʭ�
				String attrName = attr.getName();
				String attrValue = attr.getValue();

				// �`�N?�ʭȻݭn�[�W��?�A�ҥH�ݭn\??
				// System.out.print(" " + attrName + "=\"" + attrValue + "\"" +
				// " (attr) " +Node.ATTRIBUTE_NODE);
				System.out.print(" " + attrName + "=\"" + attrValue + "\"");
			}
		}

		// ????�W
		// System.out.print(">"+ "(element)"+Node.ELEMENT_NODE+" ");
		System.out.print(">");
		// �ܦ��w?���L�X�F�����W�M��?��
		// �U��?�l��?�����l����
		// ��o��e�����U���Ҧ��Ĥl�����c�����C��(�Ĥl��)
		NodeList children = element.getChildNodes();
		// System.out.println("\n children.getLength()"+children.getLength()+"
		// "+element.getNodeType());
		for (int i = 0; i < children.getLength(); i++) {

			// ?���C�@?child
			Node node = children.item(i);
			// ?��???��
			short nodeType = node.getNodeType();

			if (nodeType == Node.ELEMENT_NODE) {
				// �p�G�O����?���A????�X
				parseElement((Element) node);
			} else if (nodeType == Node.TEXT_NODE)// \n \t
			{
				// �p�G�O�奻?���A??�X??�ȡA�Τ奻?�e
				// System.out.print(node.getNodeValue() + " (TXT) "+
				// Node.TEXT_NODE+" ");
				System.out.print(node.getNodeValue());
			} else if (nodeType == Node.COMMENT_NODE) // <!-- -->
			{
				// �p�G�O�`?�A??�X�`?
				System.out.print("<!--");

				Comment comment = (Comment) node;

				// �`??�e
				String data = comment.getData();

				// System.out.print(data+" (comment) "+Node.COMMENT_NODE+" ");
				System.out.print(data);

				System.out.print("-->");
			}
		}

		// �Ҧ�?�e?�z�����Z�A?�X�A??��??
		System.out.print("</" + tagName + ">");
	}

	public static String formatXML(String xmlURL) {

		try {
			// String uri =
			// "http://api.flurry.com/eventMetrics/Event?apiAccessCode=YHJBA13CSKTMS6XHTM6M&apiKey=6XQY729FDU1CR9FKXVZP&startDate=2011-2-28&endDate=2011-3-1&eventName=Tip%20Calculated";

			URL url = new URL(xmlURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// connection.setRequestProperty("Accept", "application/xml");

			InputStream src1 = connection.getInputStream();

			// final InputSource src = new InputSource(new StringReader(xml));
			final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src1)
					.getDocumentElement();
			final Boolean keepDeclaration = Boolean.valueOf(xmlURL.startsWith("<?xml"));

			// May need this:
			// System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");

			final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
			final LSSerializer writer = impl.createLSSerializer();
			// Set this to true if the output needs to be beautified.
			writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
			// Set this to true if the declaration is needed to be outputted.
			writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

			return writer.writeToString(document);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
