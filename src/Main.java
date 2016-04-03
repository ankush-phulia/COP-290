import aurelienribon.tweenengine.Tween;
import slidinglayout.SLAnimator;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Main {
	public static void main(String[] args) {
		Tween.registerAccessor(ThePanel.class, new ThePanel.Accessor());
		SLAnimator.start();

		TheFrame frame = new TheFrame();
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
