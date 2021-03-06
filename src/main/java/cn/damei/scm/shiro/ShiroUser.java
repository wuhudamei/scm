package cn.damei.scm.shiro;

import cn.damei.scm.entity.eum.AccoutTypeEnum;
import cn.damei.scm.entity.account.User;

import java.io.Serializable;

public class ShiroUser implements Serializable {
	private static final long serialVersionUID = -1373760761780840081L;

	private Long id;
	private String loginName;
	private String name;
	private AccoutTypeEnum acctType;
	private String mobilePhone;
	private String position;
	private Long supplierId;
	private String storeCode;

	public ShiroUser(Long id, String username, String name, AccoutTypeEnum acctType,
					 Long supplierId,String storeCode) {
		this.id = id;
		this.loginName = username;
		this.name = name;
		this.acctType = acctType;
		this.supplierId = supplierId;
		this.storeCode = storeCode;
	}

	@Override
	public String toString() {
		return loginName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroUser other = (ShiroUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public User valueOf() {
		User user = new User();
		user.setId(id);
		user.setName(this.name);
		user.setUsername(loginName);
		user.setPosition(this.position);
		user.setAcctType(this.acctType);
		user.setSupplierId(this.supplierId);
		user.setStoreCode(this.storeCode);
		return user;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getStoreCode() {return storeCode;}

	public void setStoreCode(String storeCode) {this.storeCode = storeCode;}

	public AccoutTypeEnum getAcctType() {
		return acctType;
	}

	public void setAcctType(AccoutTypeEnum acctType) {this.acctType = acctType;}
}