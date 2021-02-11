package guru.springframework.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseEntityTest {

    BaseEntity entity;

    @BeforeEach
    void setUp() throws Exception {
        entity = new BaseEntity();
    }

    @Test
    void getId() {
        Long expectedId = ThreadLocalRandom.current().nextLong(1, 1000000000);
        entity.setId(expectedId);
        assertEquals(expectedId, entity.getId());
    }
}