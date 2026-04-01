package com.takeseem.learn.ai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author <a href="https://github.com/takeseem">杨浩</a>
 */
public class UtilSys {

	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static InputStream getResourceAsStream(String name) {
		InputStream in = getClassLoader().getResourceAsStream(name);
		if (in != null) return in;

		var file = new File(name);
		if (file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("name = " + name);
			}
		}
		return in;
	}

	public static Properties loadProperties(String path) {
		var props = new Properties();
		try (InputStream in = getResourceAsStream(path)) {
			if (in != null) props.load(in);
		} catch (IOException e) {
			throw new IllegalArgumentException("path = " + path, e);
		}
		return props;
	}

	public static Certificate loadX509(String path) {
		try (InputStream in = getResourceAsStream(path)) {
			if (in == null) throw new IllegalArgumentException("path = " + path);

			return CertificateFactory.getInstance("X.509").generateCertificate(in);
		} catch (IOException | CertificateException e) {
			throw new IllegalArgumentException("path = " + path, e);
		}
	}

	public static X509TrustManager getX509TrustManager(String path) {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry(UtilString.getFileName(path), loadX509(path));

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);
			return (X509TrustManager) tmf.getTrustManagers()[0];
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static SSLContext getSSLContext(String protocol, TrustManager... tm) {
		try {
			SSLContext sslContext = SSLContext.getInstance(protocol);
			sslContext.init(null, tm, null);
			return sslContext;
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new IllegalStateException(e);
		}
	}
}
