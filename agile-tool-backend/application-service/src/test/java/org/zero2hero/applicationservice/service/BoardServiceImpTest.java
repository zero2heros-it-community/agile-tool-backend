package org.zero2hero.applicationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zero2hero.applicationservice.repository.BoardRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceImpTest {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImp boardServiceImp;

    @Test
    void findByWorkspace() {


    }
}