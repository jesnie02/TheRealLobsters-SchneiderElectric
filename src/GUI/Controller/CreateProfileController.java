package GUI.Controller;


import BE.Currency;
import BE.ProfileRole;
import CustomExceptions.ApplicationWideException;
import GUI.Model.*;

import GUI.Utility.ExceptionHandler;
import GUI.Utility.SliderDecimalFilter;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import BE.Profile;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class CreateProfileController implements Initializable {

    public TextField txtDailyWorkingHours;

    // slider for overhead  and textField to show the value of the slider
    @FXML
    private MFXSlider sliderOverhead;

    // textFields for the profile name, annual salary, fixed amount and effective hours
    @FXML
    private TextField txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary, txtFixedAmount, txtEffectiveHours, txtOverheadView;

    @FXML
    private CheckBox checkOverhead, checkProduction;

    @FXML
    private Label lblHourlyResult, lblDailyResult, lblShowMassage;
    @FXML
    public Label employeeHourlyRateCurrency, employeeDailyRateCurrency;
    @FXML
    public Label lblDailyResultInEUR, lblAnnualResultEUR, lblHourlyResultEUR;


    // comboBox for country and team



    private CountryModel countryModel;
    private ProjectTeamsModel projectTeamsModel;
    private ProfileModel profileModel;
    private ProfileRoleModel profileRoleModel;
    private CurrencyModel currencyModel;


    @FXML
    private ComboBox<Currency> cBox_Currency;
    @FXML
    private CheckComboBox cBoxProfile_ProfileRoles;


    public CreateProfileController() {
        try {
            countryModel = new CountryModel();
            projectTeamsModel = new ProjectTeamsModel();
            profileModel = new ProfileModel();
            profileRoleModel = new ProfileRoleModel();
            currencyModel = new CurrencyModel();
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }

    /**
     * This method is called after all @FXML annotated members have been injected.
     * It sets up listeners on sliders and checkboxes, and populates the country and team ComboBoxes.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCheckboxListeners();
        setupListenersOnTextFields();
        populateCountryCurrencyComboBox();
        setupRegex();
        populateProfileRolesComboBox();
        bindSliderAndTextField();
    }

    private void populateProfileRolesComboBox() {
        cBoxProfile_ProfileRoles.getItems().addAll(profileRoleModel.getProfileRoles());
    }

    private void populateCountryCurrencyComboBox() {
        ObservableList<Currency> currencies = currencyModel.getCurrencies();
        cBox_Currency.setItems(currencies);

       // Set default currency to EUR if available, otherwise default to first currency in list
        Currency defaultCurrency = currencies.stream()
                .filter(currency -> currency.toString().equals("EUR"))
                .findFirst()
                .orElse(null);


        if (defaultCurrency == null) {
            ExceptionHandler.handleException(new ApplicationWideException("EUR currency not available. Defaulting to another currency."));
            defaultCurrency = currencies.get(0);
        }

        cBox_Currency.getSelectionModel().select(defaultCurrency);
        if (defaultCurrency != null) {
            employeeHourlyRateCurrency.setText(defaultCurrency.toString());
            employeeDailyRateCurrency.setText(defaultCurrency.toString());
        }

        // Listener to update labels when currency changes
        cBox_Currency.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String currencyString = newValue.toString();
                employeeHourlyRateCurrency.setText(currencyString);
                employeeDailyRateCurrency.setText(currencyString);
            }
        });
    }

    //This method binds the slider and the textfield so that the value of the slider is shown in the textfield.
    public void bindSliderAndTextField() {
        SliderDecimalFilter filter = new SliderDecimalFilter();
        txtOverheadView.setTextFormatter(new TextFormatter<>(filter));
        StringConverter<Number> converter = new NumberStringConverter(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
        Bindings.bindBidirectional(txtOverheadView.textProperty(),sliderOverhead.valueProperty(),  converter);
    }


     //This method sets up listeners on the overhead and production checkboxes.
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

         cBox_Currency.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue != null) {
                 try {
                     calculateAndSetProfileRatesInEUR();
                 } catch (ApplicationWideException e) {
                     throw new RuntimeException(e);
                 }
             }
         });
    }

    private void setupListenersOnTextFields() {
        ChangeListener<String> textFieldListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            calculateAndSetHourlyRateCreateProfile();
            calculateAndSetDailyRateCreateProdifle();
            try {
                calculateAndSetProfileRatesInEUR();
            } catch (ApplicationWideException e) {
                throw new RuntimeException(e);
            }
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
        ChangeListener<String> textFieldListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        };
        txtFixedAmount.textProperty().addListener(textFieldListener);
        lblHourlyResult.textProperty().addListener(textFieldListener);

    }


    //This method is called when the save button is clicked.
    //It validates the input, creates a new Profile object, and saves it to the database.
    @FXML
    private void createProfile(ActionEvent actionEvent) {
        if (!validateInput()) {
            return;
        }
        String firstName = txtFirstnameProfile.getText();
        String lastName = txtLastnameProfile.getText();
        double annualSalary = parseDouble(lblAnnualResultEUR.getText());//TODO skal dette opdateres i DAO og BE. snak med @jonas
        double hourlyResult = parseDouble(lblHourlyResultEUR.getText());//TODO skal dette opdateres i DAO og BE. snak med @jonas
        double dailyResult = parseDouble(lblDailyResultInEUR.getText());//TODO skal dette opdateres i DAO og BE. snak med @jonas
        boolean overheadCost = checkOverhead.isSelected();
        double fixedAmount = parseDouble(txtFixedAmount.getText());
        double dailyWorkingHours = parseDouble(txtDailyWorkingHours.getText());
        List<ProfileRole> selectedRoles = new ArrayList<>(cBoxProfile_ProfileRoles.getCheckModel().getCheckedItems());

        Profile newProfile = new Profile(firstName, lastName, overheadCost,
                annualSalary, hourlyResult, dailyResult, fixedAmount, dailyWorkingHours, selectedRoles);

        profileModel.createProfile(newProfile);
        lblShowMassage.setText("Profile has been saved");
        System.out.println(newProfile);
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

    public void calculateAndSetProfileRatesInEUR() throws ApplicationWideException {
        if (txtAnnualSalary.getText().isEmpty() || lblHourlyResult.getText().isEmpty() || lblDailyResult.getText().isEmpty()) {
            return;
        }

        Currency selectedCurrency = cBox_Currency.getSelectionModel().getSelectedItem();
        if (selectedCurrency == null) {
            return;
        }

        String currency = selectedCurrency.toString();
        double annualSalary = Double.parseDouble(txtAnnualSalary.getText().replace(",", "."));
        double fixedAmount = Double.parseDouble(txtFixedAmount.getText().replace(",", "."));
        double hourlyRate = Double.parseDouble(lblHourlyResult.getText().replace(",", "."));
        double dailyRate = Double.parseDouble(lblDailyResult.getText().replace(",", "."));
        Map<String, Double> result = profileModel.calculateAndSetProfileRatesEUR(annualSalary, fixedAmount, hourlyRate, dailyRate, currency);
        lblAnnualResultEUR.setText(String.format("%.2f", result.get("annualSalaryEUR")));
        lblHourlyResultEUR.setText(String.format("%.2f", result.get("hourlyRateEUR")));
        lblDailyResultInEUR.setText(String.format("%.2f", result.get("dailyRateEUR")));
        System.out.println(result);
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    private void setRegexValidationForTextFields(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
    }

    private void setupRegex(){
        setRegexValidationForTextFields(txtAnnualSalary);
        setRegexValidationForTextFields(txtFixedAmount);
        setRegexValidationForTextFields(txtEffectiveHours);
        //setRegexValidationForTextFields(txtOverheadView);
        setRegexValidationForTextFields(txtDailyWorkingHours);
    }


    /*
     This method validates the input in the TextFields and ComboBoxes.
     If a field is empty,it is highlighted in red and the method returns false.
     If all fields are filled, the method returns true.
     */
    private boolean validateInput() {
        boolean isValid = true;
        // List of all TextFields and ComboBoxes
        List<Control> fields = Arrays.asList(txtFirstnameProfile, txtLastnameProfile, txtAnnualSalary,
                txtFixedAmount, txtDailyWorkingHours, txtEffectiveHours, cBox_Currency);

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


    @FXML
    private void createNewRole(ActionEvent actionEvent) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createRoleView.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create role");
        stage.show();
    }
        catch (Exception e) {
        ExceptionHandler.handleException(e);
    }
}}

