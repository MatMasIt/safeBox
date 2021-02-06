import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;


public class BoxFileZipUtils {
	private ZipFile zis;
	public BoxFileZipUtils(File f) throws IOException {
		zis = new ZipFile(f);
	}
	public JSONObject getDetails() throws IOException {
		InputStream in = zis.getInputStream(this.zis.getEntry("meta.json"));
		Scanner s = new Scanner(in).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		JSONObject obj = new JSONObject(result);
		s.close();
		return obj;
	}
	public boolean extract(String fn,File outfile) throws IOException {
		return this.extractContentFile(this.zis.getEntry(fn),outfile);
	}
	public boolean extractContentFile(ZipEntry e,File outfile) throws IOException {
		if(e==null) return false;
		if(e.isDirectory()) return false;
		InputStream s =zis.getInputStream(e);
		FileOutputStream fos = new FileOutputStream(outfile);
		byte[] buffer = new byte[1024];
        int len;
        while ((len = s.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
		return true;
	}

}
