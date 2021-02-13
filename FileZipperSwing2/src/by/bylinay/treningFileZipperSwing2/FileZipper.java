package by.bylinay.treningFileZipperSwing2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * class archives the file to the directory
 * 
 * @author Администратор
 *
 */
public class FileZipper implements Runnable {
	// void Thread(Runnable threadObj, String threadName) {
	// }
	int bytesWritten = 0;

	/**
	 * starts archiving on a separate thread
	 * 
	 * @param input
	 * @param output
	 */

	public void zipFile(File input, String output, ZipDoneCallback zd, GotFileSizeCallback gfs,
			BytesWrittenCallback bw) {
		gfs.gotFileSize(input.length());

		new Thread(new Runnable() {

			@Override
			public void run() {
				doZip(input, output, bw);
				zd.zipDone();
			}

		}).start();

	}

	// static boolean doZip(String input, String output) {
	// return false;

	// }

	/**
	 * directly makes a archiving of the file
	 * 
	 * @param input  - input file you want to archive
	 * @param output - output zipped file
	 * @return
	 */

	void doZip(File input, String output, BytesWrittenCallback bwCallback) {
		// *входной

		FileInputStream in = null;
		FileOutputStream poOut = null;
		// выходной
		ZipOutputStream out = null;

		try {
			in = new FileInputStream(input);
			poOut = new FileOutputStream(output);
			out = new ZipOutputStream(poOut);
			// (new FileOutputStream(output));
			// файл внутри zip-файла
			out.putNextEntry(new ZipEntry("zippedjava.docx"));

			// размер
			byte[] b = new byte[1024];
			

			int count;

			while ((count = in.read(b)) > 0) {

				bytesWritten += count;

						bwCallback.bytesWritten(bytesWritten);
				

				
				out.write(b, 0, count);
			}

			out.close();
			in.close();
			
			System.out.println("--done");
			
			
		
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public interface ZipDoneCallback {
		void zipDone();
	}

	public interface GotFileSizeCallback {
		public void gotFileSize(long fileSize);
	}

	public interface BytesWrittenCallback {
		public void bytesWritten(long bytesWritten);
	}


	public void run() {
		

	}

}