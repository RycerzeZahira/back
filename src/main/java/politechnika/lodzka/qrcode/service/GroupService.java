package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;

import java.util.Collection;

public interface GroupService {
    Group create(CreateGroupRequest request);

    Collection<Group> getCurrentUserGroups();
}
