package inflearn_clone.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    // application.properties에 정의된 파일 경로 읽어오기
    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /inflearn_clone_file/ 경로로 들어오는 요청을 uploadPath에 있는 파일로 매핑
        registry.addResourceHandler("/inflearn_clone_file/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}