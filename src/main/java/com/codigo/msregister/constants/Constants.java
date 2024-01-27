package com.codigo.msregister.constants;

public class Constants {
    private Constants(){}
    //CODE
    public static final Integer CODE_SUCCESS=1001;
    public static final Integer CODE_ERROR_DATA_INPUT=1002;
    public static final Integer CODE_ERROR_EXIST=1003;
    public static final Integer CODE_ERROR_DATA_NOT=1004;
    public static final Integer CODE_ERROR_GENERIC=1005;

    //MESSAGES
    public static final String MESSAGE_SUCCESS="Ejecución correcta";
    public static final String MESSAGE_ERROR="Error en la Ejecución";

    //DATA
    public static final String MESSAGE_ERROR_NON_DATA="No existe Datos para ID";
    public static final String MESSAGE_ERROR_DATA_NOT_VALID="Error: Durante las validaciones de los Datos";
    public static final String MESSAGE_ZERO_ROWS="No existe Registros en la Base de Datos";
    public static final String MESSAGE_ERROR_NOT_UPDATE_PERSONS ="Error: No se ejecuto la actualización, Persona no Existe";
    public static final String MESSAGE_NON_DATA_RENIEC="No existe Registros en el API de RENIEC";

    //VALIDATIONS LENGTHS
    public static final Integer LENGTH_RUC=15;
    public static final Integer LENGTH_DNI=8;

    //STATUS
    public static final Integer STATUS_ACTIVE=1;
    public static final Integer STATUS_INACTIVE=0;

    //AUDIT
    public static final String AUDIT_ADMIN="ADMIN";

    //  REDISKEY
    public static final String REDIS_KEY_INFO_RENIEC="MS:REGISTER:INFORENIEC:";

    //STATIC VALUES
    public static final String COD_TYPE_RUC = "06";
    public static final String COD_TYPE_DNI = "01";
}
