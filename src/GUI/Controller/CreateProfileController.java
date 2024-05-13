package GUI.Controller;

import BE.Country;
import BE.ProfileRole;
import BE.ProjectTeam;
import GUI.Model.ProfileRoleModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import BE.Profile;
import GUI.Model.ProfileModel;
import GUI.Model.ProjectTeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import GUI.Model.CountryModel;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CreateProfileController implements Initializable {

    public TextField txtDailyWorkingHours;
    // slider for overhead and utilization and textField to show the value of the slider
    @FXML
    private MFXSlider sliderOverhead, sliderUtilization;


    // textFields for the profile name, annual salary, fixed amount and effective hours
    @FXML
    private TextField txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary, txtFixedAmount, txtEffectiveHours, txtOverheadView;

    @FXML
    private CheckBox checkOverhead, checkProduction;

    @FXML
    private Label lblHourlyResult, lblDailyResult, lblShowMassage, lblFixedAmountResult;

    // comboBox for country and team
    public ComboBox<String> cBoxTeam_CreateProfile;




    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private ProfileModel profileModel;
    private ProfileRoleModel profileRoleModel;


    @FXML
    private ComboBox<String> cBoxValuateProfile;


    public CreateProfileController(){
        try {
            countryModel = new CountryModel();
            projectTeamsModel = new ProjectTeamsModel();
            profileModel = new ProfileModel();
            profileRoleModel = new ProfileRoleModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is called after all @FXML annotated members have been injected.
     * It sets up listeners on sliders and checkboxes, and populates the country and team ComboBoxes.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listenersOnSliders();
        setupCheckboxListeners();
        setupListenersOnTextFields();
        populateCountryCurrencyComboBox();
        setupComboBoxCustomization();
        try {
            cBoxTeam_CreateProfile.setItems(profileRoleModel.seeAllProfileRoles());



            //cBoxTeam_CreateProfile.setItems(profileModel.getRoleList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateCountryCurrencyComboBox() {
        ObservableList<String> options = profileModel.getCountryAndCurrencyCodes();
        cBoxValuateProfile.setItems(options);
    }

    private void setupComboBoxCustomization() {
        // Configure how the list items are displayed in the dropdown
        cBoxValuateProfile.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            // Show both country and currency code in the drop-down list
                            setText(item);
                        }
                    }
                };
            }
        });

        // Configure how the selected item is displayed in the ComboBox when it is not expanded
        cBoxValuateProfile.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    // Show only the currency code when an item is selected
                    setText(item.split(" - ")[1]);
                }
            }
        });
    }

    /**
     * This method sets up listeners on the overhead and utilization sliders.
     * When the value of a slider changes, the corresponding TextField is updated with the new value.
     */
    public void listenersOnSliders() {
        sliderOverhead.valueProperty().addListener((observable, oldValue, newValue) -> {
            txtOverheadView.setText(String.format("%.2f", newValue));
        });
    }

    /**
     * This method sets up listeners on the overhead and production checkboxes.
     * When one checkbox is selected, the other one is deselected.
     */
    private void setupCheckboxListeners() {
        checkOverhead.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkProduction.setSelected(false);
            }
        });

        checkProduction.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkOverhead.setSelected(false);
            }
        });
    }

    private void setupListenersOnTextFields() {
        ChangeListener<String> textFieldListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            calculateAndSetHourlyRateCreateProfile();
            calculateAndSetDailyRateCreateProdifle();
        };

        txtFixedAmount.textProperty().addListener(textFieldListener);
        txtEffectiveHours.textProperty().addListener(textFieldListener);
        txtAnnualSalary.textProperty().addListener(textFieldListener);
        txtOverheadView.textProperty().addListener(textFieldListener);
        txtDailyWorkingHours.textProperty().addListener(textFieldListener);
    }



    /**
     * This method initializes fields, ComboBoxes, and listeners. with the values from the database.
     * getAllCountries() and getAllProjectTeams() are methods read from the database.
     */
    @FXML
    public void initialize() {
        try {

            cBoxTeam_CreateProfile.setItems(projectTeamsModel.getAllProjectTeams());

            ChangeListener<String> textFieldListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            };

            txtFixedAmount.textProperty().addListener(textFieldListener);
            lblHourlyResult.textProperty().addListener(textFieldListener);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



     //This method is called when the save button is clicked.
     //It validates the input, creates a new Profile object, and saves it to the database.
    @FXML
    private void saveProfileToDatabase(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }
        String firstName = txtFirstnameProfile.getText();
        String lastName = txtLastnameProfile.getText();
        double annualSalary = Double.parseDouble(txtAnnualSalary.getText());
        //String projectRole = cBoxTeam_CreateProfile.getValue();
        double hourlyResult = Double.parseDouble(lblHourlyResult.getText());
        double dailyResult = Double.parseDouble(lblDailyResult.getText());
        boolean overheadCost = checkOverhead.isSelected();
        double fixedAmount = Double.parseDouble(txtFixedAmount.getText());
        double dailyWorkingHours = Double.parseDouble(txtDailyWorkingHours.getText());

        Profile newProfile = new Profile(firstName, lastName, overheadCost,
                annualSalary, hourlyResult, dailyResult, fixedAmount, dailyWorkingHours);

        profileModel.saveProfile(newProfile);

        lblShowMassage.setText("Profile has been saved");
    }


    public double calculateAndSetHourlyRateCreateProfile() {
        if (txtAnnualSalary.getText().isEmpty() || txtOverheadView.getText().isEmpty() || txtFixedAmount.getText().isEmpty()
                || txtEffectiveHours.getText().isEmpty()) {
            return 0.0;
        }

        double annualSalaryProfile = Double.parseDouble(txtAnnualSalary.getText().replace(",", "."));
        double overheadMultiplierProfile = Double.parseDouble(txtOverheadView.getText().replace(",", "."));
        double annualFixedAmountProfile = Double.parseDouble(txtFixedAmount.getText().replace(",", "."));
        double effectiveHoursProfile = Double.parseDouble(txtEffectiveHours.getText().replace(",", "."));
        double result = profileModel.calculateAndSetHourlyRateCreateProfile(annualSalaryProfile,
                overheadMultiplierProfile,
                annualFixedAmountProfile,
                effectiveHoursProfile);
        lblHourlyResult.setText(String.format("%.2f", result));
        return result;
    }

    public double calculateAndSetDailyRateCreateProdifle() {
        if (txtDailyWorkingHours.getText().isEmpty() || lblHourlyResult.getText().isEmpty()) {
            return 0.0;
        }

        double dailyWorkingHours = Double.parseDouble(txtDailyWorkingHours.getText().replace(",", "."));
        double hourlyRate = Double.parseDouble(lblHourlyResult.getText().replace(",", "."));
        double result = profileModel.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
        lblDailyResult.setText(String.format("%.2f", result));
        return result;
    }




    /**
     * This method validates the input in the TextFields and ComboBoxes.
     * If a field is empty, it is highlighted in red and the method returns false.
     * If all fields are filled, the method returns true.
     */
    private boolean validateInput() {
        boolean isValid = true;

        // List of all TextFields and ComboBoxes
        List<Control> fields = Arrays.asList(txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary,txtFixedAmount,txtDailyWorkingHours,txtEffectiveHours, cBoxTeam_CreateProfile );

        for (Control field : fields) {
            if (field instanceof TextField) {
                TextField textField = (TextField) field;
                if (textField.getText().isEmpty()) {
                    textField.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    textField.setStyle("");
                }
            } else if (field instanceof ComboBox) {
                ComboBox<?> comboBox = (ComboBox<?>) field;
                if (comboBox.getValue() == null) {
                    comboBox.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    comboBox.setStyle("");
                }
            }
        }

        return isValid;
    }

}

