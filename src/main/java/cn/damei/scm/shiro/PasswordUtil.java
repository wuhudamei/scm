package cn.damei.scm.shiro;

import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import cn.damei.scm.entity.account.User;

public class PasswordUtil {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_ITERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	public static void entryptPassword(User user) {
		user.setSalt(generateSalt());
		user.setLoginPwd(hashPassword(user.getPlainPwd(), user.getSalt()));
	}

	public static String generateSalt() {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		return Encodes.encodeHex(salt);
	}

	public static String hashPassword(String plainPassword, String salt) {
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), HASH_ITERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

	public static void main(String[] args) {
		User user = new User();
		user.setUsername("admin");
		user.setPlainPwd("123456");
		entryptPassword(user);

		System.out.println("salt =" + user.getSalt());
		System.out.println("loginHashPwd =" + user.getLoginPwd());
	}
}
