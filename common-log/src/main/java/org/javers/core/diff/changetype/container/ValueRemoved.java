package org.javers.core.diff.changetype.container;

import org.javers.common.string.PrettyValuePrinter;

/**
 * Item removed from an Array or Collection
 *
 * @author bartosz walacik
 */
public class ValueRemoved extends ValueAddOrRemove {

    public ValueRemoved(int index, Object value) {
        super(index, value);
    }

    public ValueRemoved(Object value) {
        super(value);
    }

    /**
     * Removed item. See {@link #getValue()} javadoc
     */
    public Object getRemovedValue() {
        return value.unwrap();
    }

    @Override
    public String toString() {
        return prettyPrint(PrettyValuePrinter.getDefault());
    }

    @Override
    protected String prettyPrint(PrettyValuePrinter valuePrinter) {
        return (getIndex() == null ? "" : getIndex()) + ". " +
                valuePrinter.formatWithQuotes(getRemovedValue()) + " 被移除";
    }
}
