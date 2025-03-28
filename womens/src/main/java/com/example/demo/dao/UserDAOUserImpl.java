package com.example.demo.dao;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.example.demo.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Repository
public class UserDAOUserImpl implements UserDAO<User> {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager;

	public UserDAOUserImpl() {
		super();
	}

	@Override
	public User findByUser_id(String user_id) {
		try {
			return (User) entityManager.createQuery("from User where user_id = :user_id")
					.setParameter("user_id", user_id)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public User findBySeq_no(Integer seq_no) {
		try {
			return (User) entityManager.createQuery("from User where seq_no = :seq_no")
					.setParameter("seq_no", seq_no)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void uppdatePassword(String password, String id) {

		entityManager.createQuery("UPDATE User SET user_pass = :user_pass WHERE user_id = :user_id")
				.setParameter("user_pass", password)
				.setParameter("user_id", id)
				.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<User> findAll() {
		return entityManager.createQuery("from User")
				.getResultList();
	}

	@Override
	public void save(User user) {
		entityManager.merge(user);
	}
	
	@Override
	public void updateUserInformation(Integer seq_no, String user_id, String user_name, String user_name_last,
			Integer authority_kbn, Integer status_kbn, LocalDate update_time) {
		entityManager.createQuery("UPDATE User SET user_id = :user_id, user_name = :user_name, "
				+ "user_name_last = :user_name_last, authority_kbn = :authority_kbn, status_kbn = :status_kbn,"
				+ " update_time = :update_time WHERE seq_no = :seq_no")
				.setParameter("user_id", user_id)
				.setParameter("user_name", user_name)
				.setParameter("user_name_last", user_name_last)
				.setParameter("authority_kbn", authority_kbn)
				.setParameter("status_kbn", status_kbn)
				.setParameter("seq_no", seq_no)
				.setParameter("update_time", update_time)
				.executeUpdate();
	}

}
