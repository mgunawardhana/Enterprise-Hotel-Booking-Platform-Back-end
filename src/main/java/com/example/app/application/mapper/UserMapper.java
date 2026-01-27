package com.example.app.application.mapper;

import com.example.app.adapter.web.response.UserResponse;
import com.example.app.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for User domain model to DTO conversion
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "email", expression = "java(user.getEmail() != null ? user.getEmail().getValue() : null)")
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserResponse toResponse(User user);
}
