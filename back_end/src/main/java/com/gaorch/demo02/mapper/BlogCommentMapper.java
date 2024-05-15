package com.gaorch.demo02.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaorch.demo02.entity.BlogComment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogCommentMapper extends BaseMapper<BlogComment> {
    @Select("SELECT * FROM blog_comment WHERE blog_id = #{blogId}")
    public List<BlogComment> selectByBlogId(Integer blogId);

}
