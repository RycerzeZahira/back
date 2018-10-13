package politechnika.lodzka.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import politechnika.lodzka.qrcode.exception.GroupNotFoundException;
import politechnika.lodzka.qrcode.model.request.CreateGroupRequest;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.service.GroupService;

import javax.validation.Valid;

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

    @GetMapping(value = "/code")
    public ResponseEntity getGroup(@PathVariable String code) {
        return ResponseEntity.ok(repository.findByCode(code).orElseThrow(() -> new GroupNotFoundException(code)));
    }

    @GetMapping(value = "/")
    public ResponseEntity getMyGroups() {
        return ResponseEntity.ok(service.getCurrentUserGroups());
    }
}
