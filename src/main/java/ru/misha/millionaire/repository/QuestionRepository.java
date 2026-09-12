package ru.misha.millionaire.repository;

import ru.misha.millionaire.core.Question;
import ru.misha.millionaire.db.DatabaseConnectionFactory;
import ru.misha.millionaire.db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    public List<Question> findActiveQuestions() {
        String sql = """
                SELECT q.question_id, q.question_text, q.friend_hint,
                       a.answer_text, a.position_number, a.is_correct
                FROM questions q
                JOIN answers a ON a.question_id = q.question_id
                WHERE q.is_active = TRUE
                ORDER BY q.question_id, a.position_number
                """;

        List<Question> questions = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            Long currentId = null;
            String currentText = null;
            String currentHint = null;
            String[] options = new String[4];
            int correctIndex = -1;

            while (resultSet.next()) {
                long questionId = resultSet.getLong("question_id");
                if (currentId != null && currentId != questionId) {
                    questions.add(new Question(currentId, currentText, options, correctIndex, currentHint));
                    options = new String[4];
                    correctIndex = -1;
                }

                currentId = questionId;
                currentText = resultSet.getString("question_text");
                currentHint = resultSet.getString("friend_hint");
                int position = resultSet.getInt("position_number") - 1;
                options[position] = resultSet.getString("answer_text");
                if (resultSet.getBoolean("is_correct")) {
                    correctIndex = position;
                }
            }

            if (currentId != null) {
                questions.add(new Question(currentId, currentText, options, correctIndex, currentHint));
            }
            return questions;
        } catch (SQLException e) {
            throw new DatabaseException("Cannot load questions from database", e);
        }
    }
}
