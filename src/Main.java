public class Main {

    static Matrix appearance = new Matrix("appearance.txt");
    static Matrix price=new Matrix("price.txt");
    static Matrix nibMaterial=new Matrix("nib_material.txt");
    static Matrix fillingMechanism=new Matrix("filling_system.txt");
    static Matrix countryOfOrigin=new Matrix("country_of_origin.txt");
    static Matrix nibFlexebility=new Matrix("nib_flexibility.txt");
    static Matrix lineWidth=new Matrix("line_width.txt");
    static Matrix manufacturerStatus=new Matrix("manufacturer_status.txt");
    static Matrix limitedEdition=new Matrix("limited_edition.txt");

    public static void main(String[] args) {

        //Создаем объект класса DecisionMaker, в котором будут выполняться все вычисления
        DecisionMaker dm=new DecisionMaker();

        //Считываем список вариантов выбора
        dm.readOptions("decision_options.txt");

        //Находим лидеров по каждому параметру с помощью механизма доминирования
        findLeadersDominance(dm);

        //Добавляем матрицы бинарных отношений в один из массивов dm, чтобы найти лучшие варианты выбора с учетом всех параметров
        //с точки зрения механизма доминирования
        addBinaryRelationMatricesToDMDom(dm);

        //Находим те варианты выбора, которые являются лидерами по наибольшему числу параметров с точки зрения
        //механизма доминирования
        dm.getLeadersAcrossAllBrsDominance();

        //Находим наиболее предпочтительные альтернативы с помощью механизма блокировки по отдельным параметрам
        findLeadersBlock(dm);

        //Добавляем матрицы бинарных отношений в другой массив dm, чтобы найти лучшие варианты выбора с учетом всех параметров
        //с точки зрения механизма блокировки
        addBinaryRelationMatricesToDMBlock(dm);

        //Находим наиболее предпочтительные варианты выбора с точки зрения механизма блокировки по всем параметрам
        dm.getLeadersAcrossAllBrsBlock();

    }

    public static void addBinaryRelationMatricesToDMDom(DecisionMaker dm) {
        dm.addBrDom(appearance);
        dm.addBrDom(price);
        dm.addBrDom(nibMaterial);
        dm.addBrDom(fillingMechanism);
        dm.addBrDom(countryOfOrigin);
        dm.addBrDom(nibFlexebility);
        dm.addBrDom(lineWidth);
        dm.addBrDom(manufacturerStatus);
        dm.addBrDom(limitedEdition);
    }

    public static void findLeadersDominance(DecisionMaker dm) {
        dm.getDominance(appearance, "внешний вид");
        dm.getDominance(price, "цена");
        dm.getDominance(nibMaterial, "материал пера");
        dm.getDominance(fillingMechanism, "система заправки");
        dm.getDominance(countryOfOrigin, "страна производства");
        dm.getDominance(nibFlexebility, "гибкость пера");
        dm.getDominance(lineWidth, "толщина линии");
        dm.getDominance(manufacturerStatus, "статус производства");
        dm.getDominance(limitedEdition, "лимитированность издания");
    }

    public static void findLeadersBlock(DecisionMaker dm) {
        dm.getBlockingMech(appearance, "внешний вид");
        dm.getBlockingMech(price, "цена");
        dm.getBlockingMech(nibMaterial, "материал пера");
        dm.getBlockingMech(fillingMechanism, "система заправки");
        dm.getBlockingMech(countryOfOrigin, "страна производства");
        dm.getBlockingMech(nibFlexebility, "гибкость пера");
        dm.getBlockingMech(lineWidth, "толщина линии");
        dm.getBlockingMech(manufacturerStatus, "статус производства");
        dm.getBlockingMech(limitedEdition, "лимитированность издания");
    }

    public static void addBinaryRelationMatricesToDMBlock(DecisionMaker dm) {
        dm.addBrBlock(appearance);
        dm.addBrBlock(price);
        dm.addBrBlock(nibMaterial);
        dm.addBrBlock(fillingMechanism);
        dm.addBrBlock(countryOfOrigin);
        dm.addBrBlock(nibFlexebility);
        dm.addBrBlock(lineWidth);
        dm.addBrBlock(manufacturerStatus);
        dm.addBrBlock(limitedEdition);
    }
}
