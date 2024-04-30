package GUI.Controller;

import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MultipliersController {

    @FXML
    private MFXSlider sliderGM, sliderMU;
    @FXML
    private TextField txtGM, txtMU;

    private double grossMargin, markUp;

    @FXML
    private void initialize(){
        getSliderValue();
        setValueInField();
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
