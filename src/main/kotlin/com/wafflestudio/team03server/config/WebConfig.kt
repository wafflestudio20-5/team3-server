package com.wafflestudio.team03server.config

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.Exception401
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.user.service.AuthTokenService
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebMvc
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val authArgumentResolver: AuthArgumentResolver,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://waffle-market-03.s3-website-ap-northeast-1.amazonaws.com")
            .allowedMethods("*")
            .allowCredentials(false)
            .maxAge(3000)
    }
}

@Configuration
class AuthArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserContext::class.java) && parameter.parameterType == Long::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val userId = (webRequest as ServletWebRequest).request.getAttribute("userId")
        return userId
    }
}

@Configuration
class AuthInterceptor(private val authTokenService: AuthTokenService) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerCasted = (handler as? HandlerMethod) ?: return true
        if (handlerCasted.hasMethodAnnotation(Authenticated::class.java)) {
            val accessToken =
                request.getHeader("Authorization") ?: throw Exception401("로그인 후 이용하실 수 있습니다.")
            authTokenService.verifyToken(accessToken)
            request.setAttribute("userId", authTokenService.getCurrentUserId(accessToken))
        }
        return super.preHandle(request, response, handler)
    }
}
