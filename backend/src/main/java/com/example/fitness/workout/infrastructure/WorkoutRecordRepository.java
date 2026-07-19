package com.example.fitness.workout.infrastructure;

import com.example.fitness.workout.api.CreateWorkoutRecordRequest;
import com.example.fitness.workout.api.WorkoutExerciseResponse;
import com.example.fitness.workout.api.WorkoutRecordResponse;
import com.example.fitness.workout.api.WorkoutSetResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class WorkoutRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long create(Long userId, CreateWorkoutRecordRequest request, int totalSets) {
        Long recordId = insertWorkoutRecord(userId, request, totalSets);

        for (int exerciseIndex = 0; exerciseIndex < request.exercises().size(); exerciseIndex++) {
            CreateWorkoutRecordRequest.CreateWorkoutExerciseRequest exercise = request.exercises().get(exerciseIndex);
            Long exerciseId = insertExercise(userId, recordId, exercise, exerciseIndex + 1);
            for (CreateWorkoutRecordRequest.CreateWorkoutSetRequest set : exercise.sets()) {
                insertSet(userId, exerciseId, set);
            }
        }

        return recordId;
    }

    public Optional<WorkoutRecordResponse> findById(Long userId, Long recordId) {
        List<WorkoutRecordResponse> records = jdbcTemplate.query("""
                        select id, user_id, workout_date, title, body_part, total_sets, estimated_calories, note
                        from workout_record
                        where user_id = ? and id = ?
                        """,
                (rs, rowNum) -> toWorkoutRecord(rs.getLong("id"), rs.getLong("user_id"), rs.getDate("workout_date").toLocalDate(),
                        rs.getString("title"), rs.getString("body_part"), rs.getInt("total_sets"),
                        rs.getInt("estimated_calories"), rs.getString("note")),
                userId,
                recordId);
        return records.stream().findFirst();
    }

    public List<WorkoutRecordResponse> findByDate(Long userId, LocalDate date) {
        return jdbcTemplate.query("""
                        select id, user_id, workout_date, title, body_part, total_sets, estimated_calories, note
                        from workout_record
                        where user_id = ? and workout_date = ?
                        order by created_at, id
                        """,
                (rs, rowNum) -> toWorkoutRecord(rs.getLong("id"), rs.getLong("user_id"), rs.getDate("workout_date").toLocalDate(),
                        rs.getString("title"), rs.getString("body_part"), rs.getInt("total_sets"),
                        rs.getInt("estimated_calories"), rs.getString("note")),
                userId,
                Date.valueOf(date));
    }

    public List<WorkoutRecordResponse> findBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query("""
                        select id, user_id, workout_date, title, body_part, total_sets, estimated_calories, note
                        from workout_record
                        where user_id = ? and workout_date >= ? and workout_date < ?
                        order by workout_date, created_at, id
                        """,
                (rs, rowNum) -> toWorkoutRecord(rs.getLong("id"), rs.getLong("user_id"), rs.getDate("workout_date").toLocalDate(),
                        rs.getString("title"), rs.getString("body_part"), rs.getInt("total_sets"),
                        rs.getInt("estimated_calories"), rs.getString("note")),
                userId,
                Date.valueOf(startDate),
                Date.valueOf(endDate));
    }

    private Long insertWorkoutRecord(Long userId, CreateWorkoutRecordRequest request, int totalSets) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("""
                    insert into workout_record
                        (user_id, workout_date, title, body_part, total_sets, estimated_calories, note)
                    values (?, ?, ?, ?, ?, ?, ?)
                    """, new String[]{"id"});
            statement.setLong(1, userId);
            statement.setDate(2, Date.valueOf(request.workoutDate()));
            statement.setString(3, request.title());
            statement.setString(4, request.bodyPart());
            statement.setInt(5, totalSets);
            statement.setInt(6, zeroIfNull(request.estimatedCalories()));
            statement.setString(7, request.note());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private Long insertExercise(
            Long userId,
            Long recordId,
            CreateWorkoutRecordRequest.CreateWorkoutExerciseRequest exercise,
            int sortOrder
    ) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("""
                    insert into workout_exercise_entry
                        (workout_record_id, user_id, exercise_name, body_part, sort_order, estimated_calories, note)
                    values (?, ?, ?, ?, ?, ?, ?)
                    """, new String[]{"id"});
            statement.setLong(1, recordId);
            statement.setLong(2, userId);
            statement.setString(3, exercise.exerciseName());
            statement.setString(4, exercise.bodyPart());
            statement.setInt(5, sortOrder);
            statement.setInt(6, zeroIfNull(exercise.estimatedCalories()));
            statement.setString(7, exercise.note());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void insertSet(
            Long userId,
            Long exerciseId,
            CreateWorkoutRecordRequest.CreateWorkoutSetRequest set
    ) {
        jdbcTemplate.update("""
                        insert into workout_set_entry
                            (exercise_entry_id, user_id, set_no, reps, weight_kg, duration_seconds, distance_meters)
                        values (?, ?, ?, ?, ?, ?, ?)
                        """,
                exerciseId,
                userId,
                set.setNo(),
                set.reps(),
                set.weightKg(),
                set.durationSeconds(),
                set.distanceMeters());
    }

    private WorkoutRecordResponse toWorkoutRecord(
            Long id,
            Long userId,
            LocalDate workoutDate,
            String title,
            String bodyPart,
            int totalSets,
            int estimatedCalories,
            String note
    ) {
        return new WorkoutRecordResponse(
                id,
                userId,
                workoutDate,
                title,
                bodyPart,
                totalSets,
                estimatedCalories,
                note,
                findExercises(id)
        );
    }

    private List<WorkoutExerciseResponse> findExercises(Long recordId) {
        return jdbcTemplate.query("""
                        select id, exercise_name, body_part, sort_order, estimated_calories, note
                        from workout_exercise_entry
                        where workout_record_id = ?
                        order by sort_order, id
                        """,
                (rs, rowNum) -> new WorkoutExerciseResponse(
                        rs.getLong("id"),
                        rs.getString("exercise_name"),
                        rs.getString("body_part"),
                        rs.getInt("sort_order"),
                        rs.getInt("estimated_calories"),
                        rs.getString("note"),
                        findSets(rs.getLong("id"))
                ),
                recordId);
    }

    private List<WorkoutSetResponse> findSets(Long exerciseId) {
        return jdbcTemplate.query("""
                        select id, set_no, reps, weight_kg, duration_seconds, distance_meters
                        from workout_set_entry
                        where exercise_entry_id = ?
                        order by set_no, id
                        """,
                (rs, rowNum) -> new WorkoutSetResponse(
                        rs.getLong("id"),
                        rs.getInt("set_no"),
                        rs.getObject("reps", Integer.class),
                        rs.getBigDecimal("weight_kg"),
                        rs.getObject("duration_seconds", Integer.class),
                        rs.getObject("distance_meters", Integer.class)
                ),
                exerciseId);
    }

    private int zeroIfNull(Integer value) {
        return value == null ? 0 : value;
    }
}
