public class Polynomial{
    double[] array;

    public Polynomial(double[] arrayIn) {
        this.array = arrayIn;
    }
    public Polynomial() {
        this.array = new double[]{0};
    }

    public Polynomial add(Polynomial p) {
        int length;
        if (array.length >= p.array.length) {
            length = array.length;
        } else {
            length = p.array.length;
        }
        double[] newArray = new double[length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        for (int i = 0; i < p.array.length; i++) {
            newArray[i] = newArray[i] + p.array[i];
        }
        Polynomial answer = new Polynomial(newArray);
        return answer;

    }

    public double evaluate(double num) {
        double answer = 0;
        for (int i = 0; i < array.length; i++) {
            answer = answer + array[i] * Math.pow(num, (double)i);
        }
        return answer;
    }

    public boolean hasRoot(double num) {
        return evaluate(num) == 0;
    }
}