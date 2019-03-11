package com.views;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import com.control.Control;

/**
 * Key board listener
 * @author ganyee
 *
 */
public class KeyBordListenerEven {

	public void keyBordListner(){
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				// TODO Auto-generated method stub
				if(((KeyEvent)event).getID()==KeyEvent.KEY_PRESSED){
					switch (((KeyEvent)event).getKeyCode()) {
					case KeyEvent.VK_RIGHT:{
						int a = Control.getFrame().getVolumControlerSlider().getValue();
						Control.getFrame().getVolumControlerSlider().setValue(a);
						Control.forword((float)(((Control.getFrame().getProgressBar().getPercentComplete() * Control.getFrame().getProgressBar().getWidth() + 10)) / Control.getFrame().getProgressBar().getWidth()));
					}
						break;
					case KeyEvent.VK_LEFT:{
						Control.jumpTo((float)((Control.getFrame().getProgressBar().getPercentComplete() * Control.getFrame().getProgressBar().getWidth() - 5) / Control.getFrame().getProgressBar().getWidth()));
					}
						break;
					case KeyEvent.VK_UP:{
						Control.getFrame().getVolumControlerSlider().setValue(Control.getFrame().getVolumControlerSlider().getValue() + 1);
//						MyMain.getFrame().getVolumLabel().setText("" + MyMain.getFrame().getVolumControlerSlider().getValue());
					}
						break;
					case KeyEvent.VK_DOWN:
						Control.getFrame().getVolumControlerSlider().setValue(Control.getFrame().getVolumControlerSlider().getValue() - 1);
						break;
					case KeyEvent.VK_SPACE:{
						if(Control.getFrame().getMediaPlayer().isPlaying()){
							Control.pause();
						}
						else{
							Control.play();
						}
					}
						break;
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}
}
