package com.userservice.perfume.repository;


import com.userservice.perfume.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {
    Optional<Note> findNoteById(Long id);
    Optional<List<Note>> findNotesByParentNoteId(Long id);
}
