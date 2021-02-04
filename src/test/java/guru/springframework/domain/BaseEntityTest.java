package guru.springframework.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class BaseEntityTest {

    BaseEntity entity;

    @Before
    public void setUp() throws Exception {
        entity = new BaseEntity();
    }

    @Test
    public void getId() {
        Long expectedId = ThreadLocalRandom.current().nextLong(1,1000000000);
        entity.setId(expectedId);
        assertEquals(expectedId, entity.getId());
    }
}