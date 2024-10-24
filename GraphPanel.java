
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<Double> voltages;
    private List<Double> currents;

    public GraphPanel(List<Double> voltages, List<Double> currents) {
        this.voltages = voltages;
        this.currents = currents;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // Set up colors and fonts
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        // Draw axes
        g2.drawLine(50, height - 50, width - 50, height - 50); // X axis
        g2.drawLine(50, height - 50, 50, 50); // Y axis

        // Draw axis labels
        g2.drawString("Voltage (V)", width - 80, height - 20);
        g2.drawString("Current (A)", 20, 50);

        // Calculate max values for scaling
        double maxVoltage = voltages.size() > 0 ? voltages.stream().max(Double::compare).get() : 1;
        double maxCurrent = currents.size() > 0 ? currents.stream().max(Double::compare).get() : 1;

        // Draw grid lines
        g2.setColor(Color.LIGHT_GRAY);
        int tickSpacingX = (width - 100) / 10;
        for (int i = 1; i <= 10; i++) {
            int x = 50 + i * tickSpacingX;
            g2.drawLine(x, height - 50, x, 50);
        }

        int tickSpacingY = (height - 100) / 10;
        for (int i = 1; i <= 10; i++) {
            int y = height - 50 - i * tickSpacingY;
            g2.drawLine(50, y, width - 50, y);
        }

        // Draw ticks on X axis
        g2.setColor(Color.BLACK);
        for (int i = 0; i <= 10; i++) {
            int x = 50 + i * tickSpacingX;
            g2.drawLine(x, height - 50, x, height - 45);
            String tickLabel = String.format("%.1f", i * (maxVoltage / 10));
            g2.drawString(tickLabel, x - 10, height - 30);
        }

        // Draw ticks on Y axis
        for (int i = 0; i <= 10; i++) {
            int y = height - 50 - i * tickSpacingY;
            g2.drawLine(50, y, 55, y);
            String tickLabel = String.format("%.1f", i * (maxCurrent / 10));
            g2.drawString(tickLabel, 10, y + 5);
        }

        // Draw data points and lines
        if (voltages.size() > 0) {
            g2.setColor(Color.BLUE);
            for (int i = 0; i < voltages.size(); i++) {
                int x = (int) (50 + (voltages.get(i) / maxVoltage) * (width - 100));
                int y = (int) ((height - 50) - (currents.get(i) / maxCurrent) * (height - 100));
                g2.fillOval(x - 5, y - 5, 10, 10);
                g2.drawString("(" + voltages.get(i) + ", " + currents.get(i) + ")", x + 5, y - 5);

                if (i > 0) {
                    int prevX = (int) (50 + (voltages.get(i - 1) / maxVoltage) * (width - 100));
                    int prevY = (int) ((height - 50) - (currents.get(i - 1) / maxCurrent) * (height - 100));
                    g2.drawLine(prevX, prevY, x, y);
                }
            }
        }
    }
}
