package common;

import java.sql.*;

public class ExtraFeature {
	private String featureId;
	private String name;
	private String description;
	
	private Package[] packages;
	
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Package[] getPackages() {
		return packages;
	}
	public void setPackages(Package[] packages) {
		this.packages = packages;
	}
	
}
