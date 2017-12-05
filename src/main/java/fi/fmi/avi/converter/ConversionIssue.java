package fi.fmi.avi.converter;

/**
 * An issue to be reported within the {@link ConversionResult}.
 *
 * This way of error/warning reporting is preferred over "fail-fast" conversions
 * exceptions to enable returning partially populated message objects from the
 * {@link AviMessageConverter#convertMessage(Object, ConversionSpecification)}.
 *
 * @author Ilkka Rinne / Spatineo 2017
 */
public class ConversionIssue {

    /**
     * Issue type
     */
    public enum Type {
        SYNTAX_ERROR, LOGICAL_ERROR, MISSING_DATA, OTHER
    }

    private final Type type;
    private final String message;



    private final Throwable ex;

    /**
     * Creates an issue with type and a message.
     *
     * @param type issue kind
     * @param message error message
     */
    public ConversionIssue(final Type type, final String message) {
       this(type, message, null);
    }

    /**
     * Creates an issue with type, message and a cause.
     *
     * @param type
     *         issue kind
     * @param message
     *         message to report
     * @param cause the reason for the issue
     */
    public ConversionIssue(final Type type, final String message, final Throwable cause) {
        this.type = type;
        this.message = message;
        this.ex = cause;
    }

    /**
     * Access to the issue type.
     *
     * @return issue type
     */
    public Type getType() {
        return this.type;
    }

    /**
     *
     * Access to the issue message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Possible root cause of the issue.
     *
     * @return the cause or null
     */
    public Throwable getCause() {
        return ex;
    }

    /**
     * Overridden to return a readable data.
     *
     * @return type and message concatenated
     */
    public String toString() {
        return this.type + ":" + this.message;
    }

}
