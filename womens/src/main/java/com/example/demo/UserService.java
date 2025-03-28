package com.example.demo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.dao.UserDAOUserImpl;
import com.example.demo.repository.UserRepository;

//@Controller
@Service
public class UserService {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserDAOUserImpl dao;
	
//	@Autowired
//	UserFormController formController;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	private LocalDate date;
	
	// パスワード再設定処理
	public void updatePassword(String password, String user_id) {
			// パスワードのハッシュ化処理
			password = passwordHash(password);
			// パスワード再設定処理
			dao.uppdatePassword(password, user_id);

	}	
	
	// パスワードハッシュ化処理
	public String passwordHash(String password) {
		return passwordEncoder.encode(password);
	}
	
	// データベースから取得した全ユーザーのデータをソートする
	public Iterable<User> findAllSort(){
		// 定義したソート順序
		List<Integer> SORT_ORDER = List.of(0, 2, 1);
		// Iterable<User>型で、データベースの全情報を取得
		Iterable<User> userList = dao.findAll();
		// List<User>型に変換し、ソート
		List<User> sortedUserList = StreamSupport.stream(userList.spliterator(), false)
				.sorted(Comparator.comparingInt(user -> SORT_ORDER.indexOf(user.getStatus_kbn())))
				.collect(Collectors.toList());
		return sortedUserList;
		}
	
	// ユーザ新規登録処理
	public void registerUserLogic(@ModelAttribute User user, String password) {
		// 現在の日付を取得
		this.date = LocalDate.now();
		// 現在の日付などをエンティティにセット
		user.setCreate_day(date);
		user.setUpdate_time(date);
		user.setFail_count(0);
		// パスワードをハッシュ化
		password = passwordHash(password);
		// ハッシュ化したパスワードをエンティティにセット
		user.setUser_pass(password);
		// ユーザ登録
		dao.save(user);
	}
	
	// ユーザ情報変更処理
	public void updateUserLogic(@ModelAttribute User user) {
		
	}
	
	// updateUserメソッドで使用
	public static String[] isValidInputUserEdit(String useridInput, String nameInput, String namelastInput) {
        String[] results = new String[2]; // [0] が userid の結果、[1] が username/usernamelast の結果
        
        // userid の判定（最初に見つかったエラーのみを返す）
        results[0] = validateUserid(useridInput);
        
        // username または usernamelast の判定（最初に見つかったエラーのみを返す）
        results[1] = validateUsernameOrLast(nameInput, namelastInput);
        
        return results;
    }
	
	// registerUserメソッドで使用
	public static String[] isValidInputUserCreate(String useridInput, String nameInput, String namelastInput, String password1Input, String password2Input) {
		// [0] が userid、[1] が username/usernamelast、[2] が password1、[3] がpassword2、[4] がpassword1と2 の一致確認の結果
		String[] results = new String[5]; 

        // userid の判定（最初に見つかったエラーのみを返す）
        results[0] = validateUserid(useridInput);
        
        // username または usernamelast の判定（最初に見つかったエラーのみを返す）
        results[1] = validateUsernameOrLast(nameInput, namelastInput);
        
        // password1 の判定（最初に見つかったエラーのみを返す）
        results[2] = validatePassword1(password1Input);
        
        // password2 の判定（最初に見つかったエラーのみを返す）
        results[3] = validatePassword2(password2Input);
        
        // password1 と password2 にエラーが無ければ 一致するか確認
        if (results[2] == null && results[3] == null) {
        	results[4] = confirmPasswordMatch(password1Input, password2Input);
        }
		return results;
	}
	
	// updatePasswordメソッドで使用
	public static String[] isValidInputUserPassword(String password1Input, String password2Input) {
        String[] results = new String[3]; // [0] が pasword1、[1] が password2、[2] が password1と2 の一致確認の結果

        // password1 の判定（最初に見つかったエラーのみを返す）
        results[0] = validatePassword1(password1Input);
        
        // password2 の判定（最初に見つかったエラーのみを返す）
        results[1] = validatePassword2(password2Input);
        
        // password1 と password2 にエラーが無ければ 一致するか確認
        if (results[0] == null && results[1] == null) {
        	results[2] = confirmPasswordMatch(password1Input, password2Input);
        }
		return results;
	}
	
    // userid の判定ロジック
    private static String validateUserid(String input) {
        if (input.isEmpty()) {
            return "ユーザIDを入力してください。";
        }
        if (input.length() > 30) {
            return "ユーザIDは30文字以内で入力してください。";
        }
        if (!input.matches("^[a-zA-Z0-9@_]*$")) {
            return "ユーザIDに使用できない文字が含まれています。";
        }
        return null; // 問題なければ空文字
    }

    // username または usernamelast の判定ロジック
    private static String validateUsernameOrLast(String input1, String input2) {
    	String regex = "^[\\p{InHiragana}\\p{InKatakana}\\p{InCJKUnifiedIdeographs}A-Za-zａ-ｚＡ-Ｚ0-9０-９]+$";
        if (input1.isEmpty() || input2.isEmpty()) {
            return "ユーザ名を入力してください。";
        }
        if (input1.length() > 30 || input2.length() > 30) {
            return "ユーザ名は30文字以内で入力してください。";
        }
        if (!input1.matches(regex) || !input2.matches(regex)) {
            return "ユーザ名に使用できない文字が含まれています。";
        }
        return null; // 問題なければ空文字
    }
    
    // password1 の判定ロジック（パスワード再設定 または ユーザ新規登録 での使用を想定）
    private static String validatePassword1(String password1) {
    	if (password1.isEmpty()) {
    		return "新パスワードを入力してください。";
    	}
    	// 詳細設計書 の 2-2-2. 3) には記載が無いが、2-2-1. 1) に入力制限があるため設定
    	if (password1.length() > 30 ) {
    		return "パスワードは30文字以内で入力してください。";
    	}
    	// パスワードは半角英数字のみ許容。記号不可。
    	if (!password1.matches("^[a-zA-Z0-9]*$")) {
    		return "新パスワードに使用できない文字が含まれています。";
    	}
    	return null;
    }

    // password2 の判定ロジック（パスワード再設定 または ユーザ新規登録 での使用を想定）
    private static String validatePassword2(String password2) {
    	if (password2.isEmpty()) {
    		return "新パスワードの再確認を入力してください。";
    	}
    	// 詳細設計書 の 2-2-2. 3) には記載が無いが、2-2-1. 1) に入力制限があるため設定
    	if (password2.length() > 30 ) {
    		return "パスワードは30文字以内で入力してください。";
    	}
    	// パスワードは半角英数字のみ許容。記号不可。
    	if (!password2.matches("^[a-zA-Z0-9]*$")) {
    		return "新パスワードの再確認に使用できない文字が含まれています。";
    	}
    	return null;
    }
    
    // password1 と password2 が 一致するかの判定ロジック（パスワード再設定 または ユーザ新規登録 での使用を想定）
    private static String confirmPasswordMatch(String password1, String password2) {
    	if (!password1.equals(password2)) {
    		return "新パスワードと確認の入力内容が異なります。";
    	}
    	return null;
    }

}
