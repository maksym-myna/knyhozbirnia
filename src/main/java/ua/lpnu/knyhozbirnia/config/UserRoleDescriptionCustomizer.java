package ua.lpnu.knyhozbirnia.config;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class UserRoleDescriptionCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var annotation = handlerMethod.getMethodAnnotation(UserRoleDescription.class);
        if (annotation != null) {
            var securedAnnotation = handlerMethod.getMethodAnnotation(Secured.class);
            if(securedAnnotation != null) {
                String description = operation.getDescription()==null ? "" : (operation.getDescription()+"\n");
                operation.setDescription(description + "Required role: **"+ String.join("or", securedAnnotation.value()) + "**");
            }
        }
        return operation;
    }
}