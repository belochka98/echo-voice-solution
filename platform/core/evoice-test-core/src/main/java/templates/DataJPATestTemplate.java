package templates;

import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("application-test")
@Validated
public abstract class DataJPATestTemplate<E> {
    @SuppressWarnings("unchecked")
    private final Class<E> persistentEntityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected abstract CrudRepository<E, UUID> getRepository();

    protected abstract EasyRandom getEasyRandom();

    @PostConstruct
    private void setUp() {
        this.testCRUDRepository();
    }

    private void testCRUDRepository() {
        final var entities = this.getEasyRandom().objects(persistentEntityClass, 15).toList();
        final var repository = this.getRepository();

        Assertions.assertThat(repository.saveAll(entities))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(entities);

        Assertions.assertThatNoException().isThrownBy(repository::deleteAll);

        Assertions.assertThat(repository.count()).isEqualTo(0L);
    }
}
