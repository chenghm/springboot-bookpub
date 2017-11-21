package org.test.bookpub;

import java.io.File;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.filters.RemoteIpFilter;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.formatter.BookFormatter;
import org.test.bookpub.repository.BookRepository;

@Configuration
@PropertySource("classpath:/tomcat.https.properties") 
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class) 
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	private BookRepository bookRepository;

	@Bean
	public RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		return new ByteArrayHttpMessageConverter();
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(byteArrayHttpMessageConverter());
	}

	// @Override
	// public void extendMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// converters.clear();
	// converters.add(new ByteArrayHttpMessageConverter());
	//
	// }

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new BookFormatter(bookRepository));
	}

	/**
	 * setUseSuffixPatternMatch(true)时候 /books/1 和 /books/1.1请求方法得到的结果一致
	 * configurer.setUseSuffixPatternMatch(false)表示设计人员希望系统对外暴露的URL不会识别和匹配.*后缀
	 * setUseTrailingSlashMatch(false) /books/1和/books/1/不一样
	 * setUseTrailingSlashMatch(true)表示系统不区分URL的最后一个字符是否是斜杠/
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(true);
	}

	/**
	 * 通过URLhttp://localhost:8080/internal/application.properties对外暴露当前程序的配置
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/internal/**").addResourceLocations("classpath:/");
	}

	public TomcatContextCustomizer tomcatContextCustomizer(){
		return new TomcatContextCustomizer() {
			
			@Override
			public void customize(Context context) {
				context.setSessionTimeout(1);
			}
		};
	}
	
	@ConfigurationProperties(prefix = "custom.tomcat.https")
	public static class TomcatSslConnectorProperties {
		private Integer port;
		private Boolean ssl = true;
		private Boolean secure = true;
		private String scheme = "https";
		private File keystore;
		private String keystorePassword;

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public Boolean getSsl() {
			return ssl;
		}

		public void setSsl(Boolean ssl) {
			this.ssl = ssl;
		}

		public Boolean getSecure() {
			return secure;
		}

		public void setSecure(Boolean secure) {
			this.secure = secure;
		}

		public String getScheme() {
			return scheme;
		}

		public void setScheme(String scheme) {
			this.scheme = scheme;
		}

		public File getKeystore() {
			return keystore;
		}

		public void setKeystore(File keystore) {
			this.keystore = keystore;
		}

		public String getKeystorePassword() {
			return keystorePassword;
		}

		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}

		public void configureConnector(Connector connector) {
			Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
			if (port != null) {
				connector.setPort(port);
			}
			if (secure != null) {
				connector.setSecure(secure);
			}
			if (scheme != null) {
				connector.setScheme(scheme);
			}
			if (ssl != null) {
				protocol.setSSLEnabled(true);
			}
			if (keystore != null && keystore.exists()) {
				protocol.setKeystoreFile(keystore.getAbsolutePath());
				protocol.setKeystorePass(keystorePassword);
//				protocol.setTruststoreFile(keystore.getAbsolutePath());
//				protocol.setTruststorePass(keystorePassword);
//				protocol.setKeyAlias("tomcat");
//				connector.setProperty("keystoreFile", keystore.getAbsolutePath());
//				connector.setProperty("keystorePassword", keystorePassword);
			}
		}

	}

	
	@Bean
	public TomcatServletWebServerFactory servletContainer(TomcatSslConnectorProperties properties){
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
		return tomcat;
	}
	
	
	private Connector createSslConnector(TomcatSslConnectorProperties properties) {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    properties.configureConnector(connector);
	    return connector;
	}
}
