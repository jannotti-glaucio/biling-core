package tech.jannotti.billing.core.constants;

public interface ResultCodeConstants {

    public static final String CODE_SUCCESS = "000";

    public static final String CODE_EMPTY_QUERY_RESULT = "001";

    public static final String CODE_GENERIC_ERROR = "999";

    // Erros de Validação

    public static final String CODE_REQUIRED_PARAMETER_MISSING = "101";

    public static final String CODE_INVALID_INTEGER_PARAMETER = "102";

    public static final String CODE_INVALID_BOOLEAN_PARAMETER = "103";

    public static final String CODE_INVALID_DATE_PARAMETER = "104";

    public static final String CODE_INVALID_DATE_TIME_PARAMETER = "105";

    public static final String CODE_INVALID_MONTH_PARAMETER = "106";

    public static final String CODE_INVALID_YEAR_PARAMETER = "107";

    public static final String CODE_INVALID_EMAIL_PARAMETER = "108";

    public static final String CODE_INVALID_PERSON_TYPE_PARAMETER = "109";

    public static final String CODE_INVALID_DOCUMENT_TYPE_PARAMETER = "110";

    public static final String CODE_INVALID_ADDRESS_TYPE_PARAMETER = "111";

    public static final String CODE_INVALID_PAYMENT_METHOD_PARAMETER = "112";

    public static final String CODE_INVALID_MEDIA_TYPE_PARAMETER = "113";

    public static final String CODE_INVALID_COUNTRY_PARAMETER = "114";

    public static final String CODE_INVALID_STATUS_LIST_PARAMETER = "115";

    public static final String CODE_INVALID_CRUD_OPERATION_PARAMETER = "116";

    public static final String CODE_INVALID_FILTER_LENGTH_PARAMETER = "117";

    public static final String CODE_INVALID_SORT_DIRECTION_PARAMETER = "118";

    public static final String CODE_INVALID_COLLECTION_AMOUNT_TYPE_PARAMETER = "119";

    public static final String CODE_NOT_FOUND_TOKEN_PARAMETER = "120";

    public static final String CODE_LENGTH_PARAMETER_EXCEEDS_LMIT = "121";

    public static final String CODE_INVALID_BANK_REMITTANCE_DELIVERY_MODE_PARAMETER = "122";

    public static final String CODE_INVALID_FREQUENCY_TYPE_PARAMETER = "123";

    public static final String CODE_INVALID_FUTURE_DATE_PARAMETER = "124";

    public static final String CODE_INVALID_DAY_PARAMETER = "125";

    public static final String CODE_INVALID_INTEREST_FREQUENCY_PARAMETER = "126";

    public static final String CODE_INVALID_PAGGING_PARAMETERS = "131";

    public static final String CODE_INVALID_SORTING_PARAMETERS = "132";

    public static final String CODE_INVALID_SORTING_SORT_VALUE = "133";

    public static final String CODE_PAGGING_SIZE_EXCEEDS_LIMIT = "134";

    public static final String CODE_INVALID_DOCUMENT_TYPE_VALUE = "135";

    public static final String CODE_INVALID_STATE_VALUE = "136";

    public static final String CODE_INVALID_DATE_TO_FILTER_VALUE = "137";

    public static final String CODE_INVALID_CITY = "138";

    // Erros de Regra de Negocio

    public static final String CODE_TOKEN_NOT_FOUND = "201";

    public static final String CODE_ADDRESS_TOKEN_NOT_FOUND = "202";

    public static final String CODE_ACCOUNT_TOKEN_NOT_FOUND = "203";

    public static final String CODE_INVALID_USER_STATUS_TO_DELETE = "210";

    public static final String CODE_INVALID_COMPANY_STATUS_TO_DELETE = "211";

    public static final String CODE_INVALID_DEALER_STATUS_TO_DELETE = "212";

    public static final String CODE_INVALID_CUSTOMER_STATUS_TO_DELETE = "213";

    public static final String CODE_INVALID_INVOICE_STATUS_TO_CANCEL = "214";

    public static final String CODE_INVALID_PAYMENT_STATUS_TO_CANCEL = "215";

    public static final String CODE_INVALID_COLLECTION_STATUS_TO_CANCEL = "216";

    public static final String CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_CANCEL = "217";

    public static final String CODE_INVALID_APPLICATION_STATUS_TO_DELETE = "218";

    public static final String CODE_INVALID_BILLING_PLAN_STATUS_TO_DELETE = "219";

    public static final String CODE_ADMIN_USER_REQUIRED_TO_ACCESS = "220";

    public static final String CODE_COMPANY_USER_REQUIRED_TO_ACCESS = "221";

    public static final String CODE_DEALER_USER_REQUIRED_TO_ACCESS = "222";

    public static final String CODE_CUSTOMER_USER_REQUIRED_TO_ACCESS = "223";

    public static final String CODE_INVALID_SUBSCRIPTION_STATUS_TO_CANCEL = "224";

    public static final String CODE_INVALID_SUBSCRIPTION_STATUS_TO_SUSPEND = "225";

    public static final String CODE_INVALID_SUBSCRIPTION_STATUS_TO_REOPEN = "226";

    public static final String CODE_INVALID_INVOICE_STATUS_TO_PURGE = "227";

    public static final String CODE_INVALID_DOCUMENT_NUMBER = "230";

    public static final String CODE_INVALID_CURRENT_PASSWORD = "231";

    public static final String CODE_ONE_BILLING_ADDRESS_REQUIRED = "232";

    public static final String CODE_DUPLICATED_BILLING_ADDRESS = "233";

    public static final String CODE_COMPANY_BILLING_ADDRESS_REQUIRED = "234";

    public static final String CODE_DEALER_BILLING_ADDRESS_REQUIRED = "235";

    public static final String CODE_CUSTOMER_BILLING_ADDRESS_REQUIRED = "236";

    public static final String CODE_INVOICE_WITHOUT_ACTIVE_BANK_BILLET = "237";

    public static final String CODE_INVOICE_BANK_BILLET_PAYMENT_METHOD_REQUIRED = "238";

    public static final String CODE_PENALTY_START_DATE_GREATER_THAN_EXPIRATION_DATE = "239";

    public static final String CODE_BANK_BILLET_WAITING_CANCELLATION_RESPONSE = "240";

    public static final String CODE_DEALER_BANK_ACCOUNT_MISSING = "241";

    public static final String CODE_INVOICE_INVALID_EXPIRATION_DATE = "242";

    public static final String CODE_CUSTOMER_EMAIL_REQUIRED_TO_SEND = "243";

    public static final String CODE_ACTIVE_MARKET_ACCOUNT_REQUIRED = "244";

    public static final String CODE_INSUFFICIENT_BALANCE_TO_REQUEST_WITHDRAW = "245";

    public static final String CODE_INSUFFICIENT_BALANCE_TO_APPROVE_WITHDRAW = "246";

    public static final String CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_APPROVE = "247";

    public static final String CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_DENY = "248";

    public static final String CODE_INVALID_MARKET_WITHDRAW_STATUS_TO_PROVIDE = "249";

    public static final String CODE_DUPLICATED_USERNAME = "250";

    public static final String CODE_WITHDRAW_AMOUNT_BELOW_MINIMUM = "251";

    public static final String CODE_WITHDRAW_AMOUNT_ABOVE_MAXIMUM = "252";

    public static final String CODE_WITHDRAW_NET_AMOUNT_BELOW_MINIMUM = "253";

    public static final String CODE_INVOICE_AMOUNT_BELOW_MINIMUM = "254";

    public static final String CODE_INVOICE_AMOUNT_ABOVE_MAXIMUM = "255";

    public static final String CODE_INVOICE_NET_AMOUNT_BELOW_MINIMUM = "256";

    public static final String CODE_COULD_NOT_DELETE_IN_USE_BILLING_PLAN = "257";

    public static final String CODE_INVALID_USER_STATUS_TO_UPDATE = "260";

    public static final String CODE_INVALID_COMPANY_STATUS_TO_UPDATE = "261";

    public static final String CODE_INVALID_DEALER_STATUS_TO_UPDATE = "262";

    public static final String CODE_INVALID_CUSTOMER_STATUS_TO_UPDATE = "263";

    public static final String CODE_INVALID_APPLICATION_STATUS_TO_UPDATE = "266";

    public static final String CODE_INVALID_BILLING_PLAN_STATUS_TO_UPDATE = "267";

    public static final String CODE_INVALID_INVOICE_STATUS_TO_UPDATE = "268";

    public static final String CODE_INVALID_SUBSCRIPTION_STATUS_TO_UPDATE = "269";

    public static final String CODE_INVALID_USER_STATUS_TO_BLOCK = "270";

    public static final String CODE_INVALID_USER_STATUS_TO_UNBLOCK = "271";

    // Erros da Adquirente

    public static final String CODE_ACQUIRER_UNKNOW_RETURN = "398";

    public static final String CODE_ACQUIRER_GENERIC_ERROR = "399";

    // Erros do Banco

    public static final String CODE_BANKING_UNKNOW_RETURN = "498";

    public static final String CODE_BANKING_GENERIC_ERROR = "499";

}
