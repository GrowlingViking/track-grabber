package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Window exposed to the user for configuring the application.
 * Responsible of laying out elements and handling user interaction.
 */
public class Window extends JFrame {
    private final OperationManager OPERATION_MANAGER;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JLabel operationStartedLabel;
    private JLabel crewNumberLabel;
    private JLabel crewCountLabel;
    private JPanel panel;
    private SpinnerModel crewNumberInput;
    private SpinnerModel crewCountInput;
    private GridBagConstraints constraints;
    private List<JRadioButton> radioButtons;
    private ButtonGroup crewGroup;

    public Window(final OperationManager OPERATION_MANAGER, String date) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        crewGroup = new ButtonGroup();
        radioButtons = generateButtons(generateNames());

        setTitle(Messages.PROJECT_NAME.get());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        panel = new JPanel(new GridBagLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        constraints = new GridBagConstraints();
        setConstraintsInsets(5);

        // Header Label
        headerLabel = new JLabel();
        headerLabel.setText(Messages.PROJECT_NAME.get());
        headerLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 24));
        setConstraintsXY(0, 0);
        panel.add(headerLabel, constraints);

        // Operation started label
        operationStartedLabel = new JLabel();
        operationStartedLabel.setText("<html><body>" + Messages.OPERATION_STARTED.get() + "<br>" + date + "</body></html>");
        operationStartedLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(0, 1);
        constraints.gridwidth = 2;
        panel.add(operationStartedLabel, constraints);

        // isConnected label
        statusLabel = new JLabel();
        statusLabel.setText(Messages.GPS_OFFLINE.get());
        statusLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 1);
        constraints.anchor = GridBagConstraints.NORTH;
        panel.add(statusLabel, constraints);

        // adding them buttons
        setButtonsInWindow();

        // Label and input for team number
        crewNumberLabel = new JLabel();
        crewNumberLabel.setText(Messages.CREW_NUMBER.get());
        crewNumberLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 2);
        panel.add(crewNumberLabel, constraints);

        crewNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner groupNumberSpinner = new JSpinner(crewNumberInput);
        setConstraintsXY(2, 3);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(groupNumberSpinner, constraints);

        // Label for crew count
        crewCountLabel = new JLabel();
        crewCountLabel.setText(Messages.CREW_COUNT.get());
        crewCountLabel.setFont(new Font(Messages.FONT_NAME.get(), Font.PLAIN, 16));
        setConstraintsXY(2, 4);
        panel.add(crewCountLabel, constraints);

        crewCountInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner crewCountSpinner = new JSpinner(crewCountInput);
        setConstraintsXY(2, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(crewCountSpinner, constraints);

        // Register button
        JButton registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        setConstraintsXY(2, 6);
        constraints.gridwidth = 2;
        panel.add(registerButton, constraints);

        // Listener for when the window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), Messages.CONFIRM_EXIT.get(), Messages.PROJECT_NAME.get(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String crew = getSelectedRadioButton();
                String crewCount = crewCountSpinner.getModel().getValue().toString();
                String crewNumber = groupNumberSpinner.getModel().getValue().toString();
                TrackInfo trackInfo = new TrackInfo();
                trackInfo.setTrackName(crew);
                trackInfo.setCrewCount(Integer.parseInt(crewCount));
                trackInfo.setCrewNumber(Integer.parseInt(crewNumber));
                OPERATION_MANAGER.initiateTrackCutter(trackInfo);
                String dialogText = Messages.SAVE_FILE.get() + crew + "_" + crewNumber + "_" + crewCount;

                JOptionPane.showMessageDialog(
                        JOptionPane.getRootFrame(),
                        dialogText);
            }
        });
    }

    // Method for opening the window
    public void open() {
        setVisible(true);
    }

    // Method for setting the status of whether or not the gps is connected
    public void setStatus(String status) {
        statusLabel.setText("GPS: " + status);
    }

    // Setting the constraints for x and y coordinates
    private void setConstraintsXY(int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
    }

    // Setting the insets of the constraints
    private void setConstraintsInsets(int borders) {
        constraints.insets = new Insets(borders, borders, borders, borders);
    }

    // Creates a radio button and a label a places them in the given coordinates
    private void setButtonsInWindow() {
        int startY = 2;
        int x = 0;

        for (JRadioButton rb : radioButtons) {
            setConstraintsXY(x, startY);
            constraints.anchor = GridBagConstraints.WEST;
            panel.add(rb, constraints);

            startY++;
        }

    }

    private List<JRadioButton> generateButtons(List<String> crewNames) {
        List<JRadioButton> rbs = new ArrayList<JRadioButton>();

        JRadioButton radioButton = null;
        for (String n : crewNames) {
            radioButton = new JRadioButton(n);
            radioButton.setText(n);
            rbs.add(radioButton);
            crewGroup.add(radioButton);
        }

        return rbs;
    }

    private List<String> generateNames() {
        List<String> crewNames = new ArrayList<String>();
        crewNames.add("Mannskap");
        crewNames.add("Hund");
        crewNames.add("Bil");
        crewNames.add("Sjørøver");
        crewNames.add("Helikopter");
        crewNames.add("Etc.");
        return crewNames;
    }

    private String getSelectedRadioButton() {
        String chosenRadioButton = "";
        for (JRadioButton rb : radioButtons) {
            if (rb.isSelected()) {
                chosenRadioButton = rb.getText();
            }
        }
        return chosenRadioButton;
    }
}