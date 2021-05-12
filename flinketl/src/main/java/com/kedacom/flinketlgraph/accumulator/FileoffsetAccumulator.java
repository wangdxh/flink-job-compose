package com.kedacom.flinketlgraph.accumulator;

import com.kedacom.flinketlgraph.source.Fileoffset;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FileoffsetAccumulator implements Accumulator<Fileoffset, ArrayList<Fileoffset>> {
    private static Logger logger = LoggerFactory.getLogger(FileoffsetAccumulator.class);
    private  ArrayList<Fileoffset> fileoffsets = null;

    public FileoffsetAccumulator(){
        this(new ArrayList<Fileoffset>());
    }

    public FileoffsetAccumulator(ArrayList<Fileoffset> fileoffsets){
        this.fileoffsets = new ArrayList<Fileoffset>(fileoffsets){
            @Override
            public String toString() {
                String jsonStr = "no data";
                ObjectMapper mapper = new ObjectMapper();
                try {
                    jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
                } catch (JsonProcessingException e) {
                    logger.error("{}", e.getMessage());
                }
                return jsonStr;
            }
        };
    }

    @Override
    public void add(Fileoffset value) {
        this.fileoffsets.add(value);
    }

    @Override
    public ArrayList<Fileoffset> getLocalValue() {
        return this.fileoffsets;
    }

    @Override
    public void resetLocal() {
        fileoffsets.clear();
    }

    @Override
    public void merge(Accumulator<Fileoffset, ArrayList<Fileoffset>> other) {
        this.fileoffsets.addAll(other.getLocalValue());
    }

    @Override
    public Accumulator<Fileoffset, ArrayList<Fileoffset>> clone() {
        ArrayList clones = new ArrayList();
        try {
            for (Fileoffset fileoffset : this.fileoffsets){
                clones.add((Fileoffset)fileoffset.clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new FileoffsetAccumulator(clones);
    }
}
