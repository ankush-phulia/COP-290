package play;

public class Paddle {
	
	public double dia;
	public double Xdim;
	public double Ydim;
	public double cXpos;
    public double cYpos;    
    public double cXvel;
    public double cYvel;
    public double type;
    public int pos;
    public int lives;
    public int flag = 0 ; 
    public int difficulty= 1 ;
    
//    public play.Paddle(double typ,double Xstart,double Ystart){
//    	if (typ==0){
//    		this.Xdim=10;
//    		this.Ydim=200;
//    	}
//    	else{
//    		this.dia=200;
//    	}
//    	this.type=typ;
//    	this.cXpos=Xstart;
//    	this.cYpos=Ystart;
//    }
    
    public Paddle(double d,double Xstart,double Ystart,int pos){
    	this.type=1;
    	this.dia=d;
    	this.cXpos=Xstart;
    	this.cYpos=Ystart;
    	this.cXvel=0;
    	this.cYvel=0;
    	this.pos=pos;
    }
    
    public Paddle(double x,double y,double Xstart,double Ystart, int pos, int lives){
    	this.type=0;
    	this.Xdim=x;
    	this.Ydim=y;
    	this.cXpos=Xstart;
    	this.cYpos=Ystart;
    	this.cXvel=0;
    	this.cYvel=0;
    	this.pos=pos;
    	this.lives=lives;
    }
    
    public void set_type(int t){
    	this.type=t;
    }
    
    public void set_dia(double d){
    	this.dia=d;
    }
    
    public void set_xdim(double d){
    	this.Xdim=d;
    }
    
    public void set_ydim(double d){
    	this.Ydim=d;
    }
    
    public void set_Xpos(double x){
    	this.cXpos=x;
    }

    public void set_Ypos(double y){
    	this.cYpos=y;
    }
    
    public void set_cXvel(double vx){
    	this.cXvel=vx;
    }
    
    public void set_cYvel(double vy){
    	this.cYvel=vy;
    }
    
    public void set_lives(int l){
    	this.lives=l;
    }

}
