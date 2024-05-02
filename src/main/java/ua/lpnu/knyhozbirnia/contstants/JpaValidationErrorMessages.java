package ua.lpnu.knyhozbirnia.contstants;

public final class JpaValidationErrorMessages {
    public static final String MINIMUM_SIZE_CONSTRAINT_VIOLATION = "The length must be greater than {value} characters";
    public static final String MAXIMUM_SIZE_CONSTRAINT_VIOLATION = "The length must be less than {value} characters";
    public static final String NOT_NULL_CONSTRAINT_VIOLATION = "Cannot be null";
    public static final String FOREIGN_KEY_NOT_NULL_CONSTRAINT_VIOLATION = "The foreign key field cannot be null";
    public static final String NOT_EMPTY_CONSTRAINT_VIOLATION = "Cannot be empty";
    public static final String EMAIL_CONSTRAINT_VIOLATION = "Is not validly formatted";
    public static final String GENDER_CONSTRAINT_VIOLATION = "Value is invalid. It must be either 'f', 'm' or 'n' – female, or and non-binary respectively";
    public static final String POSITIVE_VALUE_CONSTRAINT_VIOLATION = "Value must be positive";
    public static final String EXACT_LENGTH_VALUE_CONSTRAINT_VIOLATION = "The length must be exactly {min} characters";
    public static final String PAST_OR_PRESENT_DATE_CONSTRAINT_VIOLATION = "Date must be in the past or present";
}
