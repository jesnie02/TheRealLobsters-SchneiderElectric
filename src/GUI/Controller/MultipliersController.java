package GUI.Controller;

import GUI.Model.CountryModel;
import GUI.Model.TeamsModel;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MultipliersController {

    @FXML
    private MFXSlider sliderGM, sliderMU;
    @FXML
    private TextField txtGM, txtMU;
    private CountryModel countryModel;
    private TeamsModel teamsModel;

    private double grossMargin, markUp;
    @FXML
    private ComboBox cBoxProfile_Multiplier, cBoxTeam_Multiplier;


    @FXML
    private void initialize(){
        try {
            getSliderValue();
            setValueInField();
            cBoxTeam_Multiplier.setItems(teamsModel.getAllProjectTeams());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public MultipliersController(){
        try {
            countryModel = new CountryModel();
            teamsModel = new TeamsModel();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public void getSliderValue(){
        sliderGM.valueProperty().addListener(((observable, oldValue, newValue) -> {
            grossMargin = newValue.doubleValue();
            setValueInField();
        }));
        sliderMU.valueProperty().addListener(((observable, oldValue, newValue) -> {
            markUp = newValue.doubleValue();
            setValueInField();
        }));
    }

    private void setValueInField(){
        txtGM.setText(String.valueOf(grossMargin));
        txtMU.setText(String.valueOf(markUp));
    }

    public double getGMValue(){
        return sliderGM.getValue();
    }

    public double getMUValue(){
        return sliderMU.getValue();
    }



}
