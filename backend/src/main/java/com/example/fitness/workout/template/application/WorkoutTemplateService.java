package com.example.fitness.workout.template.application;

import com.example.fitness.workout.api.CreateWorkoutRecordRequest;
import com.example.fitness.workout.api.WorkoutRecordResponse;
import com.example.fitness.workout.infrastructure.WorkoutRecordRepository;
import com.example.fitness.workout.template.api.CreateWorkoutFromTemplateRequest;
import com.example.fitness.workout.template.api.CreateWorkoutTemplateRequest;
import com.example.fitness.workout.template.api.WorkoutTemplateItemResponse;
import com.example.fitness.workout.template.api.WorkoutTemplateResponse;
import com.example.fitness.workout.template.infrastructure.WorkoutTemplateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutTemplateService {

    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final WorkoutRecordRepository workoutRecordRepository;

    public WorkoutTemplateService(
            WorkoutTemplateRepository workoutTemplateRepository,
            WorkoutRecordRepository workoutRecordRepository
    ) {
        this.workoutTemplateRepository = workoutTemplateRepository;
        this.workoutRecordRepository = workoutRecordRepository;
    }

    @Transactional
    public WorkoutTemplateResponse create(Long userId, CreateWorkoutTemplateRequest request) {
        Long templateId = workoutTemplateRepository.create(userId, request);
        return findById(userId, templateId);
    }

    @Transactional(readOnly = true)
    public List<WorkoutTemplateResponse> findAll(Long userId, String bodyPart) {
        return workoutTemplateRepository.findAll(userId, bodyPart);
    }

    @Transactional(readOnly = true)
    public WorkoutTemplateResponse findById(Long userId, Long templateId) {
        return workoutTemplateRepository.findById(userId, templateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "训练模板不存在"));
    }

    @Transactional
    public WorkoutRecordResponse createWorkoutRecord(
            Long userId,
            Long templateId,
            CreateWorkoutFromTemplateRequest request
    ) {
        WorkoutTemplateResponse template = findById(userId, templateId);
        CreateWorkoutRecordRequest workoutRequest = toWorkoutRecordRequest(template, request);
        int totalSets = workoutRequest.exercises().stream()
                .mapToInt(exercise -> exercise.sets().size())
                .sum();
        Long recordId = workoutRecordRepository.create(userId, workoutRequest, totalSets);
        return workoutRecordRepository.findById(userId, recordId)
                .orElseThrow(() -> new IllegalStateException("训练记录创建后未找到"));
    }

    private CreateWorkoutRecordRequest toWorkoutRecordRequest(
            WorkoutTemplateResponse template,
            CreateWorkoutFromTemplateRequest request
    ) {
        LocalDate workoutDate = request == null || request.workoutDate() == null
                ? LocalDate.now()
                : request.workoutDate();
        String note = request == null ? null : request.note();

        List<CreateWorkoutRecordRequest.CreateWorkoutExerciseRequest> exercises = new ArrayList<>();
        int totalCalories = 0;
        for (WorkoutTemplateItemResponse item : template.items()) {
            totalCalories += item.estimatedCalories();
            exercises.add(new CreateWorkoutRecordRequest.CreateWorkoutExerciseRequest(
                    item.exerciseName(),
                    template.bodyPart(),
                    item.estimatedCalories(),
                    null,
                    createSets(item)
            ));
        }

        return new CreateWorkoutRecordRequest(
                workoutDate,
                template.name(),
                template.bodyPart(),
                totalCalories,
                note,
                exercises
        );
    }

    private List<CreateWorkoutRecordRequest.CreateWorkoutSetRequest> createSets(WorkoutTemplateItemResponse item) {
        List<CreateWorkoutRecordRequest.CreateWorkoutSetRequest> sets = new ArrayList<>();
        for (int setNo = 1; setNo <= item.defaultSets(); setNo++) {
            sets.add(new CreateWorkoutRecordRequest.CreateWorkoutSetRequest(
                    setNo,
                    item.defaultReps(),
                    item.defaultWeightKg(),
                    item.defaultDurationSeconds(),
                    null
            ));
        }
        return sets;
    }
}
