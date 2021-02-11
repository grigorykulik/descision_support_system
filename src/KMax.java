public class KMax {
    // Название бинарного отношения, которому соответствует объект
    private String binaryRelationName;

    // Название варианта выбора, которому соответствует объект
    private String decisionOptionName;

    // Количество строго подчиненных x по R, эквивалентных x по R и несравнимых с x по R
    // для данного бинарного отношения и данного варианта выбора
    private int hor=0;
    private int er=0;
    private int nr=0;

    // Поля для сумм H0R, ER и NR
    private int k1=0;
    private int k2=0;
    private int k3=0;
    private int k4=0;

    // Конструктор
    public KMax(String binaryRelationName, String decisionOptionName) {
        this.binaryRelationName=binaryRelationName;
        this.decisionOptionName=decisionOptionName;
    }

    // Сеттеры и геттеры
    public String getBinaryRelationName() {
        return binaryRelationName;
    }

    public void setBinaryRelationName(String binaryRelationName) {
        this.binaryRelationName = binaryRelationName;
    }

    public String getDecisionOptionName() {
        return decisionOptionName;
    }

    public void setDecisionOptionName(String decisionOptionName) {
        this.decisionOptionName = decisionOptionName;
    }

    public int getK1() {
        return k1;
    }

    public void setK1(int k1) {
        this.k1 = k1;
    }

    public int getK2() {
        return k2;
    }

    public void setK2(int k2) {
        this.k2 = k2;
    }

    public int getK3() {
        return k3;
    }

    public void setK3(int k3) {
        this.k3 = k3;
    }

    public int getK4() {
        return k4;
    }

    public void setK4(int k4) {
        this.k4 = k4;
    }

    public void setHor(int hor) {
        this.hor = hor;
    }

    public int getHor() {
        return this.hor;
    }

    public void setEr(int er) {
        this.er=er;
    }

    public int getEr() {
        return this.er;
    }

    public void setNr(int nr) {
        this.nr=nr;
    }

    public int getNr() {
        return this.nr;
    }
}
