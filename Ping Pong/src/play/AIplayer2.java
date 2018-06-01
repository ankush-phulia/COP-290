package play;

import java.util.ArrayList;
import java.util.Random;

public class AIplayer2 {
    static int counter = 0;
    static double error1 = 5;

    public static void moveAIplayer(
            Paddle p, ArrayList<Ball> balls, BoardMulti b, double initialvelocity) {

        Ball balltobefollowed = null;
        Ball temp;
        double farthestballfromleft = 0;

        for (int i = 0; i < balls.size(); i++) {
            temp = balls.get(i);
            if ((temp.Xpos > farthestballfromleft)
                    && (temp.Xvel > 0)
                    && (temp.Xpos > 3 * b.Xdim / 4 / (1 + p.difficulty))) {
                balltobefollowed = balls.get(i);
                farthestballfromleft = temp.Xpos;
            }
        }

        if (balltobefollowed == null) {
            // balltobefollowed = balls.get(0) ;
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag = 0;
            }
        } else {
            int temp1 = balltobefollowed.flag;
            //			for (int i = 0 ; i < balls.size() ; i++){
            //				balls.get(i).flag = 0 ;
            //			}
            balltobefollowed.flag = temp1;
            // System.out.println(balltobefollowed.flag) ;

            double predictedordinatetemp =
                    (balltobefollowed.Yvel / balltobefollowed.Xvel) * 749
                            + (balltobefollowed.Ypos
                                    - (balltobefollowed.Yvel / balltobefollowed.Xvel)
                                            * balltobefollowed.Xpos);
            // double error = 5  ;
            double predictedordinate = 5;
            p.set_cYvel(initialvelocity);
            Random dice = new Random();
            double epsilon = initialvelocity / 2 + 1;
            double errortemp = (dice.nextInt(201) - 100) / ((double) 100);
            if (balltobefollowed.flag == 0) {
                error1 = errortemp;
                // System.out.println(error1);
                // System.out.println(counter++);
                balltobefollowed.flag = 1;
                if (balltobefollowed.Xpos > 3 * b.Xdim / 4 + 100) {
                    // System.out.println("eroraya");
                }
            }

            predictedordinate = predictedordinatetemp + p.Ydim * error1 / (1.5 + 4 * p.difficulty);
            if (p.cYpos - predictedordinate > epsilon) {
                if (p.cYpos - p.cYvel > p.Ydim / 2) {
                    p.set_Ypos((p.cYpos) - p.cYvel);
                }
            } else {
                if (p.cYpos + p.cYvel + p.Ydim / 2 < b.getHeight()
                        && (p.cYpos - predictedordinate < (-1 * epsilon))) {
                    p.set_Ypos(p.cYpos + p.cYvel);
                }
            }
        }
    }

    public static void moveAIplayer1(
            Paddle p, ArrayList<Ball> balls, BoardMulti b, double initialvelocity) {

        Ball balltobefollowed = null;
        Ball temp;
        double farthestballfromleft = b.getWidth();

        for (int i = 0; i < balls.size(); i++) {
            temp = balls.get(i);
            if ((temp.Xpos < farthestballfromleft)
                    && (temp.Xvel < 0)
                    && (temp.Xpos < b.Xdim / 4 * (1 + p.difficulty))) {
                balltobefollowed = balls.get(i);
                farthestballfromleft = temp.Xpos;
            }
        }

        if (balltobefollowed == null) {
            balltobefollowed = balls.get(0);
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag3 = 0;
            }
        } else {
            int temp1 = balltobefollowed.flag3;
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag3 = 0;
            }
            balltobefollowed.flag3 = temp1;
            double error = 5;
            double predictedordinate = 5;
            p.set_cYvel(initialvelocity);
            Random dice = new Random();
            double epsilon = initialvelocity / 2 + 1;
            double errortemp = (dice.nextInt(201) - 100) / ((double) 100);
            if (balltobefollowed.flag3 == 0) {
                error = errortemp;
                // System.out.println(error);
                // System.out.println(counter++);
                balltobefollowed.flag3 = 1;
            }

            double predictedordinatetemp =
                    (balltobefollowed.Ypos
                            - (balltobefollowed.Yvel / balltobefollowed.Xvel)
                                    * balltobefollowed.Xpos);

            predictedordinate = predictedordinatetemp + p.Ydim * error / (1.5 + 4 * p.difficulty);

            // p.set_cYvel(initialvelocity);

            // double epsilon = initialvelocity/2+1 ;

            if (p.cYpos - predictedordinate > epsilon) {
                if (p.cYpos - p.cYvel > p.Ydim / 2) {
                    p.set_Ypos(p.cYpos - p.cYvel);
                }
            } else {
                if (p.cYpos + p.cYvel + p.Ydim / 2 < b.getHeight()
                        && (p.cYpos - predictedordinate < (-1 * epsilon))) {
                    p.set_Ypos(p.cYpos + p.cYvel);
                }
            }
        }

        // System.out.println(balls.size());

    }

    public static void moveAIplayer3(
            Paddle p, ArrayList<Ball> balls, BoardMulti b, double initialvelocity) {
        Ball balltobefollowed = null;
        Ball temp;
        double farthestballfromleft = 0;

        for (int i = 0; i < balls.size(); i++) {
            temp = balls.get(i);
            if ((temp.Ypos > farthestballfromleft)
                    && (temp.Yvel > 0)
                    && (temp.Ypos > 3 * (b.Ydim) / 4 / (1 + p.difficulty))) {
                balltobefollowed = balls.get(i);
                farthestballfromleft = temp.Ypos;
            }
        }

        if (balltobefollowed == null) {
            balltobefollowed = balls.get(0);
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag2 = 0;
            }
        } else {
            int temp1 = balltobefollowed.flag2;
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag2 = 0;
            }
            balltobefollowed.flag2 = temp1;
            double error = 5;
            double predictedordinate = 5;
            p.set_cYvel(initialvelocity);
            Random dice = new Random();
            double epsilon = initialvelocity / 2 + 1;
            double errortemp = (dice.nextInt(201) - 100) / ((double) 100);
            if (balltobefollowed.flag2 == 0) {
                error = errortemp;
                // System.out.println(error);
                // System.out.println(counter++);
                balltobefollowed.flag2 = 1;
            }

            // double predictedordinatetemp  =
            // (balltobefollowed.Ypos-(balltobefollowed.Yvel/balltobefollowed.Xvel)*balltobefollowed.Xpos) ;
            double predictedordinatetemp =
                    (balltobefollowed.Xvel / balltobefollowed.Yvel) * 749
                            + (balltobefollowed.Xpos
                                    - (balltobefollowed.Xvel / balltobefollowed.Yvel)
                                            * balltobefollowed.Ypos);

            predictedordinate = predictedordinatetemp + p.Ydim * error / (1.5 + 4 * p.difficulty);

            p.set_cXvel(initialvelocity);

            // double epsilon = initialvelocity/2+1 ;

            if (p.cXpos - predictedordinate > epsilon) {
                if (p.cXpos - p.cXvel > p.Xdim / 2) {
                    p.set_Xpos(p.cXpos - p.cXvel);
                }
            } else {
                if (p.cXpos + p.cXvel + p.Xdim / 2 < b.getWidth()
                        && (p.cXpos - predictedordinate < (-1 * epsilon))) {
                    p.set_Xpos(p.cXpos + p.cXvel);
                }
            }
        }
        // System.out.println(balls.size());

    }

    public static void moveAIplayer4(
            Paddle p, ArrayList<Ball> balls, BoardMulti b, double initialvelocity) {

        Ball balltobefollowed = null;
        Ball temp;
        double farthestballfromleft = b.getHeight();

        for (int i = 0; i < balls.size(); i++) {
            temp = balls.get(i);
            if ((temp.Ypos < farthestballfromleft)
                    && (temp.Yvel < 0)
                    && (temp.Ypos < (b.Ydim) / 4 * (1 + p.difficulty))) {
                balltobefollowed = balls.get(i);
                farthestballfromleft = temp.Ypos;
            }
        }

        if (balltobefollowed == null) {
            balltobefollowed = balls.get(0);
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag1 = 0;
            }
        } else {
            int temp1 = balltobefollowed.flag1;
            for (int i = 0; i < balls.size(); i++) {
                balls.get(i).flag1 = 0;
            }
            balltobefollowed.flag1 = temp1;
            double error = 5;
            double predictedordinate = 5;
            p.set_cYvel(initialvelocity);
            Random dice = new Random();
            double epsilon = initialvelocity / 2 + 1;
            double errortemp = (dice.nextInt(201) - 100) / ((double) 100);
            if (balltobefollowed.flag1 == 0) {
                error = errortemp;
                // System.out.println(error);
                // System.out.println(counter++);
                balltobefollowed.flag1 = 1;
            }

            // double predictedordinatetemp  =
            // (balltobefollowed.Ypos-(balltobefollowed.Yvel/balltobefollowed.Xvel)*balltobefollowed.Xpos) ;
            // double predictedordinatetemp =
            // (balltobefollowed.Xvel/balltobefollowed.Yvel)*749+(balltobefollowed.Xpos-(balltobefollowed.Xvel/balltobefollowed.Yvel)*balltobefollowed.Ypos) ;
            double predictedordinatetemp =
                    (balltobefollowed.Xpos
                            - (balltobefollowed.Xvel / balltobefollowed.Yvel)
                                    * balltobefollowed.Ypos);

            predictedordinate = predictedordinatetemp + p.Ydim * error / (1.5 + 4 * p.difficulty);

            p.set_cXvel(initialvelocity);

            // p.set_cXvel(initialvelocity);

            // double epsilon = initialvelocity/2+1 ;

            if (p.cXpos - predictedordinate > epsilon) {
                if (p.cXpos - p.cXvel > p.Xdim / 2) {
                    p.set_Xpos(p.cXpos - p.cXvel);
                }
            } else {
                if (p.cXpos + p.cXvel + p.Xdim / 2 < b.getWidth()
                        && (p.cXpos - predictedordinate < (-1 * epsilon))) {
                    p.set_Xpos(p.cXpos + p.cXvel);
                }
            }
        }

        // System.out.println(balls.size());

    }
}
