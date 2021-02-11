import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class ReportGenerator {

    // Метод для печати вариантов выбора, доминирующих по тому или иному БО
    public static void printDominatingAlternatives(Map<String, ArrayList<String>> m) {
        String message="Механизм доминирования, оптимальный выбор по отдельным параметрам. Если скобки пусты," +
                "доминирующих вариантов выбора нет";
        System.out.println(message);
        System.out.println("--------------------------------------------------------------");

        Iterator<Map.Entry<String, ArrayList<String>>> it=m.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> pair=it.next();
            String key=pair.getKey();
            ArrayList<String> value=pair.getValue();

            System.out.println(key+": "+value);
        }
    }

    // Метод для печати количества позиций, в которых доминирует та или иная альтернатива
    public static void printDominatingLeaders(Map<String, Integer> m) {
        System.out.println();
        String message = "Механизм доминирования, вариант выбора и количество позиций, в которых он доминирует";
        System.out.println(message);
        System.out.println("--------------------------------------------------------------");

        Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            String key = pair.getKey();
            int value = pair.getValue();

            System.out.println(key + ": " + value);
        }
    }

    // Метод для печати вариантов выбора, являющихся блокирующими по тому или иному БО
    public static void printBlockingAlternatives(Map<String, ArrayList<String>> m) {
        System.out.println();
        String message="Механизм блокировки, оптимальный выбор по отдельным параметрам. Если скобки пусты," +
                "блокирующих вариантов выбора нет.";
        System.out.println(message);
        System.out.println("--------------------------------------------------------------");

        Iterator<Map.Entry<String, ArrayList<String>>> it=m.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> pair=it.next();
            String key=pair.getKey();
            ArrayList<String> value=pair.getValue();

            System.out.println(key+": "+value);
        }
    }

    // Метод для печати количества позиций, в которых та или иная альтернатива является блокирующей
    public static void printBlockingLeaders(Map<String, Integer> m) {
        System.out.println();
        String message = "Механизм блокировки, вариант выбора и количество позиций, в которых он доминирует";
        System.out.println(message);
        System.out.println("--------------------------------------------------------------");

        Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            String key = pair.getKey();
            int value = pair.getValue();

            System.out.println(key + ": " + value);
        }
    }

    // Метод для печати исходной турнирной матрицы и матрицы с примененными коэффициентами
    public static void printMatrix(double[][] arr, int rows, int columns, boolean weightsApplied) {

        String message;

        if (false==weightsApplied) {
            message="\nМатрица турнирного механизма без примененных весовых коэффициентов";
        }

        else {
            message="\nМатрица турнирного механизма c примененными весовыми коэффициентами";
        }

        System.out.println(message);
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                System.out.printf("%10.3f", arr[i][j]);
            }

            System.out.println();

        }
    }

    // Метод для печати ранжированных результатов работы турнирного механизма
    public static void printTournMechLeaders(Map<String, Double> map) {
        System.out.println();
        String message = "Турнирный механизм: вариант выбора и количество баллов";
        System.out.println(message);
        System.out.println("--------------------------------------------------------------");

        Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Double> pair = it.next();
            String key = pair.getKey();
            double value = pair.getValue();

            System.out.println(key + ": " + value);
        }
    }

    // Метод для печати к-максимальных
    public static void printKMaximums(ArrayList<KMax> k1s, ArrayList<KMax> k2s, ArrayList<KMax> k3s, ArrayList<KMax> k4s) {
        System.out.println("\nК-максимальное, бинарное отношение, вариант выбора, количество баллов:");
        System.out.println("K1: "+ k1s.get(0).getBinaryRelationName() + ": ");

        for (KMax k:k1s) {
            System.out.println(k.getDecisionOptionName() + ": " + k.getK1());
        }

        System.out.println("\nK2: "+ k2s.get(0).getBinaryRelationName() + ": ");

        for (KMax k:k2s) {
            System.out.println(k.getDecisionOptionName() + ": " + k.getK2());
        }


        System.out.println("\nK3: "+ k3s.get(0).getBinaryRelationName() + ": ");

        for (KMax k:k3s) {
            System.out.println(k.getDecisionOptionName() + ": " + k.getK3());
        }


        System.out.println("\nK4: "+ k3s.get(0).getBinaryRelationName() + ": ");

        for (KMax k:k4s) {
            System.out.println(k.getDecisionOptionName() + ": " + k.getK4());
        }
    }

    // Метод для печати результатов проверки 1-макс. на оптимальность
    public static void printResultsForK1Check(boolean isMax, KMax k) {
        String message;

        if (isMax) {
            message="\nПеречисленные выше K1-максимальные варианты выбора являются максимальными по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S1R для них равен G";
        }

        else
            message="\nПеречисленные выше K1-максимальные варианты не являются максимальными по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S1R для не равен G";

        System.out.println(message);
    }

    // Метод для печати результатов проверки 2-макс. на оптимальность
    public static void printResultsForK2Check(boolean isStrictlyMax, KMax k) {
        String message;

        if (isStrictlyMax) {
            message="Перечисленные выше K2-максимальные варианты выбора являются строго максимальными по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S2R для них равен G-1";
        }

        else
            message="Перечисленные выше K2-максимальные варианты не являются строго максимальными по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S2R для них не равен G-1";

        System.out.println(message);
    }


    // Метод для печати результатов проверки 3-макс. на оптимальность
    public static void printResultsForK3Check(boolean isLargest, KMax k) {
        String message;

        if (isLargest) {
            message="Перечисленные выше K3-максимальные варианты выбора являются наибольшими по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S3R для них равен G";
        }

        else
            message="Перечисленные выше K3-максимальные варианты не являются наибольшими по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S3R для них не равен G";

        System.out.println(message);
    }


    // Метод для печати результатов проверки 4-макс. на оптимальность
    public static void printResultsForK4Check(boolean isK4StrictlyLargest, KMax k) {
        String message;

        if (isK4StrictlyLargest) {
            message="Перечисленные выше K4-максимальные варианты выбора являются строго наибольшими по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S3R для них равен G-1";
        }

        else
            message="Перечисленные выше K4-максимальные варианты не являются строго наибольшими по бинарному отношению " +
                    k.getBinaryRelationName() + ", поскольку S3R для них не равен G-1";

        System.out.println(message);
    }
}
