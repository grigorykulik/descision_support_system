import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Matrix {
    //Имя матрицы
    private String name;

    //Количество строк
    private int rows;

    //Количество столбцов
    private int columns;

    //Сама матрица
    private int[][] elements;

    //Конструктор. Задаем имя, количество строк, количество столбцов, считываем матрицу из указанного файла
    public Matrix(String filename, String name, int rows, int columns) {

        elements=new int[rows][columns];

        this.name=name;
        this.rows=rows;
        this.columns=columns;

        try {
            Scanner sc=new Scanner(new BufferedReader(new FileReader(filename)));

            while (sc.hasNextLine()) {
                for (int i=0; i<rows; i++) {
                    String[] line=sc.nextLine().trim().split(" ");
                    for (int j=0; j<columns; j++) {
                        elements[i][j]=Integer.parseInt(line[j]);
                    }
                }
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("File not found. Make sure the file exists");
            System.exit(0);
        }
    }


    public Matrix() {

    }


    // Геттеры, сеттеры
    public int[][] getElements() {
        return this.elements;
    }

    public String getName() {
        return this.name;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }
}
