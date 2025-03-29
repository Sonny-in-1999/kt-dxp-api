package egovframework.kt.dxp.api.common.masking;

import egovframework.kt.dxp.api.common.code.ErrorCode;
import egovframework.kt.dxp.api.common.code.NormalCode;
import egovframework.kt.dxp.api.common.exception.custom.ServiceException;
import egovframework.kt.dxp.api.common.message.MessageConfig;
import egovframework.kt.dxp.api.common.response.ItemResponse;
import egovframework.kt.dxp.api.common.response.ItemsResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static egovframework.kt.dxp.api.common.MaskingUtils.*;

/**
 * @author GEONLEE
 * @since 2024-11-05
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MaskingAOP {

    private final MessageConfig messageConfig;

    @Around("@annotation(Masking)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) joinPoint.proceed();
        if (responseEntity.getBody() instanceof ItemResponse) {
            return ResponseEntity.ok().body(ItemResponse.builder()
                    .status(messageConfig.getCode(NormalCode.SEARCH_SUCCESS))
                    .message(messageConfig.getMessage(NormalCode.SEARCH_SUCCESS))
                    .item(setNewRecord(responseEntity)).build());
        } else if (responseEntity.getBody() instanceof ItemsResponse) {
            //TODO items 일 경우 처리 필요.
            return null;
        }
        return joinPoint.proceed();
    }

    private Object setNewRecord(Object response) throws Throwable {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
        if (responseEntity.getBody() instanceof ItemResponse) {
            Field dataField = responseEntity.getBody().getClass().getDeclaredField("item");
            dataField.setAccessible(true);
            Object itemRecord = dataField.get(responseEntity.getBody());
            Class<?> record = itemRecord.getClass();
            Constructor<?> constructor = record.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Object[] constructorArgs = Arrays.stream(record.getRecordComponents())
                    .map(component -> {
                        try {
                            Field field = record.getDeclaredField(component.getName());
                            field.setAccessible(true);
                            Object value = component.getAccessor().invoke(itemRecord);
                            if (field.isAnnotationPresent(Masking.class)) {
                                MaskingType maskingType = field.getAnnotation(Masking.class).maskingType();
                                return applyMasking(String.valueOf(value), maskingType);
                            }
                            return value;
                        } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
                            throw new ServiceException(ErrorCode.DATA_PROCESSING_ERROR, "Masking 처리에 실패하였습니다.");
                        }
                    }).toArray();
            return constructor.newInstance(constructorArgs);
        }
        return response;
    }

    private String applyMasking(String value, MaskingType maskingType) {
        switch (maskingType) {
            case PHONE -> mobile(value);
            case NAME -> name(value);
            case EMAIL -> email(value);
            case BIRTH_DATE -> birthday(value);
        }
        return value;
    }
}
