public class Modeling {
    public static void main(String[] args) {
        final double airDensity = 1.225;
        final double crossSectionArea = 0.1;
        final double dragCoef = 0.23;
        final double liftCoef = 0.1;
        final double mass = 0.2;
        final double g = -9.807;

        final double dragC = 0.5 * airDensity * crossSectionArea * dragCoef;
        final double liftC = 0.5 * airDensity * crossSectionArea * liftCoef;

        final double dt = 0.01;

        double hoodAngle = Math.toRadians(60), offsetAngle = Math.toRadians(20);
        double distance = 4;
        double perpV = 2, paraV = 1;

        double targetDeltaY = 2.6;

        double x = -distance, y = 1, t = 0, vxcurr = 0, vycurr = 0, ax = 0, ay = 0;

        double v = Math.pow(Math.pow(perpV / Math.tan(offsetAngle) + paraV, 2) 
            + Math.pow(perpV, 2) - g * Math.pow(distance, 2) * Math.pow(Math.sin(hoodAngle), 2) 
            / (2 * targetDeltaY * Math.pow(Math.cos(hoodAngle), 2) - 2 * distance 
             * Math.cos(hoodAngle) * Math.sin(hoodAngle)), 0.5);
        
        double vx = Math.cos(hoodAngle);
        double vy = Math.sin(hoodAngle);

        double maxY = 0;

        while (y >= 0) {
            vxcurr = ax * dt + vx; // assume constant acceleration during dt
            vycurr = ay * dt + vy;

            x = x + dt * (vx + vxcurr) / 2; // trapezoidal integration of position
            y = y + dt * (vy + vycurr) / 2;

            vx = vxcurr;
            vy = vycurr;

            v = Math.sqrt(Math.pow(vx, 2) +  Math.pow(vy, 2));

            ax = -(v / mass) * (dragC * vx + liftC * /*Math.abs*/(vy)); // update ax&ay for next iteration:
            ay = g + (v / mass) * (liftC * vx - dragC * vy); 

            t = t + dt;

            if (maxY < y) {
                maxY = y;
            }

            System.out.println("t: " + round(t) + " x: " + round(x) + " y: " + round(y) + " maxy: " + round(maxY));
            System.out.println(" vx: " + round(vx) + " vy: " + round(vy) + " v: " + round(v));
            System.out.println("  ax: " + round(ax) + " ay: " + round(ay));
        }

        

        
    }

    public static double round(double a) {
        return (double) Math.round(a * 1000) / 1000;
    }

    
}