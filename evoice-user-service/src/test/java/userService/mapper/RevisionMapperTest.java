package userService.mapper;

import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.AnnotationRevisionMetadata;
import org.springframework.data.history.Revision;
import userService.entity.User;
import userService.entity.envers.RevisionEntityCustom;
import userService.utils.EasyRandomParametersCustom;

import java.util.stream.Collectors;

@SpringBootTest(classes = RevisionMapperImpl.class)
class RevisionMapperTest {
    @Autowired
    private RevisionMapper revisionMapper = Mappers.getMapper(RevisionMapper.class);

    private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParametersCustom());

    @Test
    @DisplayName("Test of mapping List<Revision> to List<RevisionDto> (POSITIVE)")
    void apply() {
        final var user = easyRandom.nextObject(User.class);
        final var revisionEntity = easyRandom.nextObject(RevisionEntityCustom.class);
        final var metadata = new AnnotationRevisionMetadata<Long>(revisionEntity, RevisionNumber.class, RevisionTimestamp.class);
        final var revision = Revision.of(metadata, user);

        Assertions.assertDoesNotThrow(() -> revisionMapper.apply(revision));
    }

    @Test
    @SuppressWarnings("rawtypes")
    @DisplayName("Test of mapping List<Revision> to List<RevisionDto> (POSITIVE)")
    void mapRevisions() {
        final var user = easyRandom.nextObject(User.class);
        final var revisions = easyRandom.objects(RevisionEntityCustom.class, 15).map(revisionEntity -> {
            final var metadata = new AnnotationRevisionMetadata<Long>(revisionEntity, RevisionNumber.class, RevisionTimestamp.class);
            return (Revision) Revision.of(metadata, user);
        }).collect(Collectors.toList());

        Assertions.assertDoesNotThrow(() -> revisionMapper.mapRevisions(revisions));
    }
}