package in.ashokit.service;

import java.util.List;

import in.ashokit.binding.ActivateAccount;
import in.ashokit.binding.Login;
import in.ashokit.binding.User;

public interface IUserService {
	public boolean saveUser(User user);
	
	public boolean activeUserAcc(ActivateAccount account);
	
	public List<User> getAllUser();
	
	public User getUserById(Integer userId);
	
	public boolean deleteUserById(Integer userId);
	
	public boolean changeAccoStatus(Integer userId,String accStatus);
	
	public String login(Login login);
	
	public String forgetPwd(String email);

}
