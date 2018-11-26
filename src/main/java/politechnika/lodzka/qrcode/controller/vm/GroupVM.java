package politechnika.lodzka.qrcode.controller.vm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.user.User;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupVM implements Serializable {
    @JsonBackReference
    private User moderator;
    @JsonManagedReference
    private Set<Form> forms;
    private String name;
    private boolean publicGroup;
    private Long countOfUsers;
    private String code;
}
