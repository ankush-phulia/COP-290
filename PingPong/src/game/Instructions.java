package game;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import slidinglayout.SLAnimator;

public class Instructions extends JPanel {

    boolean expanded = false;
    private static final Color FG_COLOR = new Color(0xFFFFFF);
    private static final Color BG_COLOR = new Color(0x3B5998);
    private static final Color BORDER_COLOR = new Color(0x000000);
    private static final TweenManager tweenManager = SLAnimator.createTweenManager();
    private BufferedImage bgImg;
    private Runnable action;
    private boolean actionEnabled = true;
    private boolean hover = false;
    private int borderThickness = 1;

    public Instructions() {

        show_small();
        // populate_layout();

        addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        if (actionEnabled) showBorder();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        hideBorder();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (action != null && actionEnabled) action.run();
                        if (!expanded) {
                            removeAll();
                            populate_layout();
                        } else {
                            removeAll();
                            show_small();
                        }
                    }
                });
    }

    private void show_small() {
        setLayout(new BorderLayout(0, 0));
        JLabel lblHowToPlay = new JLabel("How To Play");
        lblHowToPlay.setFont(new Font("Tahoma", Font.PLAIN, 34));
        lblHowToPlay.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblHowToPlay, BorderLayout.CENTER);
    }

    private void populate_layout() {
        this.setAutoscrolls(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblHowToPlay_1 = new JLabel("How To Play");
        lblHowToPlay_1.setFont(new Font("Tahoma", Font.PLAIN, 28));
        panel.add(lblHowToPlay_1);

        JPanel panel_1 = new JPanel();
        add(panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] {0, 630, 0, 0, 0, 0, 0};
        gbl_panel_1.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
        gbl_panel_1.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel_1.setLayout(gbl_panel_1);

        JSeparator separator_1 = new JSeparator();
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.insets = new Insets(0, 0, 30, 0);
        gbc_separator_1.gridx = 5;
        gbc_separator_1.gridy = 0;
        panel_1.add(separator_1, gbc_separator_1);

        JLabel lblRandomBs = new JLabel();
        lblRandomBs.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_lblRandomBs = new GridBagConstraints();
        gbc_lblRandomBs.gridwidth = 5;
        gbc_lblRandomBs.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblRandomBs.insets = new Insets(0, 0, 5, 5);
        gbc_lblRandomBs.gridx = 1;
        gbc_lblRandomBs.gridy = 1;
        panel_1.add(lblRandomBs, gbc_lblRandomBs);
        lblRandomBs.setText(
                "<html><body>In this game the player has to protect the boundary by hitting the ball with the paddle.<br>He can add the velocity in the tangential direction in which the paddle was moving while hitting the ball.<br> Powerups appear randomly, the rectangle ones can either increase or decrease the length of the paddle and also a wall appear randomly.<br> Powerups are given to the player whose paddle was collided with the ball.<br> Beat the game by surviving or by making other players miss the ball.</body></html>");

        JSeparator separator = new JSeparator();
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.insets = new Insets(0, 0, 0, 50);
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 5;
        panel_1.add(separator, gbc_separator);
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void enableAction() {
        actionEnabled = true;
        if (hover) showBorder();
    }

    public void disableAction() {
        actionEnabled = false;
    }

    private void showBorder() {
        tweenManager.killTarget(borderThickness);
        Tween.to(this, Accessor.BORDER_THICKNESS, 0.4f).target(10).start(tweenManager);
    }

    private void hideBorder() {
        tweenManager.killTarget(borderThickness);
        Tween.to(this, Accessor.BORDER_THICKNESS, 0.4f).target(2).start(tweenManager);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D gg = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        if (bgImg != null) {
            int imgW = bgImg.getWidth();
            int imgH = bgImg.getHeight();

            if ((float) w / h < (float) imgW / imgH) {
                int tw = h * imgW / imgH;
                int th = h;
                gg.drawImage(bgImg, (w - tw) / 2, 0, tw, th, null);
            } else {
                int tw = w;
                int th = w * imgH / imgW;
                gg.drawImage(bgImg, 0, (h - th) / 2, tw, th, null);
            }
        }

        int t = borderThickness;
        gg.setColor(BORDER_COLOR);
        gg.fillRect(0, 0, t, h - 1);
        gg.fillRect(0, 0, w - 1, t);
        gg.fillRect(0, h - 1 - t, w - 1, t);
        gg.fillRect(w - 1 - t, 0, t, h - 1);
    }

    // -------------------------------------------------------------------------
    // Tween Accessor
    // -------------------------------------------------------------------------

    public static class Accessor extends SLAnimator.ComponentAccessor {
        public static final int BORDER_THICKNESS = 100;

        @Override
        public int getValues(Component target, int tweenType, float[] returnValues) {
            Instructions tp = (Instructions) target;

            int ret = super.getValues(target, tweenType, returnValues);
            if (ret >= 0) return ret;

            switch (tweenType) {
                case BORDER_THICKNESS:
                    returnValues[0] = tp.borderThickness;
                    return 1;
                default:
                    return -1;
            }
        }

        @Override
        public void setValues(Component target, int tweenType, float[] newValues) {
            Instructions tp = (Instructions) target;

            super.setValues(target, tweenType, newValues);

            switch (tweenType) {
                case BORDER_THICKNESS:
                    tp.borderThickness = Math.round(newValues[0]);
                    tp.repaint();
                    break;
            }
        }
    }
}
