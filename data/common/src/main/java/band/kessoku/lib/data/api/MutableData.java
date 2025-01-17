package band.kessoku.lib.data.api;

/**
 * A fundamental part for Kessoku Lib to hold data.
 * <p>
 *     This data is mutable.
 * </p>
 * @param <T> The type of data.
 */
public interface MutableData<T> extends Data<T> {
    void set(T newValue);
}
