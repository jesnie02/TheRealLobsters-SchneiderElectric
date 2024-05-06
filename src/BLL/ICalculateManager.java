package BLL;

import BE.Profile;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Function;

public interface ICalculateManager {




    double getDailyRateWithMultiplier(double rate, double percentage);
    double getHourlyRateWithMultiplier(double hourlyRate, double percentage);

    double avgAnnualSalary(List<Profile> profiles);
    double annualSalary(List<Profile> profiles);


    double sumOfHourlyRate(List<Profile> profiles);
    double avgHourlyRate(List<Profile> profiles);
    double avgDailyRate(List<Profile> profiles);
    double sumOfDailyRate(List<Profile> profiles);


}
