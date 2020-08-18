package com.alation.lambda.s3.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import com.alation.lambda.s3.StorageManager;
import com.alation.lambda.s3.model.AlationConfig;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

public abstract class MetadataHandle {

	private byte[] fileContent;
	private AlationConfig alationConfig;
	private S3Object s3Object;
	private StorageManager dataSourceStorageManager;

	// Holds the generated content from Metadata Handler
	private String metadataJSONContent = null;

	public String getDSMetadataJSONContent() {
		return metadataJSONContent;
	}

	public void setDSMetadataJSONContent(String metadataJSONContent) {
		this.metadataJSONContent = metadataJSONContent;
	}

	public AlationConfig getAlationConfig() {
		return alationConfig;
	}

	public void setAlationConfig(AlationConfig alationConfig) {
		this.alationConfig = alationConfig;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(InputStream inp) throws IOException {
		setFileContent(IOUtils.toByteArray(inp));
	}

	public void setFileContent(byte[] content) {
		this.fileContent = content;
	}

	public abstract String getBody();

	public S3Object getS3Object() {
		return s3Object;
	}

	public void setS3Object(S3Object s3Object) {
		this.s3Object = s3Object;
	}

	public void setDataSourceStorageManager(StorageManager dataSourceStorageManager) {
		this.dataSourceStorageManager = dataSourceStorageManager;
	}

	public StorageManager getDataSourceStorageManager() {
		return dataSourceStorageManager;
	}

	@Override
	public String toString() {
		return "MetadataHandle [fileContent=" + Arrays.toString(fileContent) + ", alationConfig=" + alationConfig
				+ ", s3Object=" + s3Object + ", dataSourceStorageManager=" + dataSourceStorageManager
				+ ", metadataJSONContent=" + metadataJSONContent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alationConfig == null) ? 0 : alationConfig.hashCode());
		result = prime * result + ((dataSourceStorageManager == null) ? 0 : dataSourceStorageManager.hashCode());
		result = prime * result + Arrays.hashCode(fileContent);
		result = prime * result + ((metadataJSONContent == null) ? 0 : metadataJSONContent.hashCode());
		result = prime * result + ((s3Object == null) ? 0 : s3Object.hashCode());
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
		MetadataHandle other = (MetadataHandle) obj;
		if (alationConfig == null) {
			if (other.alationConfig != null)
				return false;
		} else if (!alationConfig.equals(other.alationConfig))
			return false;
		if (dataSourceStorageManager == null) {
			if (other.dataSourceStorageManager != null)
				return false;
		} else if (!dataSourceStorageManager.equals(other.dataSourceStorageManager))
			return false;
		if (!Arrays.equals(fileContent, other.fileContent))
			return false;
		if (metadataJSONContent == null) {
			if (other.metadataJSONContent != null)
				return false;
		} else if (!metadataJSONContent.equals(other.metadataJSONContent))
			return false;
		if (s3Object == null) {
			if (other.s3Object != null)
				return false;
		} else if (!s3Object.equals(other.s3Object))
			return false;
		return true;
	}

	public abstract String getSchema();

}
