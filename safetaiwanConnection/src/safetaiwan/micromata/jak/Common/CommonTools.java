package safetaiwan.micromata.jak.Common;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommonTools {
	public static String appLocation() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		return s;

	}
	public static void sysPrint2D(String[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[a.length - 1].length; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void sysPrint1D(String[] a) {
		for (int i = 0; i < a.length; i++) {

			System.out.print(a[i] + " ");

		}
	}
}
