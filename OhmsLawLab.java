//Akgul Turtbayeva
//ID H00470195
public class OhmsLawLab {
    private Resistor resistor;
    private PowerSupply powerSupply;

    public OhmsLawLab(Resistor resistor, PowerSupply powerSupply) {
        this.resistor = resistor;
        this.powerSupply = powerSupply;
    }

    public double calculateCurrent() {
        return powerSupply.getVoltage() / resistor.getResistance();
    }
}

