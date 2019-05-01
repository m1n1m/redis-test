package ru.funbox.links.web.rest.errors;

public class BadRequestAlertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    private final String defaultMessage;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this.defaultMessage = defaultMessage;
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String getMessage() {
        return defaultMessage +
                " entity ["+ entityName +"] error key ["+ errorKey +"]" + super.getMessage();
    }
}
