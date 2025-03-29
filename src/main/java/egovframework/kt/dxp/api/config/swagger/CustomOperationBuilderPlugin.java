package egovframework.kt.dxp.api.config.swagger;

import egovframework.kt.dxp.api.common.message.MessageConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-09-23
 */
@Component
@RequiredArgsConstructor
public class CustomOperationBuilderPlugin implements OperationBuilderPlugin {

    private final MessageConfig messageConfig;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void apply(OperationContext context) {
        List<Response> responses = new ArrayList<>();
        if (context.httpMethod().matches("GET")) {
            responses.add(new ResponseBuilder()
                    .code("401")
                    .description("NOT_AUTHENTICATION(ERR_AT_01)")
                    .build());
            responses.add(new ResponseBuilder()
                    .code("403")
                    .description("FORBIDDEN(ERR_AT_02)")
                    .build());
            responses.add(new ResponseBuilder()
                    .code("404")
                    .description("NOT_FOUND(ERR_CE_00)")
                    .build());
            responses.add(new ResponseBuilder()
                    .code("500")
                    .description("SERVICE_ERROR(ERR_SV_01)")
                    .build());
        } else if (context.httpMethod().matches("POST")) {
            responses.add(new ResponseBuilder()
                    .code("200")
                    .build());
            responses.add(new ResponseBuilder()
                    .code("201")
                    .description("OK")
                    .build());
        }
        OperationBuilder builder = context.operationBuilder();
        builder.responses(responses).build();
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType); // Swagger 2 사용
    }
}
