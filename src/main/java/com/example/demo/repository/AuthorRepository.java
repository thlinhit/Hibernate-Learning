package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.example.demo.model.Author;

@Service
public class AuthorRepository extends AbstractRepository<Author, Long>{
	public Builder createQuery() {
        return new Builder(em);
    }
	
	public static final class Builder {
        private EntityManager entityManager;
        private CriteriaBuilder criteriaBuilder;
        private CriteriaQuery<Author> query;
        private Root<Author> root;
        
        List<Predicate> predicates = new ArrayList<>();
        
		Builder(EntityManager entityManager) {
			this.entityManager = entityManager;
            criteriaBuilder = entityManager.getCriteriaBuilder();
            query = criteriaBuilder.createQuery(Author.class);
            root = query.from(Author.class);
		}
		
        public Builder withFirstName(String firstName) {
            predicates.add(criteriaBuilder.equal(root.get("firstName"), firstName));
            return this;
        }
        
        public Builder withLastName(String lastName) {
            predicates.add(criteriaBuilder.equal(root.get("lastName"), lastName));
            return this;
        }
        
        public Builder withEmail(String email) {
            predicates.add(criteriaBuilder.equal(root.get("email"), email));
            return this;
        }
        
        public Builder withBirhtdate(Date birthdate) {
            predicates.add(criteriaBuilder.equal(root.get("birthdate"), birthdate));
            return this;
        }
        
        public List<Author> getResultList() {
            query.select(root);

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[]{}));
            }

            return entityManager.createQuery(query).getResultList();
        }

        public Author getSingleResult() {
            query.select(root);

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[]{}));
            }

            return entityManager.createQuery(query).getSingleResult();
        }
	}
}