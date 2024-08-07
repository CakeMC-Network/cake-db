package net.cakemc.database.collection;

/**
 * The type Filter.
 *
 * @param <Parameter> the type parameter
 * @param <Value>     the type parameter
 */
public abstract class Filter<Parameter, Value> {
    private final Parameter parameter;
    private final Value value;

    /**
     * Instantiates a new Filter.
     *
     * @param parameter the parameter
     * @param value     the value
     */
    protected Filter(Parameter parameter, Value value) {
        this.parameter = parameter;
        this.value = value;
    }

    /**
     * Matches boolean.
     *
     * @param document the document
     * @return the boolean
     */
    public abstract boolean matches(Document document);

    /**
     * Gets parameter.
     *
     * @return the parameter
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Value getValue() {
        return value;
    }
}
