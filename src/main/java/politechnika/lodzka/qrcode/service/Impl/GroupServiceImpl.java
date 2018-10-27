package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.Utils;
import politechnika.lodzka.qrcode.exception.NotOwnerException;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.AuthService;
import politechnika.lodzka.qrcode.service.FormService;
import politechnika.lodzka.qrcode.service.GroupService;

import java.util.Collection;

@Service
class GroupServiceImpl implements GroupService {
    private final GroupRepository repository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final FormService formService;

    GroupServiceImpl(GroupRepository repository, AuthService authService, UserRepository userRepository, FormService formService) {
        this.repository = repository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.formService = formService;
    }

    @Override
    public Group create(CreateGroupRequest request) {
        Group group = new Group();
        group.setName(request.getName());
        group.setModerator(authService.getCurrentUser());
        group.setCode(Utils.randomUUID(Utils.SAVE_LENGTH, -1, Utils.SPACE_CHAR));
        group.setPublicGroup(request.getPublicGroup());
        return repository.save(group);
    }

    @Override
    public Collection<Group> getCurrentUserGroups() {
        User user = authService.getCurrentUser();
        return repository.findByModeratorOrUsersContaining(user, user);
    }

    @Override
    public boolean addUserToGroup(String group, String email) {
        Group moderatorGroup = getModeratorGroup(group);
        User user = getUser(email);
        if (moderatorGroup.getUsers().add(user)) {
            repository.save(moderatorGroup);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUserFromGroup(String group, String email) {
        Group moderatorGroup = getModeratorGroup(group);
        User user = getUser(email);
        if (moderatorGroup.getUsers().remove(user)) {
            repository.save(moderatorGroup);
            return true;
        }
        return false;
    }

    @Override
    public User getUser(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public void remove(String code) {
        repository.delete(getModeratorGroup(code));
    }

    @Override
    public boolean addMyselfToGroupByFormCode(String formCode) {
        Form form = formService.findByCode(formCode);
        Group group = form.getGroup();
        if (group.isPublicGroup()) {
            group.getUsers().add(authService.getCurrentUser());
            repository.save(group);
            return true;
        }
        return false;
    }

    private Group getModeratorGroup(String group) {
        User moderator = authService.getCurrentUser();
        return moderator.getModeratedGroups().stream().filter(moderatedGroup -> moderatedGroup.getCode().equals(group)).findFirst().orElseThrow(() -> new NotOwnerException(moderator, group));
    }
}
