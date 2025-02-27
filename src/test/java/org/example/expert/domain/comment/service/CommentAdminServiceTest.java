package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentAdminServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentAdminService commentAdminService;

    @Test
    public void comment를_정상적으로_삭제한다() {
        // Given
        long commentId = 1L;

        // When
        commentAdminService.deleteComment(commentId);

        // Then
        verify(commentRepository, times(1)).deleteById(commentId);
    }

}