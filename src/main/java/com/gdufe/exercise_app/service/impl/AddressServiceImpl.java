package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.Address;
import com.gdufe.exercise_app.dao.AddressMapper;
import com.gdufe.exercise_app.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-23
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
