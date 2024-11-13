package com.userservice.survey.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectNote {
    private String noteName;

    public static SelectNote of(String noteName){
        SelectNote selectNote = new SelectNote();
        selectNote.setNoteName(noteName);
        return selectNote;
    }
}
