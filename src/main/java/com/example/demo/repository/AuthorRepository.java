package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.example.demo.model.Author;
import com.example.demo.model.Author_;
import com.example.demo.model.Post;
import com.example.demo.model.Post_;

@Service
public class AuthorRepository extends AbstractRepository<Author, Long> {
	public Builder createQuery() {
		return new Builder(em);
	}

	public static final class Builder {
		private EntityManager entityManager;
		private CriteriaBuilder criteriaBuilder;
		private CriteriaQuery<Author> query;
		private Root<Author> root;

		List<Predicate> predicates = new ArrayList<>();

		Join<Author, Post> fromPost;

		Builder(EntityManager entityManager) {
			this.entityManager = entityManager;
			criteriaBuilder = entityManager.getCriteriaBuilder();
			query = criteriaBuilder.createQuery(Author.class);
			root = query.from(Author.class);
		}

		public Builder withId(Long id) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.ID), id));
			return this;
		}

		public Builder withFirstName(String firstName) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.FIRST_NAME), firstName));
			return this;
		}

		public Builder withLastName(String lastName) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.LAST_NAME), lastName));
			return this;
		}

		public Builder withEmail(String email) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.EMAIL), email));
			return this;
		}

		public Builder withBirhtdate(Date birthdate) {
			predicates.add(criteriaBuilder.equal(root.get(Author_.BIRTHDATE), birthdate));
			return this;
		}

		public Builder withPostTitle(String title) {
			if (fromPost == null) {
				fromPost = root.join(Author_.POSTS, JoinType.LEFT);
				
				// ATTENTION: even FetchType for POSTs is set to EAGER.
				// In the JPQL, we must explicitly declare it in the query 
				root.fetch(Author_.POSTS);
			}

			predicates.add(criteriaBuilder.equal(fromPost.get(Post_.TITLE), title));
			return this;
		}

		public List<Author> getResultList() {
			query.select(root);

			if (!predicates.isEmpty()) {
				query.where(predicates.toArray(new Predicate[] {}));
			}
			
			return entityManager.createQuery(query).getResultList();
		}

		public Long count() {
			if (!predicates.isEmpty()) {
				query.where(predicates.toArray(new Predicate[] {}));
			}

			CriteriaQuery<Long> countQuery = new CountQueryHelper<Author>(Author.class).getCountQuery(query,
					entityManager);
			return entityManager.createQuery(countQuery).getSingleResult();
		}

		public Author getSingleResult() {
			query.select(root);

			if (!predicates.isEmpty()) {
				query.where(predicates.toArray(new Predicate[] {}));
			}

			return entityManager.createQuery(query).getSingleResult();
		}
	}
}
