package ru.dudkomv.neoflexpractice.teacher.validation.birthday;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IsBeforeTodayValidatorTest {
    @Mock
    private Clock clock;

    @InjectMocks
    private IsBeforeTodayValidator validator;

    @BeforeEach
    void setUp() {
        LocalDateTime fixedTime = LocalDateTime.of(2000, 1, 2, 12, 0, 0);
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Clock fixedClock = Clock.fixed(fixedTime.toInstant(zoneOffset), zoneOffset);

        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @Test
    void isValid() {
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        LocalDate timeBefore = LocalDate.of(2000, 1, 1);
        LocalDate timeEqual = LocalDate.of(2000, 1, 2);
        LocalDate timeAfter = LocalDate.of(2000, 1, 3);

        assertTrue(validator.isValid(timeBefore, context));
        assertFalse(validator.isValid(timeEqual, context));
        assertFalse(validator.isValid(timeAfter, context));
    }
}