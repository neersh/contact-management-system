package dto;

import lombok.Data;

@Data
public class SearchContactsCriteria {
    private String firstName;
    private String lastName;
    private String email;
}
