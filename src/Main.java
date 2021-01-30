import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        DecisionMaker dm=new DecisionMaker();
        dm.readOptions("decision_options.txt");

        Matrix appearance = new Matrix("appearance.txt");
        dm.getDominance(appearance, "внешний вид");

        System.out.println();

        Matrix price=new Matrix("price.txt");
        dm.getDominance(price, "цена");

        System.out.println();

        Matrix nibMaterial=new Matrix("nib_material.txt");
        dm.getDominance(nibMaterial, "материал пера");

        System.out.println();

        Matrix fillingMechanism=new Matrix("filling_system.txt");
        dm.getDominance(fillingMechanism, "система заправки");

        System.out.println();
        Matrix countryOfOrigin=new Matrix("country_of_origin.txt");
        dm.getDominance(countryOfOrigin, "страна производства");

        System.out.println();
        Matrix nibFlexebility=new Matrix("nib_flexibility.txt");
        dm.getDominance(nibFlexebility, "гибкость пера");


    }
}
