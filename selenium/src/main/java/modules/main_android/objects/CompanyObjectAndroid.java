package modules.main_android.objects;


import org.inferred.freebuilder.FreeBuilder;

import com.google.common.base.Optional;


@FreeBuilder
public abstract class CompanyObjectAndroid {

	// Company Address
	public static final String ADDRESS_LINE_1_FIELD = "Office 101";
	public static final String ADDRESS_LINE_2_FIELD = "1st Street";
	public static final String CITY_FIELD = "Vancouver";
	public static final String COUNTRY_DDLMI = "Canada";
	public static final String STATE_PROVINCE_DDLMI = "Alberta";
	public static final String POSTAL_CODE_FIELD = "V0V0V0";

	// Company Bank Information
	public static final String INSTITUTION_NUMBER_FIELD = "000";
	public static final String TRANSIT_NUMBER_FIELD = "11000";
	public static final String ACCOUNT_NUMBER_FIELD = "000123456789";
	public static final String ACCOUNT_HOLDER_NAME_FIELD = "HolderName";
	public static final String ACCOUNT_TYPE_DDLMI = "Company";

	// Legal Entity
	public static final String LEGAL_FIRST_NAME_FIELD = "FirstName";
	public static final String LEGAL_LAST_NAME_FIELD = "LastName";
	public static final String LEGAL_BIRTHDAY_MONTH_SPINNER = "July";
	public static final String LEGAL_BIRTHDAY_DAY_SPINNER = "27";
	public static final String LEGAL_BIRTHDAY_YEAR_SPINNER = "2015";
	public static final String LEGAL_ADDRESS_LINE_1_FIELD = "Office 222";
	public static final String LEGAL_ADDRESS_LINE_2_FIELD = "2nd St";
	public static final String LEGAL_CITY_FIELD = "Victoria";
	public static final String LEGAL_COUNTRY_DDLMI = "Canada";
	public static final String LEGAL_STATE_DDLMI = "Alberta";
	public static final String LEGAL_POSTAL_CODE_FIELD = "V1V1V1";

	// Scan Credit Card
	public static final String CARD_NUMBER_FILED = "4111111111111111";
	public static final String MM_YY_FIELD = "1229";
	public static final String CVV_FILED = "123";

	// Credit Card Information
	public static final String CREDIT_CARD_NAME_ON_CARD_FIELD = "NameOnCard";
	public static final String CREDIT_CARD_LICENSES_DDLMI = "5";
	public static final String CREDIT_CARD_ADDRESS_LINE_1_FIELD = "Office 333";
	public static final String CREDIT_CARD_ADDRESS_LINE_2_FIELD = "3rd St";
	public static final String CREDIT_CARD_CITY_FILED = "Vancouver";
	public static final String CREDIT_CARD_COUNTRY_DDLMI = "Canada";
	public static final String CREDIT_CARD_STATE_DDLMI = "Alberta";
	public static final String CREDIT_CARD_POSTAL_CODE_FIELD = "V3V3V3";

	// Company Address
	public abstract Optional<String> addressLine1();

	public abstract Optional<String> addressLine2();

	public abstract Optional<String> city();

	public abstract Optional<String> country();

	public abstract Optional<String> state();

	public abstract Optional<String> postalCode();

	// Company Bank Information
	public abstract Optional<String> institutionNumber();

	public abstract Optional<String> transitNumber();

	public abstract Optional<String> accountNumber();

	public abstract Optional<String> accountHolderName();

	public abstract Optional<String> accountType();

	// Legal Entity
	public abstract Optional<String> legalFirstName();

	public abstract Optional<String> legalLastName();

	public abstract Optional<String> leaglBirthdayMonth();

	public abstract Optional<String> leaglBirthdayDay();

	public abstract Optional<String> leaglBirthdayYear();

	public abstract Optional<String> legalAddressLine1();

	public abstract Optional<String> legalAddressLine2();

	public abstract Optional<String> legalCity();

	public abstract Optional<String> legalCountry();

	public abstract Optional<String> legalState();

	public abstract Optional<String> legalPostalCode();

	// Scan Credit Card
	public abstract Optional<String> cardNumber();

	public abstract Optional<String> MMYY();

	public abstract Optional<String> CVV();

	// Credit Card Information
	public abstract Optional<String> creditCardNameOnCard();

	public abstract Optional<String> creditCardLicenseDDLMI();

	public abstract Optional<String> creditCardAddressLine1();

	public abstract Optional<String> creditCardAddressLine2();

	public abstract Optional<String> creditCardCity();

	public abstract Optional<String> creditCardCountry();

	public abstract Optional<String> creditCardState();

	public abstract Optional<String> creditCardPostalCode();

	public abstract Builder toBuilder();

	public static class Builder extends CompanyObjectAndroid_Builder {

	}

	public CompanyObjectAndroid withDefaultCompany() {
		return new CompanyObjectAndroid.Builder()

				// Company Address
				.addressLine1(ADDRESS_LINE_1_FIELD).addressLine2(ADDRESS_LINE_2_FIELD).city(CITY_FIELD)
				.country(COUNTRY_DDLMI).state(STATE_PROVINCE_DDLMI).postalCode(POSTAL_CODE_FIELD)

				// Company Bank Information
				.institutionNumber(INSTITUTION_NUMBER_FIELD).transitNumber(TRANSIT_NUMBER_FIELD)
				.accountNumber(ACCOUNT_NUMBER_FIELD).accountHolderName(ACCOUNT_HOLDER_NAME_FIELD)
				.accountType(ACCOUNT_TYPE_DDLMI)

				// Legal Entity
				.legalFirstName(LEGAL_FIRST_NAME_FIELD).legalLastName(LEGAL_LAST_NAME_FIELD)
				.leaglBirthdayMonth(LEGAL_BIRTHDAY_MONTH_SPINNER).leaglBirthdayDay(LEGAL_BIRTHDAY_DAY_SPINNER)
				.leaglBirthdayYear(LEGAL_BIRTHDAY_YEAR_SPINNER).legalAddressLine1(LEGAL_ADDRESS_LINE_1_FIELD)
				.legalAddressLine2(LEGAL_ADDRESS_LINE_2_FIELD).legalCity(LEGAL_CITY_FIELD)
				.legalCountry(LEGAL_COUNTRY_DDLMI).legalState(LEGAL_STATE_DDLMI)
				.legalPostalCode(LEGAL_POSTAL_CODE_FIELD)

				// Scan Credit Card
				.cardNumber(CARD_NUMBER_FILED).MMYY(MM_YY_FIELD).CVV(CVV_FILED)

				// Credit Card Information
				.creditCardNameOnCard(CREDIT_CARD_NAME_ON_CARD_FIELD).creditCardLicenseDDLMI(CREDIT_CARD_LICENSES_DDLMI)
				.creditCardAddressLine1(CREDIT_CARD_ADDRESS_LINE_1_FIELD)
				.creditCardAddressLine2(CREDIT_CARD_ADDRESS_LINE_2_FIELD).creditCardCity(CREDIT_CARD_CITY_FILED)
				.creditCardCountry(CREDIT_CARD_COUNTRY_DDLMI).creditCardState(CREDIT_CARD_STATE_DDLMI)
				.creditCardPostalCode(CREDIT_CARD_POSTAL_CODE_FIELD)

				.buildPartial();
	}
}