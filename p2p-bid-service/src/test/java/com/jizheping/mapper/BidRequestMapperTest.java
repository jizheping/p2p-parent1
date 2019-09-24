package com.jizheping.mapper;

import com.jizheping.api.entity.BidRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class BidRequestMapperTest {

    @Autowired
    private BidRequestMapper bidRequestMapper;

    @Test
    public void testInsert(){
        BidRequest bidRequest = new BidRequest();
        bidRequest.setTitle("哈哈哈");
        bidRequestMapper.addBitRequest(bidRequest);
        System.out.println(bidRequest.getId());
    }
}