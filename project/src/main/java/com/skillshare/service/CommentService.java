package com.skillshare.service;

import com.skillshare.model.Comment;
import com.skillshare.model.Post;
import com.skillshare.model.User;
import com.skillshare.repository.CommentRepository;
import com.skillshare.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPost(Post post, Pageable pageable) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable);
    }

    @Transactional
    public Comment addCommentToPost(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId.toString())
            .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);

        return commentRepository.save(comment);
    }
} 