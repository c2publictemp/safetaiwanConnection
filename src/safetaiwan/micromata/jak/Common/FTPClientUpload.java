package safetaiwan.micromata.jak.Common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPClientUpload {
	String webAddress = "";
	public static void main(String[] args) {
//		uploadFileToFTP("60.250.226.78", "sowlu", "710522", "D:\\Desktop\\usbphp\\root\\index.html", "index.html");
//		uploadFileToFTP("60.250.226.78", "sowlu", "710522", "D:\\Desktop\\usbphp\\root\\underwater.kml", "underwater.kml");
	}
	public static boolean uploadFileToFTP(String webAddress, String user, String passwd, String filePath, String filename) {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;
		boolean Store = false;
		// showServerReply(client);
		try {
			// client.conn
			// ftp://60.250.226.78/../../var/www/html/

			client.connect(InetAddress.getByName(webAddress), 21);

			if (!client.login(user, passwd)) {
				client.logout();
				return false;
			}
			client.sendNoOp();// used so server timeout exception will not rise
			int reply = client.getReplyCode();
			System.out.println(reply);
			if (!FTPReply.isPositiveCompletion(reply)) {
				client.disconnect();
				return false;
			}

			client.enterLocalPassiveMode(); /* just include this line here and your code will work fine */
			// sowlu 710522

			client.changeWorkingDirectory("../../var/www/html/");
			//
			// Create an InputStream of the file to be uploaded
			//
			// String filename = filePath;
			fis = new FileInputStream(filePath);

			//delete file in ftp
			// client.deleteFile(filename);
			// Store file to server
			Store = client.storeFile(filename, fis);
			fis.close();
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
		return Store;
	}
}
