package com.example.spring.converter;

import java.util.Map;
import javax.annotation.Generated;

import com.example.spring.employee.converter.Demo;
import com.example.spring.employee.converter.IDemoConverter;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T01:58:19+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_372 (Temurin)"
)
@Component
public class IDemoConverterImpl implements IDemoConverter {

    @Override
    public Demo toDemo(Map<String, String> objectMap) {
        if ( objectMap == null ) {
            return null;
        }

        Demo demo = new Demo();

        if ( objectMap.containsKey( "code" ) ) {
            demo.setCode( Integer.parseInt( objectMap.get( "code" ) ) );
        }
        if ( objectMap.containsKey( "desc" ) ) {
            demo.setDesc( objectMap.get( "desc" ) );
        }

        return demo;
    }

    @Override
    public Demo toDemo(Demo demo) {
        if ( demo == null ) {
            return null;
        }

        Demo demo1 = new Demo();

        demo1.setCode( demo.getCode() );
        demo1.setDesc( demo.getDesc() );

        return demo1;
    }
}
