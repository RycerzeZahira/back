package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.Utils;
import politechnika.lodzka.qrcode.exception.*;
import politechnika.lodzka.qrcode.exception.scheme.SchemeNotValidException;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.request.CloneFormRequest;
import politechnika.lodzka.qrcode.model.request.UpdateFormRequest;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.model.request.CreateFormRequest;
import politechnika.lodzka.qrcode.model.request.scheme.ElementRequest;
import politechnika.lodzka.qrcode.model.request.scheme.SaveAnswersRequest;
import politechnika.lodzka.qrcode.model.response.AnswerResponse;
import politechnika.lodzka.qrcode.model.scheme.Answer;
import politechnika.lodzka.qrcode.model.scheme.Element;
import politechnika.lodzka.qrcode.model.scheme.SchemeGroup;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;
import politechnika.lodzka.qrcode.repository.AnswersRepository;
import politechnika.lodzka.qrcode.repository.FormRepository;
import politechnika.lodzka.qrcode.repository.GroupRepository;
import politechnika.lodzka.qrcode.service.AuthService;
import politechnika.lodzka.qrcode.service.FormService;
import politechnika.lodzka.qrcode.service.SchemeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Service
class FormServiceImpl implements FormService {
    private final GroupRepository groupRepository;
    private final FormRepository repository;
    private final AuthService authService;
    private final SchemeService schemeService;
    private final AnswersRepository answersRepository;

    FormServiceImpl(GroupRepository groupRepository, FormRepository repository, AuthService authService, SchemeService schemeService, AnswersRepository answersRepository) {
        this.groupRepository = groupRepository;
        this.repository = repository;
        this.authService = authService;
        this.schemeService = schemeService;
        this.answersRepository = answersRepository;
    }

    @Override
    public Form create(CreateFormRequest request) {
        User moderator = authService.getCurrentUser();
        Group group = groupRepository.findByCode(request.getGroupCode()).orElseThrow(() -> new GroupNotFoundException(request.getGroupCode()));

        if (group.getModerator().getId().longValue() != moderator.getId().longValue()) {
            throw new NoPermissionException(moderator);
        }

        Form form = new Form(group, request.getExpiredDate(), request.getRoot(), Utils.randomUUID(Utils.SAVE_LENGTH, -1, Utils.SPACE_CHAR));
        return repository.save(form);
    }

    public void saveAnswer(SaveAnswersRequest request) {
        Form form = repository.findByCode(request.getFormCode()).orElseThrow(() -> new FormNotFoundException(request.getFormCode()));
        User user = authService.getCurrentUser();

        if (!form.getGroup().getUsers().contains(user)) {
            // throw new NoPermissionException(user);
        }

        if (schemeService.validate(form.getRoot(), request.getRoot())) {
            throw new SchemeNotValidException("Scheme is not valid exception!");
        }

        if (new Date().after(form.getExpiredDate())) {
            throw new FormExpiredException(form);
        }

        if (form.getAnswers().stream().anyMatch(answer -> answer.getUser().equals(user))) {
            throw new CannotDuplicateAnswerException(user, form);
        }

        Collection<Answer> answers = assembly((SchemeGroup) form.getRoot(), request.getRoot(), authService.getCurrentUser(), form);
        answersRepository.saveAll(answers);
    }

    @Override
    public Collection<AnswerResponse> getAnswers(String formCode) {
        Collection<Answer> answers = answersRepository.findAnswerByFormCode(formCode);

        Collection<AnswerResponse> result = new ArrayList<>();

        for (Answer answer : answers) {
            result.add(assemblyAnswer(answer));
        }

        return result;
    }

    @Override
    public void clone(CloneFormRequest cloneFormRequest) {
        Form form = findFormByCode(cloneFormRequest.getFormCode());
        form.setId(null);
        form.setExpiredDate(cloneFormRequest.getDateExpired());
        form.setCode(Utils.randomUUID(Utils.SAVE_LENGTH, -1, Utils.SPACE_CHAR));
        form.setAnswers(Collections.EMPTY_SET);
        repository.save(form);
    }

    @Override
    public void update(UpdateFormRequest request) {
        Form form = findFormByCode(request.getFormCode());
        form.setExpiredDate(request.getExpiredDate());
        form.setRoot(request.getRoot());
        repository.save(form);
    }

    public Form findFormByCode(String code) {
        return repository.findByCode(code).orElseThrow(() -> new FormNotFoundException(code));
    }

    private AnswerResponse assemblyAnswer(Answer answer) {
        AnswerResponse result = new AnswerResponse();
        result.setCode(answer.getScheme().getCode());
        result.setName(answer.getScheme().getName());
        result.setParent(null);
        result.setValue(assemblyAnswerGroup(answer.getChilds()));
        return result;
    }

    private Collection<AnswerResponse> assemblyAnswerGroup(Collection<Answer> childs) {
        Collection<AnswerResponse> result = new ArrayList<>();
        for (Answer child : childs) {
            if (isGroup(child.getScheme())) {
                result.addAll(assemblyAnswerGroup(child.getChilds()));
            } else {
                AnswerResponse answerResponse = new AnswerResponse();
                answerResponse.setName(child.getScheme().getName());
                answerResponse.setCode(child.getScheme().getCode());
                answerResponse.setValue(child.getValue());
                result.add(answerResponse);
            }
        }

        return result;
    }

    public Collection<Answer> assembly(SchemeGroup root, ElementRequest request, User user, Form form) {
        Collection<Answer> answers = new ArrayList<>();
        Answer rootAnswer = new Answer();
        rootAnswer.setScheme(root);
        rootAnswer.setUser(user);
        rootAnswer.setParent(null);
        rootAnswer.setForm(form);
        answers.add(rootAnswer);
        answers.addAll(assemblyGroup(root, request, rootAnswer));
        return answers;
    }

    private Collection<Answer> assemblyGroup(SchemeGroup root, ElementRequest request, Answer parent) {
        Collection<Answer> answers = new ArrayList<>();
        for (ElementRequest req : (Collection<ElementRequest>) request.getElement()) {
            Element scheme = root.getElements().stream().filter(r -> req.getCode().equals(r.getCode())).findFirst().get();
            Answer answer = new Answer();
            answer.setParent(parent);
            answer.setScheme(scheme);
            if (isGroup(scheme)) {
                answers.addAll(assemblyGroup((SchemeGroup) scheme, req, answer));
            } else {
                answer.setValue(req.getElement());
                answers.add(answer);
            }
        }
        return answers;
    }

    private boolean isGroup(Element element) {
        return TypeClass.GROUP.equals(element.getType());
    }
}
