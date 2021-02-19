import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class DecisionMaker {
    private final int BR_ROWS=10;
    private final int BR_COLUMNS=10;

    // Матрицы бинарных отношений, которые считываем из указанного файла
    private final Matrix appearance=new Matrix("appearance.txt", "Внешний вид", BR_ROWS, BR_COLUMNS);
    private final Matrix price=new Matrix("price.txt", "Цена", BR_ROWS, BR_COLUMNS);
    private final Matrix nibMaterial=new Matrix("nib_material.txt", "Материал пера", BR_ROWS, BR_COLUMNS);
    private final Matrix fillingMechanism=new Matrix("filling_system.txt", "Система заправки", BR_ROWS, BR_COLUMNS);
    private final Matrix countryOfOrigin=new Matrix("country_of_origin.txt", "Страна производства", BR_ROWS, BR_COLUMNS);
    private final Matrix nibFlexebility=new Matrix("nib_flexibility.txt", "Гибкость пера", BR_ROWS, BR_COLUMNS);
    private final Matrix lineWidth=new Matrix("line_width.txt", "Ширина линии", BR_ROWS, BR_COLUMNS);
    private final Matrix manufacturerStatus=new Matrix("manufacturer_status.txt", "Статус производства", BR_ROWS, BR_COLUMNS);
    private final Matrix limitedEdition=new Matrix("limited_edition.txt", "Лимитированность издания", BR_ROWS, BR_COLUMNS);

    // Весовые коэффициенты:
    private final double APPEARANCE_WEIGHT=0.25;
    private final double PRICE_WEIGHT=0.15;
    private final double NIB_MATERIAL_WEIGHT=0.1;
    private final double FILLING_MECH_WEIGHT=0.03;
    private final double COUNTRY_OF_ORIGIN_WEIGHT=0.15;
    private final double NIB_FLEXIBILITY_WEIGHT=0.04;
    private final double LINE_WIDTH_WEIGHT=0.25;
    private final double MANUFACTURER_STATUS=0.02;
    private final double LIMITED_EDITION_WEIGHT=0.01;

    // Массив для хранения матриц бинарных отношений
    private final ArrayList<Matrix> binaryRelationMatrices=new ArrayList<>();

    // Массив для хранения вариантов выбора
    private final ArrayList<String> decisionOptions=new ArrayList<>();

    // Мапа для хранения пар "название бинарного отношения - массив доминирующих позиций"
    private Map<String, ArrayList<String>> dominatingAlternatives=new HashMap<>();

    // Мапа для хранения пар "название варианта выбора - количество позиций, по которым этот вариант доминирует"
    private Map<String, Integer> dominatingLeaders=new HashMap<>();

    // Мапа для хранения пар "название бинарного отношения - массив блокирующих позиций"
    private Map<String, ArrayList<String>> blockingAlternatives=new HashMap<>();

    // Мапа для хранения пар "название варианта выбора - количество позиций, который этот вариант блокирует"
    private Map<String, Integer> blockingLeaders=new HashMap<>();

    // Матрица турнирного механизма
    private double[][] tournamentMatrixOriginal;

    // Матрица турнирного механизма с примененными коэффициентами
    private double[][] tournamentMatrixWeigthsApplied;

    // Массив весовых коэффициентов
    private ArrayList<Double> weights=new ArrayList<>();

    // Мапа для хранения пар "название варианта выбора - сумма баллов, полученных в турнирном механизме"
    private Map<String, Double> tournMechLeaders=new HashMap<>();

    // Конструктор
    public DecisionMaker() {
        // Добавляем матрицы бинарных отношений в массив binaryRelationMatrices
        binaryRelationMatrices.add(appearance);
        binaryRelationMatrices.add(price);
        binaryRelationMatrices.add(nibMaterial);
        binaryRelationMatrices.add(fillingMechanism);
        binaryRelationMatrices.add(countryOfOrigin);
        binaryRelationMatrices.add(nibFlexebility);
        binaryRelationMatrices.add(lineWidth);
        binaryRelationMatrices.add(manufacturerStatus);
        binaryRelationMatrices.add(limitedEdition);

        // Добавляем весовые коэффициенты бинарных отношений в массив weights
        weights.add(APPEARANCE_WEIGHT);
        weights.add(PRICE_WEIGHT);
        weights.add(NIB_MATERIAL_WEIGHT);
        weights.add(FILLING_MECH_WEIGHT);
        weights.add(COUNTRY_OF_ORIGIN_WEIGHT);
        weights.add(NIB_FLEXIBILITY_WEIGHT);
        weights.add(LINE_WIDTH_WEIGHT);
        weights.add(MANUFACTURER_STATUS);
        weights.add(LIMITED_EDITION_WEIGHT);
    }


    // Метод для считывания вариантов выбора из файла
    public void readOptions(String filename) {
        try {
            Scanner sc=new Scanner(new BufferedReader(new FileReader(filename)));

            final int OPTIONS_NUM=10;
            while (sc.hasNextLine()) {
                for (int i=0; i<OPTIONS_NUM; i++) {
                    String line=sc.nextLine().trim();
                    decisionOptions.add(line);
                }
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("Could not read the file. Make sure the file exists.");
        }
    }


    /** Метод для поиска доминирующих вариантов выбора по каждому бинарному отношению
    Перебираем матрицы бинарных отношений. В каждой матрице БО подсчитываем количество единиц в каждой строке.
    Если количество единиц совпадает с количеством вариантов выбора, этот вариант выбора является доминирующим
    по данному бинарному отношению. Доминирующие варианты выбора складываются в отображение dominatingAlternatives,
    где ключ — это название варианта выбора, а значение — это количество бинарных отношений, для которых этот вариант
    выбора является доминирующим.
     */

    public void findDominatingAlternatives() {
        for (Matrix m:binaryRelationMatrices) {

            ArrayList<String> al=new ArrayList<>();

            for (int i=0; i<BR_ROWS; i++) {
                int counter=0;

                for (int j=0; j<BR_COLUMNS; j++) {
                    if (m.getElements()[i][j]==1) {
                        counter++;
                    }
                }

                if (counter==BR_COLUMNS) {
                    al.add(decisionOptions.get(i));
                }
            }
            dominatingAlternatives.put(m.getName(), al);
        }
        ReportGenerator.printDominatingAlternatives(dominatingAlternatives);
    }


    // Метод, который позволяет посчитать, для какого количества бинарных отношений вариант выбора является доминирующим.
    // Перебираем варианты выбора в массиве вариантов выбора decisionOptions. Для каждого варианта выбора: если этот
    // вариант выбора содержится в массиве dominatingAlternatives, значение счетчика увеличивается на 1. Название варианта
    // выбора и значение счетчика отправляются в отображение dominatingLeaders
    public void findDominatingLeaders() {
        for (String s:decisionOptions) {

            int counter=0;

            Iterator<Map.Entry<String, ArrayList<String>>> it=dominatingAlternatives.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, ArrayList<String>> pair=it.next();

                ArrayList<String> value=pair.getValue();

                for (int i=0; i<value.size(); i++) {
                    if (value.get(i).contains(s)) {
                        counter++;
                    }
                }
            }
            dominatingLeaders.put(s, counter);
        }
        ReportGenerator.printDominatingLeaders(dominatingLeaders);
    }


    // Метод для поиска блокирующих вариантов выбора по каждому бинарному отношению
    // Перебираем матрицы бинарных отношений. В каждой матрице БО подсчитываем количество нулей в каждом столбце.
    // Если количество нулей совпадает с количеством вариантов выбора минус 1, этот вариант выбора является блокирующим
    // по данному бинарному отношению. Блокирующие варианты выбора складываются в отображение blockingAlternatives,
    // где ключ — это название варианта выбора, а значение — это количество бинарных отношений, для которых этот вариант
    // выбора является блокирующим.
    public void findBlockingAlternatives() {
        for (Matrix m:binaryRelationMatrices) {

            ArrayList<String> al=new ArrayList<>();

            for (int j=0; j<BR_COLUMNS; j++) {
                int counter=0;

                for (int i=0; i<BR_COLUMNS; i++) {
                    if (m.getElements()[i][j]==0) {
                        counter++;
                    }
                }

                if (counter==BR_ROWS-1) {
                    al.add(decisionOptions.get(j));
                }

            }
            blockingAlternatives.put(m.getName(), al);
        }

        ReportGenerator.printBlockingAlternatives(blockingAlternatives);
    }


    // Метод, который позволяет посчитать, для какого количества бинарных отношений вариант выбора является блокирующим.
    // Перебираем варианты выбора в массиве вариантов выбора decisionOptions. Для каждого варианта выбора: если этот
    // вариант выбора содержится в массиве blockingAlternatives, значение счетчика увеличивается на 1. Название варианта
    // выбора и значение счетчика отправляются в отображение blockingLeaders
    public void findBlockingLeaders() {
        for (String s:decisionOptions) {

            int counter=0;

            Iterator<Map.Entry<String, ArrayList<String>>> it=blockingAlternatives.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, ArrayList<String>> pair=it.next();

                ArrayList<String> value=pair.getValue();

                for (int i=0; i<value.size(); i++) {
                    if (value.get(i).contains(s)) {
                        counter++;
                    }
                }
            }
            blockingLeaders.put(s, counter);
        }
        ReportGenerator.printBlockingLeaders(blockingLeaders);
    }


    // Метод для создания исходной турнирной матрицы. Размер матрицы: количество_вариантов_выбора * количество_бинарных_отношений.
    // Для каждой ячейки исходной турнирной матрицы вызываем метод calculateElementValue
    public void createTournamentMatrix() {
        int rows=decisionOptions.size();
        int columns=binaryRelationMatrices.size();


        tournamentMatrixOriginal =new double[rows][columns];

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                tournamentMatrixOriginal[i][j]=calculateElementValue(i, j);
            }
        }

        ReportGenerator.printMatrix(tournamentMatrixOriginal, rows, columns, false);
    }


    // Метод для вычисления значения для каждой ячейки исходной турнирной матрицы.
    // На вход получаем координаты ячейки: i и j. Берем матрицу БО под номером j из массива
    // binaryRelationMatrices.
    // Каждый раз считаем значение только для одного варианта выбора. Т.е. исходная матрица турнирного механизма
    // заполняется по одной ячейке, начиная с первой строки, слева направо.
    // Если номер переданной строки не равен текущему номеру столбца матрицы бинарных отношений, то:
    // - если в матрице БО [i][k]==1 и [k][i]==0, значение счетчика увеличивается на 1
    // - если в матрице БО [i][k]==0 и [k][i]==1, значение счетчика не увеличивается
    // - значение счетчика увеличивается на 0,5 во всех остальных случаях
    private double calculateElementValue(int i, int j) {
        Matrix currentMatrix=binaryRelationMatrices.get(j);

        double result=0;

        for (int k=0; k<currentMatrix.getColumns(); k++) {
            if (i!=k) {
                if (currentMatrix.getElements()[i][k]==1 && currentMatrix.getElements()[k][i]==0) {
                    result=result+1;
                }

                else if (currentMatrix.getElements()[i][k]==0 && currentMatrix.getElements()[k][i]==1) {
                    result=result+0;
                }

                else result=result+0.5;
            }
        }

        return result;
    }


    // Метод, который применяет весовые коэффициенты к исходной матрице БО. Создаем новую матрицу, размеры которой
    // совпадают с размерами исходной. Проходимся по строкам и столбцам и умножаем элементы на соответствующие коэффициенты
    // из массива весовых коэффициентов weights
    public void applyWeights() {
        int rows=decisionOptions.size();
        int columns=binaryRelationMatrices.size();

        tournamentMatrixWeigthsApplied=new double[rows][columns];

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                tournamentMatrixWeigthsApplied[i][j]=tournamentMatrixOriginal[i][j]*weights.get(j);
            }
        }

        ReportGenerator.printMatrix(tournamentMatrixWeigthsApplied, rows, columns, true);
    }


    // Метод для расчета итоговых значений, получаемых в результате работы турнирного механизма.
    // Проходимся по строкам и столбцам матрицы турнирного механизма с примененными коэффициентами.
    // Суммируем значения в каждой строке и отправляем их в отображение tournMechLeaders.
    // Сортируем это оборажение, чтобы ранжировать лидеров по максимальной сумме сумм.
    public void calculateAndPrintSumsByRows() {
        int rows=decisionOptions.size();
        int columns=binaryRelationMatrices.size();


        for (int i=0; i<rows; i++) {
            double counter=0;

            for (int j=0; j<columns; j++) {
                counter=counter+tournamentMatrixWeigthsApplied[i][j];
            }
            tournMechLeaders.put(decisionOptions.get(i), counter);
        }

        Map<String,Double> sorted=tournMechLeaders.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        ReportGenerator.printTournMechLeaders(sorted);
    }


    // Метод для поиска k-максимальных.
    // Перебираем матрицы бинарных отношений. Каждую матрицу отправляем в метод getArrayOfKmax, который возвращает
    // массив k-максимальных.
    // Метод getArrayOfKmax прописывает в каждый объект класса KMax семь значений: H0R, ER, NR, а также их суммы, по которым
    // объекты сортируются для получения 1,2,3,4-макс.
    // Находим максимумы в массиве по каждому из четырех значений с помощью Stream API.
    // Перебираем массив. Сравниваем каждое к-значение элемента с максимумом. Если они совпадают, объект является к-максимальным.
    // Если объект является к-максимальным, кладем его в соответствующий массив: k1s, k2s и т.д.
    // Вызываем методы, проверяющие элементы на оптимальность.
    public void findKMaximums() {

        for (Matrix m:binaryRelationMatrices) {
            ArrayList<KMax> array=getArrayOfKmax(m);

            // Массивы к-максимальных, которые мы будем передавать на печать
            ArrayList<KMax> k1s=new ArrayList<>();
            ArrayList<KMax> k2s=new ArrayList<>();
            ArrayList<KMax> k3s=new ArrayList<>();
            ArrayList<KMax> k4s=new ArrayList<>();

            KMax k1=array
                    .stream()
                    .max(Comparator.comparing(KMax::getK1))
                    .orElseThrow(NoSuchElementException::new);

            KMax k2=array
                    .stream()
                    .max(Comparator.comparing(KMax::getK2))
                    .orElseThrow(NoSuchElementException::new);

            KMax k3=array
                    .stream()
                    .max(Comparator.comparing(KMax::getK3))
                    .orElseThrow(NoSuchElementException::new);

            KMax k4=array
                    .stream()
                    .max(Comparator.comparing(KMax::getK4))
                    .orElseThrow(NoSuchElementException::new);

            for (KMax k:array) {
                if (k.getK1()==k1.getK1()) {
                    k1s.add(k);
                }
            }

            for (KMax k:array) {
                if (k.getK2()==k2.getK2()) {
                    k2s.add(k);
                }
            }

            for (KMax k:array) {
                if (k.getK3()==k3.getK3()) {
                    k3s.add(k);
                }
            }

            for (KMax k:array) {
                if (k.getK4()==k4.getK4()) {
                    k4s.add(k);
                }
            }

            ReportGenerator.printKMaximums(k1s, k2s, k3s, k4s);
            ReportGenerator.printResultsForK1Check((isK1Maximum(k1)), k1);
            ReportGenerator.printResultsForK2Check((isK2StrictlyMax(k2)), k2);
            ReportGenerator.printResultsForK3Check((isK3Largest(k3)), k3);
            ReportGenerator.printResultsForK4Check((isK4StrictlyLargest(k4)), k4);

        }
    }

    // Следующие 4 метода проверяют отобранные к-макс. на оптимальность.
    public boolean isK1Maximum(KMax k1) {
        return (k1.getK1()==decisionOptions.size());
    }

    public boolean isK2StrictlyMax(KMax k2) {
        return (k2.getK2()==(decisionOptions.size()-1));
    }

    public boolean isK3Largest(KMax k3) {
        return (k3.getK1()==decisionOptions.size());
    }

    public boolean isK4StrictlyLargest(KMax k4) {
        return (k4.getK4()==decisionOptions.size()-1);
    }

    // Метод, который возвращает массив объектов KMax с заполненными полями.
    // Эти объекты сортируются в вызывающем методе. На основе сортировки метод определяет
    // К-максимальные.
    // В качестве параметра в метод передается матрица бинарных отношений.
    // Для каждой строки этой матрицы вызываем методы calculateHOR, calculateER, calculateNR.
    // Эти методы заполняют поля в передаваемом в них объекте KMax.
    // На основе заполненных полей заполняются суммарные значения для к1, к2 и т.д.
    // Элемент с заполненными суммами для к1, к2 и т.д. помещается в массив, который и возвращает метод.
    private ArrayList<KMax> getArrayOfKmax(Matrix m) {

        ArrayList<KMax> al=new ArrayList<>();

        for (int i=0; i<m.getRows(); i++) {
            KMax k=new KMax(m.getName(), decisionOptions.get(i));

            calculateHOR(m, k, i);
            calculateER(m, k, i);
            calculateNR(m, k, i);

            calculateK1Max(k);
            calculateK2Max(k);
            calculateK3Max(k);
            calculateK4Max(k);

            al.add(k);
        }

        return al;
    }


    // Метод для подсчета строго подчиненных x по R
    // На вход получаем матрицу бинарных отношений и номер варианта выбора i, а также объект KMax
    // Если номер варианта выбора не равен номеру текущего столбца, то:
    // - если в матрице бинарных отношений для данного варианта выбора [i][j]==1 и [j][i]==0, счетчик увеличивается на 1.
    // Значение, полученное по завершении цикла и есть число строго подчиненных x по R. Оно вносится в соответствующее поле объекта KMax
    private void calculateHOR(Matrix m, KMax k, int i) {

        int counter=0;
        for (int j=0; j<m.getColumns(); j++) {

            if (j!=i) {
                if (m.getElements()[i][j]==1 && m.getElements()[j][i]==0) {
                    counter++;
                }
            }

        }

        k.setHor(counter);
    }


    // Метод для подсчета эквивалентных x по R
    // На вход получаем матрицу бинарных отношений и номер варианта выбора i, а также объект KMax
    // Если номер варианта выбора не равен номеру текущего столбца, то:
    // - если в матрице бинарных отношений для данного варианта выбора [i][j]==1 и [j][i]==1, счетчик увеличивается на 1.
    // Значение также увеличивается на 1, чтобы учесть сам x
    // Значение, полученное по завершении цикла и есть число строго подчиненных x по R. Оно вносится в соответствующее поле объекта KMax
    private void calculateER(Matrix m, KMax k, int i) {

        int counter=0;
        for (int j=0; j<m.getColumns(); j++) {
            if (j!=i) {
                if (m.getElements()[i][j]==1 && m.getElements()[j][i]==1) {
                    counter++;
                }
            }

            else continue;
        }

        k.setEr(counter+1);
    }


    // Метод для подсчета несравнимых x по R
    // На вход получаем матрицу бинарных отношений и номер варианта выбора i, а также объект KMax
    // Если номер варианта выбора не равен номеру текущего столбца, то:
    // - если в матрице бинарных отношений для данного варианта выбора [i][j]==0 и [j][i]==0, счетчик увеличивается на 1.
    // Значение, полученное по завершении цикла и есть число несравнимых с x по R. Оно вносится в соответствующее поле объекта KMax
    private void calculateNR(Matrix m, KMax k, int i) {

        int counter=0;
        for (int j=0; j<m.getColumns(); j++) {
            if (j!=i) {
                if (m.getElements()[i][j]==0 && m.getElements()[j][i]==0) {
                    counter++;
                }
            }

        }

        k.setNr(counter);
    }


    // Метод для подсчета суммы для 1-максимального.
    // Передаем на вход объект с заполненными полями для H0R, ER, NR. На их основе подсчитываем значение и так же
    // записываем в соответствующее поле объекта.
    private void calculateK1Max(KMax k) {
        int value=k.getHor()+k.getEr()+k.getNr();
        k.setK1(value);
    }

    // Аналогично
    private void calculateK2Max(KMax k) {
        int value=k.getHor()+k.getNr();
        k.setK2(value);
    }

    // Аналогично
    private void calculateK3Max(KMax k) {
        int value=k.getHor()+k.getEr();
        k.setK3(value);
    }

    // Аналогично
    private void calculateK4Max(KMax k) {
        k.setK4(k.getHor());
    }

}
