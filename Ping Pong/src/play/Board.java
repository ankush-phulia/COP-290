package play;

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
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class Board extends JPanel implements ActionListener, KeyListener{
	
	//game screens
	public String state;
	Timer timer;
	//public boolean route2=false;
    
    //AESTHETICS
	public double Xdim;
	public double Ydim;
    public Color bgcolor;
    public Color ccolor;
    public double fps;
    public int gameSpd=1;
    InputStream in;
    AudioStream audioStream;
    
    //Players
    public String position;
    public ArrayList<Paddle> players;
    
    //Balls
    public int ball_num=1;
    public ArrayList<Ball> balls ;
    public ArrayList<Ball> balls2 ;
    
    //Scores/Lives
    public int[] playerScores;
    
    //Controls
    public int[] keys;
    
    public String GameMode;
    public int cnt=0;
    
    //powerups
    public boolean power_en;
    double poweruptime = 0 ; 
    public double currenttime = 0 ; 
    public boolean powerup = false ; 
    double puXpos = 0 ; 
    double puYpos = 0  ; 
    Random pupos =  new Random() ;
    double initialdim  ;
    int player = -1;  
    int powertype = -1 ;
        
    //KeyPresses
   // public boolean paused=false;
    public boolean[] pressed=new boolean[] {false,false,false,false,false,false};
    
    public Board (int x, int y, String ownPosition,int ownLives,
			String GameMode,int ball_Num,int spd,boolean powerups,
			String Difficulty1,int Lives1,String Difficulty2,int Lives2,
			String Difficulty3,int Lives3, int[] keys,boolean sounds){    	
    	
    	//appearance
    	this.Xdim=x;
    	this.Ydim=y;
    	this.bgcolor=Color.CYAN;
    	this.ccolor=Color.blue;
    	this.fps=60;
    	this.power_en=powerups;
    	
    	//balls
    	this.ball_num=ball_Num;
    	this.gameSpd=spd;
    	this.balls=new ArrayList<Ball>();
    	this.balls2=new ArrayList<Ball>();

    	this.balls.add(new Ball(this.Xdim/2-10, this.Ydim/2-10, gen_vel()*gameSpd, gen_vel()*gameSpd,5));
    	if (ball_Num>1){
    		this.balls.add(new Ball(this.Xdim/2, this.Ydim/2, gen_vel()*gameSpd, gen_vel()*gameSpd,5));
    		if (ball_Num>2){    			
				this.balls.add(new Ball(this.Xdim/2+10, this.Ydim/2-10, gen_vel()*gameSpd, gen_vel()*gameSpd,5));
        	}
    	}
    	//paddles
    	this.position=ownPosition;
    	this.players=new ArrayList<Paddle>();

    	int k=get_pos(this.position);
    	Paddle P1=this.create_paddle(k+1,ownLives);
    	this.players.add(P1);
	
    	if (!Difficulty1.equals("")){
    		Paddle P2=this.create_paddle((k+1)%4+1,Lives1);
    		P2.difficulty=get_diff(Difficulty1);
        	this.players.add(P2);
        }
    	if (!Difficulty2.equals("")){
    		Paddle P3=this.create_paddle((k+2)%4+1,Lives2);
    		P3.difficulty=get_diff(Difficulty2);
        	this.players.add(P3);
        }
    	if (!Difficulty3.equals("")){
    		Paddle P4=this.create_paddle((k+3)%4+1,Lives3);
    		P4.difficulty=get_diff(Difficulty3);
        	this.players.add(P4);
    	}
		

    	this.playerScores=new int[] {0,0,0,0,0};
    	
    	//focused window
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.grabFocus();
        
        //controls
        this.keys=keys;
        addKeyListener(this);        
        setBackground(this.bgcolor);        
        timer = new Timer((int) (1000/this.fps), this);
        timer.start();
        
        this.GameMode=GameMode;
        
        this.addComponentListener( new ComponentAdapter() {
            public void componentShown( ComponentEvent e ) {
                Board.this.requestFocusInWindow();
            }
        });
        
        this.state="Playing";
        
        if (sounds){
         // create an audiostream from the inputstream
			try {
			    in = new FileInputStream("bgm.wav");
				audioStream = new AudioStream(in);
				// play the audio clip with the audioplayer class
			    AudioPlayer.player.start(audioStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
        }
	   
        
    }
    
    private int get_pos(String position) {
		switch (position){
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
    
    public void step(){
    	
    	if (GameMode.equals("Arcade") && cnt==7500){
    		this.state="Done";
    	}
    	
    	cnt++;
    	
    	if(this.state.equals("Playing")){
    		
    		Paddle P1=fetch(1,players);
            if (P1!=null){
            	if (this.position.equals("Left")){
            		P1.set_cYvel(15*gameSpd);
            		if (pressed[0]) {
                        if (P1.cYpos-P1.cYvel > P1.Ydim/2 ) {
                            P1.set_Ypos(P1.cYpos-P1.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P1.cYpos+P1.cYvel+P1.Ydim/2< this.Ydim) {
                        	P1.set_Ypos(P1.cYpos+P1.cYvel);
                        }
                    }
            	}
            	else if (!this.position.equals("Left")){
            		AIplayer.moveAIplayer1(P1, balls, this, 15*gameSpd);
            	}
            	else{
            		
            	}
            }
            
            Paddle P2=fetch(2,players);
            if (P2!=null){
            	if (this.position.equals("Right")){
            		P2.set_cYvel(15*gameSpd);
            		if (pressed[0]) {
                        if (P2.cYpos-P2.cYvel > P2.Ydim/2 ) {
                            P2.set_Ypos(P2.cYpos-P2.cYvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P2.cYpos+P2.cYvel+P2.Ydim/2< this.Ydim) {
                        	P2.set_Ypos(P2.cYpos+P2.cYvel);
                        }
                    }
            	}            	
            	else if (!this.position.equals("Right")){
            		AIplayer.moveAIplayer(P2, balls, this, 15*gameSpd);
            	}
            	else{
            		
            	}
            }
            
            Paddle P3=fetch(3,players);
            if (P3!=null){
            	if (this.position.equals("Top")){
            		P3.set_cXvel(15*gameSpd);
            		if (pressed[0]) {
                        if (P3.cXpos-P3.cXvel > P3.Xdim/2 ) {
                            P3.set_Xpos(P3.cXpos-P3.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P3.cXpos+P3.cXvel+P3.Xdim/2< this.Xdim) {
                        	P3.set_Xpos(P3.cXpos+P3.cXvel);
                        }
                    }
            	}            	
            	else if (!this.position.equals("Top")){
            		AIplayer.moveAIplayer4(P3, balls, this, 15*gameSpd);
            	}
            	else{
            		
            	}
            }
               
            Paddle P4=fetch(4,players);
            if (P4!=null){
            	if (this.position.equals("Bottom")){
            		P4.set_cXvel(15*gameSpd);
            		if (pressed[0]) {
                        if (P4.cXpos-P4.cXvel > P4.Xdim/2 ) {
                            P4.set_Xpos(P4.cXpos-P4.cXvel);
                        }
                    }
                    if (pressed[1]) {
                        if (P4.cXpos+P4.cXvel+P4.Xdim/2< this.Xdim) {
                        	P4.set_Xpos(P4.cXpos+P4.cXvel);
                        }
                    }
            	}            	
            	else if (!this.position.equals("Bottom")){
            		AIplayer.moveAIplayer3(P4, balls, this, 15*gameSpd);
            	}
            	else{
            		
            	}
            }
            
            for (int i=0;i<this.balls.size();i++){
            	Ball b=this.balls.get(i);
            	analyse(b);
            }

			if (power_en) {
				Random pu = new Random();

				if (!powerup) {
					if (pu.nextInt(1203) > 1199){
						poweruptime = currenttime ;
						powerup = true ;
						powertype = 2;//pupos.nextInt(3) ;
						if (powertype < 2) {
							puYpos = (pupos.nextDouble() / 2 + 0.25) * Ydim;
							puXpos = (pupos.nextDouble() / 2 + 0.25) * Xdim;
						}
						else {
							boolean safe = false;
							do {
								puYpos = (pupos.nextDouble() / 2 + 0.25) * Ydim;
								puXpos = (pupos.nextDouble() / 2 + 0.25) * Xdim;
								Rectangle2D wall = new Rectangle((int)puXpos, (int)puYpos, 100, 100);
								for (Ball b : balls) {
									Ellipse2D.Float sphere = new Ellipse2D.Float((float)(b.Xpos-b.dia/2), (float)(b.Ypos-b.dia/2), (float)b.dia, (float)b.dia);
									if (sphere.intersects(wall)) {
										safe = false;
										break;
									}
									safe = true;
								}

							} while (!safe);
						}
					}
				} else {
					if (currenttime  >  15+poweruptime){
						powerup = false  ;
					}
				}
				currenttime += 1.0/fps ;
			}
            
            repaint();
    	} 
    }
    
    public void analyse(Ball b){

    	//next positions of the ball
    	double nextLeftPos = b.Xpos - b.dia;
    	double nextRightPos = b.Xpos + b.dia;
    	double nextTopPos = b.Ypos - b.dia;
    	double nextBottomPos = b.Ypos + b.dia;
    	
    	//positions of the players
    	double playerOneRight = 0;
    	double playerOneTop = 0;
    	double playerOneBottom = 0;
    	
    	Paddle P1=this.fetch(1, players);
    	if (P1!=null){
    		playerOneRight = P1.cXpos - P1.Xdim;
    		playerOneTop = P1.cYpos-(P1.Ydim/2);
    		playerOneBottom = playerOneTop+(P1.Ydim);
    	}    	
    	
    	double playerTwoLeft = 0;
    	double playerTwoTop = 0;
    	double playerTwoBottom = 0;
    	
    	Paddle P2=this.fetch(2, players);
    	if (P2!=null){
    		playerTwoLeft = P2.cXpos + P2.Xdim;
        	playerTwoTop = P2.cYpos - (P2.Ydim/2);
        	playerTwoBottom = playerTwoTop + (P2.Ydim);
    	}
    	
    	double playerThreeRight = 0;
		double playerThreeLeft = 0;
		double playerThreeBottom = 0;
		
		Paddle P3=this.fetch(3, players);
		//System.out.println(P3==null);
		if (P3!=null){
			playerThreeRight = P3.cXpos+(P3.Xdim/2);
			playerThreeLeft = P3.cXpos-(P3.Xdim/2);
			playerThreeBottom = P3.cYpos - P3.Ydim;
		}
		
		double playerFourRight = 0;
    	double playerFourLeft = 0;
    	double playerFourTop = 0;
    	
    	Paddle P4=this.fetch(4, players);
		if (P4!=null){
			playerFourRight = P4.cXpos+(P4.Xdim/2);
			playerFourLeft = P4.cXpos-(P4.Xdim/2);
			playerFourTop = P4.cYpos + P4.Ydim;
		}
    	
		//will the ball go off the left side?
		if (P1!=null && nextLeftPos < playerOneRight) { 
            //is it going to miss the paddle?
            if (b.Ypos < playerOneTop || b.Ypos > playerOneBottom) {
            	
            	if (P1.lives>0){
            		Paddle tmp=fetch(b.origin,players);
            		if (tmp!=null && tmp.lives>0){
                    	this.playerScores[tmp.pos-1]++;
                    }
                    P1.lives--;
                    //System.out.println(P1.lives);
                    if (get_pos(this.position)+1==1 && P1.lives==0){
                    	this.state="Done2";
                    }
            	}
            }
            else{            	
            	b.origin=1;
            	//add """"spin""""
                if (get_pos(this.position)+1==1 && this.pressed[0] && !this.pressed[1]){
                	if (b.Yvel>0){
                		b.Yvel *= 0.7;
                	}
                	else{
                		b.Yvel *= 1.2;
                	}   
                }
                else if (get_pos(this.position)+1==1 && this.pressed[1] && !this.pressed[0]){
                	if (b.Yvel>0){
                		b.Yvel *= 1.2;
                	}
                	else{
                		b.Yvel *= 0.7;
                	} 
                }
            }
            b.Xvel *=-1;
            b.Xpos += b.Xvel;
            
            playCollideSound();
        }
		else if (nextLeftPos < 0){
			//b.origin=1;
            b.Xvel *= -1;
			b.Xpos += b.Xvel;
		}
        //will the ball go off the right side?
		if (P2!=null && nextRightPos > playerTwoLeft) {
            //is it going to miss the paddle?
        	if (b.Ypos < playerTwoTop || b.Ypos > playerTwoBottom) {

        		if (P2.lives>0){
        			Paddle tmp=fetch(b.origin,players);
        			if (tmp!=null && tmp.lives>0){
                    	this.playerScores[tmp.pos-1]++;
                    }
        			P2.lives--;
        			if (get_pos(this.position)+1==2 && P2.lives==0){
                    	this.state="Done2";
                    }
            	}
            }
        	else{
        		b.origin=2;
        		//add """"spin""""
                if (get_pos(this.position)+1==2 && this.pressed[0] && !this.pressed[1]){
                	if (b.Yvel>0){
                		b.Yvel *= 0.7;
                	}
                	else{
                		b.Yvel *= 1.2;
                	}   
                }
                else if (get_pos(this.position)+1==2 && this.pressed[1] && !this.pressed[0]){
                	if (b.Yvel>0){
                		b.Yvel *= 1.2;
                	}
                	else{
                		b.Yvel *= 0.7;
                	} 
                }
        	}
            b.Xvel *=-1;
			b.Xpos += b.Xvel;
			
			playCollideSound();
        }
		else if (nextRightPos > this.Xdim){
			//b.origin=2;
            b.Xvel *= -1;
			b.Xpos += b.Xvel;
		}
      //will the ball go off the top?
        if (P3!=null && nextTopPos < playerThreeBottom) {
            //is it going to miss the paddle?
        	if (b.Xpos > playerThreeRight || b.Xpos < playerThreeLeft) {
            	
        		if (P3.lives>0){   
        			
        			Paddle tmp=fetch(b.origin,players);
            		if (tmp!=null && tmp.lives>0){
                    	this.playerScores[tmp.pos-1]++;
                    }
                    P3.lives--;
                    if (get_pos(this.position)+1==3 && P3.lives==0){
                    	this.state="Done2";
                    }
            	}
            }
        	else{
        		b.origin=3;
        		//add """"spin""""
                if (get_pos(this.position)+1==3 && this.pressed[0] && !this.pressed[1]){
                	if (b.Xvel>0){
                		b.Xvel *= 0.7;
                	}
                	else{
                		b.Xvel *= 1.2;
                	}
                }
                else if (get_pos(this.position)+1==3 && this.pressed[1] && !this.pressed[0]){
                	if (b.Xvel>0){
                		b.Xvel *= 1.2;
                	}
                	else{
                		b.Xvel *= 0.7;
                	}
                }
        	}
            b.Yvel *=-1;
			b.Ypos += b.Yvel;
			
			playCollideSound();
        }
        else if (nextTopPos < 0 ) {
        	//b.origin=3;
            b.Yvel *=-1;
			b.Ypos += b.Yvel;
        }
      //will the ball go off the bottom?
        if (P4!= null && nextBottomPos > playerFourTop) {
            //is it going to miss the paddle?
        	if (b.Xpos > playerFourRight || b.Xpos < playerFourLeft) {

        		if (P4.lives>0){
        			Paddle tmp=fetch(b.origin,players);
        			if (tmp!=null && tmp.lives>0){
                    	this.playerScores[tmp.pos-1]++;
                    }
        			P4.lives--;
        			if (get_pos(this.position)+1==4 && P4.lives==0){
                    	
        				this.state="Done2";
                    }
            	}
            }
        	else{
        		b.origin=4;
        		//add """"spin""""
                if (get_pos(this.position)+1==4 && this.pressed[0] && !this.pressed[1]){
                	if (b.Xvel>0){
                		b.Xvel *= 0.7;
                	}
                	else{
                		b.Xvel *= 1.2;
                	}
                }
                else if (get_pos(this.position)+1==4 && this.pressed[1] && !this.pressed[0]){
                	if (b.Xvel>0){
                		b.Xvel *= 1.2;
                	}
                	else{
                		b.Xvel *= 0.7;
                	}
                }
        	}
            b.Yvel *=-1;
			b.Ypos += b.Yvel;
			
			playCollideSound();
        }
		//bounce off the bottom
        else if (nextBottomPos > this.Ydim) {
			//b.origin=4;
            b.Yvel *=-1;
			b.Ypos += b.Yvel;
        }
        
        if (zeros(this.players,players.size()-1)){
        	this.state="Done";
        }
        
        //checking for power up
        if (powerup){
        	if (powertype < 2 && Math.sqrt(Math.pow((b.Xpos-puXpos),2)+Math.pow((b.Ypos-puYpos),2)) < ((b.dia)/2+(40)/2)){
        		powerup = false ; 
        		//System.out.println("mila") ;
        		//System.out.println(b.origin) ;
        		switch (b.origin){
        		case 1  :
        				initialdim = players.get(0).Ydim ; 
        				if (powertype == 0){
        					players.get(0).Ydim = 2*(this.Ydim)/5 ; 
        				}else if (powertype == 1){
        					players.get(0).Ydim = (this.Ydim)/10  ; 
        				}
        				poweruptime = currenttime ; 
        				player = 1 ;
        				break ;
        		case 2 :
        			initialdim = players.get(1).Ydim ; 
    				//players.get(1).Ydim = 3*(this.Ydim)/4 ; 
    				if (powertype == 0){
    					players.get(1).Ydim = 2*(this.Ydim)/5 ; 
    				}else if (powertype == 1){
    					players.get(1).Ydim = (this.Ydim)/10  ; 
    				}
    				poweruptime = currenttime ; 
    				player = 2 ;
    				break ;
        		case 3 : 
        			initialdim = players.get(2).Xdim ; 
    				//players.get(2).Xdim = 3*(this.Xdim)/4 ; 
        			if (powertype == 0){
    					players.get(2).Xdim = 2*(this.Xdim)/5 ; 
    				}else if (powertype == 1){
    					players.get(2).Xdim = (this.Xdim)/10  ; 
    				}
        			poweruptime = currenttime ; 
    				player = 3 ;
    				break ;
        		case 4 :
        			initialdim = players.get(3).Xdim ; 
    				//players.get(3).Xdim = 3*(this.Xdim)/4 ; 
    				if (powertype == 0){
    					players.get(3).Xdim = 2*(this.Xdim)/5 ; 
    				}else if (powertype == 1){
    					players.get(3).Xdim = (this.Xdim)/10  ; 
    				}
    				poweruptime = currenttime ; 
    				player = 4 ;
    				break ;
        		}
        	}
			else if (powertype == 2) {
				Rectangle2D wall = new Rectangle((int)puXpos, (int)puYpos, 100, 100);
				Ellipse2D.Float sphere = new Ellipse2D.Float((float)(b.Xpos-b.dia/2), (float)(b.Ypos-b.dia/2), (float)b.dia, (float)b.dia);

				if (sphere.intersects(wall)) {
					if ((nextRightPos > puXpos || nextLeftPos < puXpos + 100) && (nextTopPos > puYpos && nextBottomPos < puYpos + 100)) {
						b.Xvel *= -1;
						b.Xpos += b.Xvel;
					} else if ((nextBottomPos > puYpos || nextTopPos < puYpos + 100) && (nextRightPos > puXpos && nextLeftPos < puXpos + 100)) {
						b.Yvel *= -1;
						b.Ypos += b.Yvel;
					}
				}
			}

        }
        else {
        	if (currenttime > 5+poweruptime){
        		switch (player){
            	case 1 :
            		players.get(0).Ydim = initialdim ; 
            		player = -1 ; 
            		break ;
            	case 2 :
            		players.get(1).Ydim = initialdim ; 
            		player = -1 ; 
            		break ;
            	case 3 :
            		players.get(2).Xdim = initialdim ; 
            		player = -1 ; 
            		break ;
            	case 4 :
            		players.get(3).Xdim = initialdim ; 
            		player = -1 ; 
            		break ;	
            	}
        	}
        	
        }
    	
    	//checking for collison condition between the balls 
        boolean collisionball = false ;
        Ball temp1 = b;
        Ball temp2 = null ; 
       	double res = 0  ;
        for(int j=0;j<balls.size();j++){
        	temp2 = balls.get(j) ;
        	res = Math.sqrt(Math.pow((temp1.Xpos-temp2.Xpos),2)+Math.pow((temp1.Ypos-temp2.Ypos),2));
        		
        	if (res < ((temp1.dia)/2+(temp2.dia)/2) && res >0 ){
        			
        		collisionball = true ; 
        		break ;
        	}
        }        
        
        //handling collision
        	
        if(collisionball && b.type==0) {
        	if (b.colid == 1 && temp2.colid == 1 ){
        		if (res <= b.colparam){
        			b.Xpos += b.Xvel;
                    b.Ypos += b.Yvel;
        		}
        		else {
        			b.colid = 0 ;
        			temp2.colid = 0 ;
        			handlecollision(balls,b) ;
        		}
        	}
        	
        	
        	handlecollision(balls,b) ;
        }
        else {
        	//if (b.type== -1) {b.type = 0 ;}
        	b.Xpos += b.Xvel;
            b.Ypos += b.Yvel;
        	//System.out.println(b.Xvel+","+b.Yvel);
        }
    }

    public void handlecollision(ArrayList<Ball> b,Ball b1){
    	Ball temp2 = null ;  
    	Ball temp1 = b1 ;
    	for(int j=0;j<balls.size();j++){
        		temp2 = balls.get(j) ;
        		double res = Math.sqrt(Math.pow((temp1.Xpos-temp2.Xpos),2)+Math.pow((temp1.Ypos-temp2.Ypos),2));
        		if (res < ((temp1.dia)/2+(temp2.dia)/2) && res > 0){
        			Ball.ballcollison(temp1,temp2);
        			b1.Xpos +=(temp1.Xvel);
        			b1.Ypos +=(temp1.Yvel);
        			b1.colparam = res ;
        			//b1.ballcollision = true ;
        			temp2.type = -1 ;
        			b1.colid = 1 ;
        			temp2.colid = 1 ;
        			temp2.colparam = res ;
        			 //b1.type = -1 ;
        			//temp2.Xpos += (temp2.Xvel);
        			//temp2.Ypos += (temp2.Yvel);
        			break;
        		}
    	}
    	
    }
    
  //paint the game screen
    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        switch(state){
        		
			case "Playing":
				
				g.setColor(Color.ORANGE);
				//draw "goal lines" on each side
	            g.drawLine((int)(5+Xdim/100), 0,(int) (5+Xdim/100),(int) (this.Ydim));
	            g.drawLine((int)(Xdim-(5+Xdim/100)), 0,(int) (Xdim-(5+Xdim/100)),(int) (this.Ydim));
				g.drawLine(0, (int)(5+this.Ydim/100), (int) (Xdim), (int)(5+this.Ydim/100));
				g.drawLine(0, (int)(this.Ydim-(5+this.Ydim/100)), (int) (Xdim), (int)(this.Ydim-(5+this.Ydim/100)));
				
				//draw paddles
				
				for (int i=0;i<this.players.size();i++){
					Paddle b=this.players.get(i);
					if (b.lives>0) {
						//System.out.println(b.pos);
						if (b.pos==(get_pos(position)+1)){
							g.setColor(Color.BLUE);
						}
						else{
							g.setColor(Color.RED);
						}
						g.fillRect((int)(b.cXpos-b.Xdim/2),(int)(b.cYpos-b.Ydim/2),(int) (b.Xdim), (int) (b.Ydim));
					}
				}

				//draw balls
				for (int i=0;i<this.balls.size();i++){
					Ball b=this.balls.get(i);
					g.setColor(Color.WHITE);
					g.fillOval((int)(b.Xpos-b.dia/2),(int)(b.Ypos-b.dia/2),(int) (b.dia), (int) (b.dia));
				}


				// power ups
				if (powerup){
					if (powertype < 2) {
						g.setColor(Color.DARK_GRAY);
						g.fillOval((int) puXpos - 20, (int) puYpos - 20, 40, 40);
					}
					else if (powertype == 2) {
						g.setColor(Color.PINK);
						g.fillRect((int) puXpos, (int) puYpos, 100, 100);
					}
				}

				
				//draw the scores
	            g.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
	            Paddle P1=fetch(1,players);
	            if (P1!=null){
	            	if (1==(get_pos(position)+1)){
						g.setColor(Color.BLUE);
					}
					else{
						g.setColor(Color.RED);
					}
	            	g.drawString(String.valueOf(this.playerScores[0]), (int)(this.Ydim/2-200), (int) (Xdim/2-100));
	 	            g.drawString(String.valueOf(P1.lives), (int)(this.Ydim/2-200), (int) (Xdim/2+100));
	 	        }
	            
	            Paddle P2=fetch(2,players);
	            if (P2!=null){
	            	if (2==(get_pos(position)+1)){
						g.setColor(Color.BLUE);
					}
					else{
						g.setColor(Color.RED);
					}
	            	g.drawString(String.valueOf(this.playerScores[1]), (int)(this.Ydim/2+200), (int) (Xdim/2-100));
	            	g.drawString(String.valueOf(P2.lives), (int)(this.Ydim/2+200), (int) (Xdim/2+100));
	            }
	            
	            Paddle P3=fetch(3,players);
	            if (P3!=null){
	            	if (3==(get_pos(position)+1)){
						g.setColor(Color.BLUE);
					}
					else{
						g.setColor(Color.RED);
					}
	            	g.drawString(String.valueOf(this.playerScores[2]), (int)(this.Ydim/2-100), (int) (Xdim/2-200));
			        g.drawString(String.valueOf(P3.lives), (int)(this.Ydim/2+100), (int) (Xdim/2-200));
			    }
	            
	            Paddle P4=fetch(4,players);
	            if (P4!=null){
	            	if (4==(get_pos(position)+1)){
						g.setColor(Color.BLUE);
					}
					else{
						g.setColor(Color.RED);
					}
	            	g.drawString(String.valueOf(this.playerScores[3]), (int)(this.Ydim/2-100), (int) (Xdim/2+200));
				    g.drawString(String.valueOf(P4.lives), (int)(this.Ydim/2+100), (int) (Xdim/2+200));
		        }
	            
				break;
				
			case "Done":
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
				
				int winner=0;
				int maxscore=0;
				
				if (!GameMode.equals("Arcade")){
						for (int i=0;i<players.size();i++){
						Paddle sc=this.players.get(i);
						if (sc.lives>0){
							winner=sc.pos;
						}
					}
				}
				else{
					for (int i=0;i<players.size();i++){
						Paddle sc=this.players.get(i);
						if (sc.lives>0 && this.playerScores[sc.pos-1]>=maxscore){
							winner=sc.pos;
							maxscore=this.playerScores[sc.pos-1];
						}
					}
				}
								
				g.drawString("Player "+(winner)+" won!", (int)Xdim/2-60, (int)this.Ydim/2-200);
                g.drawString("Press 'Backspace' to play again", (int)Xdim/2-160, (int)this.Ydim/2);
				break;
				
			case "Done2":
				g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
						
				g.drawString("Game Over", (int)Xdim/2-60, (int)this.Ydim/2-200);
                g.drawString("Press 'Backspace' to play again", (int)Xdim/2-160, (int)this.Ydim/2);
				break;
        }
        
    }
    
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		switch(state){
			case "Start":
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					this.players=new ArrayList<Paddle>();
					this.balls=new ArrayList<Ball>();
					this.state="Player Select";
					repaint();
	            }
				break;
				
			case "Playing":				
				if (e.getKeyCode() == keys[0]) {
	                pressed[0] = true;
	            }
	            else if (e.getKeyCode() == keys[1]) {
	                pressed[1] = true;
	            }
	            else if (e.getKeyCode() == keys[2]) {
	                pressed[2] = true;
	            }
	            else if (e.getKeyCode() == keys[3]) {
	                pressed[3] = true;
	            }
	            else if (e.getKeyCode() == keys[4]) {
	                pressed[4] = true;
	            }
	            else if (e.getKeyCode() == keys[5]) {
	                pressed[5] = true;
	            }
	            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	            	AudioPlayer.player.stop(audioStream);
	            	RXCardLayout cdl=(RXCardLayout) getParent().getLayout();
	            	cdl.show(getParent(), "MenuPanel");
	            }
	            repaint();
				break;
				
			case "Done":
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					//AudioPlayer.player.suspend();
					AudioPlayer.player.stop(audioStream);
					//AudioPlayer.player.stop(as);
	            	RXCardLayout cdl=(RXCardLayout) getParent().getLayout();
	            	cdl.show(getParent(), "MenuPanel");
	            }
				repaint();
				break;
			case "Done2":
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					//AudioPlayer.player.suspend();
					AudioPlayer.player.stop(audioStream);
	            	RXCardLayout cdl=(RXCardLayout) getParent().getLayout();
	            	cdl.show(getParent(), "MenuPanel");
	            }
				repaint();
				break;
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		if (state.equals("Playing")) {            
            if (e.getKeyCode() == keys[0]) {
                pressed[0] = false;
            }
            else if (e.getKeyCode() == keys[1]) {
                pressed[1] = false;
            }
            else if (e.getKeyCode() == keys[2]) {
                pressed[2] = false;
            }
            else if (e.getKeyCode() == keys[3]) {
                pressed[3] = false;
            }
            else if (e.getKeyCode() == keys[4]) {
                pressed[4] = false;
            }
            else if (e.getKeyCode() == keys[5]) {
                pressed[5] = false;
            }
        }
		
	}
	
    public boolean zeros(ArrayList<Paddle> arr, int z){
    	int cnt = 0;
    	for (int i=0;i<arr.size();i++){
    		if (arr.get(i).lives==0){
    			cnt++;
    		}
    	}
    	return cnt==z;
    }   
    
    public Paddle fetch(int i,ArrayList<Paddle> players){
    	for (int j=0;j<players.size();j++){
    		Paddle p=players.get(j);
    		if (p.pos==i){
    			return p;
    		}
    	}
    	return null;
    }
    
    public Paddle create_paddle (int k, int j){
    	switch (k){
    		case 1:
    			return new Paddle(Xdim/100, Ydim/5, 5+Xdim/100, this.Ydim/2,1,j);
    		case 2:
    			return new Paddle(Xdim/100, Ydim/5, Xdim-(5+Xdim/100), this.Ydim/2,2,j);
    		case 3:
    			return new Paddle(Xdim/5, Ydim/100, Xdim/2, 5+this.Ydim/100,3,j);
    		case 4:
    			return new Paddle(Xdim/5, Ydim/100, Xdim/2, this.Ydim-(5+this.Ydim/100),4,j);
    	}
    	return null;
    }
    
    public int get_diff (String s){
    	switch(s){
    		case "Easy":
    			return 0;
    		case "Medium":
    			return 1;
    		case "Difficulty":
    			return 2;
    		case "Ridiculous":
    			return 4;
    		case "WTF??!!":
    			return 8;
    	}
    	return 1;
    }
    
    public int gen_vel(){
    	Random vel=new Random();
    	int s=vel.nextInt(8)+5;
    	int s2=-(vel.nextInt(8)+5);
    	int choose=vel.nextInt(2);
    	if (choose==0){
    		return s;
    	}
    	else{
    		return s2;
    	}
    }
    
    void playCollideSound () {
		// create an audiostream from the inputstream
		try {
			in = new FileInputStream("ballBounce.wav");
			audioStream = new AudioStream(in);
			// play the audio clip with the audioplayer class
			AudioPlayer.player.start(audioStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
    
}
