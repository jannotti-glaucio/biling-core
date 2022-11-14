package tech.jannotti.billing.core.rest;

public interface ApiConstants {

    public static final String V1_API_PATH = "/v1/";

    public static final String PAGE_PARAMETER = "page";
    public static final String SIZE_PARAMETER = "size";
    public static final String SORT_PARAMETER = "sort";
    public static final String DIRECTION_PARAMETER = "direction";

    public static final String FILTER_PARAMETER = "filter";

    // TODO Migrar todo lugar que estah usando isso pra ler a Country da Company
    public static final String DEFAULT_COUNTRY = "BR";

    public static final String PAYMENT_BANK_BILLET_PDF_FILE_NAME = "bankBillet.pdf";

}
