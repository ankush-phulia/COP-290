package play;

public class Ball {

    public double dia;
    public double Xpos;
    public double Ypos;
    public double Xvel;
    public double Yvel;
    public double Xacc;
    public double Yacc;
    public int type;
    public int origin;
    public int destination;
    public boolean ballcollision;
    public double colparam;
    public int colid;
    public int flag, flag1, flag2, flag3 = 0;

    public Ball(double Xstart, double Ystart, double Vxstart, double Vystart, int or) {
        this.dia = 30;
        this.Xpos = Xstart;
        this.Ypos = Ystart;
        this.Xvel = Vxstart;
        this.Yvel = Vystart;
        this.Xacc = 0;
        this.Yacc = 0;
        this.type = 0;
        this.origin = or;
    }

    public void set_dia(double d) {
        this.dia = d;
    }

    public void set_Xpos(double x) {
        this.Xpos = x;
    }

    public void set_Ypos(double y) {
        this.Ypos = y;
    }

    public void set_Xvel(double vx) {
        this.Xvel = vx;
    }

    public void set_Yvel(double vy) {
        this.Yvel = vy;
    }

    public void set_Xacc(double ax) {
        this.Xacc = ax;
    }

    public void set_Yacc(double ay) {
        this.Yacc = ay;
    }

    public void set_type(int t) {
        this.type = t;
    }

    public void set_origin(int t) {
        this.origin = t;
    }

    public static void ballcollison(Ball b1, Ball b2) {
        double angle = Math.atan((b1.Ypos - b2.Ypos) / (b1.Xpos - b2.Xpos));
        double radial1 = b1.Xvel * (Math.cos(angle)) + b1.Yvel * (Math.sin(angle));
        double radial2 = (b2.Xvel * (Math.cos(angle)) + b2.Yvel * (Math.sin(angle)));
        double tang1 = b1.Xvel * (Math.sin(angle)) - b1.Yvel * (Math.cos(angle));
        double tang2 = b2.Xvel * (Math.sin(angle)) - b2.Yvel * (Math.cos(angle));
        b1.set_Xvel(radial2 * Math.cos(angle) + tang1 * Math.sin(angle));
        b1.set_Yvel(-tang1 * Math.cos(angle) + radial2 * Math.sin(angle));
        b2.set_Xvel(radial1 * Math.cos(angle) + tang2 * Math.sin(angle));
        b2.set_Yvel(-tang2 * Math.cos(angle) + radial1 * Math.sin(angle));
    }
}
