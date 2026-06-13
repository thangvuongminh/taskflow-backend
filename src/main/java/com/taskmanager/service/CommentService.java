package com.taskmanager.service;

import com.taskmanager.dto.request.CreateCommentRequest;
import com.taskmanager.dto.response.*;
import com.taskmanager.entity.*;
import com.taskmanager.exception.*;
import com.taskmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public List<CommentResponse> getComments(Long taskId, Long userId) {
        Task task = getTask(taskId);
        projectService.requireMember(task.getProject().getId(), userId);
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CommentResponse addComment(Long taskId, CreateCommentRequest req, Long userId) {
        Task task = getTask(taskId);
        projectService.requireMember(task.getProject().getId(), userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        Comment comment = Comment.builder().task(task).user(user).content(req.content()).build();
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment không tồn tại"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn chỉ có thể xóa comment của mình");
        }
        commentRepository.deleteById(commentId);
    }

    private Task getTask(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task không tồn tại"));
    }

    private CommentResponse toResponse(Comment c) {
        User u = c.getUser();
        return new CommentResponse(c.getId(), c.getTask().getId(),
            new UserSummary(u.getId(), u.getUsername(), u.getEmail(), u.getAvatarUrl()),
            c.getContent(), c.getCreatedAt());
    }
}
