package com.ProjectByElvara.dao.jpaImplementation;

import com.ProjectByElvara.dao.interfaces.IUserDAO;
import com.ProjectByElvara.entities.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Profile("JPA")
@Repository
public class UserDAO implements IUserDAO {
    @PersistenceContext(unitName = "entityManager")
    private final EntityManager entityManager;

    public UserDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

//    @Override
//    @Transactional
//    public void initialSave(User user) {
//        this.entityManager.persist(user);
//    }

    @Override
    public Optional<User> findById(Integer id) {
        try {
            User user = this.entityManager.find(User.class, id);
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> c = query.from(User.class);
            query.select(c).where(builder.equal(c.get("email"), email));
            User user = this.entityManager.createQuery(query).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> c = query.from(User.class);
        TypedQuery<User> typedQuery = this.entityManager.createQuery(query.select(c));
        return typedQuery.getResultList();
    }

    @Override
    public List<User> findAllByEmailPattern(String emailPattern) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> c = query.from(User.class);
        TypedQuery<User> typedQuery = this.entityManager.createQuery(query.select(c).where(builder.like(c.get("email"), "%" + emailPattern + "%")));
        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public void save(User user) {
        this.entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        this.entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        this.entityManager.remove(user);
    }
}
