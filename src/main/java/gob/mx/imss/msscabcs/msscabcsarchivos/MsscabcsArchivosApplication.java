package gob.mx.imss.msscabcs.msscabcsarchivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class MsscabcsArchivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsscabcsArchivosApplication.class, args);
	}


	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(-1);
		return multipartResolver;
	}
}
