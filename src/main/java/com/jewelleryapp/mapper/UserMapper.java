package com.jewelleryapp.mapper;

import com.jewelleryapp.dto.request.UserRequest;
import com.jewelleryapp.dto.response.UserResponse;
import com.jewelleryapp.entity.Role;
import com.jewelleryapp.entity.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // FIX: Changed 'disable = true' to the correct attribute 'disableBuilder = true'.
    // This forces MapStruct to use the default constructor and setters,
    // which avoids the conflict with UserDetails methods.
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", ignore = true) // Roles are handled by service
    @Mapping(target = "password", ignore = true) // Handled by service
    @Mapping(target = "phoneNumber", ignore = true) // Handled by service
    // When using setters, the property name is 'enabled' (from setEnabled)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // These properties don't have setters, so they are now implicitly ignored
    // (We can leave the ignores for clarity, but they are no longer needed)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    User toEntity(UserRequest userRequest);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToRoleNames")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email") // Renamed from username
    @Mapping(target = "password", ignore = true) // Never map password during a direct update
    // This is correct: The setter method is 'setEnabled()', so target is 'enabled'
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "roles", ignore = true) // Role update should be a separate process
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    void updateEntityFromRequest(UserRequest userRequest, @MappingTarget User user);

    @Named("rolesToRoleNames")
    default Set<String> rolesToRoleNames(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}