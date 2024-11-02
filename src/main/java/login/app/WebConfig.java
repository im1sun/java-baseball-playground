package login.app;

import login.security.BasicAuthenticationInterceptor;
import login.security.FormLoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginService loginService;
    private final MemberRepository memberRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("WebConfig.addInterceptors");
        registry.addInterceptor(new FormLoginInterceptor(loginService))
                .addPathPatterns("/login");
        registry.addInterceptor(new BasicAuthenticationInterceptor(loginService, memberRepository))
                .addPathPatterns("/members");
    }
}
