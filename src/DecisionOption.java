public class DecisionOption {
    private String name;
    private int nLeadingParametersDominance=0;
    private int nLeadingParametersBlock=0;

    public DecisionOption(String s) {
        this.name=s;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    public void setnLeadingParametersDominance(int i) {
        this.nLeadingParametersDominance=i;
    }

    public int getnLeadingParametersDominance() {
        return this.nLeadingParametersDominance;
    }

    public void setnLeadingParametersBlock(int i) {
        this.nLeadingParametersDominance=i;
    }

    public int getnLeadingParametersBlock() {
        return this.nLeadingParametersBlock;
    }
}
