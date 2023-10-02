import java.io.*;
import java.util.Scanner;

public class Polynomial{
    public double[] coefficients;
    public int[] exponents;

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }
    public Polynomial() {
        this.coefficients = null;
        this.exponents = null;
    }
    public Polynomial(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        if (scanner.hasNext()) {
            String polynomial = scanner.next();
            String[] terms = polynomial.split("(?=[+-])");
            double[] coefficients = new double[terms.length];
            int[] exponents = new int[terms.length];
            for (int i = 0; i < terms.length; i++) {
                if (terms[i].contains("x")) {
                    String[] moreterms = terms[i].split("x");
                    coefficients[i] = Double.parseDouble(moreterms[0]);
                    exponents[i] = Integer.parseInt(moreterms[1]);
                } else {
                    coefficients[i] = Double.parseDouble(terms[i]);
                    exponents[i] = 0;
                }
            }
            this.coefficients = coefficients;
            this.exponents = exponents;
            scanner.close();
        } else {
            scanner.close();
            throw new IllegalArgumentException("File is empty.");
        }
    }

    public Polynomial getRidOfExtra(double[] coefficients, int[] exponents) {
        int counter = 0;
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] == 0) {
                counter = counter + 1;
            }
        }
        double[] newCoef = new double[coefficients.length - counter];
        int[] newExp = new int[exponents.length - counter];
        int curr = 0;
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != 0) {
                newCoef[curr] = coefficients[i];
                newExp[curr] = exponents[i];
                curr = curr + 1;
            }
        }
        counter = 0;
        for (int i = 0; i < newExp.length; i++) {
            if (newExp[i] == -1) {
                counter = counter + 1;
            }
        }
        double[] newnewCoef = new double[newCoef.length - counter];
        int[] newnewExp = new int[newnewCoef.length - counter];
        curr = 0;
        for (int i = 0; i < newExp.length; i++) {
            if (newExp[i] != -1) {
                newnewCoef[curr] = newCoef[i];
                newnewExp[curr] = newExp[i];
                curr = curr + 1;
            }
        }
        if (newnewExp.length == 0) {
            return new Polynomial();
        }

        return new Polynomial(newnewCoef, newnewExp);
    }

    public Polynomial add(Polynomial p) {
        if (p.exponents == null && this.exponents == null) {
            return new Polynomial();
        } else if (p.exponents == null) {
            return new Polynomial(this.coefficients, this.exponents);
        } else if (this.exponents == null) {
            return new Polynomial(p.coefficients, p.exponents);
        }
        int length = this.exponents.length + p.exponents.length;
        double[] newCoef = new double[length];
        int[] newExp = new int[length];
        boolean stillNeed = true;
        int curr = this.exponents.length;
        for (int i = 0; i < newExp.length; i++) {
            newExp[i] = -1;
            newCoef[i] = 0;
        }

        for (int i = 0; i < this.exponents.length; i++) {
            newCoef[i] = this.coefficients[i];
            newExp[i] = this.exponents[i];
        }

        for (int i = 0; i < p.exponents.length; i++) {
            stillNeed = true;
            for (int j = 0; j < newExp.length; j++) {
                if (newExp[j] == p.exponents[i]) {
                    newCoef[j] = newCoef[j] + p.coefficients[i];
                    stillNeed = false;
                }
            }
            if (stillNeed == true) {
                newExp[curr] = p.exponents[i];
                newCoef[curr] = p.coefficients[i];
                curr = curr + 1;
            }
        }
        return getRidOfExtra(newCoef, newExp);
    }

    public Polynomial multiply(Polynomial p) {
        if (p.exponents == null || this.exponents == null) {
            return new Polynomial();
        }
        int length = this.exponents.length * p.exponents.length;
        double[] newCoef = new double[length];
        int[] newExp = new int[length];
        int curr = 0;
        boolean stillNeed = true;
        for (int i = 0; i < newExp.length; i++) {
            newExp[i] = -1;
            newCoef[i] = 0;
        }

        for (int i = 0; i < this.exponents.length; i++) {
            for (int j = 0; j < p.exponents.length; j++) { 
                stillNeed = true;
                for(int check = 0; check < newExp.length; check++) {
                    if (newExp[check] == this.exponents[i] + p.exponents[j]) {
                        newCoef[check] = newCoef[check] + (this.coefficients[i] * p.coefficients[j]);
                        stillNeed = false;
                    }
                }
                if (stillNeed == true) {
                    newExp[curr] = this.exponents[i] + p.exponents[j];
                    newCoef[curr] = this.coefficients[i] * p.coefficients[j];
                    curr = curr + 1;
                }
            }
        }
        return getRidOfExtra(newCoef, newExp);
    }

    public void saveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            if  (this.exponents == null) {
                writer.write("0");
            } else {
                for (int i = 0; i < exponents.length; i++) {
                    if (i == 0) {
                        if (exponents[i] == 0) {
                            writer.write(String.valueOf(coefficients[i]));
                        } else {
                            if (coefficients[i] > 0) {
                                writer.write(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                            } else {
                                writer.write(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                            }
                        }
    
                    } else {
                        if (exponents[i] == 0) {
                            if (coefficients[i] > 0) {
                                writer.write("+" + String.valueOf(coefficients[i]));
                            } else {
                                writer.write(String.valueOf(coefficients[i]));
                            }
                        } else {
                            if (coefficients[i] > 0) {
                                writer.write("+" + String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                            } else {
                                writer.write(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                            }
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the file.");
            e.printStackTrace();
        }
    }

    public double evaluate(double num) {
        if  (this.exponents == null) {
            return 0;
        }
        double answer = 0;
        for (int i = 0; i < exponents.length; i++) {
            answer = answer + coefficients[i] * Math.pow(num, exponents[i]);
        }
        return answer;
    }

    public boolean hasRoot(double num) {
        return evaluate(num) == 0;
    }

    public void printPoly() {
        if (this.exponents == null) {
            System.out.println("0");
        } else  {
        for (int i = 0; i < exponents.length; i++) {
            if (i == 0) {
                if (exponents[i] == 0) {
                    System.out.print(String.valueOf(coefficients[i]));
                } else {
                    if (coefficients[i] > 0) {
                        System.out.print(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                    } else {
                        System.out.print(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                    }
                }

            } else {
                if (exponents[i] == 0) {
                    if (coefficients[i] > 0) {
                        System.out.print("+" + String.valueOf(coefficients[i]));
                    } else {
                        System.out.print(String.valueOf(coefficients[i]));
                    }
                } else {
                    if (coefficients[i] > 0) {
                        System.out.print("+" + String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                    } else {
                        System.out.print(String.valueOf(coefficients[i]) + "x" + String.valueOf(exponents[i]));
                    }
                }
            }
        }
        System.out.println();
    }
        
    }

    public void printExp() {
        if (this.exponents == null) {
            System.out.println("[0]");
        } else {
        System.out.print("[");
        System.out.print(exponents[0]);
        for (int i = 1; i < exponents.length; i++) {
            System.out.print(",");
            System.out.print(exponents[i]);
        }
        System.out.print("]");
        System.out.println();
    }
    }

    public void printCoef() {
        if (this.exponents == null) {
            System.out.println("[0]");
        } else {
        System.out.print("[");
        System.out.print((int)coefficients[0]);
        for (int i = 1; i < coefficients.length; i++) {
            System.out.print(",");
            System.out.print((int)coefficients[i]);
        }
        System.out.print("]");
        System.out.println();
    }
}

}
