package politechnika.lodzka.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import politechnika.lodzka.qrcode.controller.vm.GroupVM;
import politechnika.lodzka.qrcode.exception.GroupNotFoundException;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.service.GroupService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/group")
public class GroupController {
    private final GroupService service;
    private final GroupRepository repository;

    public GroupController(GroupService service, GroupRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody CreateGroupRequest request) {
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity getGroup(@PathVariable String code) {
        return ResponseEntity.ok(toVM(repository.findByCode(code).orElseThrow(() -> new GroupNotFoundException(code))));
    }

    @GetMapping(value = "/")
    public ResponseEntity getMyGroups() {
        return ResponseEntity.ok(service.getCurrentUserGroups());
    }

    @GetMapping(value = "/addUser/{groupCode}/{email}")
    public ResponseEntity addUserToGroup(@PathVariable @NotBlank String groupCode, @PathVariable @Email String email){
        if (service.addUserToGroup(groupCode, email)) {
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.badRequest().body("User already is in the group");
        }
    }

    @GetMapping(value = "/removeUser/{groupCode}/{email}")
    public ResponseEntity removeUser(@PathVariable @NotBlank String groupCode, @PathVariable @Email String email){
        if (service.removeUserFromGroup(groupCode, email)) {
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.badRequest().body("User is not in the group");
        }
    }

    @DeleteMapping(value = "/{code}")
    public ResponseEntity deleteGroup(@PathVariable @NotBlank String code) {
        service.remove(code);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/addMyselfToGroupByFormCode/{formCode}")
    public ResponseEntity addMyselfToGroupByFormCode(@PathVariable @NotBlank String formCode) {
        if (service.addMyselfToGroupByFormCode(formCode)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("Group is not public");
    }

    @GetMapping(value = "/getPublicGroupsWithoutMe")
    public ResponseEntity getPublicGroupsWithoutMe() {
        return ResponseEntity.ok(service.getPublicGroupsWithoutMe().stream().map(this::toVM).collect(Collectors.toList()));
    }

    private GroupVM toVM(Group group) {
        return new GroupVM(group.getModerator(), group.getForms(), group.getName(), group.isPublicGroup(), Long.valueOf(group.getUsers().size()), group.getCode());
    }
}
