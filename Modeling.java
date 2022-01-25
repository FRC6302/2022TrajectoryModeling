public class Modeling {
    public static void main(String[] args) {

        //all units metric: kg, meters,

        final double airDensity = 2.54;  //1.225; find out right units, kg/m3 or g/m3
        final double crossSectionArea = 0.0366; //pi * radius^2
        final double dragCoef = 0.000; //0.23; //stolen from another team
        final double magnusCoef = 0.000; //.5; //to be determined experimentally
        final double mass = 0.2; //kg
        final double radius = 0.108; //meters
        final double g = -9.79692; //calculated online using our location's latitude and elevation
        final double spinV = 0; //rev per s
        
        final double dragC = 0.5 * airDensity * crossSectionArea * dragCoef;
        //final double magnusC = 0.5 * airDensity * crossSectionArea * magnusCoef;
        final double magnusC = magnusCoef * (16/3) * Math.pow(Math.PI, 2) * Math.pow(radius, 3) * airDensity * spinV;

        final double dt = 0.01; //more precise with lower dt

        double hoodAngle = Math.toRadians(60), offsetAngle = Math.toRadians(50);
        double distance = 6;
        double perpV = 5, paraV = 1;

        final double targetHeight = 2.64;

        double x = -distance, y = 1, t = 0, vxcurr = 0, vycurr = 0, v = 0, ax = 0, ay = g, theta = hoodAngle;

                    
        double firstTerm = Math.pow((perpV / Math.tan(offsetAngle)) + paraV, 2);
        double secondTerm = Math.pow(perpV, 2);
        double thirdTerm = g * Math.pow(distance, 2) * Math.pow(Math.sin(hoodAngle), 2);
        double fourthTerm = (2 * (targetHeight - y) * Math.pow(Math.cos(hoodAngle), 2)
            - (2 * distance * Math.cos(hoodAngle) * Math.sin(hoodAngle)));
        
        v = Math.pow(firstTerm + secondTerm + (thirdTerm / fourthTerm), 0.5);
        //v = 8.6;
         
        System.out.println("init v: " + v);

        System.out.println("first: " + firstTerm);
        System.out.println("second: " + secondTerm);
        System.out.println("third: " + thirdTerm);
        System.out.println("fourth: " + fourthTerm);
        //double v = 5;
        double vx = v * Math.cos(hoodAngle);
        double vy = v * Math.sin(hoodAngle);

        double maxY = 0;
        
        boolean scored = false;

        double startTime = System.nanoTime();

        while (x <= 0) {
            vxcurr = ax * dt + vx; // assume constant acceleration during dt
            vycurr = ay * dt + vy;

            x = x + dt * (vx + vxcurr) / 2; // trapezoidal integration of position
            y = y + dt * (vy + vycurr) / 2;

            vx = vxcurr;
            vy = vycurr;

            v = Math.sqrt(Math.pow(vx, 2) +  Math.pow(vy, 2));

            theta = Math.asin(vy / v);

            //ax = -(v / mass) * (dragC * vx + magnusC * vy); // update ax&ay for next iteration:
            //ay = g + (v / mass) * (magnusC * vx - dragC * vy); 
            ax = (-magnusC * vy - dragC * v * vx) / mass;
            ay = g + (magnusC * vx - dragC * v * vy) / mass;

            t = t + dt;

            if (maxY < y) {
                maxY = y;
            }

            System.out.println("t: " + round(t) + " x: " + round(x) + " y: " + round(y) + " maxy: " + round(maxY));
            System.out.println(" vx: " + round(vx) + " vy: " + round(vy) + " v: " + round(v) + " theta: " + round(Math.toDegrees(theta)));
            System.out.println("  ax: " + round(ax) + " ay: " + round(ay));
        }



        /*while (x < 0) {
            ax = (-magnusC * vy - dragC * v * vx) / mass;
            //ay = g + (magnusC * vx - dragC * v * vy) / mass;

            //vxcurr = ax * dt + vx; // assume constant acceleration during dt
            //vycurr = ay * dt + vy;

            vx = vxcurr;
            //vy = vycurr;

            v = Math.sqrt(Math.pow(vx, 2) +  Math.pow(vy, 2));

            theta = Math.asin(vy / v);

            x = x + (ax / vx) * dt;

            t = t + dt;

            System.out.println(x);
        }*/

        
        double timeElapsed = System.nanoTime() - startTime;

        if ( y > targetHeight - 0.2 && y < targetHeight + 0.2) {
            scored = true;
        }

        String message;
        if (y > targetHeight) {
            message = "too high!";
        }
        else if (y < targetHeight) {
            message = "too low!";
        }
        else {
            message = "perfect shot!";
        }
        
        System.out.println();
        System.out.println("computing time: " + timeElapsed / 1000000000);

        System.out.println();
        System.out.println("final time: " + t);
        System.out.println("final delta x: " + (distance + x));
        System.out.println("final y: " + y);
        System.out.println("scored? " + scored + ", " + message);

        System.out.println();
        
    }

    public static double round(double a) {
        return (double) Math.round(a * 1000) / 1000;
    }

    
}