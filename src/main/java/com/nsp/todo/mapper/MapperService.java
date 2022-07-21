package com.nsp.todo.mapper;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class MapperService {

    @SneakyThrows
    public <T,K> T updateObject(K source, T set){
       T t = (T)set.getClass().newInstance();
        BeanUtils.copyProperties(set,t);
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] setFields = t.getClass().getDeclaredFields();
        for (Field sF:sourceFields){
            sF.setAccessible(true);
            for(Field setF:setFields){
                setF.setAccessible(true);
                if(sF.get(source)!=null){
                    if(sF.getName().equals(setF.getName()) & sF.getType().getName().equals(setF.getType().getName())){
                        if(sF.getGenericType()==setF.getGenericType() & sF.getName().equals(setF.getName())){
                            setF.set(t,sF.get(source));
                        }
                    }
                }
            }
        }
        return t;
    }

}
