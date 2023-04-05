package userService.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.response.ResponseOk;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.util.List;

@UtilityClass
@SuppressWarnings("unchecked")
public class TestUtils {
    @SneakyThrows
    public <S> ResponseOk<S> parseResponseAsObject(String json, Class<S> genericSourceClass) {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final var parser = new JacksonTester<>(TestUtils.class, ResolvableType.forClassWithGenerics(ResponseOk.class, genericSourceClass), objectMapper);
        return (ResponseOk<S>) parser.parseObject(json);
    }

    @SneakyThrows
    public <S> ResponseOk<List<S>> parseResponseAsList(String json, Class<S> genericSourceClass) {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final var parser = new JacksonTester<>(TestUtils.class, ResolvableType.forClassWithGenerics(ResponseOk.class, ResolvableType.forClassWithGenerics(List.class, genericSourceClass)), objectMapper);
        return (ResponseOk<List<S>>) parser.parseObject(json);
    }
}
