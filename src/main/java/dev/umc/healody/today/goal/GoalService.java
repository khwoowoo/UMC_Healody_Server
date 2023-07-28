package dev.umc.healody.today.goal;

import dev.umc.healody.today.goal.dto.GoalRequestDto;
import dev.umc.healody.today.goal.dto.GoalResponseDto;
import dev.umc.healody.user.User;
import dev.umc.healody.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createGoal(Long userId, GoalRequestDto requestDto) {
        // 사용자 아이디를 통해 Entity 조회
        Optional<User> byId = userRepository.findById(userId);
        User user = new User();
        if(byId.isPresent())
            user = byId.get();

        Goal goal = requestDto.toEntity(user);
        goalRepository.save(goal);
        return "이번 달 회원님의 목표는 " + goal.getBehavior().getDisplayValue() + "입니다";
    }

    public GoalResponseDto findGoal(Long goalId) {
        Optional<Goal> byGoalId = goalRepository.findById(goalId);
        Goal goal = new Goal();
        if(byGoalId.isPresent())
            goal = byGoalId.get();

        GoalResponseDto responseDto = new GoalResponseDto();
        return responseDto.toDto(goal);
    }

    @Transactional
    public Long updateGoal(Long goalId, GoalRequestDto requestDto) {
        Optional<Goal> byGoalId = goalRepository.findById(goalId);
        Goal goal = new Goal();
        if(byGoalId.isPresent())
            goal = byGoalId.get();

        if(requestDto.getStartDate() != null)
            goal.updateStartDate(requestDto.getStartDate());

        if(requestDto.getFinishDate() != null)
            goal.updateFinishDate(requestDto.getFinishDate());

        if(requestDto.getBehavior() != null)
            goal.updateBehavior(requestDto.getBehavior());

        if(requestDto.getValue() != null)
            goal.updateValue(requestDto.getValue());

        return goal.getId();
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        // 이거 따로 메소드로 만드는게 좋을듯
        Optional<Goal> byGoalId = goalRepository.findById(goalId);
        Goal goal = new Goal();
        if(byGoalId.isPresent())
            goal = byGoalId.get();

        goalRepository.delete(goal);
    }
}
