package src;
import java.util.*;
public class Common {
  static public int configurationTokenToInteger (String s) {
    int i = 0;

    try {
      i = Integer.parseInt(s.trim());
    } catch (NumberFormatException nfe) {
      System.out.println("NumberFormatException: " + nfe.getMessage());
    }
    return i;
  }

  static public double R1 () {
    Random generator = new Random(System.currentTimeMillis());
    double U = generator.nextDouble();
    while (U < 0 || U >= 1) {
      U = generator.nextDouble();
    }
    double V = generator.nextDouble();
    while (V < 0 || V >= 1) {
      V = generator.nextDouble();
    }
    double X =  Math.sqrt((8/Math.E)) * (V - 0.5)/U;
    if (!(R2(X,U))) { return -1; }
    if (!(R3(X,U))) { return -1; }
    if (!(R4(X,U))) { return -1; }
    return X;
  }

  static public boolean R2 (double X, double U) {
      return (X * X) <= (5 - 4 * Math.exp(.25) * U);
  }

  static public boolean R3 (double X, double U) {
      return !((X * X) >= (4 * Math.exp(-1.35) / U + 1.4));
  }

  static public boolean R4 (double X, double U) {
      return (X * X) < (-4 * Math.log(U));
  }

}

