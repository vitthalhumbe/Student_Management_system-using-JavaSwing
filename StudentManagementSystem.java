/*
 * StudentManagementSystem - Java application for managing student information.
 * Author: Vitthal Humbe
 * Code availability: This code is inspired by educational materials and may be found in the book
 * "Programming with Java" by E. Balagurusamy. Specific code details may vary.
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

// Main class representing the Student Management System application
public class StudentManagementSystem {
    // Array representing column names for the table
    private String[] colName = new String[] {"Delete", "ID", "Name", "Age", "Standard"};

    // Initial data for the table (one row of sample data)
    private static Object[][] studentList = new Object[][] {{false, 1, "Vitthal", 18, 12}};

    // Table model and table component for managing student data
    private DefaultTableModel studentModel;
    private JTable studentTable;

    // Constructor for the main class
    private StudentManagementSystem() {
        // JFrame setup
        JFrame frame = new JFrame("Student Management System");
        frame.setBounds(100, 100, 500, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Creating tabs for different functionalities
        JTabbedPane tabs = new JTabbedPane();
        CreateUpdateStudentPanel createUpdateStudentPanel = new CreateUpdateStudentPanel();
        ReadDeleteStudentPanel readDeleteStudentPanel = new ReadDeleteStudentPanel();

        // Adding tabs to the tabbed pane
        tabs.addTab("Create Student", createUpdateStudentPanel);
        tabs.setMnemonicAt(0, KeyEvent.VK_1);

        tabs.addTab("Read/Delete Student", readDeleteStudentPanel);
        tabs.setMnemonicAt(1, KeyEvent.VK_1);

        // Setting the content pane of the frame to the tabbed pane
        frame.setContentPane(tabs);
        frame.setVisible(true);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        new StudentManagementSystem();
    }

    // Utility method to add a labeled text field to a container
    private static JTextField addLabelAndTextField(String labelText, int yPos, Container containingPanel) {
        // Label creation and positioning
        JLabel label = new JLabel(labelText);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = yPos;
        containingPanel.add(label, gridBagConstraints);

        // Text field creation and positioning
        JTextField textField = new JTextField();
        GridBagConstraints gridBagConstraintsForTextField = new GridBagConstraints();
        gridBagConstraintsForTextField.fill = GridBagConstraints.BOTH;
        gridBagConstraintsForTextField.insets = new Insets(0, 0, 5, 0);
        gridBagConstraintsForTextField.gridx = 1;
        gridBagConstraintsForTextField.gridy = yPos;
        containingPanel.add(textField, gridBagConstraintsForTextField);
        textField.setColumns(10);
        return textField;
    }

    // Utility method to add a button to a container
    private static JButton addButton(String btnText, int yPos, Container containingPanel) {
        // Button creation and positioning
        JButton button = new JButton(btnText);
        GridBagConstraints gridBagConstraintsForButton = new GridBagConstraints();
        gridBagConstraintsForButton.fill = GridBagConstraints.BOTH;
        gridBagConstraintsForButton.insets = new Insets(0, 0, 5, 5);
        gridBagConstraintsForButton.gridx = 0;
        gridBagConstraintsForButton.gridy = yPos;
        containingPanel.add(button, gridBagConstraintsForButton);
        return button;
    }

    // Utility method to append a row to a 2D array
    private static void append(Object[][] array, Object[] values) {
        Object[][] result = Arrays.copyOf(array, array.length+1);
        result[result.length-1] = values;
        StudentManagementSystem.studentList = result;
    }

    // Panel for creating and updating student information
    public class CreateUpdateStudentPanel extends JPanel {
        // Text fields for input
        private JTextField idTextField, nameTextField, ageTextField, stdTextField;

        // Constructor for the CreateUpdateStudentPanel
        CreateUpdateStudentPanel() {
            // Setting up borders and layout for the panel
            Border border = getBorder();
            Border margin = new EmptyBorder(10, 10, 10, 10);
            setBorder(new CompoundBorder(border, margin));

            GridBagLayout panelGridBagLayout = new GridBagLayout();
            panelGridBagLayout.columnWidths = new int[] {86, 86, 0};
            panelGridBagLayout.rowHeights = new int[] {20, 20, 20, 20, 20, 0};
            panelGridBagLayout.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
            panelGridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            setLayout(panelGridBagLayout);

            // Adding labeled text fields for input
            idTextField = StudentManagementSystem.addLabelAndTextField("ID :", 0, this);
            nameTextField = StudentManagementSystem.addLabelAndTextField("Name :", 1, this);
            ageTextField = StudentManagementSystem.addLabelAndTextField("Age :", 2, this);
            stdTextField = StudentManagementSystem.addLabelAndTextField("Standard :", 3, this);

            // Adding a button for creating a student with associated action
            JButton createStudentButton = StudentManagementSystem.addButton("Create", 4, this);
            createStudentButton.addActionListener(e -> createStudent());
        }

        // Method to create a student based on input and update the UI
        private void createStudent() {
            // Retrieving input from text fields
            String studentId = idTextField.getText();
            String studentName = nameTextField.getText();
            String studentAge = ageTextField.getText();
            String studentStd = stdTextField.getText();

            // Creating an array of student data
            Object[] studentData = new Object[] {false, studentId, studentName, studentAge, studentStd};

            // Appending the data to the main student list
            StudentManagementSystem.append(StudentManagementSystem.studentList, studentData);

            // Updating the table model to reflect the new data
            studentModel.addRow(studentData);

            // Clearing text fields after creating a student
            idTextField.setText("");
            nameTextField.setText("");
            ageTextField.setText("");
            stdTextField.setText("");
        }
    }

    // Panel for reading and deleting student information
    public class ReadDeleteStudentPanel extends JPanel {
        // Constructor for the ReadDeleteStudentPanel
        ReadDeleteStudentPanel() {
            setPreferredSize(new Dimension(400, 100));

            // Adding a button for deleting selected students with associated action
            JButton deleteButton = StudentManagementSystem.addButton("Delete", 0, this);
            deleteButton.addActionListener(e -> deleteStudent());

            // Creating a table with a custom model for displaying student data
            studentModel = new DefaultTableModel(StudentManagementSystem.studentList, colName);
            studentTable = new JTable(studentModel) {

                @Override
                public Class<?> getColumnClass(int column) {
                    // Specifying the column classes for proper rendering
                    switch (column) {
                        case 0:
                            return Boolean.class;
                        case 1:
                            return String.class;
                        case 2:
                            return String.class;
                        case 3:
                            return String.class;
                        case 4:
                            return String.class;
                        default:
                            return Boolean.class;
                    }
                }
            };

            // Setting auto-resize mode and adding the table to a scroll pane
            studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane pane = new JScrollPane(studentTable);
            add(pane, BorderLayout.CENTER);
        }

        // Method to delete selected students from the table
        private void deleteStudent() {
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();

            // Iterating through rows to find and remove selected students
            for (int i = 0; i < model.getRowCount(); i++) {
                Boolean checked = (Boolean) model.getValueAt(i, 0);
                if (checked != null && checked) {
                    model.removeRow(i);
                    i--; // Decrementing the counter to avoid skipping rows after removal
                }
            }
        }
    }
}
