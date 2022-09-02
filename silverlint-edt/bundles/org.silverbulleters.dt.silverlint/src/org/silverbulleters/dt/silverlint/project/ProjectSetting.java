/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.project;

public class ProjectSetting {

	private String token;
	private String serverUrl;
	private String projectKey;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		if (!super.equals(object)) return false;

		ProjectSetting that = (ProjectSetting) object;

		if (token != null ? !token.equals(that.token) : that.token != null) return false;
		if (serverUrl != null ? !serverUrl.equals(that.serverUrl) : that.serverUrl != null) return false;
		if (projectKey != null ? !projectKey.equals(that.projectKey) : that.projectKey != null) return false;

		return true;
	}

	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (token != null ? token.hashCode() : 0);
		result = 31 * result + (serverUrl != null ? serverUrl.hashCode() : 0);
		result = 31 * result + (projectKey != null ? projectKey.hashCode() : 0);
		return result;
	}

	@Override
	public java.lang.String toString() {
		return "ProjectSetting{" +
				"token='" + token + '\'' +
				", serverUrl='" + serverUrl + '\'' +
				", projectKey='" + projectKey + '\'' +
				'}';
	}
}
