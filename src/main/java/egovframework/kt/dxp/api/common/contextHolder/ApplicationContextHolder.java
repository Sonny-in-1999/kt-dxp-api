package egovframework.kt.dxp.api.common.contextHolder;

import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring bean 에 등록되지 않은 객체에서 Runtime 시점에 Spring bean 에 등록된 객체 조회 시 활용<br />
 *
 * @author GEONLEE
 * @since 2024-04-02
 **/
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextHolder.class);
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
