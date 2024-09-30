import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        // these are awful tests but its proof of concept, the rest should work no problem
        Polynomial p = new Polynomial();
        System.out.println("Default polynomial evaluated at 3: " + p.evaluate(3));

        double[] c1 = {6, 0, 0, 5};
        int[] e1 = {3, 1, 0,2}; 
        Polynomial p1 = new Polynomial(c1, e1);
        System.out.println("Polynomial p1 evaluated at 2: " + p1.evaluate(2)); 


        double[] c2 = {0, -2, 0, 0, -9};
        int[] e2 = {4, 2, 0,2,3}; 
        Polynomial p2 = new Polynomial(c2, e2);
        System.out.println("Polynomial p2 evaluated at 1: " + p2.evaluate(1)); 


        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));


        if (s.hasRoot(1)) {
            System.out.println("1 is a root of s");
        } else {
            System.out.println("1 is not a root of s");
        }


        Polynomial p3 = new Polynomial(new double[]{1, -1}, new int[]{1, 0}); 
        Polynomial p4 = new Polynomial(new double[]{1}, new int[]{0}); 
        Polynomial product = p3.multiply(p4);
        System.out.println("Product of (x - 1) and 1 evaluated at 2: " + product.evaluate(2)); 


        try {
            s.saveToFile("polynomial.txt");
            System.out.println("Polynomial s saved to polynomial.txt");
        } catch (IOException e) {
            System.out.println("Error saving polynomial to file: " + e.getMessage());
        }
    }
}