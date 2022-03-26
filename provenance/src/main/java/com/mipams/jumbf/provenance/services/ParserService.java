package org.mipams.jumbf.provenance.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mipams.jumbf.provenance.ProvenanceBoxServiceManager;

import org.mipams.jumbf.core.entities.JumbfBox;
import org.mipams.jumbf.core.entities.XTBox;
import org.mipams.jumbf.core.util.MipamsException;
import org.mipams.jumbf.core.util.CorruptedJumbfFileException;
import org.mipams.jumbf.core.services.ParserInterface;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ParserService implements ParserInterface{

    private static final Logger logger = LoggerFactory.getLogger(ParserService.class); 

    @Autowired
    ProvenanceBoxServiceManager boxServiceManager;

    @Override
    public String parseMetadataFromJumbfFile(String path) throws MipamsException{
        return null;
    }
}