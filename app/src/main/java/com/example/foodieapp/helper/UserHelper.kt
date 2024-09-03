package com.example.foodieapp.helper

import com.example.foodieapp.data.api.dto.UserDTO
import com.example.foodieapp.data.model.User

object UserHelper {
    fun convertToUser(userDTO: UserDTO?): User? {
        userDTO ?: return null
        return User(
            email = userDTO.email,
            name = userDTO.name,
            phone = userDTO.phone,
            userGroup = userDTO.userGroup,
            registerDate = userDTO.registerDate,
            token = userDTO.token
        )
    }
}