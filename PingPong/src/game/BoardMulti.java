package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import network.LocalServer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class BoardMulti extends JPanel implements ActionListener, KeyListener {

    // game screens
    static String state;
    Timer timer;
    public static boolean isHost;
    public static int hostPosition;
    public static String hostIp;
    static LocalServer gameServer;
    public static long startTime;

    // AESTHETICS
    public double Xdim;
    public double Ydim;
    public Color bgcolor;
    public Color ccolor;
    public double fps;
    public int gameSpd = 1;
    InputStream in;
    AudioStream audioStream;

    // Players
    public static String position = "Left";
    public static ArrayList<Paddle> players;
    public boolean player2 = false;
    public static boolean PCplayers;
    public static boolean[] isPC;

    // networks
    public static ArrayList<InetAddress> IPs;
    public static ArrayList<Integer> positions;

    // Balls
    public int ball_num = 1;
    static ArrayList<Ball> balls;

    // Scores/Lives
    public static int[] playerScores;

    // powerups
    boolean power_en;
    double poweruptime = 0;
    public double currenttime = 0;
    public static boolean powerup = false;
    static double puXpos = 0;
    static double puYpos = 0;
    Random pupos = new Random();
    double initialdim;
    static int player = -1;
    static int powertype = -1;

    // Controls
    public int[] keys;

    // KeyPresses
    public boolean[] pressed = new boolean[] {false, false, false, false, false, false};

    public BoardMulti(
            boolean isHost,
            LocalServer gs,
            int x,
            int y,
            String ownPosition,
            int ownLives,
            String GameMode,
            int ball_Num,
            int spd,
            boolean powerups,
            boolean player2,
            int[] keys,
            boolean[] isPCList,
            boolean PCplayersEnabled,
            String ipOfHost,
            ArrayList<InetAddress> IPsList,
            ArrayList<Integer> positionsList,
            boolean sounds) {

        // network
        BoardMulti.isHost = isHost;
        BoardMulti.gameServer = gs;
        System.out.println(gs.getAllClients());

        IPs = IPsList;
        positions = positionsList;
        hostIp = ipOfHost;

        if (!isHost) {
            hostPosition = positions.get(0);
        }

        // appearance
        this.Xdim = x;
        this.Ydim = y;
        this.bgcolor = Color.CYAN;
        this.ccolor = Color.white;
        this.fps = 60;
        this.power_en = powerups;

        // balls
        balls = new ArrayList<>();
        BoardMulti.balls.add(
                new Ball(
                        this.Xdim / 2 - 10,
                        this.Ydim / 2 - 10,
                        gen_vel() * gameSpd,
                        gen_vel() * gameSpd,
                        5));
        if (ball_Num > 1) {
            BoardMulti.balls.add(
                    new Ball(
                            this.Xdim / 2,
                            this.Ydim / 2,
                            gen_vel() * gameSpd,
                            gen_vel() * gameSpd,
                            5));
            if (ball_Num > 2) {
                BoardMulti.balls.add(
                        new Ball(
                                this.Xdim / 2 + 10,
                                this.Ydim / 2 - 10,
                                gen_vel() * gameSpd,
                                gen_vel() * gameSpd,
                                5));
            }
        }

        // paddles
        BoardMulti.position = ownPosition;
        PCplayers = PCplayersEnabled;
        isPC = isPCList;

        // players
        BoardMulti.players = new ArrayList<>();
        this.player2 = player2;
        int k = get_pos(BoardMulti.position);

        Paddle P1 = this.create_paddle(k + 1, ownLives);
        BoardMulti.players.add(P1);

        Paddle P2 = this.create_paddle((k + 1) % 4 + 1, ownLives);
        BoardMulti.players.add(P2);

        Paddle P3 = this.create_paddle((k + 2) % 4 + 1, ownLives);
        BoardMulti.players.add(P3);

        Paddle P4 = this.create_paddle((k + 3) % 4 + 1, ownLives);
        BoardMulti.players.add(P4);

        for (int i = 0; i < 4; i++) {
            Paddle p = fetch(i + 1, players);
            if (isPC[i] && !PCplayers) {
                p.lives = 0;
            }
        }

        BoardMulti.playerScores = new int[] {0, 0, 0, 0, 0};

        // focused window
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.grabFocus();

        // controls
        this.keys = keys;
        addKeyListener(this);
        setBackground(this.bgcolor);
        timer = new Timer((int) (1000 / this.fps), this);
        timer.start();

        this.addComponentListener(
                new ComponentAdapter() {
                    public void componentShown(ComponentEvent e) {
                        BoardMulti.this.requestFocusInWindow();
                    }
                });

        BoardMulti.state = "Ready";

        startTime = System.currentTimeMillis();

        if (sounds) {
            // create an audiostream from the inputstream
            try {
                in = new FileInputStream("Sounds/bgm.wav");
                audioStream = new AudioStream(in);
                // game the audio clip with the audioplayer class
                AudioPlayer.player.start(audioStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int get_pos(String position) {
        switch (position) {
            case "Left":
                return 0;
            case "Right":
                return 1;
            case "Top":
                return 2;
            case "Bottom":
                return 3;
        }
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        step();
    }

    public void step() {

        if (BoardMulti.state.equals("Ready")) {
            if (isHost && System.currentTimeMillis() - startTime > 1000) {
                state = "Steady";
                InetAddress lostIP = gameServer.writeToAllClients("STATE:Steady");
                replacePlayerWithAI(lostIP);
            }
        } else if (BoardMulti.state.equals("Steady")) {
            if (isHost && System.currentTimeMillis() - startTime > 2000) {
                state = "Go";
                InetAddress lostIP = gameServer.writeToAllClients("STATE:Go");
                replacePlayerWithAI(lostIP);
            }
        } else if (BoardMulti.state.equals("Go")) {
            System.out.println("Go");
            if (isHost && System.currentTimeMillis() - startTime > 3000) {
                // System.out.println("yo");
                state = "Playing";
                InetAddress lostIP = gameServer.writeToAllClients("STATE:Playing");
                replacePlayerWithAI(lostIP);
            }
        } else if (BoardMulti.state.equals("Playing")) {
            // System.out.println("yoyo");
            Paddle P1 = fetch(1, players);
            if (P1 != null) {
                if (BoardMulti.position.equals("Left")) {
                    P1.set_cYvel(15 * gameSpd);
                    if (pressed[0]) {
                        if (P1.cYpos - P1.cYvel > P1.Ydim / 2) {
                            P1.set_Ypos(P1.cYpos - P1.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P1.cYpos + P1.cYvel + P1.Ydim / 2 < this.Ydim) {
                            P1.set_Ypos(P1.cYpos + P1.cYvel);
                        }
                    }

                    InetAddress lostIP = gameServer.writeToAllClients("Pos:1:Y:" + P1.cYpos);
                    replacePlayerWithAI(lostIP);

                } else if (player2 && BoardMulti.position.equals("Right")) {
                    P1.set_cYvel(15 * gameSpd);
                    if (pressed[3]) {
                        if (P1.cYpos - P1.cYvel > P1.Ydim / 2) {
                            P1.set_Ypos(P1.cYpos - P1.cYvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P1.cYpos + P1.cYvel + P1.Ydim / 2 < this.Ydim) {
                            P1.set_Ypos(P1.cYpos + P1.cYvel);
                        }
                    }
                } else if (PCplayers && isPC[P1.pos - 1]) {
                    AIplayer2.moveAIplayer1(P1, balls, this, 15 * gameSpd);
                } else {

                }
            }

            Paddle P2 = fetch(2, players);
            if (P2 != null) {
                if (BoardMulti.position.equals("Right")) {
                    P2.set_cYvel(15 * gameSpd);
                    if (pressed[0]) {
                        if (P2.cYpos - P2.cYvel > P2.Ydim / 2) {
                            P2.set_Ypos(P2.cYpos - P2.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P2.cYpos + P2.cYvel + P2.Ydim / 2 < this.Ydim) {
                            P2.set_Ypos(P2.cYpos + P2.cYvel);
                        }
                    }

                    InetAddress lostIP = gameServer.writeToAllClients("Pos:2:Y:" + P2.cYpos);
                    replacePlayerWithAI(lostIP);

                } else if (player2 && BoardMulti.position.equals("Left")) {
                    P2.set_cYvel(15 * gameSpd);
                    if (pressed[3]) {
                        if (P2.cYpos - P2.cYvel > P2.Ydim / 2) {
                            P2.set_Ypos(P2.cYpos - P2.cYvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P2.cYpos + P2.cYvel + P2.Ydim / 2 < this.Ydim) {
                            P2.set_Ypos(P2.cYpos + P2.cYvel);
                        }
                    }
                } else if (PCplayers && isPC[P2.pos - 1]) {
                    AIplayer2.moveAIplayer(P2, balls, this, 15 * gameSpd);
                } else {

                }
            }

            Paddle P3 = fetch(3, players);
            if (P3 != null) {
                if (BoardMulti.position.equals("Top")) {
                    P3.set_cXvel(15 * gameSpd);
                    if (pressed[0]) {
                        if (P3.cXpos - P3.cXvel > P3.Xdim / 2) {
                            P3.set_Xpos(P3.cXpos - P3.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P3.cXpos + P3.cXvel + P3.Xdim / 2 < this.Xdim) {
                            P3.set_Xpos(P3.cXpos + P3.cXvel);
                        }
                    }

                    InetAddress lostIP = gameServer.writeToAllClients("Pos:3:X:" + P3.cXpos);
                    replacePlayerWithAI(lostIP);

                } else if (player2 && BoardMulti.position.equals("Bottom")) {
                    P3.set_cXvel(15 * gameSpd);
                    if (pressed[3]) {
                        if (P3.cXpos - P3.cXvel > P3.Xdim / 2) {
                            P3.set_Xpos(P3.cXpos - P3.cXvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P3.cXpos + P3.cXvel + P3.Xdim / 2 < this.Xdim) {
                            P3.set_Xpos(P3.cXpos + P3.cXvel);
                        }
                    }
                } else if (PCplayers && isPC[P3.pos - 1]) {
                    AIplayer2.moveAIplayer4(P3, balls, this, 15 * gameSpd);
                } else {

                }
            }

            Paddle P4 = fetch(4, players);
            if (P4 != null) {
                if (BoardMulti.position.equals("Bottom")) {
                    P4.set_cXvel(15 * gameSpd);
                    if (pressed[0]) {
                        if (P4.cXpos - P4.cXvel > P4.Xdim / 2) {
                            P4.set_Xpos(P4.cXpos - P4.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P4.cXpos + P4.cXvel + P4.Xdim / 2 < this.Xdim) {
                            P4.set_Xpos(P4.cXpos + P4.cXvel);
                        }
                    }

                    InetAddress lostIP = gameServer.writeToAllClients("Pos:4:X:" + P4.cXpos);
                    replacePlayerWithAI(lostIP);

                } else if (player2 && BoardMulti.position.equals("Top")) {
                    P4.set_cXvel(15 * gameSpd);
                    if (pressed[3]) {
                        if (P4.cXpos - P4.cXvel > P4.Xdim / 2) {
                            P4.set_Xpos(P4.cXpos - P4.cXvel);
                        }
                    }
                    if (pressed[4]) {
                        if (P4.cXpos + P4.cXvel + P4.Xdim / 2 < this.Xdim) {
                            P4.set_Xpos(P4.cXpos + P4.cXvel);
                        }
                    }
                } else if (PCplayers && isPC[P4.pos - 1]) {
                    AIplayer2.moveAIplayer3(P4, balls, this, 15 * gameSpd);
                } else {

                }
            }

            if (isHost) {
                String ballInfo = "game.Ball";
                for (int i = 0; i < BoardMulti.balls.size(); i++) {
                    Ball b = BoardMulti.balls.get(i);
                    analyse(b);
                    ballInfo += ":" + b.Xpos + ":" + b.Ypos;
                }

                InetAddress lostIP = gameServer.writeToAllClients(ballInfo);
                replacePlayerWithAI(lostIP);

                if (power_en) {
                    Random pu = new Random();

                    if (!powerup) {
                        if (pu.nextInt(1201) > 1199) {
                            poweruptime = currenttime;
                            powerup = true;
                            powertype = pupos.nextInt(3);

                            if (powertype < 2) {
                                puYpos = (pupos.nextDouble() / 2 + 0.25) * Ydim;
                                puXpos = (pupos.nextDouble() / 2 + 0.25) * Xdim;
                            } else {
                                boolean safe = false;
                                do {
                                    puYpos = (pupos.nextDouble() / 2 + 0.25) * Ydim;
                                    puXpos = (pupos.nextDouble() / 2 + 0.25) * Xdim;
                                    Rectangle2D wall =
                                            new Rectangle((int) puXpos, (int) puYpos, 100, 100);
                                    for (Ball b : balls) {
                                        Ellipse2D.Float sphere =
                                                new Ellipse2D.Float(
                                                        (float) (b.Xpos - b.dia / 2),
                                                        (float) (b.Ypos - b.dia / 2),
                                                        (float) b.dia,
                                                        (float) b.dia);
                                        if (sphere.intersects(wall)) {
                                            safe = false;
                                            break;
                                        }
                                        safe = true;
                                    }

                                } while (!safe);
                            }

                            lostIP =
                                    gameServer.writeToAllClients(
                                            "PowerUpStarted:"
                                                    + powertype
                                                    + ":"
                                                    + puXpos
                                                    + ":"
                                                    + puYpos);
                            replacePlayerWithAI(lostIP);
                        }
                    } else {
                        if (currenttime > 15 + poweruptime) {
                            powerup = false;
                            lostIP = gameServer.writeToAllClients("PowerUpEnded");
                            replacePlayerWithAI(lostIP);
                        }
                    }
                    currenttime += 1.0 / fps;
                }
            }

            repaint();
        }
    }

    public void analyse(Ball b) {

        // next positions of the ball
        double nextLeftPos = b.Xpos - b.dia;
        double nextRightPos = b.Xpos + b.dia;
        double nextTopPos = b.Ypos - b.dia;
        double nextBottomPos = b.Ypos + b.dia;

        // positions of the players
        double playerOneRight = 0;
        double playerOneTop = 0;
        double playerOneBottom = 0;

        Paddle P1 = BoardMulti.fetch(1, players);
        if (P1 != null) {
            playerOneRight = P1.cXpos - P1.Xdim;
            playerOneTop = P1.cYpos - (P1.Ydim / 2);
            playerOneBottom = playerOneTop + (P1.Ydim);
        }

        double playerTwoLeft = 0;
        double playerTwoTop = 0;
        double playerTwoBottom = 0;

        Paddle P2 = BoardMulti.fetch(2, players);
        if (P2 != null) {
            playerTwoLeft = P2.cXpos + P2.Xdim;
            playerTwoTop = P2.cYpos - (P2.Ydim / 2);
            playerTwoBottom = playerTwoTop + (P2.Ydim);
        }

        double playerThreeRight = 0;
        double playerThreeLeft = 0;
        double playerThreeBottom = 0;

        Paddle P3 = BoardMulti.fetch(3, players);
        if (P3 != null) {
            playerThreeRight = P3.cXpos + (P3.Xdim / 2);
            playerThreeLeft = P3.cXpos - (P3.Xdim / 2);
            playerThreeBottom = P3.cYpos - P3.Ydim;
        }

        double playerFourRight = 0;
        double playerFourLeft = 0;
        double playerFourTop = 0;

        Paddle P4 = BoardMulti.fetch(4, players);
        if (P4 != null) {
            playerFourRight = P4.cXpos + (P4.Xdim / 2);
            playerFourLeft = P4.cXpos - (P4.Xdim / 2);
            playerFourTop = P4.cYpos + P4.Ydim;
        }

        // will the ball go off the left side?
        if (P1 != null && nextLeftPos < playerOneRight) {
            // is it going to miss the paddle?
            if (b.Ypos < playerOneTop || b.Ypos > playerOneBottom) {

                if (P1.lives > 0) {
                    Paddle tmp = fetch(b.origin, players);
                    if (tmp != null && tmp.lives > 0) {
                        BoardMulti.playerScores[tmp.pos - 1]++;
                        if (isHost) {
                            gameServer.writeToAllClients("Score:" + (tmp.pos - 1));
                        }
                    }
                    P1.lives--;
                    if (isHost) {
                        gameServer.writeToAllClients("Lives:1");
                    }
                }
            } else {
                b.origin = 1;
                // add """"spin""""
                if (get_pos(BoardMulti.position) + 1 == 1 && this.pressed[0] && !this.pressed[1]) {
                    if (b.Yvel > 0) {
                        b.Yvel *= 0.7;
                    } else {
                        b.Yvel *= 1.2;
                    }
                } else if (get_pos(BoardMulti.position) + 1 == 1
                        && this.pressed[1]
                        && !this.pressed[0]) {
                    if (b.Yvel > 0) {
                        b.Yvel *= 1.2;
                    } else {
                        b.Yvel *= 0.7;
                    }
                }
            }
            b.Xvel *= -1;
            b.Xpos += b.Xvel;

            playCollideSound();
        } else if (nextLeftPos < 0 && P1.lives == 0) {
            b.origin = 1;
            b.Xvel *= -1;
            b.Xpos += b.Xvel;
        }
        // will the ball go off the right side?
        if (P2 != null && nextRightPos > playerTwoLeft) {
            // is it going to miss the paddle?
            if (b.Ypos < playerTwoTop || b.Ypos > playerTwoBottom) {

                if (P2.lives > 0) {
                    Paddle tmp = fetch(b.origin, players);
                    if (tmp != null && tmp.lives > 0) {
                        BoardMulti.playerScores[tmp.pos - 1]++;
                        if (isHost) {
                            gameServer.writeToAllClients("Score:" + (tmp.pos - 1));
                        }
                    }
                    P2.lives--;
                    if (isHost) {
                        gameServer.writeToAllClients("Lives:2");
                    }
                }
            } else {
                b.origin = 2;
                // add """"spin""""
                if (get_pos(BoardMulti.position) + 1 == 2 && this.pressed[0] && !this.pressed[1]) {
                    if (b.Yvel > 0) {
                        b.Yvel *= 0.7;
                    } else {
                        b.Yvel *= 1.2;
                    }
                } else if (get_pos(BoardMulti.position) + 1 == 2
                        && this.pressed[1]
                        && !this.pressed[0]) {
                    if (b.Yvel > 0) {
                        b.Yvel *= 1.2;
                    } else {
                        b.Yvel *= 0.7;
                    }
                }
            }

            b.Xvel *= -1;
            b.Xpos += b.Xvel;

            playCollideSound();
        } else if (nextRightPos > this.Xdim && P2.lives == 0) {
            b.origin = 2;
            b.Xvel *= -1;
            b.Xpos += b.Xvel;
        }
        // will the ball go off the top?
        if (P3 != null && nextTopPos < playerThreeBottom) {
            // is it going to miss the paddle?
            if (b.Xpos > playerThreeRight || b.Xpos < playerThreeLeft) {

                if (P3.lives > 0) {

                    Paddle tmp = fetch(b.origin, players);
                    if (tmp != null && tmp.lives > 0) {
                        BoardMulti.playerScores[tmp.pos - 1]++;
                        if (isHost) {
                            gameServer.writeToAllClients("Score:" + (tmp.pos - 1));
                        }
                    }
                    P3.lives--;
                    if (isHost) {
                        gameServer.writeToAllClients("Lives:3");
                    }
                }
            } else {
                b.origin = 3;
                // add """"spin""""
                if (get_pos(BoardMulti.position) + 1 == 3 && this.pressed[0] && !this.pressed[1]) {
                    if (b.Xvel > 0) {
                        b.Xvel *= 0.7;
                    } else {
                        b.Xvel *= 1.2;
                    }
                } else if (get_pos(BoardMulti.position) + 1 == 3
                        && this.pressed[1]
                        && !this.pressed[0]) {
                    if (b.Xvel > 0) {
                        b.Xvel *= 1.2;
                    } else {
                        b.Xvel *= 0.7;
                    }
                }
            }

            b.Yvel *= -1;
            b.Ypos += b.Yvel;

            playCollideSound();
        } else if (nextTopPos < 0 && P3.lives == 0) {
            b.origin = 3;
            b.Yvel *= -1;
            b.Ypos += b.Yvel;
        }
        // will the ball go off the bottom?
        if (P4 != null && nextBottomPos > playerFourTop) {
            // is it going to miss the paddle?
            if (b.Xpos > playerFourRight || b.Xpos < playerFourLeft) {

                if (P4.lives > 0) {
                    Paddle tmp = fetch(b.origin, players);
                    if (tmp != null && tmp.lives > 0) {
                        BoardMulti.playerScores[tmp.pos - 1]++;
                        if (isHost) {
                            gameServer.writeToAllClients("Score:" + (tmp.pos - 1));
                        }
                    }
                    P4.lives--;
                    if (isHost) {
                        gameServer.writeToAllClients("Lives:4");
                    }
                }
            } else {
                b.origin = 4;
                if (get_pos(BoardMulti.position) + 1 == 4 && this.pressed[0] && !this.pressed[1]) {
                    if (b.Xvel > 0) {
                        b.Xvel *= 0.7;
                    } else {
                        b.Xvel *= 1.2;
                    }
                } else if (get_pos(BoardMulti.position) + 1 == 4
                        && this.pressed[1]
                        && !this.pressed[0]) {
                    if (b.Xvel > 0) {
                        b.Xvel *= 1.2;
                    } else {
                        b.Xvel *= 0.7;
                    }
                }
            }

            b.Yvel *= -1;
            b.Ypos += b.Yvel;

            playCollideSound();
        }
        // bounce off the bottom
        else if (nextBottomPos > this.Ydim && P4.lives == 0) {
            b.origin = 4;
            b.Yvel *= -1;
            b.Ypos += b.Yvel;
        }

        if (zeros(BoardMulti.players, players.size() - 1)) {
            BoardMulti.state = "Done";
            if (isHost) {
                InetAddress lostIP = gameServer.writeToAllClients("Done");
                replacePlayerWithAI(lostIP);
            }
        }

        // checking for power up
        if (powerup && isHost) {
            if (powertype < 2
                    && Math.sqrt(Math.pow((b.Xpos - puXpos), 2) + Math.pow((b.Ypos - puYpos), 2))
                            < ((b.dia) / 2 + (40) / 2)) {
                InetAddress lostIP = gameServer.writeToAllClients("PowerUpEnded");
                replacePlayerWithAI(lostIP);
                powerup = false;

                System.out.println("mila");
                System.out.println(b.origin);

                switch (b.origin) {
                    case 1:
                        initialdim = players.get(0).Ydim;
                        if (powertype == 0) {
                            players.get(0).Ydim = 2 * (this.Ydim) / 5;
                        } else if (powertype == 1) {
                            players.get(0).Ydim = (this.Ydim) / 10;
                        }
                        poweruptime = currenttime;
                        lostIP = gameServer.writeToAllClients("ChangeDim:0:Y" + players.get(0));
                        replacePlayerWithAI(lostIP);
                        player = 1;
                        break;

                    case 2:
                        initialdim = players.get(1).Ydim;
                        if (powertype == 0) {
                            players.get(1).Ydim = 2 * (this.Ydim) / 5;
                        } else if (powertype == 1) {
                            players.get(1).Ydim = (this.Ydim) / 10;
                        }
                        poweruptime = currenttime;
                        lostIP = gameServer.writeToAllClients("ChangeDim:1:Y" + players.get(1));
                        replacePlayerWithAI(lostIP);
                        player = 2;
                        break;

                    case 3:
                        initialdim = players.get(2).Xdim;
                        if (powertype == 0) {
                            players.get(2).Xdim = 2 * (this.Xdim) / 5;
                        } else if (powertype == 1) {
                            players.get(2).Xdim = (this.Xdim) / 10;
                        }
                        poweruptime = currenttime;
                        lostIP = gameServer.writeToAllClients("ChangeDim:2:X" + players.get(2));
                        replacePlayerWithAI(lostIP);
                        player = 3;
                        break;

                    case 4:
                        initialdim = players.get(3).Xdim;
                        if (powertype == 0) {
                            players.get(3).Xdim = 2 * (this.Xdim) / 5;
                        } else if (powertype == 1) {
                            players.get(3).Xdim = (this.Xdim) / 10;
                        }
                        poweruptime = currenttime;
                        lostIP = gameServer.writeToAllClients("ChangeDim:3:X" + players.get(3));
                        replacePlayerWithAI(lostIP);
                        player = 4;
                        break;
                }
            } else if (powertype == 2) {
                Rectangle2D wall = new Rectangle((int) puXpos, (int) puYpos, 100, 100);
                Ellipse2D.Float sphere =
                        new Ellipse2D.Float(
                                (float) (b.Xpos - b.dia / 2),
                                (float) (b.Ypos - b.dia / 2),
                                (float) b.dia,
                                (float) b.dia);

                if (sphere.intersects(wall)) {
                    if ((nextRightPos > puXpos || nextLeftPos < puXpos + 100)
                            && (nextTopPos > puYpos && nextBottomPos < puYpos + 100)) {
                        b.Xvel *= -1;
                        b.Xpos += b.Xvel;
                    } else if ((nextBottomPos > puYpos || nextTopPos < puYpos + 100)
                            && (nextRightPos > puXpos && nextLeftPos < puXpos + 100)) {
                        b.Yvel *= -1;
                        b.Ypos += b.Yvel;
                    }
                }
            }

        } else if (!powerup && isHost) {
            InetAddress lostIP;
            if (currenttime > 5 + poweruptime) {
                switch (player) {
                    case 1:
                        players.get(0).Ydim = initialdim;
                        lostIP =
                                gameServer.writeToAllClients(
                                        "ChangeDim:0:Y" + players.get(0) + ":-1");
                        replacePlayerWithAI(lostIP);
                        player = -1;
                        break;

                    case 2:
                        players.get(1).Ydim = initialdim;
                        lostIP =
                                gameServer.writeToAllClients(
                                        "ChangeDim:1:Y" + players.get(1) + ":-1");
                        replacePlayerWithAI(lostIP);
                        player = -1;
                        break;

                    case 3:
                        players.get(2).Xdim = initialdim;
                        lostIP =
                                gameServer.writeToAllClients(
                                        "ChangeDim:2:X" + players.get(2) + ":-1");
                        replacePlayerWithAI(lostIP);
                        player = -1;
                        break;

                    case 4:
                        players.get(3).Xdim = initialdim;
                        lostIP =
                                gameServer.writeToAllClients(
                                        "ChangeDim:3:X" + players.get(3) + ":-1");
                        replacePlayerWithAI(lostIP);
                        player = -1;
                        break;
                }
            }
        }

        // checking for collision condition between the balls
        boolean collisionball = false;
        Ball temp1 = b;
        Ball temp2 = null;
        double res = 0;
        for (int j = 0; j < balls.size(); j++) {
            temp2 = balls.get(j);
            res =
                    Math.sqrt(
                            Math.pow((temp1.Xpos - temp2.Xpos), 2)
                                    + Math.pow((temp1.Ypos - temp2.Ypos), 2));

            if (res < ((temp1.dia) / 2 + (temp2.dia) / 2) && res > 0) {

                collisionball = true;
                break;
            }
        }

        // handling collision

        if (collisionball && b.type == 0) {
            if (b.colid == 1 && temp2.colid == 1) {
                if (res <= b.colparam) {
                    b.Xpos += b.Xvel;
                    b.Ypos += b.Yvel;
                } else {
                    b.colid = 0;
                    temp2.colid = 0;
                    handlecollision(balls, b);
                }
            }

            handlecollision(balls, b);
        } else {
            // if (b.type== -1) {b.type = 0 ;}
            b.Xpos += b.Xvel;
            b.Ypos += b.Yvel;
            // System.out.println(b.Xvel+","+b.Yvel);
        }
    }

    public void handlecollision(ArrayList<Ball> b, Ball b1) {
        Ball temp2 = null;
        Ball temp1 = b1;
        for (int j = 0; j < balls.size(); j++) {
            temp2 = balls.get(j);
            double res =
                    Math.sqrt(
                            Math.pow((temp1.Xpos - temp2.Xpos), 2)
                                    + Math.pow((temp1.Ypos - temp2.Ypos), 2));
            if (res < ((temp1.dia) / 2 + (temp2.dia) / 2) && res > 0) {
                Ball.ballcollison(temp1, temp2);
                b1.Xpos += (temp1.Xvel);
                b1.Ypos += (temp1.Yvel);
                b1.colparam = res;

                temp2.type = -1;
                b1.colid = 1;
                temp2.colid = 1;
                temp2.colparam = res;

                playCollideSound();
                break;
            }
        }
    }

    // paint the game screen
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.setColor(ccolor);

        switch (state) {
            case "Ready":
            case "Steady":
            case "Go":
                g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                g.setColor(Color.MAGENTA);
                g.drawString(state, (int) (this.Ydim / 2), (int) (Xdim / 2) - 20);

            case "Playing":
                g.setColor(Color.ORANGE);
                // draw "goal lines" on each side
                g.drawLine((int) (5 + Xdim / 100), 0, (int) (5 + Xdim / 100), (int) (this.Ydim));
                g.drawLine(
                        (int) (Xdim - (5 + Xdim / 100)),
                        0,
                        (int) (Xdim - (5 + Xdim / 100)),
                        (int) (this.Ydim));
                g.drawLine(
                        0, (int) (5 + this.Ydim / 100), (int) (Xdim), (int) (5 + this.Ydim / 100));
                g.drawLine(
                        0,
                        (int) (this.Ydim - (5 + this.Ydim / 100)),
                        (int) (Xdim),
                        (int) (this.Ydim - (5 + this.Ydim / 100)));

                // draw paddles

                for (int i = 0; i < BoardMulti.players.size(); i++) {
                    Paddle b = BoardMulti.players.get(i);
                    if (b.lives > 0) {
                        if (b.pos == (get_pos(position) + 1)) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.RED);
                        }
                        g.fillRect(
                                (int) (b.cXpos - b.Xdim / 2),
                                (int) (b.cYpos - b.Ydim / 2),
                                (int) (b.Xdim),
                                (int) (b.Ydim));
                    }
                }

                // power ups
                g.setColor(Color.darkGray);
                if (powerup) {
                    if (powertype < 2) {
                        g.fillOval((int) puXpos - 20, (int) puYpos - 20, 40, 40);
                    } else if (powertype == 2) {
                        g.fillRect((int) puXpos, (int) puYpos, 100, 100);
                    }
                }

                // draw balls
                for (int i = 0; i < BoardMulti.balls.size(); i++) {
                    Ball b = BoardMulti.balls.get(i);
                    g.setColor(Color.WHITE);
                    g.fillOval(
                            (int) (b.Xpos - b.dia / 2),
                            (int) (b.Ypos - b.dia / 2),
                            (int) (b.dia),
                            (int) (b.dia));
                }

                // draw the scores
                g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
                Paddle P1 = fetch(1, players);
                if (P1 != null && P1.lives > 0) {
                    if (1 == (get_pos(position) + 1)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawString(
                            String.valueOf(BoardMulti.playerScores[0]),
                            (int) (this.Ydim / 2 - 200),
                            (int) (Xdim / 2 - 100));
                    g.drawString(
                            String.valueOf(P1.lives),
                            (int) (this.Ydim / 2 - 200),
                            (int) (Xdim / 2 + 100));
                }

                Paddle P2 = fetch(2, players);
                if (P2 != null && P2.lives > 0) {
                    if (2 == (get_pos(position) + 1)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawString(
                            String.valueOf(BoardMulti.playerScores[1]),
                            (int) (this.Ydim / 2 + 200),
                            (int) (Xdim / 2 - 100));
                    g.drawString(
                            String.valueOf(P2.lives),
                            (int) (this.Ydim / 2 + 200),
                            (int) (Xdim / 2 + 100));
                }

                Paddle P3 = fetch(3, players);
                if (P3 != null && P3.lives > 0) {
                    if (3 == (get_pos(position) + 1)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawString(
                            String.valueOf(BoardMulti.playerScores[2]),
                            (int) (this.Ydim / 2 - 100),
                            (int) (Xdim / 2 - 200));
                    g.drawString(
                            String.valueOf(P3.lives),
                            (int) (this.Ydim / 2 + 100),
                            (int) (Xdim / 2 - 200));
                }

                Paddle P4 = fetch(4, players);
                if (P4 != null && P4.lives > 0) {
                    if (4 == (get_pos(position) + 1)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawString(
                            String.valueOf(BoardMulti.playerScores[3]),
                            (int) (this.Ydim / 2 - 100),
                            (int) (Xdim / 2 + 200));
                    g.drawString(
                            String.valueOf(P4.lives),
                            (int) (this.Ydim / 2 + 100),
                            (int) (Xdim / 2 + 200));
                }

                break;

            case "Done":
                g.setColor(Color.BLACK);
                g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));

                int winner = 0;

                for (int i = 0; i < players.size(); i++) {
                    Paddle sc = BoardMulti.players.get(i);
                    if (sc.lives > 0) {
                        winner = sc.pos;
                    }
                }
                g.drawString(
                        "Player " + (winner) + " won!",
                        (int) Xdim / 2 - 60,
                        (int) this.Ydim / 2 - 200);
                g.drawString(
                        "Press 'Backspace' to game again",
                        (int) Xdim / 2 - 160,
                        (int) this.Ydim / 2);
                break;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        switch (state) {
            case "Playing":
                if (e.getKeyCode() == keys[0]) {
                    pressed[0] = true;
                } else if (e.getKeyCode() == keys[1]) {
                    pressed[1] = true;
                } else if (e.getKeyCode() == keys[2]) {
                    pressed[2] = true;
                } else if (e.getKeyCode() == keys[3]) {
                    pressed[3] = true;
                } else if (e.getKeyCode() == keys[4]) {
                    pressed[4] = true;
                } else if (e.getKeyCode() == keys[5]) {
                    pressed[5] = true;
                }
                /*if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                	game.RXCardLayout cdl=(game.RXCardLayout) getParent().getLayout();
                	cdl.show(getParent(), "MenuPanel");
                }*/
                repaint();
                break;

            case "Done":
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (isHost) {
                        AudioPlayer.player.stop(audioStream);
                        RXCardLayout cdl = (RXCardLayout) getParent().getLayout();
                        cdl.show(getParent(), "MenuPanel");
                    }
                }
                repaint();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (state.equals("Playing")) {
            if (e.getKeyCode() == keys[0]) {
                pressed[0] = false;
            } else if (e.getKeyCode() == keys[1]) {
                pressed[1] = false;
            } else if (e.getKeyCode() == keys[2]) {
                pressed[2] = false;
            } else if (e.getKeyCode() == keys[3]) {
                pressed[3] = false;
            } else if (e.getKeyCode() == keys[4]) {
                pressed[4] = false;
            } else if (e.getKeyCode() == keys[5]) {
                pressed[5] = false;
            }
        }
    }

    public boolean zeros(ArrayList<Paddle> arr, int z) {
        int cnt = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).lives == 0) {
                cnt++;
            }
        }
        return cnt == z;
    }

    public static Paddle fetch(int i, ArrayList<Paddle> players) {
        for (int j = 0; j < players.size(); j++) {
            Paddle p = players.get(j);
            if (p.pos == i) {
                return p;
            }
        }
        return null;
    }

    public Paddle create_paddle(int k, int j) {
        switch (k) {
            case 1:
                return new Paddle(Xdim / 100, Ydim / 5, 5 + Xdim / 100, this.Ydim / 2, 1, j);
            case 2:
                return new Paddle(
                        Xdim / 100, Ydim / 5, Xdim - (5 + Xdim / 100), this.Ydim / 2, 2, j);
            case 3:
                return new Paddle(Xdim / 5, Ydim / 100, Xdim / 2, 5 + this.Ydim / 100, 3, j);
            case 4:
                return new Paddle(
                        Xdim / 5, Ydim / 100, Xdim / 2, this.Ydim - (5 + this.Ydim / 100), 4, j);
        }
        return null;
    }

    public static boolean parse_packet(String response) {
        System.out.println(response);
        if (response == null) return false;

        String[] tokens = response.split(":");
        Paddle p;
        switch (tokens[0]) {
            case "Pos":
                p = fetch(Integer.parseInt(tokens[1]), players);
                if (tokens[2].equalsIgnoreCase("X")
                        && get_pos(position) + 1 != Integer.parseInt(tokens[1]))
                    p.cXpos = Double.parseDouble(tokens[3]);
                else if (tokens[2].equalsIgnoreCase("Y")
                        && get_pos(position) + 1 != Integer.parseInt(tokens[1]))
                    p.cYpos = Double.parseDouble(tokens[3]);
                break;

            case "game.Ball":
                for (int i = 0; i < balls.size(); i++) {
                    Ball b = balls.get(i);
                    b.Xpos = Double.parseDouble(tokens[2 * i + 1]);
                    b.Ypos = Double.parseDouble(tokens[2 * i + 2]);
                }
                break;

            case "Score":
                playerScores[Integer.parseInt(tokens[1])]++;
                break;

            case "Lives":
                p = fetch(Integer.parseInt(tokens[1]), players);
                // System.out.println(Integer.parseInt(tokens[1]));
                p.lives--;
                break;

            case "Done":
                state = "Done";
                break;

            case "STATE":
                state = tokens[1];
                break;

            case "PowerUpStarted":
                powerup = true;
                powertype = Integer.parseInt(tokens[1]);
                puXpos = Double.parseDouble(tokens[2]);
                puYpos = Double.parseDouble(tokens[3]);
                break;

            case "PowerUpEnded":
                powerup = false;
                break;

            case "ChangeDim":
                if (tokens[2].equals("X")) {
                    players.get(Integer.parseInt(tokens[1])).Xdim = Double.parseDouble(tokens[3]);
                } else if (tokens[2].equals("Y")) {
                    players.get(Integer.parseInt(tokens[1])).Ydim = Double.parseDouble(tokens[3]);
                }

                if (tokens.length == 4) {
                    player = Integer.parseInt(tokens[1] + 1);
                } else if (tokens.length == 5) {
                    player = -1;
                }
                break;
        }
        return true;
    }

    private static void replacePlayerWithAI(InetAddress lostIP) {
        if (lostIP != null) {
            System.out.println("lostIP: " + lostIP);

            if (lostIP.toString().substring(1).equals(hostIp)) {
                changeHost();
            } else {
                System.out.println(IPs);
                for (int index = 0; index < IPs.size(); index++) {

                    if (lostIP.equals(IPs.get(index))) {
                        System.out.println(positions.get(index));
                        isPC[positions.get(index)] = true;
                        PCplayers = true;
                    }
                }
            }
        }
    }

    private static void changeHost() {
        System.out.println("previous host position: " + hostPosition);
        isPC[hostPosition] = true;
        PCplayers = true;
        do {
            hostPosition++;
        } while (isPC[hostPosition]);

        if (hostPosition == get_pos(position)) {
            System.out.println("This pc is made HOST now...");
            isHost = true;
        }
        System.out.println("Current host position: " + hostPosition);
    }

    public int gen_vel() {
        Random vel = new Random();
        int s = vel.nextInt(8) + 5;
        int s2 = -(vel.nextInt(8) + 5);
        int choose = vel.nextInt(2);
        if (choose == 0) {
            return s;
        } else {
            return s2;
        }
    }

    void playCollideSound() {
        // create an audiostream from the inputstream
        try {
            AudioStream as;
            in = new FileInputStream("Sounds/ballBounce.wav");
            as = new AudioStream(in);
            // game the audio clip with the audioplayer class
            AudioPlayer.player.start(as);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
