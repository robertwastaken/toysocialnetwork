package ro.ubbcluj.map.mavenfx2.repository;


import ro.ubbcluj.map.mavenfx2.domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    void save(E entity);
    void delete(ID id);
    Long getSize();
}