package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ashokit.entity.UserMaster;

@Repository
public interface UserRegRepo extends JpaRepository<UserMaster, Integer> {
	public UserMaster findByEmailAndPassword(String email,String pwd);
	public UserMaster findByEmail(String email);


}
