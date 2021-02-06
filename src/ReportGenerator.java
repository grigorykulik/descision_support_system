import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ReportGenerator {
    public static void printLeadersByBrsDominanceOrBlocking(String parameterName, ArrayList<Integer> indices,
                                                        ArrayList<DecisionOption> decisionOptions,
                                                        Matrix m, Mode mode) {
        String message=new String();

        switch (mode) {
            case DOM:
                message="Механизм доминирования, оптимальный выбор по параметру " + parameterName+".";
                break;

            case BLOCK:
                message="Механизм блокировки, оптимальный выбор по параметру " + parameterName+".";
                break;
        }

        System.out.println(message);
        System.out.println("--------------------------------------------------------------------");
        for (Integer mi:indices) {
            System.out.println(decisionOptions.get(mi).getName());
        }
        System.out.println();

    }

    public static void printLeadersAcrossBrs(Map<String, Integer> map, Mode mode) {
        String message=new String();

        switch (mode){
            case DOM:
                message="Механизм доминирования: наиболее предпочтительные альтернативы и количество параметров в которых эти альтернативы лидируют";
                break;

            case BLOCK:
                message="Механизм блокировки: наиболее предпочтительные альтернативы и количество параметров в которых эти альтернативы лидируют";
                break;
        }


        Iterator<Map.Entry<String, Integer>> it=map.entrySet().iterator();

        System.out.println(message);
        System.out.println("--------------------------------------------------------------------");

        while (it.hasNext()) {
            Map.Entry<String, Integer> pair=it.next();
            String key=pair.getKey();
            Integer value=pair.getValue();

            System.out.println(key+": "+value);
        }
    }
}
