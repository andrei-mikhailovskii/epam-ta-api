package com.epam.tc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardAttachmentEntity {

    String isUpload;
    String mimeType;
    String name;
    String fileName;

}
