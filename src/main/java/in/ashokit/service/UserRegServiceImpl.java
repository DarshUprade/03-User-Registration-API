package in.ashokit.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.binding.ActivateAccount;
import in.ashokit.binding.Login;
import in.ashokit.binding.User;
import in.ashokit.entity.UserMaster;
import in.ashokit.repo.UserRegRepo;
import in.ashokit.utils.EmailUtils;

@Service
public class UserRegServiceImpl implements IUserService {
	@Autowired
	private UserRegRepo repo;
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public boolean saveUser(User user) {
		// creating UserMaster Object
		UserMaster entity = new UserMaster();
		// date is Available in user ref we have to copy that data into entity obj
		BeanUtils.copyProperties(user, entity);
		                                         // Source,Destination
		// setting pwd and AccStatus
		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("In-Active");
		// sending to db as a entity Obj
		UserMaster save = repo.save(entity);

		String subject = "Your Registration Successful..";
		String filename="REG_EMAIL_BODY.txt";
		String body=readEmailBody(save.getFullName(),entity.getPassword(),filename);

		emailUtils.sendMail(user.getEmail(), subject, body);

		return save.getUserId() != null;
	}

	@Override
	public boolean activeUserAcc(ActivateAccount account) {
		UserMaster entity = new UserMaster();
		entity.setEmail(account.getEmail());
		entity.setPassword(account.getTempPwd());
		// Query by Exmple(retrieve date base on condition)
		Example<UserMaster> of = Example.of(entity);
		// select * from User_Master where email=?, pwd=?
		List<UserMaster> list = repo.findAll(of);  
		if (list.isEmpty()) { // if record not getting return false
			return false;
		} else { // if record getting
			UserMaster userMaster = list.get(0);
			userMaster.setPassword(account.getNewPwd());
			userMaster.setAccStatus("Active");
			repo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUser() {
		// getting all record but return type of service method is List<user>
		List<UserMaster> findAll = repo.findAll();

		List<User> users = new ArrayList<>();
		for (UserMaster entity : findAll) { // copy UserMaster to user
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		// select * from userMaster where userId=?
		Optional<UserMaster> findById = repo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			repo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeAccoStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = repo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {
		// UserMaster entity=new UserMaster();
//		entity.setEmail(login.getEmail());
//		entity.setPassword(login.getPassword());
//		//select * from UserMaster where email=? and password=?
//		Example<UserMaster> of = Example.of(entity);
//		List<UserMaster> findAll = repo.findAll(of);
//		if(findAll.isEmpty()) {
//			return "Invalid Credentials"; 	
//		}else {
//			UserMaster userMaster = findAll.get(0);
//			if(userMaster.getAccStatus().equals("Active")) {
//				return "Login Successfully";
//			}else {
//				return "Account is Not Activated";
//			}
//		}
		UserMaster entity = repo.findByEmailAndPassword(login.getEmail(), login.getPassword());
		if (entity == null) {
			return "Invalid Credentials";
		}
		if (entity.getAccStatus().equals("Active")) {
			return "Login Successfully";
		} else {
			return "Account is Not Activated";
		}
	}

	@Override
	public String forgetPwd(String email) {
		UserMaster entity = repo.findByEmail(email);
		if (entity == null) {
			return "Invalied Email Id ";
		}
		String subject = "Forgot Password";
		String filename="RECOVER_PWD_BODY.txt";
		String body=readEmailBody(entity.getFullName(),entity.getPassword(),filename);
		boolean sendMail = emailUtils.sendMail(email, subject, body);
		if(sendMail) {
			return "Password Send to your Registration email";
		}
		return null;
	}

	private String generateRandomPwd() {
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		// combine all strings
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

		StringBuilder sb = new StringBuilder();

		Random random = new Random();
		// specify length of random string
		int length = 6;
		for (int i = 0; i < length; i++) {
			// generate random index number
			int index = random.nextInt(alphaNumeric.length());
			// get character specified by index
			// from the string
			char randomChar = alphaNumeric.charAt(index);
			// append the character to string builder
			sb.append(randomChar);
		}
		return sb.toString();
	}

	private String readEmailBody(String fullName, String pwd, String filename) {
		String url = "";
		String mailBody = null;
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			StringBuffer buffer = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				// process the data
				buffer.append(line);
				line = br.readLine();
			}
			br.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("PWD", pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}
}
