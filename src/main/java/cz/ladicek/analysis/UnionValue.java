package cz.ladicek.analysis;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UnionValue extends BasicValue {
    private final Set<Type> union;

    public static BasicValue create(BasicValue value) {
        if (value == null) { // void
            return null;
        }
        if (value.getType() == null) { // uninitialized value
            return new UnionValue(null, Set.of());
        }
        return new UnionValue(value.getType(), Set.of(value.getType()));
    }

    public static BasicValue create(BasicValue lub, BasicValue value1, BasicValue value2) {
        HashSet<Type> union = new HashSet<>();
        union.addAll(((UnionValue) value1).union);
        union.addAll(((UnionValue) value2).union);
        return new UnionValue(lub.getType(), Set.copyOf(union));
    }

    private UnionValue(Type lubType, Set<Type> union) {
        super(lubType);
        this.union = Objects.requireNonNull(union);
    }

    @Override
    public String toString() {
        return super.toString() + " | union of " + union;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnionValue)) return false;
        if (!super.equals(o)) return false;
        UnionValue that = (UnionValue) o;
        return Objects.equals(union, that.union);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), union);
    }
}
