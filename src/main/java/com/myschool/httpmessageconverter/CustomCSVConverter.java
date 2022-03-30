package com.myschool.httpmessageconverter;

import com.myschool.representation.DiscountRO;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class CustomCSVConverter implements HttpMessageConverter<DiscountRO> {
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(new MediaType("text","customcsv", StandardCharsets.UTF_8));
    }

    @Override
    public DiscountRO read(Class<? extends DiscountRO> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Scanner s = new Scanner(inputMessage.getBody()).useDelimiter("\\A");
        if(s.hasNext())
        {
            String[] values = s.next().split(",");
            DiscountRO ro = new DiscountRO();
            ro.setStudentId(Long.parseLong(values[0]));
            ro.setDisc(Integer.parseInt(values[1]));

            return ro;
        }
        return null;
    }

    @Override
    public void write(DiscountRO discountRO, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
