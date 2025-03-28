package com.example.demo.dao;

import java.io.Serializable;
import java.time.LocalDate;

public interface UserDAO <T> extends Serializable {
	
	public T findByUser_id(String id);
	
	public T findBySeq_no(Integer id);
	
	public void uppdatePassword(String password, String id);
	
	public Iterable<T> findAll();
	
	public void save(T user);
	
	public void updateUserInformation(Integer seq_no, String user_id, String user_name, String user_name_last,
			Integer authority_kbn, Integer status_kbn, LocalDate update_time);
	
}
