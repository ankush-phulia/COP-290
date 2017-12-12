package play;

import slidinglayout.SLAnimator;
import slidinglayout.SLConfig;
import slidinglayout.SLKeyframe;
import slidinglayout.SLPanel;
import slidinglayout.SLSide;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Main_Frame extends JFrame {
	private final SLPanel panel = new SLPanel();
	private final Single_Player spp = new Single_Player();
	private final Multi_Player mpp = new Multi_Player();
	private final Instructions ins = new Instructions();	
	private final Options opt = new Options();
	
	private final SLConfig mainCfg, sppCfg, mppCfg, insCfg, optCfg;
	public int[] keys=new int[] {KeyEvent.VK_Q,KeyEvent.VK_A,KeyEvent.VK_Z,KeyEvent.VK_U,KeyEvent.VK_J,KeyEvent.VK_M};
	public boolean sounds=false;
	
	public Main_Frame(int x,int y) {

		//default controls
		//this.keys=new int[] {KeyEvent.VK_Q,KeyEvent.VK_A,KeyEvent.VK_UP,KeyEvent.VK_DOWN,KeyEvent.VK_C,KeyEvent.VK_V,KeyEvent.VK_O,KeyEvent.VK_P};;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Swing Pong");
		getContentPane().setBackground(Color.WHITE);
		
		/*JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(50, 30, 300, 50);*/
        
		getContentPane().add(panel, BorderLayout.CENTER);

		spp.setAction(sppAction);
		mpp.setAction(mppAction);
		ins.setAction(insAction);
		opt.setAction(optAction);

		mainCfg = new SLConfig(panel)
			.gap(0,0)
			.row(1f).col(x/2).col(x/2)
			.beginGrid(0, 0)
				.row(1f).row(1f).col(1f)
				.place(0, 0, spp)
				.place(1, 0, ins)
			.endGrid()
			.beginGrid(0, 1)
				.row(1f).row(1f).col(1f)
				.place(0, 0, mpp)
				.place(1, 0, opt)
			.endGrid();

		sppCfg = new SLConfig(panel)
			.gap(10, 10)
			.row(1f).col(x)
			.place(0, 0, spp)
			.endGrid();

		mppCfg = new SLConfig(panel)
				.gap(10, 10)
				.row(1f).col(x)
				.place(0, 0, mpp)
				.endGrid();
		
		optCfg = new SLConfig(panel)
				.gap(10, 10)
				.row(1f).col(x)
				.place(0, 0, opt)
				.endGrid();
		
		insCfg = new SLConfig(panel)
				.gap(10, 10)
				.row(1f).col(x)
				.place(0, 0, ins)
				.endGrid();

		panel.setTweenManager(SLAnimator.createTweenManager());
		panel.initialize(mainCfg);
		
		javax.swing.UIManager.put("OptionPane.font", new Font("Tahoma", Font.PLAIN, 34));
	
	}

	private void disableActions() {
		spp.disableAction();
		mpp.disableAction();
		ins.disableAction();
		opt.disableAction();
	}

	private void enableActions() {
		spp.enableAction();
		mpp.enableAction();
		ins.enableAction();
		opt.enableAction();
	}

	private final Runnable sppAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(sppCfg, 0.6f)
				.setEndSide(SLSide.BOTTOM, mpp)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					spp.setAction(sppBackAction);
					spp.expanded=true;
					spp.enableAction();
				}}))
			.play();
	}};

	private final Runnable sppBackAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(mainCfg, 0.6f)
				.setStartSide(SLSide.BOTTOM, mpp)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					spp.setAction(sppAction);
					spp.expanded=false;
					enableActions();
				}}))
			.play();
	}};

	private final Runnable mppAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(mppCfg, 0.6f)
				.setEndSide(SLSide.BOTTOM, opt)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					mpp.setAction(mppBackAction);
					mpp.State=1;
					mpp.enableAction();
				}}))
			.play();
	}};

	private final Runnable mppBackAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(mainCfg, 0.6f)
				.setStartSide(SLSide.BOTTOM, opt)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					mpp.setAction(mppAction);
					mpp.State=0;
					enableActions();
				}}))
			.play();
	}};

	private final Runnable insAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(insCfg, 0.6f)
				.setEndSide(SLSide.BOTTOM, opt)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					ins.setAction(insBackAction);
					ins.expanded=true;
					ins.enableAction();
				}}))
			.play();
	}};

	private final Runnable insBackAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(mainCfg, 0.6f)
				.setStartSide(SLSide.BOTTOM, opt)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					ins.setAction(insAction);
					ins.expanded=false;
					enableActions();
				}}))
			.play();
	}};

	private final Runnable optAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(optCfg, 0.6f)
				.setEndSide(SLSide.RIGHT)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					opt.setAction(optBackAction);
					opt.expanded=true;
					opt.enableAction();
				}}))
			.play();
	}};

	private final Runnable optBackAction = new Runnable() {@Override public void run() {
		disableActions();

		panel.createTransition()
			.push(new SLKeyframe(mainCfg, 0.6f)
				.setStartSide(SLSide.RIGHT)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					opt.setAction(optAction);
					opt.expanded=false;
					enableActions();
				}}))
			.play();
	}};
	
	
}
