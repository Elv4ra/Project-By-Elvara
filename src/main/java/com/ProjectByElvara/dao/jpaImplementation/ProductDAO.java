package com.ProjectByElvara.dao.jpaImplementation;

import com.ProjectByElvara.dao.interfaces.IProductDAO;
import com.ProjectByElvara.entities.Product;
import com.ProjectByElvara.entities.additionalClasses.Category;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Profile("JPA")
@Repository
public class ProductDAO implements IProductDAO {
    @PersistenceContext(unitName = "entityManager")
    private final EntityManager entityManager;

    public ProductDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try {
            Product product = this.entityManager.find(Product.class, id);
            return Optional.ofNullable(product);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByNameTrademarkAndSize(String productName, String trademark, String size) {
        try {
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = builder.createQuery(Product.class);
            Root<Product> c = query.from(Product.class);
            Product product = this.entityManager.createQuery(
                    query.select(c).where(builder.equal(c.get("productName"), productName),
                                    builder.equal(c.get("trademark"), trademark),
                                    builder.like(c.get("size"), size))).getSingleResult();
            return Optional.ofNullable(product);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> c = query.from(Product.class);
        TypedQuery<Product> typedQuery = this.entityManager.createQuery(query.select(c));
        return typedQuery.getResultList();
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> c = query.from(Product.class);
        TypedQuery<Product> typedQuery = this.entityManager.createQuery(query.select(c).where(builder.equal(c.get("category"), category)));
        return typedQuery.getResultList();
    }

    @Override
    public void save(Product product) {
        this.entityManager.persist(product);
    }

    @Override
    public void update(Product product) {
        this.entityManager.merge(product);
    }
}
