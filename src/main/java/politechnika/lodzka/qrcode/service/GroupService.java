package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;
import politechnika.lodzka.qrcode.model.user.User;

import java.util.Collection;

public interface GroupService {
    Group create(CreateGroupRequest request);

    Collection<Group> getCurrentUserGroups();

    boolean addUserToGroup(String group, String email);

    boolean removeUserFromGroup(String group, String email);

    User getUser(String email);

    void remove(String code);

    boolean addMyselfToGroupByFormCode(String formCode);
}
