package no.hvl.dowhile.core.gui;

import no.hvl.dowhile.core.Operation;
import no.hvl.dowhile.core.OperationManager;
import no.hvl.dowhile.core.TrackInfo;
import no.hvl.dowhile.utility.Messages;
import no.hvl.dowhile.utility.StringTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has an interface for configuring details related to one track.
 */
public class TrackPanel extends JPanel {
    private final OperationManager OPERATION_MANAGER;
    private final Window WINDOW;
    private JLabel operationInfoLabel;
    private JLabel currentImportLabel;
    private JLabel remainingFilesLabel;
    private GridBagConstraints constraints;
    private List<JRadioButton> radioButtons;
    private ButtonGroup radioButtonGroup;

    public TrackPanel(final OperationManager OPERATION_MANAGER, final Window WINDOW) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
        this.WINDOW = WINDOW;

        setLayout(new GridBagLayout());
        WINDOW.getContentPane().add(this, BorderLayout.NORTH);
        constraints = new GridBagConstraints();
        WINDOW.setConstraintsInsets(constraints, 5);

        // Header Label
        JLabel headerLabel = WINDOW.makeLabel(Messages.PROJECT_NAME.get(), WINDOW.HEADER_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 0);
        add(headerLabel, constraints);

        // Operation info label
        operationInfoLabel = WINDOW.makeLabel("<html><body>"
                        + Messages.OPERATION_INFO.get() + "<br>"
                        + Messages.OPERATION_INFO_NAME.get() + "Ingen operasjon." + "<br>"
                        + Messages.OPERATION_INFO_START.get() + "Ingen operasjon."
                        + "</body></html>",
                WINDOW.TEXT_FONT_SIZE
        );
        WINDOW.setConstraintsXY(constraints, 2, 0);
        constraints.gridwidth = 2;
        add(operationInfoLabel, constraints);

        // Current file imported from GPS
        String currentImportedFile = Messages.IMPORTED_FROM_GPS.get() + "Ingen fil.";
        currentImportLabel = WINDOW.makeLabel(currentImportedFile, WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 1);
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.WEST;
        add(currentImportLabel, constraints);

        // adding buttons
        radioButtonGroup = new ButtonGroup();
        constraints.gridwidth = 1;
        radioButtons = generateButtons(generateNames());
        setButtonsInWindow();

        // Label and input for team number
        JLabel crewNumberLabel = WINDOW.makeLabel(Messages.CREW_NUMBER.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 1, 3);
        constraints.gridwidth = 1;
        add(crewNumberLabel, constraints);

        SpinnerModel crewNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner groupNumberSpinner = new JSpinner(crewNumberInput);
        WINDOW.setConstraintsXY(constraints, 1, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(groupNumberSpinner, constraints);

        // Label and input for crew count
        JLabel crewCountLabel = WINDOW.makeLabel(Messages.CREW_COUNT.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 1, 5);
        add(crewCountLabel, constraints);

        SpinnerModel crewCountInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner crewCountSpinner = new JSpinner(crewCountInput);
        WINDOW.setConstraintsXY(constraints, 1, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(crewCountSpinner, constraints);

        // Label and input for area searched
        JLabel areaLabel = WINDOW.makeLabel(Messages.AREA_SEARCHED.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 3, 3);
        add(areaLabel, constraints);

        JTextField areaInput = new JTextField();
        WINDOW.setConstraintsXY(constraints, 3, 4);
        add(areaInput, constraints);

        // Label and input for track number
        JLabel trackNumberLabel = WINDOW.makeLabel(Messages.TRACK_NUMBER.get(), WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 3, 5);
        add(trackNumberLabel, constraints);

        SpinnerModel trackNumberInput = new SpinnerNumberModel(0, 0, 15, 1);
        JSpinner trackNumberSpinner = new JSpinner(trackNumberInput);
        WINDOW.setConstraintsXY(constraints, 3, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(trackNumberSpinner, constraints);

        // Register button
        JButton registerButton = new JButton(Messages.REGISTER_BUTTON.get());
        WINDOW.setConstraintsXY(constraints, 0, 9);
        constraints.gridwidth = 4;
        add(registerButton, constraints);

        // Remaining files imported from GPS waiting to be processed
        String remainingFiles = Messages.IMPORTED_FILES_LEFT_TO_PROCESS.get("" + 0);
        remainingFilesLabel = WINDOW.makeLabel(remainingFiles, WINDOW.TEXT_FONT_SIZE);
        WINDOW.setConstraintsXY(constraints, 0, 10);
        add(remainingFilesLabel, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetching the input data and sending it to the OperationManager
                String crew = getSelectedRadioButton();
                int crewCount = Integer.parseInt(crewCountSpinner.getModel().getValue().toString());
                int crewNumber = Integer.parseInt(groupNumberSpinner.getModel().getValue().toString());
                String areaSearched = areaInput.getText();
                int trackNumber = Integer.parseInt(trackNumberSpinner.getModel().getValue().toString());
                TrackInfo trackInfo = new TrackInfo(crew, crewCount, crewNumber, areaSearched, trackNumber);
                OPERATION_MANAGER.initiateTrackCutter(trackInfo);

                // Message to user
                String dialogText = Messages.SAVE_FILE.get() + crew + "_" + crewNumber + "_" + crewCount;
                JOptionPane.showMessageDialog(
                        JOptionPane.getRootFrame(),
                        dialogText);
            }
        });
    }

    /**
     * Updating the label with info about the operation.
     *
     * @param operation the current operation.
     */
    public void updateOperationInfo(Operation operation) {
        operationInfoLabel.setText("<html><body>"
                + Messages.OPERATION_INFO.get() + "<br>"
                + Messages.OPERATION_INFO_NAME.get() + operation.getName() + "<br>"
                + Messages.OPERATION_INFO_START.get() + StringTools.formatDate(operation.getStartTime())
                + "</body></html>"
        );
    }

    /**
     * Updating info about the file currently being processed.
     *
     * @param filename  the new filename.
     * @param filesLeft amount of files left after the current file.
     */
    public void updateCurrentFile(String filename, int filesLeft) {
        String currentImportedFile = Messages.IMPORTED_FROM_GPS.get() + filename;
        String remainingFiles = Messages.IMPORTED_FILES_LEFT_TO_PROCESS.get("" + filesLeft);
        currentImportLabel.setText(currentImportedFile);
        remainingFilesLabel.setText(remainingFiles);
    }

    /**
     * places the radio buttons in the given coordinates in the panel
     */
    private void setButtonsInWindow() {
        int startY = 3;
        int x = 0;

        for (JRadioButton rb : radioButtons) {
            WINDOW.setConstraintsXY(constraints, x, startY);
            constraints.anchor = GridBagConstraints.WEST;
            add(rb, constraints);

            startY++;
        }

    }

    /**
     * Generates the radio buttons
     *
     * @param crewNames List of strings with the names used on the radio buttons
     * @return a List with the radio buttons
     */
    private List<JRadioButton> generateButtons(List<String> crewNames) {
        List<JRadioButton> rbs = new ArrayList<>();

        JRadioButton radioButton = null;
        for (String n : crewNames) {
            radioButton = new JRadioButton(n);
            radioButton.setText(n);
            rbs.add(radioButton);
            radioButtonGroup.add(radioButton);
        }

        return rbs;
    }

    /**
     * Method for creating names that will be put in the radio buttons
     *
     * @return a List with the names
     */
    private List<String> generateNames() {
        List<String> crewNames = new ArrayList<>();
        crewNames.add("Lag");
        crewNames.add("Hund");
        crewNames.add("Bil");
        crewNames.add("ATV");
        crewNames.add("Helikopter");
        crewNames.add("Båt");
        return crewNames;
    }

    /**
     * Finds the selected radio button and returns it
     *
     * @return a String with the selected radio button
     */
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
