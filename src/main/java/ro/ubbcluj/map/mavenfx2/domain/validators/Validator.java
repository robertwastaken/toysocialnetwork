package ro.ubbcluj.map.mavenfx2.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
