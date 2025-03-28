package com.example.demo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="tb_user_ac")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer seq_no;
	
	@Column(length = 30, nullable = false)
	private String user_id;
	
	@Column(length = 100, nullable = false)
	@NotNull
	// バリデーションでパターンを設定すると処理の順序の関係でハッシュ化がバリデーションに
	// 引っかかってると思われるためコメントアウト。正確な原因は未検証
//	@Pattern(regexp="[a-zA-Z0-9]+")
//	@Pattern(regexp="^[a-zA-Z0-9]*$")
	private String user_pass;
	
	@Column(length = 15, nullable = false)
	@NotNull
	private String user_name;
	
	@Column(length = 15, nullable = false)
	@NotNull
	private String user_name_last;
	
	@Column
	@Min(1)
	@Max(4)
	private Integer authority_kbn;
	
	@Column
	@Min(0)
	@Max(2)
	private Integer status_kbn;
	
	@Column
	@Min(0)
	@Max(3)
	private Integer fail_count;
	
	@Column
	@NotNull
	private LocalDate create_day;
	
	@Column
	private LocalDate login_day;
	
	@Column
	private LocalDate delete_day;
	
	@Column
	@NotNull
	private LocalDate update_time;
	
	// デフォルトコンストラクタ
	public User() {
		
	}
	
	// ユーザ情報変更の失敗時に入力されてた値を保持するためのコンストラクタ、ユーザ新規登録でも使用
	public User(String user_id, String user_name, String user_name_last, Integer authority_kbn, Integer status_kbn) {
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_name_last = user_name_last;
		this.authority_kbn = authority_kbn;
		this.status_kbn = status_kbn;
	}

	public Integer getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(Integer seq_no) {
		this.seq_no = seq_no;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_pass() {
		return user_pass;
	}

	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_name_last() {
		return user_name_last;
	}

	public void setUser_name_last(String user_name_last) {
		this.user_name_last = user_name_last;
	}

	public Integer getAuthority_kbn() {
		return authority_kbn;
	}

	public void setAuthority_kbn(Integer authority_kbn) {
		this.authority_kbn = authority_kbn;
	}

	public Integer getStatus_kbn() {
		return status_kbn;
	}

	public void setStatus_kbn(Integer status_kbn) {
		this.status_kbn = status_kbn;
	}

	public Integer getFail_count() {
		return fail_count;
	}

	public void setFail_count(Integer fail_count) {
		this.fail_count = fail_count;
	}

	public LocalDate getCreate_day() {
		return create_day;
	}

	public void setCreate_day(LocalDate create_day) {
		this.create_day = create_day;
	}

	public LocalDate getLogin_day() {
		return login_day;
	}

	public void setLogin_day(LocalDate login_day) {
		this.login_day = login_day;
	}

	public LocalDate getDelete_day() {
		return delete_day;
	}

	public void setDelete_day(LocalDate delete_day) {
		this.delete_day = delete_day;
	}

	public LocalDate getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(LocalDate update_time) {
		this.update_time = update_time;
	}

}
