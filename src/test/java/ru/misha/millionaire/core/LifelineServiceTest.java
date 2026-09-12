package ru.misha.millionaire.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LifelineServiceTest {

    @Test
    void fiftyFiftyReturnsCorrectAndOneWrong() {
        Question q = new Question("Q", new String[]{"A", "B", "C", "D"}, 2, "hint");
        LifelineService service = new LifelineService();

        int[] keep = service.fiftyFifty(q);

        assertEquals(2, keep.length);
        assertTrue(keep[0] == 2 || keep[1] == 2);
        assertNotEquals(keep[0], keep[1]);
    }

    @Test
    void audiencePollHasFourValuesAndSumsTo100() {
        Question q = new Question("Q", new String[]{"A", "B", "C", "D"}, 1, "hint");
        LifelineService service = new LifelineService();

        int[] poll = service.audiencePoll(q);

        assertEquals(4, poll.length);
        int sum = poll[0] + poll[1] + poll[2] + poll[3];
        assertEquals(100, sum);
    }
}