package org.mipams.jumbf.core.services.content_types;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.List;

import org.mipams.jumbf.core.entities.BinaryDataBox;
import org.mipams.jumbf.core.entities.BmffBox;
import org.mipams.jumbf.core.entities.EmbeddedFileDescriptionBox;
import org.mipams.jumbf.core.services.boxes.BinaryDataBoxService;
import org.mipams.jumbf.core.services.boxes.EmbeddedFileDescriptionBoxService;
import org.mipams.jumbf.core.util.MipamsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmbeddedFileContentType implements ContentTypeService {

    @Autowired
    EmbeddedFileDescriptionBoxService embeddedFileDescriptionBoxService;

    @Autowired
    BinaryDataBoxService binaryDataBoxService;

    @Override
    public String getContentTypeUuid() {
        return "40CB0C32-BB8A-489D-A70B-2AD6F47F4369";
    }

    @Override
    public List<BmffBox> parseContentBoxesFromJumbfFile(InputStream inputStream, long availableBytesForBox)
            throws MipamsException {

        EmbeddedFileDescriptionBox dBox = embeddedFileDescriptionBoxService.parseFromJumbfFile(inputStream,
                availableBytesForBox);

        BinaryDataBox binaryDataBox = binaryDataBoxService.parseFromJumbfFile(inputStream, availableBytesForBox);
        binaryDataBox.setReferencedExternally(dBox.isContentReferencedExternally());

        return List.of(dBox, binaryDataBox);
    }

    @Override
    public void writeContentBoxesToJumbfFile(List<BmffBox> inputBox, OutputStream outputStream)
            throws MipamsException {

        EmbeddedFileDescriptionBox embeddedFileDescriptionBox = (EmbeddedFileDescriptionBox) inputBox.get(0);
        embeddedFileDescriptionBoxService.writeToJumbfFile(embeddedFileDescriptionBox, outputStream);

        BinaryDataBox binaryDataBox = (BinaryDataBox) inputBox.get(1);
        binaryDataBoxService.writeToJumbfFile(binaryDataBox, outputStream);
    }
}