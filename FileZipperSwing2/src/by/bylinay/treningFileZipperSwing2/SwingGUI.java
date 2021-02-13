package by.bylinay.treningFileZipperSwing2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import by.bylinay.treningFileZipperSwing2.FileZipper.BytesWrittenCallback;
import by.bylinay.treningFileZipperSwing2.FileZipper.GotFileSizeCallback;
import by.bylinay.treningFileZipperSwing2.FileZipper.ZipDoneCallback;



/**
 * class makes a button, button-starts the process
 * 
 * @param input      - input file you want to archive
 * @param output     - output zipped file
 * @param button     - button to start the archiving process
 * @param btnOpenDir - button for selecting a file from a directory
 * @param
 * 
 */
public class SwingGUI extends JFrame {
	static int PERSENT = 100;
	public File input = null;
	public String output = "result" + File.separator + "archive.zip";
	private static final long serialVersionUID = 1L;
	private JFormattedTextField messageField = null;
	private JButton button = null;
	private JButton btnOpenDir = null;
	private JProgressBar progressBar = null;
	private JPanel contents = null;
	private JProgressBar progressBar2 = null;
	private String textForField = "tuta budet pakazan put' k fajlu ";
	FileZipper zipper = new FileZipper();
    long fileSize = -1;

	/**
	 * 
	 * makes a button, button-starts the process
	 */

	public void makesButtonMakesAction() {
		// Создаем окно
		JFrame frame = new JFrame("VerticalLayoutTest");
		// Определяем размеры
		frame.setSize(300, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Создаем панель с менеджером
		// вертикального расположения компонентов
		contents = new JPanel();
		// Добавим кнопки и текстовое поле в панель
		progressBar = new JProgressBar();
		progressBar2 = new JProgressBar();
		button = new JButton("ARCHIVE A FILE   ");
		btnOpenDir = new JButton("OPEN DIRECTORIY   ");
		contents.add(button);
		contents.add(btnOpenDir);
		contents.add(progressBar);
		progressBar2.setStringPainted(true);
		progressBar2.setMinimum(0);
		progressBar2.setMaximum(PERSENT);
		contents.add(progressBar2);
		messageField = new JFormattedTextField();
		contents.add(messageField);
		messageField.setValue(textForField);
		// Размещаем панель в контейнере
		frame.setContentPane(contents);
		// Открываем окно
		frame.setVisible(true);

		btnOpenDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Vybor   fajla      ");
				// Определение режима - только фаил
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = chooser.showOpenDialog(frame);
				input = chooser.getSelectedFile();
				textForField = input.getPath();
				messageField.setValue(textForField);
				progressBar2.setValue(0);
				System.out.printf(textForField, result );
			}
		});
		button.addActionListener(e -> {

			zipper.zipFile(input, output, zd, gfs, bw);
			disabledButton();
			launchIndicator();
		});
	}
	ZipDoneCallback zd = new FileZipper.ZipDoneCallback() {
		@Override
		public void zipDone() {
			enabledButton();
			disableIndicator();
		}
	};
	
	GotFileSizeCallback gfs = new GotFileSizeCallback() {
		@Override
		public void gotFileSize(long size) {
			fileSize = size;
		}
	};

	BytesWrittenCallback bw = new BytesWrittenCallback() {
		@Override
		public void bytesWritten(long bytesWritten) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
			progressBar2.setValue((int) (PERSENT * bytesWritten / fileSize));	
			}
		});
		}
	};

	void disabledButton() {
		button.setEnabled(false);
	}

	void enabledButton() {
		button.setEnabled(true);
	}

	void launchIndicator() {
		progressBar.setIndeterminate(true);
	}

	void disableIndicator() {
		progressBar.setIndeterminate(false);
		
	}
}
