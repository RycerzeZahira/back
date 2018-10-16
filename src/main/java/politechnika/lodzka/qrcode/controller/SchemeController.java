package politechnika.lodzka.qrcode.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.service.SchemeService;

@RestController
@RequestMapping(value = "/scheme")
public class SchemeController {

    private final SchemeService service;

    public SchemeController(SchemeService service, GroupRepository groupRepository) {
        this.service = service;
    }

    @GetMapping(value = "/availableFields")
    public ResponseEntity availableFields() {
        return ResponseEntity.ok(service.getAvailableFields());
    }
}
