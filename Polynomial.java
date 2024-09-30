import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Polynomial{

double[] coeff;
int [] exponents;

public Polynomial(){
    this.coeff = new double[1];
    this.coeff[0] = 0;
    this.exponents = new int[1];
    this.exponents[0] = 0;
}

public Polynomial(double[] coefficients, int[] exponents){
    this.coeff = coefficients;
    this.exponents = exponents;
}


public Polynomial(File file) throws IOException {

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line = br.readLine();

        ArrayList<Double> coefficientsList = new ArrayList<>();
        ArrayList<Integer> exponentsList = new ArrayList<>();
        int i = 0;
        
        while (i < line.length()) {
            boolean isNegative = false;
            if (line.charAt(i) == '-') {
                isNegative = true;
                i++;
            } else if (line.charAt(i) == '+') {
                i++;  
            }

            StringBuilder coefficientBuilder = new StringBuilder();
            while (i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.')) {
                coefficientBuilder.append(line.charAt(i));
                i++;
            }
            
            double coefficient;
            if (coefficientBuilder.length() > 0) {
                coefficient = Double.parseDouble(coefficientBuilder.toString());
            } else {
                coefficient = isNegative ? -1 : 1;
            }

            int exponent = 0; 
            if (i < line.length() && line.charAt(i) == 'x') {
                i++;  
                exponent = 1;  

                if (i < line.length() && line.charAt(i) == '^') {
                    i++;  
                    exponent = 0;
                    while (i < line.length() && Character.isDigit(line.charAt(i))) {
                        exponent = exponent * 10 + (line.charAt(i) - '0');
                        i++;
                    }
                }
            }

            coefficientsList.add(coefficient);
            exponentsList.add(exponent);
        }

        this.coeff = new double[coefficientsList.size()];
        this.exponents = new int[exponentsList.size()];

        for (int idx = 0; idx < coefficientsList.size(); idx++) {
            this.coeff[idx] = coefficientsList.get(idx);
            this.exponents[idx] = exponentsList.get(idx);
        }
    }
}




public Polynomial add(Polynomial poly) {
    int i = 0;
    int j = 0;

    int addedLength = this.coeff.length + poly.coeff.length;

    double[] newCoeff = new double[addedLength];
    int[] newExponents = new int[addedLength];

    int idx = 0;

    while (i < this.coeff.length && j < poly.coeff.length) {
        if (this.exponents[i] == poly.exponents[j]) {
            newCoeff[idx] = this.coeff[i] + poly.coeff[j];
            // we have to skip zero terms, i get funky behaviour otherwise
            if (newCoeff[idx] != 0) { 
                newExponents[idx] = this.exponents[i];
                idx++;
            }

            i++;
            j++;
        } else if (this.exponents[i] < poly.exponents[j]) {
            newCoeff[idx] = this.coeff[i];
            newExponents[idx] = this.exponents[i];
            i++;
            idx++;
        } else {
            newCoeff[idx] = poly.coeff[j];
            newExponents[idx] = poly.exponents[j];
            j++;
            idx++;
        }
    }

    while (i < this.coeff.length) {
        newCoeff[idx] = this.coeff[i];
        newExponents[idx] = this.exponents[i];
        i++;
        idx++;
    }

    while (j < poly.coeff.length) {
        newCoeff[idx] = poly.coeff[j];
        newExponents[idx] = poly.exponents[j];
        j++;
        idx++;
    }

    // this method is basically adding both arrays together and trimming off any zeroes, avoiding the idx our of bounds error entierly.
    double[] resCoeff = new double[idx];
    int[] resExponents = new int[idx];
    System.arraycopy(newCoeff, 0, resCoeff, 0, idx);
    System.arraycopy(newExponents, 0, resExponents, 0, idx);

    return new Polynomial(resCoeff, resExponents);
}

public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coeff.length; i++) {
            System.out.println(exponents[i]);
            result += coeff[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }




    public Polynomial multiply(Polynomial poly) {
        // It was much easier to us a hash map over arraylists
        Map<Integer, Double> resultMap = new HashMap<>();


        for (int i = 0; i < this.coeff.length; i++) {
            for (int j = 0; j < poly.coeff.length; j++) {

                int newExponent = this.exponents[i] + poly.exponents[j];
                double newCoefficient = this.coeff[i] * poly.coeff[j];

                resultMap.put(newExponent, resultMap.getOrDefault(newExponent, 0.0) + newCoefficient);
            }
        }

        int size = resultMap.size();

        double[] resultCoeffs = new double[size];
        int[] resultExponents = new int[size];
        
        int idx = 0;


        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            resultExponents[idx] = entry.getKey();
            resultCoeffs[idx] = entry.getValue();
            idx++;
        }

        return new Polynomial(resultCoeffs, resultExponents);
    }




    public void saveToFile(String file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            StringBuilder polyStr = new StringBuilder();
    
            for (int i = 0; i < coeff.length; i++) {
                double coefficient = coeff[i];
                int exponent = exponents[i];
    
                if (coefficient == 0) {
                    continue;
                }
    
                if (polyStr.length() > 0) {
                    if (coefficient > 0) {
                        polyStr.append(" + ");
                    } else {
                        polyStr.append(" - ");
                        coefficient = -coefficient; 
                    }
                } else {
                    if (coefficient < 0) {
                        polyStr.append("-");
                        coefficient = -coefficient;
                    }
                }
    
                if (exponent == 0) {
                    polyStr.append(coefficient);  
                } else if (coefficient != 1) {
                    polyStr.append(coefficient).append("x");  
                } else {
                    polyStr.append("x");  
                }
    
                if (exponent > 1) {
                    polyStr.append("^").append(exponent);
                }
            }
    
            if (polyStr.length() == 0) {
                polyStr.append("0");
            }

            writer.write(polyStr.toString());
        }
    }
}



