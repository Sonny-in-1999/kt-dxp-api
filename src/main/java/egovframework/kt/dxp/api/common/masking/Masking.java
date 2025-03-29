package egovframework.kt.dxp.api.common.masking;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GEONLEE
 * @since 2024-11-05
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Masking {
    MaskingType maskingType() default MaskingType.DEFAULT;
}

