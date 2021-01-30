import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DecisionMaker {
    private ArrayList<String> decisionOptions=new ArrayList<>();

    public void readOptions(String filename) {
        try {
            Scanner sc=new Scanner(new BufferedReader(new FileReader(filename)));

            while (sc.hasNextLine()) {
                for (int i=0; i<10; i++) {
                    String line=sc.nextLine().trim();
                    decisionOptions.add(line);
                }
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("Could not read the file. Make sure the file exists.");
        }
    }

    public void printOptions() {
        for (String s:decisionOptions) {
            System.out.println(s);
        }
    }

    public void getDominance(Matrix m, String parameterName) {
        ArrayList<Integer> counters=new ArrayList<>();
        ArrayList<Integer> maxIndices=new ArrayList<>();

        for (int i=0; i<m.getRows(); i++) {

            int counter=0;

            for (int j=0; j<m.getColumns(); j++) {
                if (m.getMtrx()[i][j].getValue()==1) {
                    counter++;
                }
            }

            counters.add(counter);
        }

        int max=counters
                .stream()
                .mapToInt(v->v)
                .max()
                .orElseThrow(NoSuchElementException::new);

        for (int i=0; i<counters.size(); i++) {
            if (counters.get(i)==max) {
                maxIndices.add(i);
            }
        }

        System.out.println("Механизм доминирования, оптимальный выбор по параметру " + parameterName+". " +
                "Если оптимальных выборов несколько, они равноправны:");
        System.out.println("--------------------------------------------------------------------");
        for (Integer mi:maxIndices) {
            System.out.println(decisionOptions.get(mi));
        }
    }
}
