package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureService unitOfMeasureService;

    @BeforeEach
    void setUp() {
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository,
                new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    void findAllCommands() {
        // given
        UnitOfMeasure unit1 = new UnitOfMeasure();
        unit1.setId(1L);
        UnitOfMeasure unit2 = new UnitOfMeasure();
        unit2.setId(2L);

        when(unitOfMeasureRepository.findAll()).thenReturn(Set.of(unit1, unit2));

        // when
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.findAllCommands();

        // then
        assertNotNull(commands);
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
    }

}