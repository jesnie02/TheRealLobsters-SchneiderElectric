package BLL;

import BE.Currency;
import BE.Profile;
import CustomExceptions.ApplicationWideException;
import DAL.Profile_DAO;
import java.util.List;
import java.util.Map;

public class ProfileManager {

    private Profile_DAO profileDAO;
    private CalculatorManager calculatorManager;
    private CurrencyManager currencyManager;


    public ProfileManager() throws ApplicationWideException {
        calculatorManager = new CalculatorManager();
        profileDAO = new Profile_DAO();
        currencyManager = new CurrencyManager();
    }


    public List<Profile> getAllProfiles() throws ApplicationWideException {
        return profileDAO.getAllProfiles();
    }


    public void createProfile(Profile newProfile) throws ApplicationWideException {
        profileDAO.createProfile(newProfile);
    }


    public Profile getProfileByName(String name) throws ApplicationWideException {
    for (Profile profile : getAllProfiles()) {
        if ((profile.getFName() + " " + profile.getLName()).equals(name)) {
            return profile;
        }
    }
    return null;
    }

    /**
     * Calculates and sets the hourly rate for a profile during creation.
     *
     * @param annualSalaryProfile       The annual salary of the profile.
     * @param overheadMultiplierProfile The overhead multiplier of the profile.
     * @param annualFixedAmountProfile  The annual fixed amount of the profile.
     * @param effectiveHoursProfile     The effective hours of the profile.
     * @return The calculated hourly rate.
     */
    public double calculateAndSetHourlyRateCreateProfile(
            double annualSalaryProfile, double overheadMultiplierProfile,
            double annualFixedAmountProfile, double effectiveHoursProfile) {

        return calculatorManager.calculateAndSetHourlyRateCreateProfile(
                annualSalaryProfile, overheadMultiplierProfile,
                annualFixedAmountProfile, effectiveHoursProfile);
    }

    /**
     * Calculates and sets the daily rate for a profile during creation.
     * @param dailyWorkingHours The daily working hours of the profile.
     * @param hourlyRate The hourly rate of the profile.
     * @return The calculated daily rate.
     */
    public double calculateAndSetDailyRateCreateProfile(double dailyWorkingHours, double hourlyRate) {
        return calculatorManager.calculateAndSetDailyRateCreateProfile(dailyWorkingHours, hourlyRate);
    }

    public void deleteProfile(Profile profile) throws ApplicationWideException {
        profileDAO.deleteProfile(profile.getProfileId());
    }

    public boolean updateProfile(Profile profile) throws ApplicationWideException {
        return profileDAO.updateProfile(profile);
    }

    public void updateProfileRates(Profile profile) throws ApplicationWideException {
        List<Profile> allProfiles = getAllProfiles();
        Profile currentProfile = allProfiles.stream()
                .filter(p -> p.getProfileId() == profile.getProfileId())
                .findFirst()
                .orElseThrow(() -> new ApplicationWideException("Profile not found"));

        double overheadMultiplier = currentProfile.getOverheadMultiplier();
        double effectiveHours = currentProfile.getEffectiveWorkingHours();

        double newHourlyRate = calculateAndSetHourlyRateCreateProfile(
                profile.getAnnualSalary(), overheadMultiplier,
                profile.getFixedAmount(), effectiveHours);

        double newDailyRate = calculateAndSetDailyRateCreateProfile(
                profile.getDailyWorkingHours(), newHourlyRate);

        profile.setHourlyRate(newHourlyRate);
        profile.setDailyRate(newDailyRate);
    }


    public Map<String, Double> calculateAndSetProfileRatesEUR(double annualSalary, double fixedAmount, double hourlyRate, double dailyRate, String currencyCode) throws ApplicationWideException {
        Currency selectedCurrency = currencyManager.getCurrencyByCode(currencyCode);
        if (selectedCurrency == null) {
            throw new ApplicationWideException("Currency not found");
        }
        double conversionRate = selectedCurrency.getConversionRate();
        return calculatorManager.calculateAndSetProfileRatesEUR(annualSalary, fixedAmount, hourlyRate, dailyRate, conversionRate);
    }
}
