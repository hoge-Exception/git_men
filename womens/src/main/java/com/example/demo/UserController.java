package com.example.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.UserDAOUserImpl;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Controller
public class UserController {

	@Autowired
	UserRepository repository;

	@Autowired
	UserDAOUserImpl dao;

	@Autowired
	UserService service;

	private LocalDate date;

	@GetMapping("/")
	public String index() {
		return "/001Login";
	}

	@GetMapping("/002TopPage")
	public String topPage() {
		return "/002TopPage";
	}

	// ユーザ情報メニューを表示
	@GetMapping("/011_01UserInformationMenu")
	public String userInformationMenu() {
		return "/011_01UserInformationMenu";
	}

	// ユーザ情報一覧を表示
	@GetMapping("/011_02UserInformationList")
	public ModelAndView userInformationList(ModelAndView mav) {
		// 遷移先のビューを設定★★★★★★★保留★★★★★★★
		mav.setViewName("/011_02UserInformationList");
		// 全ユーザーの情報を取得し、データをソートする
		Iterable<User> userList = service.findAllSort();
		// モデルに全ユーザーの情報を追加
		mav.addObject("userData", userList);
		return mav;
	}

	// パスワード再設定を表示
	@PostMapping("/011_02_02PasswordReset")
	public ModelAndView passwordReset(@RequestParam String user_id, ModelAndView mav) {
		// 遷移先のビューを設定
		mav.setViewName("/011_02_02PasswordReset");
		// 指定のユーザーの情報を取得
		User userData = dao.findByUser_id(user_id);
		// ユーザーのデータをリスト型に変換
		User[] user = new User[] { userData };
		// モデルにユーザーの情報を追加
		mav.addObject("userData", user);
		return mav;
	}

	// パスワード再設定処理
	@PostMapping("/updatePassword")
	@Transactional
	public ModelAndView updatePassword(@RequestParam String user_id,
			@RequestParam String password1,
			@RequestParam String password2,
			ModelAndView mav) {

		// エラーの有無を判定
		String[] isValidMessages = service.isValidInputUserPassword(password1, password2);
		// password1を判定
		String errorMessage1 = isValidMessages[0];
		// password2を判定
		String errorMessage2 = isValidMessages[1];
		// password1と2の入力値が一致するかを判定
		String errorMessage3 = isValidMessages[2];

		// 登録の可否を判定　初期値は不可を設定
		boolean flag = false;

		if (errorMessage1 == null && errorMessage2 == null && errorMessage3 == null) {
			// エラーメッセージが全てnullの場合
			flag = true;
		}
		
		User userData = dao.findByUser_id(user_id);
		User[] user = { userData };
		mav.addObject("userData", user);

		// エラーがあればエラーメッセージをセット
		if (errorMessage1 != null || errorMessage2 != null || errorMessage3 != null) {
			mav.addObject("errorMessage1", errorMessage1);
			mav.addObject("errorMessage2", errorMessage2);
			mav.addObject("errorMessage3", errorMessage3);
			mav.addObject("success", false);
		}

		if (flag) {
			// パスワード変更
			service.updatePassword(password1, user_id);
			mav.addObject("success", true);
		}

		mav.setViewName("/011_02_02PasswordReset");
		return mav;
	}

	// ユーザ情報変更を表示
	@PostMapping("/011_02_01UserInformationChange")
	public ModelAndView userInformationChange(@RequestParam String user_id, ModelAndView mav) {
		// 遷移先のビューを設定
		mav.setViewName("/011_02_01UserInformationChange");
		// 指定のユーザーの情報を取得
		User userData = dao.findByUser_id(user_id);
		// ユーザーのデータをリスト型に変換
		User[] user = new User[] { userData };
		// モデルにユーザーの情報を追加
		mav.addObject("userData", user);
		return mav;
	}

	// ユーザ情報変更処理
	@PostMapping("/updateUser")
	@Transactional
	public ModelAndView updateUser(@ModelAttribute User user,
			@RequestParam String user_id_check,
			@RequestParam String user_name,
			@RequestParam String user_name_last,
			@RequestParam Integer authority_kbn,
			@RequestParam Integer status_kbn,
			ModelAndView mav) {

		// エラーの有無を判定
		String[] isValidMessages = service.isValidInputUserEdit(user_id_check, user_name, user_name_last);
		// user_idを判定
		String errorMessage1 = isValidMessages[0];
		// user_nameとuser_name_lastを判定
		String errorMessage2 = isValidMessages[1];
		// user_idの重複を判定　予定
		String errorMessage3 = null;

		// 指定のユーザーの情報を取得
		User updateUserData = dao.findBySeq_no(user.getSeq_no());

		// 更新の可否を判定　初期値は不可を設定
		boolean flag = false;

		// user_idが変更されているか、重複していないか確認
		if (user_id_check.equals(updateUserData.getUser_id())) {
			if (errorMessage1 == null && errorMessage2 == null) {
				flag = true; // 変更なし        		
			}
		} else {
			if (dao.findByUser_id(user_id_check) == null) {
				if (errorMessage1 == null && errorMessage2 == null) {
					flag = true; // 重複なし
				}
			} else {
				errorMessage3 = "ユーザIDは既に登録されています";
			}
		}

		// 入力されたユーザの情報を元にオブジェクト生成
		User userData = new User(user_id_check, user_name, user_name_last, authority_kbn, status_kbn);
		// 複数回入力を誤る場合を想定してseq_noを保持
		userData.setSeq_no(user.getSeq_no());
		// ユーザのデータをリスト型に変換
		User[] userList = new User[] { userData };

		mav.addObject("userData", userList);

		// エラーがあればエラーメッセージをセット
		if (errorMessage1 != null || errorMessage2 != null || errorMessage3 != null) {
			mav.addObject("errorMessage1", errorMessage1);
			mav.addObject("errorMessage2", errorMessage2);
			mav.addObject("errorMessage3", errorMessage3);
			mav.addObject("success", false);
		}

		if (flag) {
			this.date = LocalDate.now();
			user.setUpdate_time(date);
			dao.updateUserInformation(user.getSeq_no(), user_id_check, user_name, user_name_last,
					authority_kbn, status_kbn, user.getUpdate_time());
			mav.addObject("success", true);
		}

		mav.setViewName("/011_02_01UserInformationChange");

		//        // errorMessageとsuccessに格納されている値を確認するために使用
		//        String errorMessageValue1 = (String) mav.getModel().get("errorMessage1");
		//        System.out.println("ErrorMessage value: " + errorMessageValue1);
		//        String errorMessageValue2 = (String) mav.getModel().get("errorMessage2");
		//        System.out.println("ErrorMessage value: " + errorMessageValue2);
		//        String errorMessageValue3 = (String) mav.getModel().get("errorMessage3");
		//        System.out.println("ErrorMessage value: " + errorMessageValue3);
		//
		//    	Boolean successValue = (Boolean) mav.getModel().get("success");
		//    	System.out.println("Success value: " + successValue);
		return mav;
	}

	// ユーザ新規登録を表示
	@GetMapping("/011_03UserNewRegistration")
	public String userNewRegistration() {
		return "/011_03UserNewRegistration";
	}

	// 処理失敗時にページに戻る際パスワード以外の入力された値を保持したい
	@PostMapping("/registerUser")
	@Transactional
	public ModelAndView registerUser(@ModelAttribute User user,
			@RequestParam String password1,
			@RequestParam String password2,
			ModelAndView mav) {

		// エラーの有無を判定
		String[] isValidMessages = service.isValidInputUserCreate(user.getUser_id(), user.getUser_name(),
				user.getUser_name_last(), password1, password2);
		// user_idを判定
		String errorMessage1 = isValidMessages[0];
		// user_nameとuser_name_lastを判定
		String errorMessage2 = isValidMessages[1];
		// password1を判定
		String errorMessage3 = isValidMessages[2];
		// password2を判定
		String errorMessage4 = isValidMessages[3];
		// password1と2が一致するか判定
		String errorMessage5 = isValidMessages[4];

		// 登録の可否を判定　初期値は不可を設定
		boolean flag = false;

		if (errorMessage1 == null) {
			// user_id の入力値がエラーではない かつ user_id が重複していない か確認
			if (!(dao.findByUser_id(user.getUser_id()) == null)) {
				errorMessage1 = "ユーザIDは既に登録されています。";

				// 他全てのエラーメッセージがnullの場合は登録可
			} else if (errorMessage2 == null && errorMessage3 == null && errorMessage4 == null
					&& errorMessage5 == null) {
				flag = true;
			}
		}

		// 入力されたユーザの情報を元にオブジェクト生成
		User userData = new User(user.getUser_id(), user.getUser_name(), user.getUser_name_last(),
				user.getAuthority_kbn(), user.getStatus_kbn());
		// ユーザのデータをリスト型に変換
		User[] userList = new User[] { userData };

		mav.addObject("userData", userList);

		// エラーがあればエラーメッセージをセット
		if (errorMessage1 != null || errorMessage2 != null || errorMessage3 != null || errorMessage4 != null
				|| errorMessage5 != null) {
			mav.addObject("errorMessage1", errorMessage1);
			mav.addObject("errorMessage2", errorMessage2);
			mav.addObject("errorMessage3", errorMessage3);
			mav.addObject("errorMessage4", errorMessage4);
			mav.addObject("errorMessage5", errorMessage5);
			mav.addObject("success", false);
		}

		if (flag) {
			// ユーザ登録処理
			service.registerUserLogic(user, password1);
			mav.addObject("success", true);
		}

		mav.setViewName("/011_03UserNewRegistration");
		return mav;
	}

}
