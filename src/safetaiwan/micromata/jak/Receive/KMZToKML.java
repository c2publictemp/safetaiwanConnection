package safetaiwan.micromata.jak.Receive;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

import safetaiwan.micromata.jak.Common.CommonTools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class KMZToKML {
	
	private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException {     
    	String KMZ = "https://link.safetaiwan.tw/link/file/SafeTaiwan%E5%9C%B0%E7%90%83%E7%89%88.kmz";
    	String saveDir = CommonTools.appLocation()+"/resources/exampledata/safetaiwan.kmz";
    	downloadFile(KMZ,saveDir);
    	
//    	createKMZ();
        System.out.println("file out.kmz created");
    }

    public static void createKMZ()  throws IOException  {
        FileOutputStream fos = new FileOutputStream("out.kmz");
        ZipOutputStream zoS = new ZipOutputStream(fos);     
        ZipEntry ze = new ZipEntry("doc.kml");
        zoS.putNextEntry(ze);
        PrintStream ps = new PrintStream(zoS);          
        ps.println("<?xml version='1.0' encoding='UTF-8'?>");
        ps.println("<kml xmlns='http://www.opengis.net/kml/2.2'>");     
        // write out contents of KML file ...
        ps.println("<Placemark>");
        // add reference to image via inline style
        ps.println("  <Style><IconStyle>");
        ps.println("    <Icon><href>image.png</href></Icon>");
        ps.println("  </IconStyle></Style>");
        ps.println("</Placemark>");
        ps.println("</kml>");
        ps.flush();                 
        zoS.closeEntry(); // close KML entry

        // now add image file entry to KMZ
        FileInputStream is = null;
        try {                   
            is = new FileInputStream("image.png");
            ZipEntry zEnt = new ZipEntry("image.png");
            zoS.putNextEntry(zEnt);
            // copy image input to KMZ output
            // write contents to entry within compressed KMZ file
            IOUtils.copy(is, zoS);
        } finally {
            IOUtils.closeQuietly(is);
        }
        zoS.closeEntry();
        zoS.close();
    }   
    
    public static void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        
        int responseCode = httpConn.getResponseCode();
        
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
} 