package userService.controller;

import component.response.ResultResponseFactory;
import dto.UserDto;
import dto.envers.RevisionDto;
import dto.response.ResponseOk;
import lombok.SneakyThrows;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.history.Revision;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import userService.entity.User;
import userService.entity.envers.RevisionEntityCustom;
import userService.mapper.RevisionMapper;
import userService.mapper.UserMapper;
import userService.service.UserService;
import userService.utils.EasyRandomParametersCustom;
import userService.utils.TestUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AutoConfigureJsonTesters
@WebMvcTest(UserController.class)
@ActiveProfiles("application-test")
@SuppressWarnings({"rawtypes", "unchecked"})
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private RevisionMapper revisionMapper;
    @MockBean
    private ResultResponseFactory responseFactory;

    private static final String PREFIX_URL = "/v1/api/users/";
    private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParametersCustom());

    @BeforeEach
    void setUp() {
        Mockito.doAnswer(invocationOnMock -> {
            final var dto = invocationOnMock.getArgument(0, UserDto.class);
            return new ResponseOk(dto);
        }).when(responseFactory).createResponseOk(ArgumentMatchers.any(UserDto.class));

        Mockito.doAnswer(invocationOnMock -> {
            final var user = easyRandom.nextObject(User.class);
            user.setId(invocationOnMock.getArgument(0, UUID.class));
            return user;
        }).when(userService).getUser(ArgumentMatchers.any(UUID.class));

        Mockito.doAnswer(invocationOnMock -> {
            final var user = invocationOnMock.getArgument(0, User.class);
            return UserDto.builder()
                    .id(user.getId())
                    .active(user.isActive())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .patronymic(user.getPatronymic())
                    .dateBirthday(user.getDateBirthday())
                    .phone(user.getPhone())
                    .sex(user.isSex())
                    .build();
        }).when(userMapper).apply(ArgumentMatchers.any(User.class));

        Mockito.doAnswer(invocationOnMock -> {
            final var revision = invocationOnMock.getArgument(0, Revision.class);
            return RevisionDto.builder()
                    .id(revision.getRequiredRevisionNumber().longValue())
                    .operation(revision.getMetadata().getRevisionType())
                    .date(LocalDate.ofInstant(revision.getMetadata().getRequiredRevisionInstant(), ZoneId.systemDefault()))
                    .userName(((RevisionEntityCustom) revision.getMetadata().getDelegate()).getUserName())
                    .object(revision.getEntity())
                    .build();
        }).when(revisionMapper).apply(ArgumentMatchers.any(Revision.class));

        Mockito.doAnswer(invocationOnMock -> {
            final var dto = (List) invocationOnMock.getArgument(0);
            return new ResponseOk(dto);
        }).when(responseFactory).createResponseOk(ArgumentMatchers.anyList());

        Mockito.doReturn(easyRandom.objects(User.class, 15).collect(Collectors.toList())).when(userService).getAllUsers();

        Mockito.doAnswer(invocationOnMock -> {
            final var users = (List<User>) invocationOnMock.getArgument(0, List.class);
            return users.stream().map(user -> UserDto.builder()
                    .id(user.getId())
                    .active(user.isActive())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .patronymic(user.getPatronymic())
                    .dateBirthday(user.getDateBirthday())
                    .phone(user.getPhone())
                    .sex(user.isSex())
                    .build()).collect(Collectors.toList());
        }).when(userMapper).to(ArgumentMatchers.anyList());
    }

    @Test
    @SneakyThrows
    @DisplayName("Test for getting a user by id (POSITIVE)")
    void getUserById() {
        final var userId = UUID.randomUUID();

        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.get(PREFIX_URL + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(
                userId,
                TestUtils.parseResponseAsObject(response.getContentAsString(), UserDto.class).getData().getId()
        );
    }

    @Test
    @SneakyThrows
    @DisplayName("Test for getting list of all users (POSITIVE)")
    void getAllUsers() {
        MockHttpServletResponse response = mvc.perform(
                        MockMvcRequestBuilders.get(PREFIX_URL + "all")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(
                15,
                TestUtils.parseResponseAsList(response.getContentAsString(), UserDto.class).getData().size()
        );
    }
}