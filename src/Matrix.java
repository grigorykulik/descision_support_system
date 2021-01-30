import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Matrix {
    private final int rows=10;
    private final int columns=10;

    private Element[][] mtrx=new Element[rows][columns];

    //Геттеры, сеттеры
    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    //Конструктор
    public Matrix(String filename) {
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                mtrx[i][j]=new Element();
            }
        }

        try {
            Scanner sc=new Scanner(new BufferedReader(new FileReader(filename)));

            while (sc.hasNextLine()) {
                for (int i=0; i<mtrx.length; i++) {
                    String[] line=sc.nextLine().trim().split(" ");
                    for (int j=0; j<line.length; j++) {
                        mtrx[i][j].setValue(Integer.parseInt(line[j]));
                        mtrx[i][j].setRow(i);
                        mtrx[i][j].setColumn(j);
                    }
                }
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("File not found. Make sure the file exists");
            System.exit(0);
        }
    }

    //Распечатка матрицы
    public void print() {
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                System.out.print(mtrx[i][j].getValue()+" ");
            }

            System.out.println();
        }
    }

    public Element[][] getMtrx() {
        return this.mtrx;
    }
}
