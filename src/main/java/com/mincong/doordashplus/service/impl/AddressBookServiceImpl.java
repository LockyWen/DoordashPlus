package com.mincong.doordashplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincong.doordashplus.entity.AddressBook;
import com.mincong.doordashplus.mapper.AddressBookMapper;
import com.mincong.doordashplus.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
