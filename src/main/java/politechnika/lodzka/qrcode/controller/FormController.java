package politechnika.lodzka.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import politechnika.lodzka.qrcode.exception.FormNotFoundException;
import politechnika.lodzka.qrcode.model.request.CloneFormRequest;
import politechnika.lodzka.qrcode.model.request.CreateFormRequest;
import politechnika.lodzka.qrcode.model.request.UpdateFormRequest;
import politechnika.lodzka.qrcode.model.request.scheme.SaveAnswersRequest;
import politechnika.lodzka.qrcode.repository.FormRepository;
import politechnika.lodzka.qrcode.service.FormService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/form")
public class FormController {

    private final FormService service;
    private final FormRepository repository;

    public FormController(FormService service, FormRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(value = "/create")
    public ResponseEntity create(@Valid @RequestBody CreateFormRequest request) {
        service.create(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody UpdateFormRequest request) {
        service.update(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity getSchemeForm(@PathVariable String code) {
        return ResponseEntity.ok(repository.findFormByCode(code).orElseThrow(() -> new FormNotFoundException(code)));
    }

    @GetMapping(value = "/group/{code}")
    public ResponseEntity getFormsInGroup(@PathVariable String code) {
        return ResponseEntity.ok(repository.findByGroupCode(code));
    }

    @PostMapping(value = "/save")
    public ResponseEntity saveAnswers(@Valid @RequestBody SaveAnswersRequest request) {
        service.saveAnswer(request);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/answers/{formCode}")
    public ResponseEntity getAnswers(@PathVariable String formCode) {
        return ResponseEntity.ok(service.getAnswers(formCode));
    }

    @PostMapping(value = "/clone")
    public ResponseEntity clone(@Valid @RequestBody CloneFormRequest cloneFormRequest) {
        service.clone(cloneFormRequest);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "")
    public ResponseEntity getAllUserForms() {
        return ResponseEntity.ok(service.getCurrentUserForms());
    }
}
