package com.mindzzz.service.impl;

import com.mindzzz.entity.BlogComments;
import com.mindzzz.mapper.BlogCommentsMapper;
import com.mindzzz.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
