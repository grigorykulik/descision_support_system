import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class DecisionMaker {
    //Список названий ручек из файла
    private ArrayList<DecisionOption> decisionOptions=new ArrayList<>();

    //Массив лидеров с т. з. механизма доминирования
    private ArrayList<Matrix> leadersAcrossAllBrsDominance=new ArrayList<>();

    //Массив лидеров с т. з. механизма блокировки
    private ArrayList<Matrix> leadersAcrossAllBrsBlock=new ArrayList<>();

    private Map<String, Integer> mapLeadersDom =new HashMap<>();
    private Map<String, Integer> mapLeadersBlock =new HashMap<>();

    //Метод для считывания вариантов выбора из файла
    public void readOptions(String filename) {
        try {
            Scanner sc=new Scanner(new BufferedReader(new FileReader(filename)));

            final int OPTIONS_NUM=10;
            while (sc.hasNextLine()) {
                for (int i=0; i<OPTIONS_NUM; i++) {
                    String line=sc.nextLine().trim();
                    DecisionOption d=new DecisionOption(line);
                    decisionOptions.add(d);
                }
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("Could not read the file. Make sure the file exists.");
        }
    }


    //Метод, который позволяет добавить матрицу бинарных отношений в массив leadersAcrossAllBrsDominance
    public void addBrDom(Matrix m) {
        this.leadersAcrossAllBrsDominance.add(m);
    }

    //Метод, который позволяет добавить матрицу бинарных отношений в массив leadersAcrossAllBrsBlock
    public void addBrBlock(Matrix m) {
        this.leadersAcrossAllBrsBlock.add(m);
    }

    // Метод для поиска лидеров по отдельным параметрам с помощью механизма доминирования
    public void getDominance(Matrix m, String parameterName) {
        //Массив счетчиков
        ArrayList<Integer> counters=new ArrayList<>();
        //Массив
        ArrayList<Integer> maxIndices=new ArrayList<>();

        //Перебираем строки матрицы
        for (int i=0; i<m.getRows(); i++) {
            int counter=0;

            //Перебираем столбцы матрицы. Считаем, сколько единиц проставлено в каждой строке
            for (int j=0; j<m.getColumns(); j++) {
                if (m.getMtrx()[i][j].getValue()==1) {
                    counter++;
                }
            }

            //Добавляем полученное количество единиц в массив.
            counters.add(counter);
        }

        //Находим максимальное значение в массиве counters
        int max=counters
                .stream()
                .mapToInt(v->v)
                .max()
                .orElseThrow(NoSuchElementException::new);

        // Перебираем массив, в котором посчитано количество единиц в каждой строке.
        // Если элемент этого массива равен максимальному, сохраняем индекс этого элемента в отдельный массив
        // maxIndices. Таким образом, в массиве maxIndices хранятся индексы тех альтернатив (строк), у которых больше
        // всего единиц.
        for (int i=0; i<counters.size(); i++) {
            if (counters.get(i)==max) {
                maxIndices.add(i);
            }
        }

        //Печать результата
        ReportGenerator.printLeadersByBrsDominanceOrBlocking(parameterName,
                maxIndices, decisionOptions, m, Mode.DOM);

        // Перебираем массив индексов maxIndices. Добавляем каждый элемент в массив leadersDom матрицы
        for (Integer mi:maxIndices) {
            m.addLeadersDom(decisionOptions.get(mi));
        }
    }


    public void getBlockingMech(Matrix m, String parameterName) {
        ArrayList<Integer> counters=new ArrayList<>();
        ArrayList<Integer> minIndices=new ArrayList<>();

        for (int j=0; j<m.getColumns(); j++) {

            int counter=0;
            for (int i=0; i<m.getRows(); i++) {
                if (m.getMtrx()[i][j].getValue()==1) {
                    counter++;
                }
            }
            counters.add(counter);
        }

        int min=counters
                .stream()
                .mapToInt(v->v)
                .min()
                .orElseThrow(NoSuchElementException::new);

        for (int i=0; i<counters.size(); i++) {
            if (counters.get(i)==min) {
                minIndices.add(i);
            }
        }

        ReportGenerator.printLeadersByBrsDominanceOrBlocking(parameterName,
                minIndices, decisionOptions, m, Mode.BLOCK);

        for (Integer mi:minIndices) {
            m.addLeadersBlock(decisionOptions.get(mi));
        }
    }


    //Метод для поиска тех альтернатив, которые лидируют по наибольшему числу параметров
    public void getLeadersAcrossAllBrsDominance() {

        //Перебираем список альтернатив
        for (DecisionOption d:decisionOptions) {

            int counter=0;

            //Перебираем матрицы. Если массив leadersDom матрицы m содержит тот или иной вариант выбора, счетчик
            //увеличивается
            for (Matrix m:leadersAcrossAllBrsDominance) {
                if (m.getLeadersDom().contains(d)) {
                    counter++;
                }
            }

            //Добавляем в мапу название варианта выбора и количество параметров, в которых он лидирует
            mapLeadersDom.put(d.getName(), counter);

            //Для каждого варианта выбора прописываем количество позиций, в которых он лидирует
            d.setnLeadingParametersDominance(counter);
        }

        Map<String,Integer> sorted =
                mapLeadersDom.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ReportGenerator.printLeadersAcrossBrs(sorted, Mode.DOM);
    }

    //Метод для поиска тех альтернатив, которые лидируют по наибольшему числу параметров c т.з. механизма блокировки
    public void getLeadersAcrossAllBrsBlock() {

        //Перебираем список альтернатив
        for (DecisionOption d:decisionOptions) {

            int counter=0;

            //Перебираем матрицы. Если массив leadersBlock матрицы m содержит тот или иной вариант выбора, счетчик
            //увеличивается
            for (Matrix m:leadersAcrossAllBrsBlock) {
                if (m.getLeadersBlock().contains(d)) {
                    counter++;
                }
            }

            //Добавляем в мапу название варианта выбора и количество параметров, в которых он лидирует
            mapLeadersBlock.put(d.getName(), counter);

            //Для каждого варианта выбора прописываем количество позиций, в которых он лидирует
            d.setnLeadingParametersBlock(counter);
        }

        Map<String,Integer> sorted =
                mapLeadersBlock.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ReportGenerator.printLeadersAcrossBrs(sorted, Mode.BLOCK);
    }

}
