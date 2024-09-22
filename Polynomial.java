class Polynomial{

double[] coeff;

public Polynomial(){
    this.coeff = new double[1];
    this.coeff[0] = 0;
}

public Polynomial(double[] coefficients){
    this.coeff = new double[coefficients.length];
    for (int i = 0; i <coefficients.length; i++) {
        this.coeff[i] = coefficients[i];
    }
}

public Polynomial add(Polynomial poly) {
    int bigger = Math.max(this.coeff.length, poly.coeff.length);
    int smaller = Math.min(this.coeff.length, poly.coeff.length);

    double[] resCoeff = new double[bigger];

    for (int i = 0; i < smaller; i++) {
        resCoeff[i] = this.coeff[i] + poly.coeff[i];
    }

    if (this.coeff.length > smaller) {
        for (int i = smaller; i < bigger; i++) {
            resCoeff[i] = this.coeff[i];
        }
    }

    else if (poly.coeff.length > smaller) {
        for (int i = smaller; i < bigger; i++) {
            resCoeff[i] = poly.coeff[i];
        }
    }
    Polynomial addedPoly = new Polynomial(resCoeff);

    return addedPoly;
}

public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coeff.length; i++) {
            result += coeff[i] * Math.pow(x, i);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}