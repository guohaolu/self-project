package org.javers.core.diff.changetype;

import org.javers.common.string.PrettyValuePrinter;
import org.javers.common.validation.Validate;

import java.util.Objects;

/**
 * Change on a Value property, like int or String
 *
 * @author bartosz walacik
 */
public class ValueChange extends PropertyChange<Object> {
    private final Atomic left;
    private final Atomic right;

    public ValueChange(PropertyChangeMetadata metadata, Object leftValue, Object rightValue){
        super(metadata);
        this.left = new Atomic(leftValue);
        this.right = new Atomic(rightValue);
    }

    @Override
    public Object getLeft() {
        return left.unwrap();
    }

    @Override
    public Object getRight() {
        return right.unwrap();
    }

    @Override
    public String prettyPrint(PrettyValuePrinter valuePrinter) {
        Validate.argumentIsNotNull(valuePrinter);

        if (isPropertyAdded()) {
            return valuePrinter.formatWithQuotes(getPropertyNameWithPath()) +
                    "新增" + valuePrinter.formatWithQuotes(right.unwrap());
        }
        else if (isPropertyRemoved()) {
            return valuePrinter.formatWithQuotes(getPropertyNameWithPath()) +
                    "移除" + valuePrinter.formatWithQuotes(left.unwrap());
        } else {
            if (left.isNull()) {
                return valuePrinter.formatWithQuotes(getPropertyNameWithPath()) +
                        " -> " + valuePrinter.formatWithQuotes(getRight());
            }else if (right.isNull()) {
                return valuePrinter.formatWithQuotes(getPropertyNameWithPath()) +
                        ":" + valuePrinter.formatWithQuotes(getLeft() )+ " 被移除";
            } else {
                return valuePrinter.formatWithQuotes(getPropertyNameWithPath()) +
                        " 变更: " + valuePrinter.formatWithQuotes(getLeft()) + " -> " +
                        valuePrinter.formatWithQuotes(getRight());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ValueChange) {
            ValueChange that = (ValueChange) obj;
            return super.equals(that)
                    && Objects.equals(this.getLeft(), that.getLeft())
                    && Objects.equals(this.getRight(), that.getRight());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLeft(), getRight());
    }

    @Override
    public String toString() {
        PrettyValuePrinter printer = PrettyValuePrinter.getDefault();
        return this.getClass().getSimpleName() + "{ property: '"+getPropertyName() +"'," +
                " left:"+printer.formatWithQuotes(getLeft())+", " +
                " right:"+printer.formatWithQuotes(getRight())+" }";
    }
}
