package com.control;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import com.sun.jna.NativeLibrary;
import com.views.DisplayPane;
import com.views.VideoTime;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Main function
 * @author ganyee
 *
 */
public class Control{

	public static String videoPath=null;
	private static DisplayPane frame;
	private static String filePath;
	private static VideoTime videoTime;
	static String[] optionDecode = { "--subsdec-encoding=utf-8" };

	public Control() {
		// Decide the platform
		if (RuntimeUtil.isWindows())
			filePath = "C:\\face\\plugin";
		else if (RuntimeUtil.isMac())
			filePath = "/Applications/VLC.app/Contents/MacOS/lib";
		else if (RuntimeUtil.isNix())
			filePath = "/home/linux/vlc/install/lib";

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), filePath);
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());
		
		frame = new DisplayPane();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//frame = new DisplayFram();
					//frame.setVisible(true);

					videoTime = new VideoTime();
					//String[] optionDecode = { "--subsdec-encoding=GB18030" };
					// frame.getMediaPlayer().playMedia("D:\\Downloads\\ELECTROSHOCK
					// - ESMA 2011-SD.mp4",optionDecode);
					//frame.getMediaPlayer().prepareMedia("D:\\Downloads\\ELECTROSHOCK - ESMA 2011-SD.mp4", optionDecode);
					//Publish progress of movie and get the total time and current time
					new SwingWorker<String, Integer>() {

						@Override
						protected String doInBackground() throws Exception {
							while (true) {
								//current time and total time
								long totalTime = frame.getMediaPlayer().getLength();
								long currentTime = frame.getMediaPlayer().getTime();
								videoTime.timeCalculate(totalTime, currentTime);
								frame.getCurrentLabel()
										.setText(videoTime.getMinitueCurrent() + ":" + videoTime.getSecondCurrent());
								frame.getTotalLabel()
										.setText(videoTime.getMinitueTotal() + ":" + videoTime.getSecondTotal());
								
								//Get the percent of the current movie progress
								float percent = (float) currentTime / totalTime;
								publish((int) (percent * 100));
								Thread.sleep(200);
							}
						}

						protected void process(java.util.List<Integer> chunks) {
							for (int v : chunks) {
								frame.getProgressBar().setValue(v);
							}
						};
					}.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Play the opened movie
	public static void play() {
		frame.getMediaPlayer().play();
		frame.getPlayButton().setText("||");

	}

	//Pause
	public static void pause() {
		frame.getMediaPlayer().pause();
		frame.getPlayButton().setText(">");
	}

	//Stop
	public static void stop() {
		frame.getMediaPlayer().stop();
		frame.getPlayButton().setText(">");
	}

	//Forward
	public static void forword(float to) {
		frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
	}

	//Backword
	public static void backword() {
		frame.getPlayComponent().backward(frame.getMediaPlayer());
	}

	//Set current progress time
	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
	}

	//Set volume
	public static void setVolum(int v) {
		frame.getMediaPlayer().setVolume(v);
		frame.getVolumLabel().setText("" + frame.getMediaPlayer().getVolume());
	}
public static String path=null;
	//Open view from your computer
	public static void openVedio() {
		
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			String name = file.getName();
			Control.videoPath = file.getAbsolutePath();
			System.out.println(videoPath);
			// System.out.println("name: " + name + " abpath: " +
			// file.getAbsolutePath() + " path: " + file.getPath());
			

			frame.getMediaPlayer().playMedia(file.getAbsolutePath());
			frame.getPlayButton().setText("||");
			
		}

	}

	
	//Open subtitle of movie
	public static void openSubtitle() {
		JFileChooser chooser = new JFileChooser();
		int v = chooser.showOpenDialog(null);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			frame.getMediaPlayer().setSubTitleFile(file);
		}
	}

	//Exit to program
	public static void exit() {
		frame.getMediaPlayer().release();
		System.exit(0);
	}

	

	public static DisplayPane getFrame() {
		return frame;
	}
	
	

}
