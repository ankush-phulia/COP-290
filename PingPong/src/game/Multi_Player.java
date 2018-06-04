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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;
import network.ConnectionToServer;
import network.LocalServer;
import slidinglayout.SLAnimator;

@SuppressWarnings("serial")
public class Multi_Player extends JPanel {

    LocalServer gameServer;
    Thread clientsThread = new Thread();
    List<ConnectionToServer> otherConnections = new ArrayList<>();

    int State = 0;
    private static final Color FG_COLOR = new Color(0xFFFFFF);
    private static final Color BG_COLOR = new Color(0x3B5998);
    private static final Color BORDER_COLOR = new Color(0x000000);
    private static final TweenManager tweenManager = SLAnimator.createTweenManager();
    private BufferedImage bgImg;
    private Runnable action;
    private boolean actionEnabled = true;
    private boolean hover = false;
    private int borderThickness = 1;

    public Multi_Player() {

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
                        if (action != null && actionEnabled && State != 2) action.run();
                        if (State == 0) {
                            removeAll();
                            try {
                                populate_layout();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        } else if (State == 1) {
                            if (gameServer != null && gameServer.alive()) {
                                gameServer.disconnect();
                            }
                            for (ConnectionToServer cs : otherConnections) {
                                cs.disconnect();
                            }
                            removeAll();
                            show_small();
                        } else {

                        }
                    }
                });
    }

    private void show_small() {
        setLayout(new BorderLayout(0, 0));
        JLabel lblMultiPlayer = new JLabel("Multi player");
        lblMultiPlayer.setFont(new Font("Tahoma", Font.PLAIN, 34));
        lblMultiPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblMultiPlayer, BorderLayout.CENTER);
    }

    private void populate_layout() throws ParseException {
        this.setAutoscrolls(true);
        setLayout(new RXCardLayout(0, 0));

        JPanel panel_2 = new JPanel();
        add(panel_2, "Multiplayer");
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel_2.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel label = new JLabel("Multi player");
        label.setFont(new Font("Tahoma", Font.PLAIN, 28));
        panel.add(label);

        JPanel panel_1 = new JPanel();
        panel_2.add(panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
        gbl_panel_1.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panel_1.columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel_1.rowWeights =
                new double[] {
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE
                };
        panel_1.setLayout(gbl_panel_1);

        JSeparator separator_6 = new JSeparator();
        GridBagConstraints gbc_separator_6 = new GridBagConstraints();
        gbc_separator_6.insets = new Insets(0, 0, 75, 250);
        gbc_separator_6.gridx = 0;
        gbc_separator_6.gridy = 0;
        panel_1.add(separator_6, gbc_separator_6);

        JSeparator separator_28 = new JSeparator();
        GridBagConstraints gbc_separator_28 = new GridBagConstraints();
        gbc_separator_28.insets = new Insets(0, 0, 50, 200);
        gbc_separator_28.gridx = 2;
        gbc_separator_28.gridy = 1;
        panel_1.add(separator_28, gbc_separator_28);

        JButton btnStartNewGame = new JButton("Host a New Game");
        btnStartNewGame.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        try {
                            gameServer = new LocalServer(8080);
                            RXCardLayout cdl = (RXCardLayout) getLayout();
                            Multi_New mn = new Multi_New(gameServer);
                            JOptionPane.showMessageDialog(
                                    getParent().getParent().getParent().getParent().getParent(),
                                    "Your IPs are : " + LocalServer.getAllAvailableIP().toString());
                            add(mn, "new game");
                            cdl.show(Multi_Player.this, "new game");
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(
                                    getParent().getParent().getParent().getParent().getParent(),
                                    "Error Establishing Host");
                        }
                    }
                });
        btnStartNewGame.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_btnStartNewGame = new GridBagConstraints();
        gbc_btnStartNewGame.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnStartNewGame.insets = new Insets(0, 0, 5, 5);
        gbc_btnStartNewGame.gridx = 2;
        gbc_btnStartNewGame.gridy = 2;
        panel_1.add(btnStartNewGame, gbc_btnStartNewGame);

        JSeparator separator_26 = new JSeparator();
        GridBagConstraints gbc_separator_26 = new GridBagConstraints();
        gbc_separator_26.insets = new Insets(0, 0, 15, 15);
        gbc_separator_26.gridx = 2;
        gbc_separator_26.gridy = 3;
        panel_1.add(separator_26, gbc_separator_26);

        JLabel lblOr = new JLabel("OR");
        lblOr.setHorizontalAlignment(SwingConstants.CENTER);
        lblOr.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_lblOr = new GridBagConstraints();
        gbc_lblOr.insets = new Insets(0, 0, 5, 5);
        gbc_lblOr.gridx = 2;
        gbc_lblOr.gridy = 4;
        panel_1.add(lblOr, gbc_lblOr);

        JSeparator separator_24 = new JSeparator();
        GridBagConstraints gbc_separator_24 = new GridBagConstraints();
        gbc_separator_24.insets = new Insets(0, 0, 15, 15);
        gbc_separator_24.gridx = 2;
        gbc_separator_24.gridy = 5;
        panel_1.add(separator_24, gbc_separator_24);

        JLabel lblEnterIpAddress = new JLabel("Enter IP Address");
        lblEnterIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnterIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_lblEnterIpAddress = new GridBagConstraints();
        gbc_lblEnterIpAddress.insets = new Insets(0, 0, 5, 5);
        gbc_lblEnterIpAddress.gridx = 2;
        gbc_lblEnterIpAddress.gridy = 6;
        panel_1.add(lblEnterIpAddress, gbc_lblEnterIpAddress);

        JSeparator separator_20 = new JSeparator();
        GridBagConstraints gbc_separator_20 = new GridBagConstraints();
        gbc_separator_20.insets = new Insets(0, 0, 15, 15);
        gbc_separator_20.gridx = 2;
        gbc_separator_20.gridy = 7;
        panel_1.add(separator_20, gbc_separator_20);

        MaskFormatter mf;
        mf = new MaskFormatter("###.###.###.###");
        final JFormattedTextField ipAddress = new JFormattedTextField(mf);
        ipAddress.setHorizontalAlignment(SwingConstants.CENTER);
        ipAddress.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_ipAddress = new GridBagConstraints();
        gbc_ipAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_ipAddress.insets = new Insets(0, 0, 5, 5);
        gbc_ipAddress.gridx = 2;
        gbc_ipAddress.gridy = 8;
        panel_1.add(ipAddress, gbc_ipAddress);

        JButton btnJoinExistingOne = new JButton("Join Existing One");
        btnJoinExistingOne.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(ipAddress.getText());
                        String ipOfHost = ipAddress.getText();
                        try {
                            ipOfHost =
                                    InetAddress.getByName(ipAddress.getText())
                                            .toString()
                                            .substring(1);
                        } catch (UnknownHostException uhe) {

                        }
                        ConnectionToServer cs = new ConnectionToServer(ipOfHost, 8080);

                        if (cs.connectionEstablished()) {
                            try {
                                gameServer = new LocalServer(8080);
                            } catch (IOException ioe) {
                                cs.disconnect();
                                return;
                            }

                            // cs.writeToServer("MYIP:" + cs.getIPAddress());
                            clientsThread = gameServer.acceptClient();

                            String resp, type;

                            do {
                                resp = cs.readFromServer();
                                // System.out.println(resp);
                                type = parseResponse(resp);
                                if (type.equalsIgnoreCase("DISCONNECT")) return;

                            } while (!type.equalsIgnoreCase("START"));

                            cs.readingStream.isStateBoardMulti = true;
                            for (ConnectionToServer conn : otherConnections) {
                                conn.readingStream.isStateBoardMulti = true;
                            }

                            clientsThread.stop();

                            // start the game here
                            RXCardLayout cdl = (RXCardLayout) getLayout();

                            String[] tokens = resp.split(":");
                            boolean[] isPC =
                                    new boolean[] {
                                        Boolean.parseBoolean(tokens[tokens.length - 4]),
                                        Boolean.parseBoolean(tokens[tokens.length - 3]),
                                        Boolean.parseBoolean(tokens[tokens.length - 2]),
                                        Boolean.parseBoolean(tokens[tokens.length - 1])
                                    };
                            ArrayList<InetAddress> IPs = new ArrayList<InetAddress>();
                            int ipsnum = Integer.parseInt(tokens[8]);
                            String mypos = "Left";

                            ArrayList<Integer> positions = new ArrayList<Integer>();
                            positions.add(Multi_New.get_pos(tokens[1]));

                            for (int i = 0; i < ipsnum; i++) {
                                String[] pair = tokens[9 + i].split(",");
                                positions.add(Integer.parseInt(pair[1]));
                                if (cs.getIPAddress().toString().equals(pair[0])) {
                                    mypos = get_rev_pos(Integer.parseInt(pair[1]));
                                }
                                try {
                                    IPs.add(InetAddress.getByName(pair[0].substring(1)));
                                } catch (UnknownHostException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            BoardMulti game =
                                    new BoardMulti(
                                            false,
                                            gameServer,
                                            getWidth(),
                                            getHeight(),
                                            mypos,
                                            Integer.parseInt(tokens[2]),
                                            tokens[3],
                                            Integer.parseInt(tokens[4]),
                                            Integer.parseInt(tokens[5]),
                                            Boolean.parseBoolean(tokens[6]),
                                            false,
                                            getWindowAncestor().keys,
                                            isPC,
                                            Boolean.parseBoolean(tokens[7]),
                                            ipOfHost,
                                            IPs,
                                            positions,
                                            getWindowAncestor().sounds);

                            System.out.println("Starting new game...");

                            add(game, "Game");
                            cdl.show(Multi_Player.this, "Game");
                            State = 2;
                            game.requestFocusInWindow();

                        } else {
                            // do something here if connection could not be established
                            System.out.println("Connection with server could not be established!");
                        }
                    }
                });
        btnJoinExistingOne.setFont(new Font("Tahoma", Font.PLAIN, 24));
        GridBagConstraints gbc_btnJoinExistingOne = new GridBagConstraints();
        gbc_btnJoinExistingOne.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnJoinExistingOne.insets = new Insets(0, 0, 5, 5);
        gbc_btnJoinExistingOne.gridx = 2;
        gbc_btnJoinExistingOne.gridy = 10;
        panel_1.add(btnJoinExistingOne, gbc_btnJoinExistingOne);

        JSeparator separator_52 = new JSeparator();
        GridBagConstraints gbc_separator_52 = new GridBagConstraints();
        gbc_separator_52.insets = new Insets(0, 0, 0, 5);
        gbc_separator_52.gridx = 1;
        gbc_separator_52.gridy = 11;
        panel_1.add(separator_52, gbc_separator_52);
    }

    public Main_Frame getWindowAncestor() {
        Main_Frame topFrame = (Main_Frame) SwingUtilities.getWindowAncestor(this);
        return topFrame;
    }

    public String get_rev_pos(int i) {
        switch (i) {
            case 0:
                return "Left";
            case 1:
                return "Right";
            case 2:
                return "Top";
            case 3:
                return "Bottom";
        }
        return "Left";
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

    private String parseResponse(String response) {
        if (response == null) return "";

        String[] tokens = response.split(":");
        switch (tokens[0]) {
                /*case "MYIP":
                				String ipOfClient = tokens[1];
                				gameServer.writeToAllClients("CONNECTTOIP:" + ipOfClient);
                				break;
                */
            case "CONNECTTOIP":
                System.out.println(response);

                InetAddress ipOfOtherClient;
                try {
                    ipOfOtherClient = InetAddress.getByName(tokens[1].substring(1));
                } catch (UnknownHostException uhe) {
                    // do something if unable to connect

                    uhe.printStackTrace();
                    break;
                }

                List<InetAddress> myIPs = LocalServer.getAllAvailableIP();
                boolean isMe = false;
                for (InetAddress ia : myIPs) {
                    if (
                    /*!ia.isLoopbackAddress() && */ ia.equals(ipOfOtherClient)) {
                        isMe = true;
                    }
                }
                if (!isMe) {
                    ConnectionToServer cs =
                            new ConnectionToServer(ipOfOtherClient.toString().substring(1), 8080);
                    otherConnections.add(cs);
                }
                break;

            case "DISCONNECT":
                gameServer = null;
                clientsThread.stop();
                for (ConnectionToServer cs : otherConnections) {
                    cs.disconnect();
                }
                System.out.println("Game server disconnected!");
                return "DISCONNECT";

            default:
        }
        return tokens[0];
    }

    // -------------------------------------------------------------------------
    // Tween Accessor
    // -------------------------------------------------------------------------

    public static class Accessor extends SLAnimator.ComponentAccessor {
        public static final int BORDER_THICKNESS = 100;

        @Override
        public int getValues(Component target, int tweenType, float[] returnValues) {
            Multi_Player tp = (Multi_Player) target;

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
            Multi_Player tp = (Multi_Player) target;

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
