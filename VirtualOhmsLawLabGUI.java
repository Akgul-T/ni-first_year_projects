
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirtualOhmsLawLabGUI extends JFrame {
    // Buttons for user interactions
    private JButton calculateButton;
    private JButton saveButton;
    private JButton showGraphButton;

    // Table to display results
    private JTable resultTable;
    private DefaultTableModel tableModel;

    // Slider for voltage input
    private JSlider voltageSlider;

    // Radio buttons for selecting resistance values
    private JRadioButton[] resistanceButtons;
    private ButtonGroup resistanceGroup;

    // Lists to store voltage and current values for graphing
    private List<Double> voltages;
    private List<Double> currents;

    public VirtualOhmsLawLabGUI() {
        // Initialize lists to store voltage and current values
        voltages = new ArrayList<>();
        currents = new ArrayList<>();

        // Set the title and layout of the main frame
        setTitle("Virtual Ohm's Law Lab");
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 230, 250)); // Light background color for the frame

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(230, 230, 250)); // Match panel background with frame

        // Voltage label and slider
        JLabel voltageLabel = new JLabel("Voltage (V):");
        voltageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(voltageLabel);

        voltageSlider = new JSlider(0, 1000, 1);
        voltageSlider.setMajorTickSpacing(200);
        voltageSlider.setPaintTicks(true);
        voltageSlider.setPaintLabels(true);
        voltageSlider.setBackground(new Color(230, 230, 250));
        inputPanel.add(voltageSlider);

        // Resistance label and radio buttons
        JLabel resistanceLabel = new JLabel("Resistance (Ω):");
        resistanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(resistanceLabel);

        // Create radio buttons for different resistance values
        resistanceButtons = new JRadioButton[]{
                new JRadioButton("5 Ohms"),
                new JRadioButton("10 Ohms"),
                new JRadioButton("50 Ohms")
        };

        // Group radio buttons to allow only one selection
        resistanceGroup = new ButtonGroup();
        JPanel resistancePanel = new JPanel(new GridLayout(1, 3));
        resistancePanel.setBackground(new Color(230, 230, 250));
        for (JRadioButton button : resistanceButtons) {
            button.setBackground(new Color(230, 230, 250));
            resistanceGroup.add(button);
            resistancePanel.add(button);
        }
        resistanceButtons[0].setSelected(true); // Default selection
        inputPanel.add(resistancePanel);

        add(inputPanel, BorderLayout.NORTH); // Add input panel to the top of the frame

        // Table panel setup
        String[] columnNames = {"Voltage (V)", "Current (A)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        resultTable.setBackground(new Color(245, 245, 255)); // Light background for the table
        add(new JScrollPane(resultTable), BorderLayout.CENTER); // Add table to the center of the frame

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 230, 250));
        calculateButton = new JButton("Calculate");
        saveButton = new JButton("Save to File");
        showGraphButton = new JButton("Show Graph");
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(showGraphButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the frame

        // Button actions
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCurrent(); // Calculate current based on input and add to table
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResultsToFile(); // Save table data to a text file
            }
        });

        showGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGraph(); // Display graph in a new window
            }
        });

        // Set frame size, default close operation, and make it visible
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to get the selected resistance value
    private double getSelectedResistance() {
        for (int i = 0; i < resistanceButtons.length; i++) {
            if (resistanceButtons[i].isSelected()) {
                switch (i) {
                    case 0:
                        return 5.0;
                    case 1:
                        return 10.0;
                    case 2:
                        return 50.0;
                }
            }
        }
        return 5; // Default resistance if no button is selected
    }

    // Method to calculate the current based on voltage and resistance
    private void calculateCurrent() {
        try {
            double voltage = voltageSlider.getValue();
            double resistance = getSelectedResistance();
            Resistor resistor = new Resistor(resistance);
            PowerSupply powerSupply = new PowerSupply();
            powerSupply.setVoltage(voltage);
            OhmsLawLab lab = new OhmsLawLab(resistor, powerSupply);
            double current = lab.calculateCurrent();

            // Add result to the table and lists
            tableModel.addRow(new Object[]{voltage, current});
            voltages.add(voltage);
            currents.add(current);
        } catch (NumberFormatException ex) {
            // Show error message if input is invalid
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for voltage and resistance.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save the results from the table to a text file
    private void saveResultsToFile() {
        try (FileWriter writer = new FileWriter("results.txt")) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write("Voltage: " + tableModel.getValueAt(i, 0) + " V, Current: " + tableModel.getValueAt(i, 1) + " A\n");
            }
            // Show success message after saving
            JOptionPane.showMessageDialog(this, "Results saved to results.txt", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // Show error message if file saving fails
            JOptionPane.showMessageDialog(this, "Error saving results to file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to show the graph in a new window
    private void showGraph() {
        JFrame graphFrame = new JFrame("Graph");
        graphFrame.setSize(500, 400);
        graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GraphPanel graphPanel = new GraphPanel(voltages, currents);
        graphFrame.add(graphPanel);
        graphFrame.setVisible(true);
    }

    // Main method to start the application
    public static void main(String[] args) {
        new VirtualOhmsLawLabGUI();
    }
}
