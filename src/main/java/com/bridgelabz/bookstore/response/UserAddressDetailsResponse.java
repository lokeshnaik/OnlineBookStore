package com.bridgelabz.bookstore.response;

import com.bridgelabz.bookstore.dto.UserDetailsDTO;
import com.bridgelabz.bookstore.model.UserDetailsDAO;
import lombok.Data;

import java.util.List;

@Data
public class UserAddressDetailsResponse {

    private String message;
    private int status;
    private List<UserDetailsDTO> userDetailsList;

    public UserAddressDetailsResponse( int status, String message, List<UserDetailsDTO> userDetailsList) {
        this.message = message;
        this.status = status;
        this.userDetailsList = userDetailsList;
    }

    public UserAddressDetailsResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }
}
