package cn.damei.scm.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import cn.damei.scm.entity.account.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthAccessTokenUser {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("roles")
	private List<String> roleNameList;

	@JsonProperty("scope")
	private List<String> permissionList;

	@JsonProperty("userinfo")
	private User user;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
