import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Matrix {
    //Количество строк
    private final int ROWS=10;
    //Количество столбцов
    private final int COLUMNS=10;

    //Массив лидеров с т.з. механизма домbнирования
    private ArrayList<DecisionOption> leadersDom=new ArrayList<>();

    //Массив лидеров с т.з. механизма блокировки
    private ArrayList<DecisionOption> leadersBlock=new ArrayList<>();

    private Element[][] mtrx=new Element[ROWS][COLUMNS];

    //Геттеры, сеттеры
    public int getRows() {
        return this.ROWS;
    }
    public int getColumns() {
        return this.COLUMNS;
    }
    public Element[][] getMtrx() {
        return this.mtrx;
    }
    public ArrayList<DecisionOption> getLeadersDom() {
        return leadersDom;
    }
    public ArrayList<DecisionOption> getLeadersBlock() {
        return leadersBlock;
    }


    //Конструктор
    public Matrix(String filename) {
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLUMNS; j++) {
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


    public void addLeadersDom(DecisionOption leader) {
        leadersDom.add(leader);
    }

    public void addLeadersBlock(DecisionOption leader) {
        leadersBlock.add(leader);
    }
}
