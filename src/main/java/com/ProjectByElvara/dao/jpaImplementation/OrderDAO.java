package com.ProjectByElvara.dao.jpaImplementation;

import com.ProjectByElvara.entities.*;
import com.ProjectByElvara.dao.interfaces.IOrderDAO;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Profile("JPA")
@Repository
public class OrderDAO implements IOrderDAO {
    @PersistenceContext(unitName = "entityManager")
    private final EntityManager entityManager;

    public OrderDAO(EntityManagerFactory factory) {
        this.entityManager = factory.createEntityManager();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        try {
            Order order = this.entityManager.find(Order.class, id);
            return Optional.ofNullable(order);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAllByUserId(Integer userId) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        TypedQuery<Order> typedQuery = this.entityManager.createQuery(query.select(o)
                .where(builder.equal(o.get("user").get("id"), userId))
                .orderBy(builder.asc(o.get("orderStatus")), builder.desc(o.get("updateTime"))));
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> findAllByStatus(OrderStatus status) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        TypedQuery<Order> typedQuery = this.entityManager.createQuery(query.select(o)
                .where(builder.equal(o.get("orderStatus"), status))
                .orderBy(builder.asc(o.get("orderStatus")), builder.desc(o.get("updateTime"))));
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> findAllByUserIdAndStatus(Integer userId, OrderStatus status) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        TypedQuery<Order> typedQuery = this.entityManager.createQuery(query.select(o)
                .where(builder.equal(o.get("orderStatus"), status),
                        builder.equal(o.get("user").get("id"), userId))
                .orderBy(builder.asc(o.get("orderStatus")), builder.desc(o.get("updateTime"))));
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> findAll() throws SQLException {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        TypedQuery<Order> typedQuery = this.entityManager.createQuery(query.select(o)
                .orderBy(builder.asc(o.get("orderStatus")), builder.desc(o.get("updateTime"))));
        return typedQuery.getResultList();
    }

    @Override
    public List<Order> findAllAcceptedByProduct(Integer productId) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        TypedQuery<Order> typedQuery = this.entityManager.createQuery(query.select(o)
                .where(builder.equal(o.join("orderProducts").get("product").get("id"), productId),
                        builder.equal(o.get("orderStatus"), OrderStatus.ACCEPTED))
                .orderBy(builder.asc(o.get("orderStatus")), builder.desc(o.get("updateTime"))));
        return typedQuery.getResultList();
    }

    @Override
    public void save(Order order) throws SQLException {
        this.entityManager.persist(order);
    }

    @Override
    public void update(Order order) {
        this.entityManager.merge(order);
    }

    @Override
    public void delete(Order order) {
        this.entityManager.remove(order);
    }
}
