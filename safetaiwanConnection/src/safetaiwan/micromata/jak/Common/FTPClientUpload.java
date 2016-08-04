package safetaiwan.micromata.jak.Common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FTPClientUpload {
	String webAddress = "";
	public static void main(String[] args)  {
		uploadFileToFTP("60.250.226.78","sowlu","710522","D:\\Desktop\\usbphp\\root\\GWREGION.kml","GWREGION.kml");
	}
	public static void uploadFileToFTP(String webAddress,String user,String passwd,String filePath,String filename){
		FTPClient client = new FTPClient();
		FileInputStream fis = null;
		
//        showServerReply(client);
		try {
			 
//			ftp://60.250.226.78/../../var/www/html/
		    client.connect(webAddress);
//		   sowlu 710522
		    client.login(user, passwd);
		    client.changeWorkingDirectory("../../var/www/html/");
		    //
		    // Create an InputStream of the file to be uploaded
		    //
//		    String filename = filePath;
		    fis = new FileInputStream(filePath);

		    //
		    // Store file to server
		    //
		    client.storeFile(filename, fis);
		    client.logout();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (fis != null) {
		            fis.close();
		        }
		        client.disconnect();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
}
