package ru.misha.millionaire.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private List<Question> createQuestions(int count) {
        List<Question> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new Question(
                    "Q" + i,
                    new String[]{"A" + i, "B" + i, "C" + i, "D" + i},
                    0,
                    "Hint" + i
            ));
        }
        return list;
    }

    @Test
    void correctAnswersWinAfterTenRounds() {
        GameEngine engine = new GameEngine();
        engine.startGame("Alex", createQuestions(12));

        for (int i = 0; i < 10; i++) {
            assertTrue(engine.answer(0));
        }

        assertTrue(engine.isGameOver());
        assertEquals("WIN", engine.getEndReason());
        assertEquals(10, engine.getAnsweredCount());
        assertEquals(150000, engine.getCurrentMoney());
    }

    @Test
    void wrongAnswerEndsGameAndZeroesMoney() {
        GameEngine engine = new GameEngine();
        engine.startGame("Alex", createQuestions(10));

        assertFalse(engine.answer(1));
        assertTrue(engine.isGameOver());
        assertEquals("WRONG", engine.getEndReason());
        assertEquals(0, engine.getAnsweredCount());
        assertEquals(0, engine.getCurrentMoney());
    }

    @Test
    void correctAnswerIncreasesMoneyByLadder() {
        GameEngine engine = new GameEngine();
        engine.startGame("Alex", createQuestions(10));

        engine.answer(0);
        assertEquals(500, engine.getCurrentMoney());
        assertEquals(1, engine.getAnsweredCount());

        engine.answer(0);
        assertEquals(1000, engine.getCurrentMoney());
        assertEquals(2, engine.getAnsweredCount());
    }

    @Test
    void lifelinesCanBeUsedOnlyOnce() {
        GameEngine engine = new GameEngine();
        engine.startGame("Alex", createQuestions(10));

        assertTrue(engine.canUseFiftyFifty());
        int[] keep = engine.useFiftyFifty();
        assertEquals(2, keep.length);
        assertFalse(engine.canUseFiftyFifty());
        assertEquals(0, engine.useFiftyFifty().length);

        assertTrue(engine.canUseAudience());
        int[] poll = engine.useAudiencePoll();
        assertEquals(4, poll.length);
        assertFalse(engine.canUseAudience());

        assertTrue(engine.canUseFriend());
        String hint = engine.useFriendCall();
        assertFalse(hint.isEmpty());
        assertFalse(engine.canUseFriend());
    }

    @Test
    void playerNameIsNormalizedAndValidated() {
        GameEngine engine = new GameEngine();
        engine.startGame("  Ivan   Ivanov  ", createQuestions(10));
        assertEquals("Ivan Ivanov", engine.getPlayerName());

        assertThrows(IllegalArgumentException.class,
                () -> engine.startGame("Name_With_Symbols!", createQuestions(10)));

        assertThrows(IllegalArgumentException.class,
                () -> engine.startGame("ThisNameIsWayTooLongForLimit", createQuestions(10)));
    }
}