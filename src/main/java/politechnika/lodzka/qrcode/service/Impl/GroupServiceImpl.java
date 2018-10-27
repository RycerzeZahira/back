package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.Utils;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.service.AuthService;
import politechnika.lodzka.qrcode.service.GroupService;

import java.util.Collection;

@Service
class GroupServiceImpl implements GroupService {
    private final GroupRepository repository;
    private final AuthService authService;

    GroupServiceImpl(GroupRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public Group create(CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.getName());
        group.setModerator(authService.getCurrentUser());
        group.setCode(Utils.randomUUID(Utils.SAVE_LENGTH, -1, Utils.SPACE_CHAR));
        return repository.save(group);
    }

    @Override
    public Collection<Group> getCurrentUserGroups() {
        User user = authService.getCurrentUser();
        return repository.findByModeratorOrUsersContaining(user, user);
    }
}
